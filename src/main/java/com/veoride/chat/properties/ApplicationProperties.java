package com.veoride.chat.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class ApplicationProperties {
	
	private String newUserMessageContent;
	private String hostLeftMessageContent;
	
	private String messagingTemplatePrefix;
	private String messagingTemplateChatRoom;
	private String messagingTemplateAvailableRooms;
	
	private String stompGameRoomClient;
	private String stompAvailableRoomsClient;

	public String getNewUserMessageContent() {
		return newUserMessageContent;
	}

	public void setNewUserMessageContent(String newUserMessageContent) {
		this.newUserMessageContent = newUserMessageContent;
	}

	public String getHostLeftMessageContent() {
		return hostLeftMessageContent;
	}

	public void setHostLeftMessageContent(String hostLeftMessageContent) {
		this.hostLeftMessageContent = hostLeftMessageContent;
	}

	public String getMessagingTemplatePrefix() {
		return messagingTemplatePrefix;
	}

	public void setMessagingTemplatePrefix(String messagingTemplatePrefix) {
		this.messagingTemplatePrefix = messagingTemplatePrefix;
	}

	public String getMessagingTemplateChatRoom() {
		return messagingTemplateChatRoom;
	}

	public void setMessagingTemplateChatRoom(String messagingTemplateChatRoom) {
		this.messagingTemplateChatRoom = messagingTemplateChatRoom;
	}

	public String getMessagingTemplateAvailableRooms() {
		return messagingTemplateAvailableRooms;
	}

	public void setMessagingTemplateAvailableRooms(String messagingTemplateAvailableRooms) {
		this.messagingTemplateAvailableRooms = messagingTemplateAvailableRooms;
	}

	public String getStompGameRoomClient() {
		return stompGameRoomClient;
	}

	public void setStompGameRoomClient(String stompGameRoomClient) {
		this.stompGameRoomClient = stompGameRoomClient;
	}

	public String getStompAvailableRoomsClient() {
		return stompAvailableRoomsClient;
	}

	public void setStompAvailableRoomsClient(String stompAvailableRoomsClient) {
		this.stompAvailableRoomsClient = stompAvailableRoomsClient;
	}
	
}
