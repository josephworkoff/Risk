/** File: RPCServiceImpl.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: Implementation class for GWT RPC. Coordinates communication between clients.
*/

package com.server;

import com.client.RPCService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.shared.LobbyInfo;


import java.util.ArrayList; 

public class RPCServiceImpl extends RemoteServiceServlet implements RPCService {

    ArrayList<Lobby> lobbies = new ArrayList<Lobby>();

    /**
     * Finds an open lobby, or creates one.
     * @return LobbyInfo object with lobbyid and player id set
     */
    public LobbyInfo getLobby(){
        if (lobbies == null){
            lobbies = new ArrayList<Lobby>();
        }
        if (lobbies.size() > 0){
            for (Lobby l: lobbies){
                //Find an open lobby
                if (l.getStatus() == Lobby.Status.EMPTY || l.getStatus() == Lobby.Status.SEARCHING){
                    int pid = l.connectPlayer();
                    if (pid > 0){
                        return new LobbyInfo(l.getID(), pid);
                    }
                }
            }
        }
        
        Lobby lobby = new Lobby();
        
        int lobbyID = lobbies.size();
        
        lobby.setID(lobbyID);
        lobbies.add(lobby);
        
        int pid = lobby.connectPlayer();

        return new LobbyInfo(lobby.getID(), pid);
    }



    /**
     * Informs the client whether their game is ready to begin.
     * @param lobbyID
     * @return True if 2 players are connected.
     */
    public boolean checkLobbyReadyToStart(int lobbyID){
        return (lobbies.get(lobbyID).getStatus() == Lobby.Status.READY);
    }



    /**
     * Creates a lobby with custom settings and returns its information to the client.
     * @param info - LobbyInfo object containing initial gamestate information.
     * @return LobbyInfo object with lobby number and player id
     */
    public LobbyInfo getCustomLobby(LobbyInfo info){
        System.out.println("Received connection for seeded game: initial=" + info.getInitial());
        LobbyInfo lobbyNums = getLobby();
        
        Lobby lobby = lobbies.get(lobbyNums.getLobbyID());

        lobby.seed(info);

        return lobbyNums;
    }


    /**
     * Sends the lobby's initial territory setup to the client.
     * @param lobbyID
     * @return
     */
    public LobbyInfo getLobbyTerritories(int lobbyID){
        Lobby lobby = lobbies.get(lobbyID);
        return lobby.getLobbyInfo();
    }



    /**
     * Sends an update to the other player's server message buffer.
     * @param lobbyID
     * @param playerID
     * @param message
     */
    public void sendUpdate(int lobbyID, int playerID, String message){
        // System.out.println("Message received for Lobby " + lobbyID + ":Player " + playerID + ": " + message);
        Lobby lobby = lobbies.get(lobbyID);
        lobby.writeMessage(playerID, message);
    }


    
    /**
     * Retrieves an update from the player's message buffer.
     */
    public String receiveUpdate(int lobbyID, int playerID, int messageIndex){
        Lobby lobby = lobbies.get(lobbyID);
        return lobby.readMessage(playerID, messageIndex);
    }

}