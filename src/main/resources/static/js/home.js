'use strict';

var homePage = document.querySelector('#home-page');
var newGamePage = document.querySelector('#new-game-page');
var joinGamePage = document.querySelector('#join-game-page');
var newGameForm = document.querySelector('#newGameForm');
var connectingElement = document.querySelector('.connecting');

var availableRoomsArea = document.querySelector('#available-rooms');

var roomIdList = [];
var roomIdListJson = JSON.stringify(roomIdList);

var stompClientForAvailableRooms = null;

//Listens for event that user hits the "Host" button to host a game
function host(event) {
	displayNewGameInputForm();
}

//Listens for event that user hits the "Client" button to join a game
function client(event) {
	displayJoinGameInputForm();
}

//Creates a thread that listens for available rooms which is sent as json data from the server side.
//Thread is listening on "/topic/public/availableRooms"
function createStompClientForRetrievingAvailableRooms() {
	var socket = new SockJS('/availableRooms');
	stompClientForAvailableRooms = Stomp.over(socket);
	stompClientForAvailableRooms.connect({}, function( frame ){
	    console.log( "Connected :- "+frame );
	    stompClientForAvailableRooms.subscribe(`/topic/public/availableRooms`, onDisplayChatRoomInfoPage);
	}, onError);
}

//Input Form that host fills out with the chat room to begin, or that the client can fill out to join an existing room.
//Once filled out, pings the server to send an updated list of all available chat room IDs
function displayNewGameInputForm() {
	homePage.classList.add('hidden');
	newGamePage.classList.remove('hidden');
}

function displayJoinGameInputForm() {
	stompClientForAvailableRooms.send(`/app/chat.availableRooms`, {}, roomIdListJson);

	homePage.classList.add('hidden');
	joinGamePage.classList.remove('hidden');
}

//Error connecting to Websocket server.
function onError(error) {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}

//Displays Available Games based on data retrieved from the server side.
function onDisplayChatRoomInfoPage(payload) {
	roomIdList = JSON.parse(payload.body);
	
	var availableRoomsElement = document.createElement('p');
	
	for (var i=0; i<roomIdList.length; i++ ) {
		var availableRoomsText = document.createTextNode(roomIdList[i]);
		var linebreak = document.createElement("br");
		availableRoomsElement.appendChild(availableRoomsText);
		availableRoomsElement.appendChild(linebreak);
	}
	availableRoomsArea.appendChild(availableRoomsElement);
}
document.getElementById("host-start-game").addEventListener('click', host, true)
document.getElementById("client-join-game").addEventListener('click', client, true)
