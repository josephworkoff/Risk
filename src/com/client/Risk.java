/** File: Risk.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: Entrypoint class for GWT.
*/

package com.client;

import java.util.Map;
import java.util.List;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.shared.LobbyInfo;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window.Location;

import java.util.ArrayList;
import java.util.HashMap; 

import com.google.gwt.user.client.Timer; 

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * Entry point class for GWT
 */
public class Risk implements EntryPoint {

    /**
     * Game Controller
     */
    RiskGameController controller;

    /**
     * Display Controller
     */
    DisplayController displayController;

    /**
     * Board with all territories
     */
    Board board;

    /**
     * Deck with all cards
     */
    Deck deck;

    /**
     * Player array
     */
    Player[] players;


    /**
     * Whether the game was started with url parameters
     */
    boolean seeded = false;


    /**
     * Message buffer
     */
    String msg = "";


    int lobbyID;
    int playerID;

    LobbyInfo lobbyNums;
    LobbyInfo lobbyTerrs;

    Player localPlayer;

    boolean waiting;

    private RPCServiceAsync rpcsvc = GWT.create(RPCService.class);

    Logger logger = Logger.getLogger("");


    public static final int POLLING_RATE = 1000;


    private boolean multiplayer = false;


    /**
     * Parses the map of URL Get Query String parameters given by Location.getParameterMap()
     * Fills the input data structures with temp objects containing specified stats.
     * @param params - Map<String, List<String>> of parameters
     * @param gameSettings - Map<String, Integer>
     *      Parameters parsed as {OPTION: Value} pairs
     * @param territorySeeds - List<Territory>
     *      Parameters parsed into territory objects. Territories' names are player colors. IDs are valid territory IDs.
     * @param cardSeeds - List<Card>
     *      Parsed into Card objects. Card.Location = 0 for Red, 1 for Blue. ID's are valid territory IDs.
     */
    public void parseParameterMap(Map<String, List<String>> params, 
                                        Map<String, Integer> gameSettings, 
                                        List<Territory> territorySeeds,
                                        List<Card> cardSeeds){
        String color = "";
        String s = "";
        char designChar;
        Card.Design design;
        int terrID = 0;
        int num = 0;
        Territory terr = null;
        ArrayList<Integer> list;
        Card c = null;


        for(String str : params.keySet()){
            //Game Settings
            //Number of players setting
            if (str.equalsIgnoreCase("players")){
                // s = params.get(str).get(0);
                // num = Util.parseInt(s);

                // if (num != -999){
                //     gameSettings.put("PLAYERS", num);
                // }
            }
            //Number of Bots setting
            else if (str.equalsIgnoreCase("bots")){
                s = params.get(str).get(0);
                num = Util.parseInt(s);
                if (num != -999){
                    gameSettings.put("BOTS", num);
                }
            }
            else if (str.equalsIgnoreCase("delay")){
                s = params.get(str).get(0);
                num = Util.parseInt(s);
                if (num != -999){
                    gameSettings.put("BOTDELAY", num);
                }
            }
            else if (str.equalsIgnoreCase("initial")){
                s = params.get(str).get(0);
                num = Util.parseInt(s);
                if (num != -999){
                    gameSettings.put("INITIAL", num);
                }
            }

            
            //end settings

            //Card seeds
            else if(str.length() == 2 && str.endsWith("C")){
                switch (str.toLowerCase().charAt(0)) {
                    case 'r':
                        num = 0;
                        break;
                    case 'b':
                        num = 1;
                        break;
                    default:
                        continue;
                }


                //card parameters come in as color: [card,card]
                //loop through this color player's card
                for (String card: params.get(str)){
                    terrID = Util.parseInt(card.substring(0, card.length() - 1));
                    if (terrID < 0 || terrID > 42){
                        continue;
                    }

                    designChar = card.toLowerCase().charAt(card.length() - 1);
                    
                    switch (designChar) {
                        case 'w':
                            design = Card.Design.WILD;
                            break;
                        case 'i':
                            design = Card.Design.INFANTRY;
                            break;
                        case 'c':
                            design = Card.Design.CAVALRY;
                            break;
                        case 'a':
                            design = Card.Design.ARTILLERY;
                            break;
                        default:
                            continue;
                    }

                    this.msg += " " + card +  " " + designChar + " " + design;

                    c = new Card(terrID, null, design);
                    c.setLocation(num);

                    cardSeeds.add(c);

                }
            } //end cards

            //Territory seed
            else if(Util.parseInt(str) > 0 && Util.parseInt(str) <= 42){
                terrID = Util.parseInt(str);

                
                //s = R3, R33
                s = params.get(str).get(0);
                
                //player color
                switch (s.toLowerCase().charAt(0)) {
                    case 'r':
                        color = "Red";
                        break;
                    case 'b':
                        color = "Blue";
                        break;
                    default:
                        continue;
                }
                
                
                // //occupancy
                num = Util.parseInt(s.substring(1));
                if (num <= 0 || num > 100){
                    continue;
                }

                terr = new Territory(terrID, color);
                terr.setOcc(num);

                
                territorySeeds.add(terr);

            } //end territories

        }

    }

    

    /**
     * Checks if the online parameter is in the get query.
     */
    private void checkMultiplayer(){
        Map<String, List<String>> params = Location.getParameterMap();
        for (String str : params.keySet()){
            if (str.toLowerCase().equals("online")){
                this.multiplayer = true;
            }
        }
    }

    Map<String, Integer> gameSettings;
    List<Territory> territorySeeds;
    List<Card> cardSeeds;

    /**
     * Entrypoint function.
     */
    public void onModuleLoad() {

        checkMultiplayer();

        // // Map URL Query String
        Map<String, List<String>> params = Location.getParameterMap();

        this.gameSettings = new HashMap<String, Integer>();
        this.territorySeeds = new ArrayList<Territory>();
        this.cardSeeds = new ArrayList<Card>();

        parseParameterMap(params, gameSettings, territorySeeds, cardSeeds);

        if (!multiplayer){
            Util.debug("LOCAL GAME");
            startGame();
            return;
        }

        if (rpcsvc == null) {
            rpcsvc = GWT.create(RPCService.class);
        }

        AsyncCallback<LobbyInfo> getLobbyCallback = new AsyncCallback<LobbyInfo>(){
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }
          
            public void onSuccess(LobbyInfo lobbyInfo) {
                // lobbyID = lobbyInfo[0];
                // playerID = lobbyInfo[1];
                lobbyNums = lobbyInfo;
                Util.debug("Got Lobby info: LID=" + lobbyNums.getLobbyID() + " PID=" + lobbyNums.getPlayerID());
                awaitStart();
            }
        };

        boolean custom = false;

        LobbyInfo info = new LobbyInfo();
        if (gameSettings.containsKey("INITIAL")){
            info.setInitial(gameSettings.get("INITIAL"));
            custom = true;
        }
        if (territorySeeds.size() > 0){
            int[] terrSeeds = new int[42];
            int[] terrOwners = new int[42];
            for (Territory t: territorySeeds){
                terrSeeds[t.getID() - 1] = t.getOccupancy();
                terrOwners[t.getID() - 1] = (t.getName().equalsIgnoreCase("Red")) ? 1 : 2;
            }
            info.setTerrSeeds(terrSeeds);
            info.setTerrOwners(terrOwners);
            info.setSeededTerritories(true);
            custom = true;
        }

        if (custom){
            Util.debug("Queueing for custom lobby.");
            rpcsvc.getCustomLobby(info, getLobbyCallback);
        }
        else{
            Util.debug("Queueing for regular lobby.");
            rpcsvc.getLobby(getLobbyCallback);
        }



    }


    AsyncCallback<java.lang.Boolean> awaitStartCallback = new AsyncCallback<java.lang.Boolean>(){
        public void onFailure(Throwable caught) {
            // TODO: Do something with errors.
        }
      
        public void onSuccess(java.lang.Boolean ready) {
            if (ready){
                Util.debug("Found player");
                retrieveTerritories();
            }
            else{
                Util.debug("No player found.");
                awaitStart();
            }
        }
    };

    Timer checkLobbyReadyTimer = new Timer() {
        @Override
        public void run(){
            rpcsvc.checkLobbyReadyToStart(lobbyNums.getLobbyID(), awaitStartCallback);
        }
    };  


    /**
     * Begins polling for lobby to start.
     */
    private void awaitStart(){
        Util.debug("Awaiting Start.");
        checkLobbyReadyTimer.schedule(POLLING_RATE);
    }



    /**
     * Retrieves a LobbyInfo from the server with initial territory occupancies.
     */
    private void retrieveTerritories(){
        AsyncCallback<LobbyInfo> getTerritoriesCallback = new AsyncCallback<LobbyInfo>(){
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }
            
            public void onSuccess(LobbyInfo terrInfo) {
                lobbyTerrs = terrInfo;
                Util.debug("Received terrs.");
                startGame();
            }
        };
        
        Util.debug("Retrieving game info");
        rpcsvc.getLobbyTerritories(this.lobbyID, getTerritoriesCallback);


    }
    

    
    /**
     * 
     */
    private void startGame(){

        Util.debug("Starting game.");
        
        if (!multiplayer && gameSettings.size() > 0){
            this.controller = new RiskGameController(gameSettings);
        }
        else{
            this.controller = new RiskGameController();
        }

        if (this.multiplayer){
            this.controller.setLobbyID(lobbyNums.getLobbyID());
            this.controller.setPlayerLobbyID(lobbyNums.getPlayerID());
            this.controller.createPlayers(lobbyNums.getPlayerID());
        }
        else{
            this.controller.createPlayers();
        }
        
        this.board = controller.getBoard();
        Util.debug("Made Board.");
        
        this.deck = controller.getDeck();
        Util.debug("Made Deck.");
        this.players = controller.getPlayers();
        Util.debug("Made Players.");
        
        this.displayController = new DisplayController(controller, board, deck, players);
        this.controller.setDisplayController(this.displayController);

        if (this.multiplayer){
            this.controller.multiplayerStart(lobbyTerrs);
        }
        else{
            if (territorySeeds.size() > 0){
                this.controller.seededStart(territorySeeds);
                seeded = true;
            }
            else{
                this.controller.randomStart();
            }

            if (cardSeeds.size() > 0){
                this.controller.seededCards(cardSeeds);
            }
        }

        Util.debug("DisplayController Created.");
        
        //Render the board
        this.displayController.render(RootLayoutPanel.get());
        Util.debug("Display rendered");
        
        for (Player p: players){
            p.setDisplayController(this.displayController);
        }

        this.controller.getNeutralPlayer().setDisplayController(this.displayController);

        //Time game from now
        this.controller.setStartTime(System.currentTimeMillis());

        //Begin
        int turnPlayer = 0;
        this.controller.readyGame(seeded);
    }

}

