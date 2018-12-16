package com.veoride.chat.controller;

import static java.lang.String.format;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.veoride.chat.backend.GameManager;
import com.veoride.chat.model.Characters;
import com.veoride.chat.model.Game;
import com.veoride.chat.model.Message;
import com.veoride.chat.model.Message.MessageType;
import com.veoride.chat.properties.ApplicationProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private GameManager gameManager;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	/**
	 * Server sends all available chat rooms to the client side.
	 * @param chatRooms
	 */
	@MessageMapping("/chat.availableRooms")
	public void getAvailableChatRooms(@Payload List<String> chatRooms) {
		List<String> gameIds = this.gameManager.getActiveGameIds();
		if (!gameIds.isEmpty()) {
			logger.info("Current Active Game Ids: " + gameIds.size());
			messagingTemplate.convertAndSend(appProps.getMessagingTemplateAvailableRooms(), gameIds);
		} else {
			logger.info("No Active Games");

		}
	}

	/**
	 * 
	 * @param currRoomId
	 * @param chatMessage
	 * @param headerAccessor
	 */
	@MessageMapping("/game.startGame/{currGameId}")
	public void startGame(@DestinationVariable String currGameId, @Payload Game game, 
			SimpMessageHeaderAccessor headerAccessor) {

		Optional<Game> currGame = gameManager.getRoomById(currGameId);
		if (currGame.isPresent()) {
			currGame.get().setId(currGameId);
			gameManager.addChatRoom(currGame.get());
			
			messagingTemplate.convertAndSend(format(appProps.getMessagingTemplateChatRoom(), currGameId), currGame.get());
		} else {
			String[] players = game.getPlayers();
			String[] characters = game.getCharacters();

			game.chooseCharacters(players, characters);
			game.chooseMiddleCards();
			game.assignPlayers(players);
			game.setId(currGameId);

			gameManager.addChatRoom(game);
			
			messagingTemplate.convertAndSend(format(appProps.getMessagingTemplateChatRoom(), currGameId), game);
		}

	}

	/**
	 * 
	 * @param currRoomId
	 * @param chatMessage
	 * @param headerAccessor
	 */
	@MessageMapping("/game.playGame/{currGameId}")
	public void playGame(@DestinationVariable String currGameId, @Payload Game game, 
			SimpMessageHeaderAccessor headerAccessor) {

		Map<String, Characters> roleAssignments = game.getRoleAssignments();
		Characters[] middleCards = game.getMiddleCards();

		Optional<Game> currGame = gameManager.getRoomById(currGameId);
		if (currGame.isPresent()) {
			currGame.get().setId(currGameId);
			currGame.get().setRoleAssignments(roleAssignments);
			currGame.get().setMiddleCards(middleCards);
			gameManager.addChatRoom(currGame.get());
		} else {
			//TODO: throw error
		}

		messagingTemplate.convertAndSend(format(appProps.getMessagingTemplateChatRoom(), currGameId), currGame.get());
	}

}
