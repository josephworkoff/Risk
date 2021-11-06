/** File: Lobby.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: Lobby object class for storing coordination information for two players.
*/

package com.server;

import java.util.ArrayList; 
import java.util.Random;

import com.shared.LobbyInfo;

// import com.client.RiskGameController;

public class Lobby {

    private static final long TIMEOUT = 60000;

    private int ID;

    private boolean player1;
    private boolean player2;

    private ArrayList<String> player1Messages;
    private ArrayList<String> player2Messages;

    private int[] territories;
    private int[] territorySeeds;
    private int initial;

    private long p1timestamp;
    private long p2timestamp;

    private boolean seeded;

    public enum Status{
        EMPTY,
        SEARCHING,
        READY,
        INPROGRESS,
        ENDED
    }

    boolean inProgress = false;

    private Status status;

    /**
     * Default constructor
     */
    public Lobby(){  
        this.player1 = false;
        this.player2 = false;

        this.ID = -1;

        this.player1Messages = new ArrayList<String>();
        this.player2Messages = new ArrayList<String>();

        this.territories = new int[42];
        this.territorySeeds = new int[42];

        this.seeded = false;

        this.status = Status.EMPTY;

        this.p1timestamp = System.currentTimeMillis();
        this.p2timestamp = System.currentTimeMillis();

    }

    /**
     * Id setter
     * @param ID
     */
    public void setID(int ID){this.ID = ID;}

    /**
     * ID getter 
     * @return id
     */
    public int getID(){return this.ID;}

    /**
     * Status getter
     * @return Lobby.Status
     */
    public Lobby.Status getStatus(){return this.status;}

    /**
     * Territories getter. Converts to Integer array for sending to client. 
     */
    public Integer[] getTerritories(){
        Integer[] terrs = new Integer[42];
        for (int i = 0; i < 42; i++){
            terrs[i] = Integer.valueOf(this.territories[i]);
        }

        return terrs;
    }

    /**
     * Territory seeds. Converts to Integer array for sending to client. 
     */
    public Integer[] getTerritorySeeds(){
        Integer[] terrs = new Integer[42];
        for (int i = 0; i < 42; i++){
            terrs[i] = Integer.valueOf(this.territorySeeds[i]);
        }

        return terrs;
    }
    

    public LobbyInfo getLobbyInfo(){
        LobbyInfo info = new LobbyInfo();
        info.setInitial(this.initial);
        info.setTerrSeeds(this.territorySeeds);
        info.setTerrOwners(this.territories);
        return info;
    }


    /**
     * Links a client to this lobby.
     * @return The client's player id.
     */
    public int connectPlayer(){
        if (!this.player1){
            this.player1 = true;
            this.status = Status.SEARCHING;
            return 1;
        }
        else if (!this.player2){
            this.player2 = true;
            readyGame();
            return 2;
        }
        else{ //this lobby already has two players
            return -1;
        }
    }


    /**
     * Declares the lobby ready to begin. Initializes territories. Sets lobby status to ready.
     */
    public void readyGame(){
        if (!this.seeded){
            initTerritories();
        }
        this.status = Status.READY;
    }


    /**
     * Seeds the lobby's initial gamestate.
     * @param info
     */
    public void seed(LobbyInfo info){
        this.initial = info.getInitial();

        if (info.getSeededTerritories()){
            this.territories = info.getTerrOwners();
            this.territorySeeds = info.getTerrSeeds();
            this.seeded = true;
        }
    }



    /**
     * Retrieves the indexed message from the player's message buffer.
     * @param player Player's server id
     * @param messageIndex Index to check for message
     * @return String Message, or "NONE" if no new message.
     */
    public String readMessage(int player, int messageIndex){

        ArrayList<String> messages;
        long ts;
        if (player == 1){ //P1 sending message to P2
            messages = player1Messages;
            ts = this.p2timestamp;
            this.p1timestamp = System.currentTimeMillis();
        }
        else{ //P2 sending to P1
            messages = player2Messages;
            ts = this.p1timestamp;
            this.p2timestamp = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() - ts > TIMEOUT){
            return "DISCONNECTED:";
        }
        if (messages.size() > messageIndex){
            return messages.get(messageIndex);
        }
        return "NONE";
      
    }


    /**
     * Places a message in the player's message buffer.
     * @param player Player's server id
     * @param message Message to write
     */
    public void writeMessage(int player, String message){

        ArrayList<String> messages = (player == 1) ? player2Messages : player1Messages;
        messages.add(message);

        // ArrayList<String> messages;
        // ArrayList<String> senderMessages;
        // long ts;
        // if (player == 1){ //P1 sending message to P2
        //     messages = player2Messages;
        //     senderMessages = player1Messages;
        //     ts = this.p2timestamp;
        //     this.p1timestamp = System.currentTimeMillis();
        // }
        // else{ //P2 sending to P1
        //     messages = player1Messages;
        //     senderMessages = player2Messages;
        //     ts = this.p1timestamp;
        //     this.p2timestamp = System.currentTimeMillis();
        // }

        // messages.add(message);

        // if ((System.currentTimeMillis() - ts) > TIMEOUT){
        //     senderMessages.add("DISCONNECTED");
        // }

    }




    /**
     * Randomly generates an initial gamestate.
     */
    protected void initTerritories(){

        System.out.println("Generating territories.");
        // Integer[][] terrs = new Integer[42][2];

        
        if (this.territories == null){
            this.territories = new int[42];
        }

        Random rand = new Random();
        
        int numClaimed = 0;
        boolean p1 = true;
        int tid;

        while (numClaimed < 28){
            tid = rand.nextInt(42);
            if (this.territories[tid] == 0){
                this.territories[tid] = (p1) ? 1 : 2;
                numClaimed++;
                p1 = !p1;
            }
        }
    }
}