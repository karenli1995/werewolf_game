package com.veoride.chat.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.veoride.chat.model.Game;

/**
 * Manages addition or removal of chatrooms.
 * @author karenli
 *
 */
@Component
public class ChatManager {
	private List<Game> myRooms;
	private List<String> myRoomIds;

	ChatManager() {
		myRooms = new ArrayList<Game>();
		myRoomIds = new ArrayList<String>();
	}
	
//	/**
//	 * Add a new chat room.
//	 * @param room
//	 */
//	public void addChatRoom(Game room) {
//		myRooms.add(room);
//		myRoomIds.add(room.getId());
//	}
//	
//	/**
//	 * Removes a chat room once host has ended chat.
//	 * @param id
//	 */
//	public void removeChatRoom(String id) {
//		for (Game room : myRooms) {
//			if (id.equals(room.getId())) {
//				myRooms.remove(room);
//			}
//		}
//		
//		myRoomIds.remove(id);
//	}
//	
//	/**
//	 * Retrieves a chat room by ID.
//	 * @param id
//	 * @return
//	 */
//	public Optional<Game> getRoomById(String id) {
//		for (Game room : myRooms) {
//			if (room.getId().equals(id)) {
//				return Optional.of(room);
//			}
//		}
//		return Optional.empty();
//	}

	public List<Game> getMyRooms() {
		return myRooms;
	}

	public void setMyRooms(List<Game> myRooms) {
		this.myRooms = myRooms;
	}
	
	public List<String> getMyRoomIds() {
		return myRoomIds;
	}
	
	public void setMyRoomIds(List<String> myRoomIds) {
		this.myRoomIds = myRoomIds;
	}
}
