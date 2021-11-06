package com.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.shared.LobbyInfo;

public interface RPCServiceAsync {

    void getLobby(AsyncCallback<LobbyInfo> callback);
    void getCustomLobby(LobbyInfo info, AsyncCallback<LobbyInfo> callback);
    void checkLobbyReadyToStart(int lobbyID, AsyncCallback<java.lang.Boolean> callback);
    void getLobbyTerritories(int lobbyID, AsyncCallback<LobbyInfo> callback);
    void sendUpdate(int lobbyID, int playerID, String message, AsyncCallback<Void> callback);
    void receiveUpdate(int lobbyID, int playerID, int messageIndex, AsyncCallback<String> callback);
}