/** File: Broadcaster.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: Broadcaster class for providing an interface for communicating with the server through RPC calls.
*/


package com.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.Timer; 
import java.util.ArrayList;



/**
 * Broadcaster Class
 */
public class Broadcaster{
    public static final int POLLING_RATE = 250;
    public static final RPCServiceAsync RPCSERVICE = GWT.create(RPCService.class);


    /**
     * Sends a message from the client to server.
     * @param lobbyID - Server's lobby ID to store the message.
     * @param playerID - Server player ID of the sender
     * @param message - Coded message to send.
     * @param callback - Callback object
     */
    public static void broadcast(final int lobbyID, final int playerID, final String message, final AsyncCallback callback){
        Util.debug("Entering Broadcaster.broadcast. Sending: " + message);
        Timer sendTimer = new Timer() {
            @Override
            public void run(){
                RPCSERVICE.sendUpdate(lobbyID, playerID, message, callback);
                Util.debug("RPC Broadcast: LID=" + lobbyID + " PID=" + playerID + " MSG=" + message);
            }
        };  
        sendTimer.schedule(POLLING_RATE);

    }


    /**
     * Retrieves a message from the server.
     * @param lobbyID - Server's lobby ID to search for the message.
     * @param playerID - Server player ID of the requester
     * @param updateIndex - Index of the last successful message
     * @param callback - Callback object
     */
    public static void receive(final int lobbyID, final int playerID, final int updateIndex, final AsyncCallback callback){
        Timer receiveTimer = new Timer() {
            @Override
            public void run(){
                RPCSERVICE.receiveUpdate(lobbyID, playerID, updateIndex, callback);
            }
        };  
        receiveTimer.schedule(POLLING_RATE);
    }


    /**
     * Generates a coded message for a reinforcement action.
     * @param terr Territory reinforcement
     * @param numTroops Number of troops placed
     * @return Reinforce message
     */
    public static String writeReinforce(Territory terr, int numTroops){
        Util.debug("Entering controller.writeReinforce: Terr = " + terr);
        return "REINFORCE:" + terr.getID() + ":" + numTroops;
    }


    /**
     * Generates a coded message for a card turn in action.
     * @param numTroops Number of troops earned from the trade in
     * @return Turn in message
     */
    public static String writeTurnIn(int numTroops){
        return "TURNIN:" + numTroops;

    }


    /**
     * Generates a coded message for an attack roll action.
     * @param attacker - Attacking territory
     * @param defender - Defending territory
     * @param attackDice - Number of attack dice rolled
     * @return Attack message
     */
    public static String writeAttack(Territory attacker, Territory defender, int attackDice){
        return "BATTLE:" + attacker.getID() + ":" + defender.getID() + ":" + attackDice;
    }


    /**
     * Generates a coded message for a defense roll action.
     * @param defenseRoll - Number of defense dice rolled
     * @return Defense message
     */
    public static String writeDefense(int defenseRoll){
        return "DEFENSE:" + defenseRoll;
    }


    /**
     * Generates a coded message for battle rolls.
     * @param attackDice Attack dice values rolled
     * @param defenseDice Defense dice values rolled
     * @return Rolls message
     */
    public static String writeRolls(ArrayList<Integer> attackDice, ArrayList<Integer> defenseDice){
        String msg = "ROLLS:";
        for (Integer i: attackDice){
            msg += i.intValue();
        }
        msg += ":";
        for (Integer i: defenseDice){
            msg += i.intValue();
        }
        return msg;
    }


    /**
     * Generates a coded message for a transfer action.
     * @param src Source territory
     * @param dst Destination Territory
     * @param numTroops Number of troops moved.
     * @return Transfer message
     */
    public static String writeTransfer(Territory src, Territory dst, int numTroops){
        return "TRANSFER:" + src.getID() + ":" + dst.getID() + ":" + numTroops;
    }


    /**
     * Generates a coded message for a pass turn action.
     * @return Pass message
     */
    public static String writePass(){
        return "PASS:";
    }


    /**
     * Generates a coded message for a disconnection.
     * @return Disconnection message
     */
    public static String writeDisconnect(){
        return "DISCONNECTED:";
    }


    /**
     * Generates a coded message for victory declaration.
     * @return Victory message
     */
    public static String writeVictory(){
        return "VICTORY:";
    }

}