package com.veoride.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import com.veoride.chat.properties.ApplicationProperties;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private ApplicationProperties appProps;
	
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	//thread to create game rooms
        registry.addEndpoint(appProps.getStompGameRoomClient()).withSockJS();
        
        //thread to retrieve all available rooms
        registry.addEndpoint(appProps.getStompAvailableRoomsClient()).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker(appProps.getMessagingTemplatePrefix());
    }
}
