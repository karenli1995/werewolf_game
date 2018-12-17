'use strict'

var playerNamesInput = document.querySelector('#player_names');
var chosenCharactersInput = document.getElementsByName('chosen_characters');
var newGameIdInput = document.querySelector('#new_game_id');
var joinGameIdInput = document.querySelector('#join_game_id');

var chatPage = document.querySelector('#game-page');
var newGamePage = document.querySelector('#new-game-page');
var newGameForm = document.querySelector('#newGameForm');
var joinGameForm = document.querySelector('#joinGameForm');
var roomIdDisplay = document.querySelector('#game-id-display');
var connectingElement = document.querySelector('.connecting');

var midCardsArea = document.querySelector('#midCardsArea');
//midCardsArea.setAttribute('horizontal','');
var playersArea = document.querySelector('#playersArea');
//playersArea.setAttribute('horizontal','');

var gameId = null;
var numPlayers = null;
var currPlayersList = null;
var chosenCharsList = null;
var playerToCharDict = null;
var middleCardsList = null;

var card = document.createElement(null);
card.innerHTML = "<img src=\"/images/card_back.jpeg\" width=\"100\">";

var firstCardSelected = false; //we double clicked the first card
var secondCardSelected = false;

var validClientInput = false;
var validHostInput = false;

var currentSubscription;
var stompClient = null;

//Listens for event that user has completed the input form for creating or joining a chat room.
//If it is a host filling out the form, we can begin the chat.
//If it is a client filling out the form, we will validate that the room they plan to join already exists.
function newGameConnect(event) {
	var newGameId = newGameIdInput.value.trim();
	var playerNamesStr = playerNamesInput.value.trim();

	var chosenCharactersStr = '';
	for (var i=0, n=chosenCharactersInput.length;i<n;i++) {
		if (chosenCharactersInput[i].checked) {
			chosenCharactersStr += chosenCharactersInput[i].value + ' ';
		}
	}

	var playerNamesList = playerNamesStr.split(" ");
	var chosenCharactersList = chosenCharactersStr.trim().split(" ");

	currPlayersList = playerNamesList;
	chosenCharsList = chosenCharactersList;
	numPlayers = currPlayersList.length;
	gameId = newGameId;

	//if(validateHostInput(newGameId)) {
	startGame();
	//}
}

function joinGameConnect(event) {
	var joinGameId = joinGameIdInput.value.trim();
	gameId = joinGameId;

	//if(validateClientInput(joinGameId)) {
	joinGame();
	//}
}

//Begins a gameroom and displays appropriate html elements for the gameroom.
//Creates a STOMP Client "/gameroom"
function startGame() {
	newGamePage.classList.add('hidden');
	chatPage.classList.remove('hidden');

	var socket = new SockJS('/gameroom');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, onConnected, onError);
	event.preventDefault();
}

function joinGame() {
	joinGamePage.classList.add('hidden');
	chatPage.classList.remove('hidden');

	var socket = new SockJS('/gameroom');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, onConnected, onError);
	event.preventDefault();
}

function createGUI(payload) {
	var message = JSON.parse(payload.body);
	middleCardsList = message.middleCards;
	playerToCharDict = message.roleAssignments;


	var gameAreaElement = document.createElement('span');
	gameAreaElement.style.display = 'inline-block';
//	gameAreaElement.classList.add('chat-message');

	for (var i = 0; i < middleCardsList.length; i++) { 
		var newCard = card.cloneNode(true);
		var midCardIdText = document.createTextNode(i+1);

		newCard.addEventListener("ondblclick", function() {
			window.alert("This card was: " + middleCardsList[i]);
		});

		(function(idx, currCard) {
			newCard.addEventListener("click", function() { cardClick(idx, currCard); }, false);
		})(i, newCard);

		newCard.appendChild(midCardIdText);

		gameAreaElement.appendChild(newCard);
	}

	//iterate through playersToCharDict
	Object.keys(playerToCharDict).forEach(function(key) {
		var newCard = card.cloneNode(true);
		var playerIdText = document.createTextNode(key);

		newCard.addEventListener("ondblclick", function() {
			window.alert("This card was: " + playerToCharDict[key]);
		}, false);

		(function(idx, currCard) {
			newCard.addEventListener("click", function() { cardClick(idx, currCard); }, false);
		})(key, newCard);

		newCard.appendChild(playerIdText);

		gameAreaElement.appendChild(newCard);
	});

	playersArea.appendChild(gameAreaElement);
	playersArea.replaceChild(gameAreaElement, playersArea.childNodes[0]);
	playersArea.scrollTop = playersArea.scrollHeight;

	midCardsArea.appendChild(gameAreaElement);
	midCardsArea.replaceChild(gameAreaElement, midCardsArea.childNodes[0]);
}

//Leave the current room and enter a new one.
//Creates a thread that listens for message types and is sent as json data from the server side.
//Thread is listening on "/topic/public/${gameId}"
//A different thread for every chat room
function enterRoom() {
	roomIdDisplay.textContent = gameId;

	var newGameData = {
			id: gameId,
			characters: chosenCharsList,
			players: currPlayersList,
			roleAssignments: playerToCharDict,
			middleCards: middleCardsList
	};

	stompClient.send(`/app/game.startGame/${gameId}`,
			{},
			JSON.stringify(newGameData)
	);


	if (currentSubscription) {
		currentSubscription.unsubscribe();
	}

	// Subscribe to the Public Topic
	currentSubscription = stompClient.subscribe(`/topic/public/${gameId}`, createGUI);
}

//Triggered on connection to the "/gameroom" STOMP client
//Connects to a game room.
function onConnected() {
	enterRoom();
	connectingElement.classList.add('hidden');
}

var cardClick = function(id, currCard) {	
	currCard.innerHTML = "<img src=\"/images/card_back_selected.jpeg\" width=\"100\">";

	if(id === firstCardSelected) return;

	if (firstCardSelected == false && secondCardSelected == false) { //no card has been selected
		firstCardSelected = id;
	} else if (firstCardSelected !== false && secondCardSelected !== false) { // both cards selected; update dict and send to server

		//determine if there is a middle card selected
		var middleCardIndex = null;
		var firstCardIsMiddle = false;
		//only one of the cards can possibly be a middle card
		if (Number.isInteger(firstCardSelected) && Number.isInteger(secondCardSelected)) {
//			window.alert("Thou cannot choose 2 middle cards. Try again.");
//			firstCardSelected = secondCardSelected = false;
//			return;
		}

		//figure out which card chosen was the middle card, if there was one...
		if (Number.isInteger(firstCardSelected)) {
			firstCardIsMiddle = true;
			middleCardIndex = firstCardSelected;
		} else if (Number.isInteger(secondCardSelected)) {
			middleCardIndex = secondCardSelected;
		} 

		//if no middle card just switch the roleAssignments map
		if (middleCardIndex == null) {
			var playerCharacter1 = playerToCharDict[firstCardSelected];
			var playerCharacter2 = playerToCharDict[secondCardSelected];
			playerToCharDict[firstCardSelected] = playerCharacter2;
			playerToCharDict[secondCardSelected] = playerCharacter1;

		} else { //otherwise have to update the middle cards as well
			var middleCharacter = middleCardsList[middleCardIndex];

			if (firstCardIsMiddle) {
				switchCards(secondCardSelected, middleCardIndex, middleCharacter);
			} else {
				switchCards(firstCardSelected, middleCardIndex, middleCharacter);
			}			
		}

		//reset any selected cards
		firstCardSelected = secondCardSelected = false;

		var updatedGame = {
				id: gameId,
				roleAssignments: playerToCharDict,
				middleCards: middleCardsList
		};

		stompClient.send(`/app/game.playGame/${gameId}`,
				{},
				JSON.stringify(updatedGame)
		);

	} else if (firstCardSelected !== false && secondCardSelected == false) { // one card has been selected
		secondCardSelected = id;
	}
};

//helper method to switch a middle card with some player card
function switchCards(playerId, midCardIndex, midCharacter) {
	var playerCharacter = playerToCharDict[playerId];
	playerToCharDict[playerId] = midCharacter;
	middleCardsList[midCardIndex] = playerCharacter;
}

//performs form validation if Host filled out the input form to create a game.
function validateHostInput(roomNum) {

	if (roomIdList.includes(roomNum)) {
		validHostInput = false;
		window.alert("Game room " + roomNum + " already exists. Enter a new Game Room ID to host or join the existing game.");
	} else {
		validHostInput = true;
	}
	return validHostInput;
}

//performs form validation if Client filled out the input form to join a game.
function validateClientInput(roomNum) {

	if (!roomIdList.includes(roomNum)) {
		validClientInput = false;
		window.alert("Enter a valid Game Room ID to join.");
	} else {
		validClientInput = true;
	}
	return validClientInput;
}

newGameForm.addEventListener('submit', newGameConnect, true)
joinGameForm.addEventListener('submit', joinGameConnect, true)

