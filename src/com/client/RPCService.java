package com.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.shared.LobbyInfo;

@RemoteServiceRelativePath("game")
public interface RPCService extends RemoteService {

    public LobbyInfo getLobby();
    public LobbyInfo getCustomLobby(LobbyInfo info);
    public LobbyInfo getLobbyTerritories(int lobbyID);
    public boolean checkLobbyReadyToStart(int lobbyID);
    public void sendUpdate(int lobbyID, int playerID, String message);
    public String receiveUpdate(int lobbyID, int playerID, int messageIndex);

}