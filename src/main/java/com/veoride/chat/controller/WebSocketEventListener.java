package com.veoride.chat.controller;

import static java.lang.String.format;

import com.veoride.chat.backend.ChatManager;
import com.veoride.chat.model.Message;
import com.veoride.chat.properties.ApplicationProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
	
	@Autowired
	private ApplicationProperties appProps;
	
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * Listens for web socket connections.
     * @param event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    /**
     * Listens for disconnections from web sockets.
     * @param event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("Received a web socket disconnection");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = (String) headerAccessor.getSessionAttributes().get("room_id");
        
        userDisconnectMessage(username, roomId);
    }

    
    /**
     * Determines who left the chat, and deactivates chat rooms after a host leaves (TODO).
     * @param username
     * @param roomId
     * @param chatRoom
     * @param hostname
     */
	private void userDisconnectMessage(String username, String roomId) {
		if (username != null) {

            logger.info("User Disconnected : " + username);

            Message chatMessage = new Message();
            chatMessage.setType(Message.MessageType.LEAVE);
            chatMessage.setSender(username);
            chatMessage.setContent(username + " left!");

            messagingTemplate.convertAndSend(format(appProps.getMessagingTemplateChatRoom(), roomId), chatMessage);
        }
	}
}