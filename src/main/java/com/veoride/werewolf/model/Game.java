package com.veoride.werewolf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;


	private String id;
	
	
	private String[] players;
	private String[] characters;
	
	
	
	
	
	private int myNumPlayers;
	private List<Characters> allCharacters;
    private List<Characters> chosenCharacters;
    private Map<String, Characters> roleAssignments;
    private Characters[] middleCards;
	
	Game() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
    public String[] getPlayers() {
		return players;
	}

	public void setPlayers(String[] players) {
		this.players = players;
	}

	public String[] getCharacters() {
		return characters;
	}

	public void setCharacters(String[] characters) {
		this.characters = characters;
	}
	
	public int getMyNumPlayers() {
		return myNumPlayers;
	}

	public void setMyNumPlayers(int myNumPlayers) {
		this.myNumPlayers = myNumPlayers;
	}

	public List<Characters> getAllCharacters() {
		return allCharacters;
	}

	public void setAllCharacters(List<Characters> allCharacters) {
		this.allCharacters = allCharacters;
	}

	public List<Characters> getChosenCharacters() {
		return chosenCharacters;
	}

	public void setChosenCharacters(List<Characters> chosenCharacters) {
		this.chosenCharacters = chosenCharacters;
	}

	public Map<String, Characters> getRoleAssignments() {
		return roleAssignments;
	}

	public void setRoleAssignments(Map<String, Characters> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}

	public Characters[] getMiddleCards() {
		return middleCards;
	}

	public void setMiddleCards(Characters[] middleCards) {
		this.middleCards = middleCards;
	}
	
	

	public void chooseCharacters(String[] playerNames, String[] chars) {
        
        allCharacters = Arrays.asList(Characters.WEREWOLF1, Characters.WEREWOLF2, Characters.SEER,
                Characters.ROBBER, Characters.TROUBLEMAKER, Characters.TANNER, Characters.DRUNK, Characters.HUNTER,
                Characters.MASON1, Characters.MASON2, Characters.INSOMNIAC, Characters.MINION, Characters.DOPPELGANGER,
                Characters.VILLAGER1, Characters.VILLAGER2, Characters.VILLAGER3);
        
        chosenCharacters = new ArrayList<Characters>();
        roleAssignments = new HashMap<String, Characters>();
		
        for(int j=0; j<chars.length; j++) {
        	chosenCharacters.add(Characters.valueOf(chars[j]));
        }
		
//        int[] initRoles = new int[chars.length];
//        for(int j=0; j<initRoles.length; j++) {
//            initRoles[j] = Integer.parseInt(chars[j]);
//        }
        
        myNumPlayers = playerNames.length;

//        int i = 0;
//        while (i < myNumPlayers + 3) {
//            int n = initRoles[i] - 1;
//            //check that it is num between 1-size(allCharacters)
//            chosenCharacters.add(allCharacters.get(n));
//            i++;
//        }
        System.out.println("HI" + chosenCharacters);
    }
    
    public void chooseMiddleCards() {
        middleCards = new Characters[3];
        int i = 0;
        while (i < 3) {
            Random rand = new Random();
            int n = rand.nextInt(chosenCharacters.size()-1);
            middleCards[i] = chosenCharacters.get(n);
            chosenCharacters.remove(n);
            i++;
        }        
    }

    public void assignPlayers(String[] playerNames) {
        Collections.shuffle(chosenCharacters);
        int j = 0;
        while (j < myNumPlayers) {
        	String player = playerNames[j];
            roleAssignments.put(player, chosenCharacters.get(j));
            j++;
        }
    }

}
