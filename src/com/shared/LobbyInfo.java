/** File: LobbyInfo.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: LobbyInfo object class for relaying lobby information between client and server.
*/

package com.shared;

import java.io.Serializable;

/**
 * LobbyInfo class
 */
public class LobbyInfo implements Serializable{
    private int lobbyID;
    private int playerID;
    
    private int initial;
    private int[] terrSeeds;
    private int[] terrOwners;
    private boolean seededTerritories;

    /**
     * Default constructor
     */
    public LobbyInfo(){
        this.lobbyID = -1;
        this.playerID = -1;
        this.initial = 40;
        this.terrSeeds = null;
        this.seededTerritories = false;
    }
    
    /**
     * Constructor
     * @param lobbyID
     * @param playerID
     */
    public LobbyInfo(int lobbyID, int playerID){
        this.lobbyID = lobbyID;
        this.playerID = playerID;
        this.initial = 40;
        this.terrSeeds = null;
        this.seededTerritories = false;
    }

    /**
     * Lobby Id getter
     * @return Lobby ID
     */
    public int getLobbyID(){return this.lobbyID;}
    
    /**
     * Player ID getter
     * @return Player id
     */
    public int getPlayerID(){return this.playerID;}

    /**
     * Initial getter
     * @return Initial Allocation number
     */
    public int getInitial(){return this.initial;}

    /**
     * TerrSeeds Getter
     * @return Array of Integers representing territories' occupancies
     */
    public int[] getTerrSeeds(){return this.terrSeeds;}

    /**
     * Terrowners getter
     * @return Array of Strings representing territories' owners
     */
    public int[] getTerrOwners(){return this.terrOwners;}

    /**
     * Initial Setter
     * @param initial
     */
    public void setInitial(int initial){this.initial = initial;}

    /**
     * terrseeds setter
     * @param terrSeeds
     */
    public void setTerrSeeds(int[] terrSeeds){this.terrSeeds = terrSeeds;}

    /**
     * terrowners setter
     * @param terrOwners
     */
    public void setTerrOwners(int[] terrOwners){this.terrOwners = terrOwners;}

    /**
     * seededterritories getter
     * @return
     */
    public boolean getSeededTerritories(){return this.seededTerritories;}

    /**
     * seededterritories setter
     * @param seeded
     */
    public void setSeededTerritories(boolean seeded){this.seededTerritories = seeded;}
}