package com.veoride.chat.controller;

import static java.lang.String.format;

import java.util.List;
import java.util.Map;

import com.veoride.chat.backend.ChatManager;
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
	private ChatManager chatManager;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	/**
	 * Server sends all available chat rooms to the client side.
	 * @param chatRooms
	 */
	@MessageMapping("/chat.availableRooms")
	public void getAvailableChatRooms(@Payload List<String> chatRooms) {
		List<String> roomIds = this.chatManager.getMyRoomIds();
		if (!roomIds.isEmpty()) {
			logger.info("Current Active Room Ids: " + roomIds.size());
			messagingTemplate.convertAndSend(appProps.getMessagingTemplateAvailableRooms(), roomIds);
		} else {
			logger.info("No Active Room Ids");

		}
	}

	/**
	 * Sends a message to a specified room ID.
	 * @param roomId ID of chat room message is sent to
	 * @param chatMessage message
	 */
	@MessageMapping("/chat.sendMessage/{roomId}")
	public void sendMessage(@DestinationVariable String roomId, @Payload Message chatMessage) {
		messagingTemplate.convertAndSend(format(appProps.getMessagingTemplateChatRoom(), roomId), chatMessage);
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
		
		String[] players = game.getPlayers();
		String[] characters = game.getCharacters();
		
		game.chooseCharacters(players, characters);
		game.chooseMiddleCards();
		game.assignPlayers(players);
		
		messagingTemplate.convertAndSend(format(appProps.getMessagingTemplateChatRoom(), currGameId), game);

		
//		String username = chatMessage.getSender();
//		//gets the last room ID that user was in
//		String prevRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", currRoomId);
//		if (prevRoomId != null) {
//			Message leaveMessage = new Message();
//			leaveMessage.setType(MessageType.LEAVE);
//			leaveMessage.setSender(username);
//            leaveMessage.setContent(username + " left!");
//            
////    		headerAccessor.getSessionAttributes().put("hostname", hostName);
//			messagingTemplate.convertAndSend(format(appProps.getMessagingTemplateChatRoom(), prevRoomId), leaveMessage);
//		}
//
//		// Add username in web socket session
//		headerAccessor.getSessionAttributes().put("username", username);
//		messagingTemplate.convertAndSend(format(appProps.getMessagingTemplateChatRoom(), currRoomId), chatMessage);
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
		
		game.setRoleAssignments(roleAssignments);
		game.setMiddleCards(middleCards);
		
		messagingTemplate.convertAndSend(format(appProps.getMessagingTemplateChatRoom(), currGameId), game);
	}

}
