package com.veoride.chat.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.veoride.chat.model.Game;

/**
 * Manages addition or removal of game rooms.
 * @author karenli
 *
 */
@Component
public class GameManager {

	private Map<String, Game> myGames;

	GameManager() {
		myGames = new HashMap<String, Game>();
	}

	/**
	 * Add or update a new game.
	 * @param game
	 */
	public void addChatRoom(Game game) {
		myGames.put(game.getId(), game);
	}

	/**
	 * Removes a game room once it has ended.
	 * @param id
	 */
	public void removeChatRoom(String id) {
		myGames.remove(id);
	}

	/**
	 * Retrieves a game room by ID.
	 * @param id
	 * @return
	 */
	public Optional<Game> getRoomById(String id) {
		if (myGames.get(id) == null) {
			return Optional.empty();
		} else {
			return Optional.of(myGames.get(id));
		}
	}

	public List<String> getActiveGameIds() {
		List<String> allGames = new ArrayList<String>();
		for (String id: myGames.keySet()) {
			allGames.add(id);
		}

		return allGames;
	}
}
