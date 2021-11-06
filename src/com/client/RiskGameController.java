/** File: RiskGameController.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: Controller Class for the Risk game
*/

package com.client;

import java.util.Arrays; 
import java.util.Random;

import java.util.ArrayList; 
import java.util.Collections; //Sorting
import java.util.Map;
import java.util.List;
import java.util.PriorityQueue;

import com.client.MessageComparator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.shared.LobbyInfo;


/**
 * Game Controller Class. Contains methods for setting up the game.
 */
public class RiskGameController{

    /** Main Board Object */
    private Board board;

    /**Main Deck object */
    private Deck deck;

    /** Determines Manual or Automatic Troop Placement */
    private boolean autoPlace = false;
    
    /** Total number of players. Hard capped at 2 for the moment. */
    private int numPlayers = 2;

    /** Number of Human Players. */
    private int numHumans;

    /** Number of CPU Players. */
    private int numBots;

    /** Delay in ms between CPU players' actions */
    private int botDelay;

    /** Player Array */
    private Player[] players;

    /** Neutral Player object used to designate territories as neutral at the start of the game. */
    private Player neutralPlayer;

    /** Game Start Timestamp */
    private long startTime;

    private boolean onlineGame = false;
    
    private int lobbyID;
    private int lobbyPlayerID; 
    private int updateIndex = 0;

    private Player localPlayer;
    private Player turnPlayer;

    private PriorityQueue<String> messageQueue;

    private int attackRoll;
    private int defenseRoll;

    /** Territory currently selected for attack */
    private Territory selectedTerritory;

    /** Territory current targeted for attack */
    private Territory targetedTerritory;

    /** Player array index for current turn player */
    private int playerIndex;
    
    /** List of cards being turned in */
    private ArrayList<Card> turnInCards;
    
    /** True if the game has been ended. */
    private boolean gameOver;



    private boolean seeded;


    private DisplayController display;

    private int initialTroopsToPlace = 40;

    private Player foreignPlayer = null;

    private boolean pollingActive;

    private Phase phase;

    enum Phase{
        BEGIN,
        INITIAL_ALLOCATION,
        INITIAL_PLACING,
        ALLOCATION,
        PLACING,
        SELECTING,
        TARGETING,
        ROLLING_ATTACK,
        ROLLING_DEFENSE,
        TRANSFERING
    }
    

    //Getters/Setters

    /**
     * Board setter
     * @param board
     */
    public void setBoard(Board board){this.board = board;}

    /**
     * Deck setter
     * @param deck
     */
    public void setDeck(Deck deck){this.deck = deck;}

    /**
     * numPlayers setter
     * @param numPlayers
     */
    public void setNumplayers(int numPlayers){this.numPlayers = numPlayers;}

    /**
     * numHumans setter
     * @param numHumans
     */
    public void setNumHumans(int numHumans){this.numHumans = numHumans;}

    /**
     * players setter
     * @param players
     */
    public void setPlayers(Player[] players){this.players = players;}

    /**
     * Start Time setter
     * @param timestamp
     */
    public void setStartTime(long timestamp){this.startTime = timestamp;}

    public void setDisplayController(DisplayController display){this.display = display;}

    /**
     * Board getter
     * @return board
     */
    public Board getBoard(){return this.board;}

    /**
     * Board getter
     * @return board
     */
    public Deck getDeck(){return this.deck;}

    /**
     * deck getter
     * @return board
     */
    public boolean getAutoPlace(){return this.autoPlace;}

    /**
     * numplayers getter
     * @return board
     */
    public int getNumplayers(){return this.numPlayers;}

    /**
     * numhumans getter
     * @return board
     */
    public int getNumHumans(){return this.numHumans;}

    /**
     * players getter
     * @return board
     */
    public Player[] getPlayers(){return this.players;}

    /**
     * start time getter
     * @return board
     */
    public long getStartTime(){return this.startTime;}

    /**
     * neutral player getter
     * @return board
     */
    public Player getNeutralPlayer(){return this.neutralPlayer;}

    public Player getLocalPlayer(){return this.localPlayer;}

    public void setLobbyID(int lobbyID){this.lobbyID = lobbyID;}
    public void setPlayerLobbyID(int playerLobbyID){this.lobbyPlayerID = playerLobbyID;}
    public void setupdateIndex(int updateIndex){this.updateIndex = updateIndex;}

    /**
     * Default Constructor.
     */
    public RiskGameController() {

        board = new Board();
        deck = new Deck(board);
        
        this.numPlayers = 2;
        this.numHumans = 2;
        this.numBots = 0;
        
    }



    /**
     * Constructs Controller with a specific number of players and bots
     * @param numPlayers - Number of human players
     * @param numBots - Number of CPU players
     */
    public RiskGameController(int numPlayers, int numBots){
        this.numPlayers = numPlayers;
        this.numBots = numBots;

        this.board = new Board();
        this.deck = new Deck(board);

        createPlayers();
    }


    /**
     * Constructs Controller based on map of options.
     * Map should contain "players", "bots", "delay" parameters .
     * If these are missing or have invalid values, Controller is constructed with default settings: 2 Players, 0 Bots.
     * @param params - Param map in format from Location.getParameterMap()
     */
    public RiskGameController(Map<String, Integer> gameSettings){
        this.numPlayers = 2;
        this.numBots = 0;
        this.numHumans = 2;
        this.botDelay = 2000;
        this.initialTroopsToPlace = 40;

        // if (gameSettings.keySet().contains("PLAYERS")){
        //     this.numPlayers = gameSettings.get("PLAYERS");
        // }
        if (gameSettings.keySet().contains("BOTS")){
            this.numBots = gameSettings.get("BOTS");
        }
        if (gameSettings.keySet().contains("BOTDELAY")){
            this.botDelay = gameSettings.get("BOTDELAY");
        }
        if (gameSettings.keySet().contains("INITIAL")){
            this.initialTroopsToPlace = gameSettings.get("INITIAL");
        }

        //Make sure number of bots is less than number of players
        if (this.numBots > this.numPlayers){
            this.numBots = 0;
        }
        else{
            this.numHumans = this.numPlayers - this.numBots;
        }

        this.board = new Board();
        this.deck = new Deck(board);

        // createPlayers();
    }



    /** 
     * Creates Human and Computer players according to numPlayers and numBots.
     */
    public void createPlayers(){
        Util.debug("Creating local players");

        String buff = "";
        String color;
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        String[] names = {"Player1", "Player2", "Player 3", "Player 4"};

        int pidx = 0;
        
        players = new Player[this.numPlayers];

        //get Human Players
        for (pidx = 0; pidx < this.numHumans; pidx++){
            players[pidx] = new HumanPlayer();

            players[pidx].setName(names[pidx]);
            players[pidx].setID(pidx + 1);
            players[pidx].setColor(colors[pidx]);
            players[pidx].setController(this);
            
            Util.debug("Created local player " + players[pidx].getColor() + " " + players[pidx].getID());
        }
        
        //create CPU player if necessary
        
        for (int i = pidx; i < this.numHumans + this.numBots; i++){
            players[i] = new ComputerPlayer(i, "CPU", colors[i], botDelay);
            players[i].setController(this);

            Util.debug("Created CPU player " + players[i].getColor() + " " + players[i].getID());
        }

        //Create grey player
        this.neutralPlayer = new ComputerPlayer(-1, "Neutral", "Grey", 0);
        this.neutralPlayer.setController(this);

        this.localPlayer = null;
    }



    /**
     * Creates players for an online game.
     * @param playerID - Server player id of the local player
     */
    public void createPlayers(int playerID){
        Util.debug("Creating online players");

        String buff = "";
        String color;
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        String[] names = {"Player1", "Player2", "Player 3", "Player 4"};

        int pidx = 0;
        
        players = new Player[this.numPlayers];

        //get Human Players
        for (pidx = 0; pidx < this.numHumans; pidx++){

            if (pidx == playerID - 1){
                players[pidx] = new HumanPlayer();
                this.localPlayer = players[pidx];
            }
            else{
                players[pidx] = new ForeignPlayer();
                players[pidx].setLobbyID(this.lobbyID);
                players[pidx].setLobbyPlayerID(this.lobbyPlayerID);
                this.foreignPlayer = players[pidx];
            }


            players[pidx].setName(names[pidx]);
            players[pidx].setID(pidx + 1);
            players[pidx].setColor(colors[pidx]);
            players[pidx].setController(this);


            Util.debug("Created " + players[pidx].isA() + " Player " + players[pidx].getColor() + " PlayerID:" + players[pidx].getID());
        }
        Util.debug("LocalPlayer = " + this.localPlayer.getColor());
    
        //Create grey player
        this.neutralPlayer = new ComputerPlayer(-1, "Neutral", "Grey", 0);
        this.neutralPlayer.setController(this);
    }



    /**
     * Assigns territory ownership and occupancy based on map of parameters for local games.
     * At the end, marks all unassigned territories as neutral.
     * @param territorySeeds - List of temporary territory objects
     */
    public void seededStart(List<Territory> territorySeeds){
        int redIDX = 0;
        int blueIDX = 0;

        int idx = 0;

        int terrID = 0;
        Territory terr = null;

        //Map player color indices
        for (int i = 0; i < this.players.length; i++){
            if (players[i].getColor() == "Red"){
                redIDX = i;
            }
            else if (players[i].getColor() == "Blue"){
                blueIDX = i;
            }
        }

        //Assign specified territories
        for (Territory t: territorySeeds){
            if (t.getName() == "Red"){
                idx = redIDX;
            }
            else if(t.getName() == "Blue"){
                idx = blueIDX;
            }

            terr = board.getTerritoryByID(t.getID());
            players[idx].gainTerritory(t.getOccupancy(), terr);
        }

        //Mark all unassigned territories as neutral
        for (Territory t: board.getTerritories()){
            if (t.getOwner() == null){
                this.neutralPlayer.gainTerritory(1, t);
            }
        }

    }



    /**
     * Seeds the game with the lobby's settings.
     * @param info Lobbyinfo object containing the initial territory owners, occupancies, and initial troops to allocate
     */
    public void multiplayerStart(LobbyInfo info){
        Util.debug("Multiplayer start. info.initial="+ info.getInitial());
        this.onlineGame = true;       
        int player;
        int occ;
        Territory terr;
        for (int i = 0; i < 42; i++){
            player = info.getTerrOwners()[i];
            terr = board.getTerritoryByIndex(i);
            occ = (info.getTerrSeeds()[i] < 1) ? 1 : info.getTerrSeeds()[i];
            
            if (player == 0){
                this.neutralPlayer.gainTerritory(occ, terr);
            }
            else{
                this.players[player - 1].gainTerritory(occ, terr);
            }
        }
        this.initialTroopsToPlace = (info.getInitial() == 0) ? 40 : info.getInitial();
    }



    /**
     * Assign seeded cards to players for local games.
     * @param cardSeeds - List of temporary card objects
     */
    public void seededCards(List<Card> cardSeeds){
        int idx = 0;
        int terrID = 0;
        Territory terr = null;
        String design = "";
        Card card = null;

        for (Card c: cardSeeds){
            idx = c.getLocation();
            terr = board.getTerritoryByID(c.getID());

            card = deck.createNewCard(terr, c.getDesign());

            players[idx].earnCard(card);
        }

    }



    /**
     * Randomly allocates all territories to the players. 
     */
    public void randomStart(){
        Random rand = new Random();
        int randomTerritory = rand.nextInt(42);

        int turnPlayer = 0;
        int numClaimed = 0;

        Territory terr;

        //Randomly assign neutral territories, as per 2-player rules.
        if (this.numPlayers == 2){
            for (int i = 0; i < 14; i++){
                while (board.getTerritoryByIndex(randomTerritory).getOwner() != null){
                    randomTerritory = rand.nextInt(42);
                }
                terr = board.getTerritoryByIndex(randomTerritory);
                this.neutralPlayer.gainTerritory(1, terr);
                numClaimed++;
            }
        }

        while (numClaimed < 42){

            //Randomly assign an unoccupied territory.
            while (board.getTerritoryByIndex(randomTerritory).getOwner() != null){
                randomTerritory = rand.nextInt(42);
            }
            terr = board.getTerritoryByIndex(randomTerritory);
            players[turnPlayer].gainTerritory(1, terr);
            players[turnPlayer].gainPower(1);

            numClaimed++;
            turnPlayer = (turnPlayer + 1) % numPlayers;
        }

    }



    /**
     * Sends a message to the server, including this client's lobbyid and playerid
     * @param message Coded message string
     * @param callback Callback object
     */
    public void broadcast(String message, AsyncCallback<Void> callback){
        // Util.debug("Controller.broadcast");
        // Util.debug(": PID" + this.lobbyPlayerID + " LID" + this.lobbyID);
        if (this.onlineGame){
            Broadcaster.broadcast(this.lobbyID, this.lobbyPlayerID, message, callback);
        }
    }


    /**
     * Requests a message from the server for this client.
     * @param callback
     */
    public void receive(AsyncCallback<String> callback){
        // Util.debug("Controller.Receive: PID" + this.lobbyPlayerID + " LID" + this.lobbyID);
        if (this.onlineGame && this.pollingActive){
            Broadcaster.receive(this.lobbyID, this.lobbyPlayerID, this.updateIndex, callback);
        }
    }










    AsyncCallback<Void> testBroadcastCallback = new AsyncCallback<Void>(){
        public void onFailure(Throwable caught) {
            Util.debug("ERROR: FAILED TO WRITE MESSAGE TO SERVER");
        }
      
        public void onSuccess(Void v) {
            Util.debug("Message broadcasted to server.");
        }
    };








    /**
     * Callback object passed when requesting a message.
     */
    private AsyncCallback<String> receiveUpdateCallback = new AsyncCallback<String>(){
        public void onFailure(Throwable caught) {
            Util.debug("ERROR: FAILED TO RECEIVE MESSAGE FROM SERVER");
        }
      
        public void onSuccess(String msg) {
            if (!msg.equals("NONE"))
                Util.debug("Message received from server: " + msg);
            queueUpdate(msg);
            receive(this);

        }
    };




    private void beginPolling(){
        Util.debug("Beginning Polling.");
        this.pollingActive = true;
        receive(receiveUpdateCallback);
    }
    
    private void stopPolling(){
        Util.debug("Stopping Polling.");
        this.pollingActive = false;
    }



    /**
     * Callback object passed when broadcasting a message.
     */
    AsyncCallback<Void> broadcastCallback = new AsyncCallback<Void>(){
        public void onFailure(Throwable caught) {
            Util.debug("ERROR: FAILED TO WRITE MESSAGE TO SERVER");
        }
      
        public void onSuccess(Void v) {
            Util.debug("Message broadcasted to server.");
        }
    };



    /**
     * Performs start-of-game housekeeping
     * If the game has a random start, begins the initial allocation phase.
     * Otherwise begins with the first turn.
     * @param seeded - True if the game was seeded through a Get query string
     */
    public void readyGame(boolean seeded){        
        
        if (this.onlineGame){
            this.messageQueue = new PriorityQueue<>(new MessageComparator());
            beginPolling();
        }

        Util.debug("Entering Controller.ReadyGame:");

        this.selectedTerritory = null;
        this.targetedTerritory = null;
        this.turnInCards = new ArrayList<Card>();
        this.gameOver = false;

        this.display.readyGame(localPlayer);

        for (Player p: this.players){
            p.setInitialTroopsToPlace(this.initialTroopsToPlace);
        }
        this.playerIndex = 0;
        this.turnPlayer = this.players[0];

        if (!seeded){
            this.turnPlayer.setTroopsToPlace(2);
            readyTurn(this.turnPlayer);
            initialAllocation();
        }
        else{
            readyTurn(this.turnPlayer);
            players[0].takeTurn();
        }
    }


    /**
     * Performs start-of-turn housekeeping. 
     * Cleans displays.
     * Disables all buttons.
     * Resets selected/targetedTerritories
     * Sets the turnPlayer
     * Enables the Start button and prompts to click.
     * @param player - Turn player
     */
    public void readyTurn(Player player){
        /**
         * Click Start Turn
         * > Click Turn In
         * > Forced to Turn In
         *      Submit Cards (Click 3 buttons, click submit)
         * 
         * > Click Reinforce
         * 
         * Loop:
         *      Select a territory
         *      Enter number of troops (Input number, click submit)
         * End Loop:
         * 
         * Loop:
         *      Select a Territory
         *      Select enemy adjacent territory
         *      Enter number of dice (Click button, click submit)
         *      Wait for enemy selection
         * 
         *      > Enter number to move (Input number, click submit)
         *      
         * Pass  
         * 
         * End Loop:
         * 
         * 
         */

        
        this.phase = Phase.BEGIN;
        
        this.turnPlayer = player;
        
        Util.debug(this.turnPlayer.isA() + " " + this.turnPlayer.getColor() + " Entering Controller.ReadyTurn:");

        this.attackRoll = 0;
        this.defenseRoll = 0;

        this.selectedTerritory = null;
        this.targetedTerritory = null;

        this.turnInCards.clear();

        this.display.readyTurn(this.turnPlayer);
        

    }




    /**
     * Handles back and forth allocation at the start of the game.
     * Sets Phase to INITIAL_ALLOCATION
     */
    public void initialAllocation(){

        Util.debug(this.turnPlayer.getColor() +  " Controller.InitialAllocation");

        this.phase = Phase.INITIAL_ALLOCATION;

        this.turnPlayer.chooseOwnedTerritory(true);
    }


    
    /**
     * Performs allocation of troops.
     * Checks if the player is out of troops to place.
     * True:     
     *      PLACING: Calls battlePhase.
     *      INITIAL_PLACING: calls betweenPlayerAllocations
     * False: 
     *      Calls reinforcePhase
     *      Calls initialAllocation 
     * @param terr Territory to reinforce
     * @param troops Number of troops to place
     */
    public void reinforce(Territory terr, int troops) {
        Util.debug(turnPlayer.getColor() +  " Entering controller.reinforce during phase " + this.phase);
        
        this.turnPlayer.allocateTroops(terr, troops);
        Util.debug("Controller.Reinforce: Placed " + troops + " troops in " + terr.getName());
        this.display.localReinforcementsPlacedDisplay(turnPlayer, terr, troops);
        
        //Send Reinforcement Broadcast
        if (this.onlineGame && (this.turnPlayer == this.localPlayer)){
            broadcast(Broadcaster.writeReinforce(terr, troops), broadcastCallback);
        }

        //Placing during reinforcement phase
        if (this.phase == Phase.ALLOCATION){
            //If all troops placed, move to battle phase.
            if (turnPlayer.getTroopsToPlace() <= 0){
                battlePhase();
            }
            //otherwise keep placing
            else{
                reinforcePhase();
            }
        }
        //Placing during initial allocation
        else if (this.phase == Phase.INITIAL_ALLOCATION){
            //Player has placed 2 and is done.
            if (this.turnPlayer.getTroopsToPlace() <= 0){
                betweenPlayerAllocations();
            }
            //Player has more to place.
            else{
                initialAllocation();
            }
        }
        else{
            Util.debug("In reinforce: controller.Reinforce reached during wrong phase: " + this.phase);
        }
        
    }



    /**
     * Called when a player has placed their two troops during initial allocation.
     * Randomly places a troop in a neutral territory.
     * Checks if all players have completed allocation.
     * If so, begins the game by calling player 1's taketurn.
     * Otherwise, moves to the next player.
     */
    private void betweenPlayerAllocations(){

        Util.debug(turnPlayer.getColor() +  " Entering controller.betweenPlayerAllocations");
        
        //place a random grey troop
        if (this.neutralPlayer != null){
            this.neutralPlayer.allocateTroop();
        }
        
        boolean begin = true;
        for (Player p: this.players){
            if (p.getInitialTroopsToPlace() > 0){
                begin = false;
            }
        }
        
        if (begin){
            Util.debug("controller.betweenPlayerAllocations: Beginning game");
            this.playerIndex = 0;
            this.turnPlayer = this.players[0];
            this.turnPlayer.takeTurn();
        }
        else{
            Util.debug("controller.betweenPlayerAllocations: Moving to next player.");
            this.playerIndex = (this.playerIndex + 1) % this.players.length;
            this.players[this.playerIndex].setTroopsToPlace(2);
            readyTurn(this.players[this.playerIndex]);
            initialAllocation();
        }
    }


    /**
     * Displays the Start of Turn UI
     * Prompts for card selection, or advancement to reinforcement phase.
     * Forces card selection if 5 or more cards in hand.
     */
    public void startTurn(){

        // Util.debug(turnPlayer.getColor() +  " Entering controller.startTurn");

        if (this.gameOver){
            return;
        }

        this.turnPlayer.startTurn();

        int handSize = turnPlayer.getCards().size();

        Util.debug(turnPlayer.getColor() +  " Entering controller.startTurn.\n" + 
            handSize + " cards in hand.");

        for (Card c: this.turnPlayer.getCards()){
            Util.debug(c.getCardString());
        }
        Util.debug("Match: " + Deck.matchInHand(this.turnPlayer.getCards()));

        //Players must turn in cards when they have 5 or more cards
        if (handSize >= 5){
            this.turnPlayer.chooseCards();
        }
        //
        else if (handSize >= 3 && Deck.matchInHand(this.turnPlayer.getCards())){
            this.turnPlayer.chooseToTurnInCards();
        }
        else{
            reinforcePhase();
        }

    }


    /**
     * Displays the Card Turn-In UI
     */
    public void turnInPhase() {
        Util.debug(turnPlayer.getColor() +  " Entering turnInPhase");
        this.turnPlayer.chooseCards();
    }


    /**
     * Handles trading in a set of 3 matching cards.
     * Assumes the cards have been checked and match.
     * Grants the turn player bonus troops. 
     * Removes the cards from the game and clears the input list.
     * Moves to reinforcePhase.
     * @param cards ArrayList of 3 cards to turn in.
     */
    public void turnInSet(ArrayList<Card> cards){
        Util.debug(turnPlayer.getColor() +  " Entering controller.turnInSet\n");
        //Bonus 2 troops for turning in a card whose territory player owns.
        int bonus = 0;
        for (Card c: cards){
            Util.debug(c.getCardString());
            if (this.turnPlayer.getTerritories().contains(c.getTerritory())){
                bonus = 2;
            }
        }

        //Add bonus to player's troopsToPlace

        int earned = this.deck.tradeIn(cards) + bonus;

        this.turnPlayer.setTroopsToPlace(this.turnPlayer.getTroopsToPlace() + earned);

        //Remove cards from game
        this.turnPlayer.discardCards(cards);

        //empty turnin list
        cards.clear();

        //Display
        this.display.localTradeInSetDisplay(this.turnPlayer, earned);

        //Move on
        reinforcePhase();
    }



    /**
     * Displays the Reinforcement Phase UI.
     * Sets Phase to ALLOCATING
     * Prompts the player to select a territory to reinforce.
     */
    public void reinforcePhase(){
        Util.debug(turnPlayer.getColor() +  " Entering controller.reinforcePhase.");

        this.selectedTerritory = null;
        this.phase = Phase.ALLOCATION;

        this.turnPlayer.chooseOwnedTerritory(false);
    }



    /**
     * Displays the Battle Phase UI.
     * Resets selected/targeted Territories
     * Sets Phase to SELECTING
     * Prompts player to select an attacker.
     */
    public void battlePhase(){
        Util.debug(turnPlayer.getColor() +  " Entering battlePhase");

        this.selectedTerritory = null;
        this.targetedTerritory = null;

        this.phase = Phase.SELECTING;

        this.turnPlayer.chooseAttacker();

        // Next step is selectTerritory
        // Player gets there from TerritoryButtonClickHandler
        // CPU calls it from chooseAttacker
    }



    /**
     * Gets the attacker, defender, and attackdice from the attacker.
     * Prompts defender for defensedice
     */
    public void antebellum(Territory attacker, Territory defender, int attackRoll){
        this.selectedTerritory = attacker;
        
        this.targetedTerritory = defender;
        this.attackRoll = attackRoll;

        //Send Attack Broadcast
        if (this.onlineGame && (this.turnPlayer == this.localPlayer)){
            broadcast(Broadcaster.writeAttack(attacker, defender, attackRoll), broadcastCallback);
        }

        Util.debug(turnPlayer.getColor() +  " Entering controller.antebellum, ST=" 
            + this.selectedTerritory.getName() + ", TT=" 
            + this.targetedTerritory.getName() + ", AR="
            + attackRoll);        

        defender.getOwner().chooseNumberOfDefenseDice(attacker, defender, attackRoll);

    }




    /**
     * Prepares for a battle between the selected and targeted territories.
     */
    public void bellum(int defenseRoll){

        //Send Battle Broadcast
        if (this.onlineGame && (this.turnPlayer == this.foreignPlayer)){
            broadcast(Broadcaster.writeDefense(defenseRoll), broadcastCallback);
        }

        Util.debug(turnPlayer.getColor() +  " Entering controller.bellum\nAttacker=" + this.selectedTerritory.getName() +
        "\nTT=" + this.targetedTerritory.getName() +
        "\naRoll=" + this.attackRoll + "\ndRoll=" + this.defenseRoll);

        this.defenseRoll = defenseRoll;

        //Only wait for rolls if it's the foreign player's turn
        if ( this.onlineGame && this.turnPlayer != localPlayer){
            return;
        }
        ArrayList<ArrayList<Integer>> rolls = generateRolls(this.attackRoll, this.defenseRoll);
        postbellum(rolls.get(0), rolls.get(1));
    }


    /**
     * Generates random rolls for the attacking and defending territories.
     * @return ArrayList of 2 Arraylists with damages.
     *      0 - Attacker
     *      1 - Defender
     */
    private ArrayList<ArrayList<Integer>> generateRolls(int attackRoll, int defenseRoll){

        Util.debug(turnPlayer.getColor() +  " Entering generateRolls");

        ArrayList<Integer> attackDice = new ArrayList<Integer>();
        
        ArrayList<Integer> defenseDice = new ArrayList<Integer>();
        Random rand = new Random();

        int roll = 0;

        for (int i = 0; i < attackRoll; i++){
            //if cheater flag is set, roll 7s
            roll = (this.turnPlayer.getCheater()) ? 7 : rand.nextInt(6) + 1;
            attackDice.add(roll);
        }
        for (int i = 0; i < defenseRoll; i++){
            defenseDice.add(rand.nextInt(6) + 1);
        }
        
        //Sort by descending
        Collections.sort(attackDice, Collections.reverseOrder());
        Collections.sort(defenseDice, Collections.reverseOrder());

        Util.debug(attackDice.toString() + " " + defenseDice.toString() + " ");

        //return
        ArrayList<ArrayList<Integer>> rolls = new ArrayList<ArrayList<Integer>>();
        rolls.add(attackDice);
        rolls.add(defenseDice);
        return rolls;
        
    }



    /**
     * Applies the damages to two territories.
     * Checks if the defender was conquered. If so, calls conquered.
     * Otherwise prompts attacker to roll again.
     * @param attackDice
     * @param defenseDice
     */
    private void postbellum(ArrayList<Integer> attackDice, ArrayList<Integer> defenseDice){

        //Send Rolls Broadcast
        if (this.onlineGame && (this.turnPlayer == this.localPlayer)){
            broadcast(Broadcaster.writeRolls(attackDice, defenseDice), broadcastCallback);
        }

        Util.debug(turnPlayer.getColor() +  " Entering postbellum");

        int[] damages = damageCalculation(attackDice, defenseDice);

        this.display.localDisplayRolls(attackDice, defenseDice);

        this.selectedTerritory.takeDamage(damages[0]);

        //conquered
        if (this.targetedTerritory.takeDamage(damages[1])){
            conquer();
        }
        else{
            this.display.localBattleResultsDisplay(this.selectedTerritory, damages[0], this.targetedTerritory, damages[1]);
            this.turnPlayer.chooseNumberOfAttackDice(this.selectedTerritory, this.targetedTerritory);
        }
        
    }



    /**
     * Performs damage calculation for a battle.
     * Compares the highest rolls from the attackDice and defenseDice.
     * @param attackDice
     * @param defenseDice
     * @return Damages - Integer array of attacker damage and defender damage.
     */
    private int[] damageCalculation(ArrayList<Integer> attackDice, ArrayList<Integer> defenseDice){
        
        Util.debug(turnPlayer.getColor() +  " Entering damageCalculation");

        int higherDiceCount = (attackDice.size() > defenseDice.size()) ? defenseDice.size() : attackDice.size();
        int attackDamage = 0;
        int defenseDamage = 0;

        //Compare dice rolls
        for (int i = 0; i < higherDiceCount; i++){
            if (attackDice.get(i) > defenseDice.get(i)){
                defenseDamage++;
            }
            else{
                attackDamage++;
            }
        }

        return new int[] {attackDamage, defenseDamage};
    }



    /**
     * Conquers a Territory after battle.
     * Prints the battle result.
     * Sets Player's earnedCard flag.
     * Sets Phase to TRANSFERING
     * Prompts the attacker to choose a number of troops to transfer.
     */
    private void conquer(){
        Util.debug(this.selectedTerritory.getName() + this.selectedTerritory.getOccupancy() + 
        " conqd " + this.targetedTerritory.getName() + this.targetedTerritory.getOccupancy() + " ");

        this.turnPlayer.setEarnedCard(true);

        this.display.localConquerDisplay(this.turnPlayer, this.targetedTerritory);

        this.phase = Phase.TRANSFERING;

        this.turnPlayer.chooseNumberOfTroopsToMove(this.attackRoll, this.selectedTerritory, this.targetedTerritory);

        // Next step is transfer
        // Player gets there from submitHandler
        // CPU calls it from chooseNumberOfTroopsToMove

    }



    /**
     * Moves troops from the attacking territory into the conquered territory.
     * Triggers victory if only one player left.
     * Otherwise returns to the battlephase
     * @param numTroops - Troops to transfer
     */
    public void transfer(int numTroops){

        Util.debug(turnPlayer.getColor() +  " Entering controller.transfer, moving: " + numTroops);

        //Send transfer Broadcast
        if (this.onlineGame && (this.turnPlayer == this.localPlayer)){
            broadcast(Broadcaster.writeTransfer(this.selectedTerritory, this.targetedTerritory, numTroops), broadcastCallback);
        }

        Util.debug(this.selectedTerritory.getName() + this.selectedTerritory.getOccupancy() + 
        "-" + this.targetedTerritory.getName() + this.targetedTerritory.getOccupancy() + " ");

        boolean contConquered = this.targetedTerritory.getContinent().checkDomination();

        this.display.localTransferDisplay(this.turnPlayer, this.selectedTerritory, this.targetedTerritory, numTroops, contConquered);

        if (board.moveUnitsAfterVictory(this.selectedTerritory, this.targetedTerritory, numTroops)){
            triggerVictory(false);
        }
        else{
            battlePhase();
        }
    }


    /**
     * Ends the game in favor of the turn player.
     */
    public void triggerVictory(boolean dc){

        //Send Victory Broadcast
        if (this.onlineGame && (this.turnPlayer == this.localPlayer)){
            broadcast(Broadcaster.writeVictory(), broadcastCallback);
        }

        this.gameOver = true;

        Util.debug(turnPlayer.getColor() +  " controller.triggerVictory");

        long end = System.currentTimeMillis();
        long start = this.startTime;

        long diff = end - start;

        int seconds = (int)(diff/ 1000) % 60 ;
        int minutes = (int)((diff/ (1000*60)) % 60);
        int hours = (int)((diff/ (1000*60*60)) % 24);

        String h = Integer.toString(hours);
        String m = Integer.toString(minutes);
        String s = Integer.toString(seconds);

        h = (h.length() == 1) ? "0" + h : h;
        m = (m.length() == 1) ? "0" + m : m;
        s = (s.length() == 1) ? "0" + s : s;

        String timestamp = h + ":" + m + ":" + s;

        Util.debug("Won w/ " + this.turnPlayer.getContinents().toString());

        this.display.localVictoryDisplay(this.turnPlayer, dc, timestamp);
        stopPolling();

    }



    /**
     * Performs end of turn housekeeping.
     * Deals a card if turnplayer conquered a territory this turn.
     * Sets turnPlayer + turnIndex to the next player.
     */
    public void endPhase(){

        //Send Battle Broadcast
        if (this.onlineGame && (this.turnPlayer == this.localPlayer)){
            broadcast(Broadcaster.writePass(), broadcastCallback);
        }

        Util.debug(turnPlayer.getColor() +  " Entering endPhase");

        
        if (this.turnPlayer.getEarnedCard()){
            turnPlayer.earnCard(this.deck.draw());
        }

        this.display.localEndPhaseDisplay();
        this.turnPlayer.setCheater(false);
        
        // this.turnPlayer = ;
        // this.turnPlayer.takeTurn();
        
        this.playerIndex = (playerIndex + 1) % this.players.length;
        readyTurn(this.players[playerIndex]);
        this.players[playerIndex].takeTurn();

    }







    /**
     * Sets the turn player, and displays it in the Player Display.
     */
    public void setTurnPlayer(Player player) {
        // this.displayGUI.printPlayer(player.getColor() + "'s Turn");
        this.turnPlayer = player;
    }
    


    private void queueUpdate(String msg){
        // Util.debug("Controller.queueUpdate");
        String code = msg.split(":")[0];

        if (code.equals("DISCONNECTED")){
            updateHandler(msg);
            return;
        }

        if (!code.equals("NONE")){
            this.updateIndex++;
            this.messageQueue.add(msg);
            for (String message : this.messageQueue) 
                Util.debug(message); 
        }
        else{
            while (!this.messageQueue.isEmpty()){
                updateHandler(this.messageQueue.poll());
            }
        }
    }



    /**
     * Parses the message code and calls the appropriate function
     * @param msg Message
     */
    private void updateHandler(String msg){
        Util.debug("Entering Controller.updateSwitch with msg: " + msg);
        String[] params = msg.split(":");
        String code = params[0];
        
        switch (code) {
            case "REINFORCE":
                reinforceUpdate(params);
                break;
            case "TURNIN":
                turnInUpdate(params);
                break;
            case "BATTLE":
                battleUpdate(params);
                break;
            case "DEFENSE":
                defenseUpdate(params);
                break;
            case "ROLLS":
                rollsUpdate(params);
                break;
            case "TRANSFER":
                transferUpdate(params);
                break;
            case "PASS":
                passUpdate();
                break;
            case "DISCONNECTED":
                disconnectedUpdate();
                break;
            case "VICTORY":
                victoryUpdate();
                break;
            default:
                Util.debug("READ BAD MESSAGE IN FOREIGNPLAYER.UPDATESWITCH: "  + msg);
                break;
        }
    }


    private void reinforceUpdate(String[] code){
        Util.debug("Entering controller.reinforceUpdate with msg: " + code);

        int terrID = -1;
        int numTroops = -1;
        try {
            terrID = Util.parseInt(code[1]);
            numTroops = Util.parseInt(code[2]);
        } catch (Exception e) {
            Util.debug("READ BAD MESSAGE IN controller.REINFORCEUPDATE: " + code + "\n" + e.getMessage());
            return;
        }

        Territory terr = getBoard().getTerritoryByID(terrID);
        reinforce(terr, numTroops);

        Util.debug("Parsed args in controller.reinforceUpdate:" + 
        "\nterrID=" + terrID + 
        "\nnumTroops=" + numTroops
        );

    }


    private void turnInUpdate(String[] code) {
        Util.debug("Entering controller.turnInUpdate with msg: " + code);

        int numTroopsEarned = -1;

        try {
            numTroopsEarned = Util.parseInt(code[1]);
        } catch (Exception e) {
            Util.debug("READ BAD MESSAGE IN FOREIGNPLAYER.turnInUpdate: " + code + "\n" + e.getMessage());
            return;
        }
        
        Util.debug("Parsed args in controller.turnInUpdate:" + 
        "\nnumTroopsEarned=" + numTroopsEarned
        );

        this.turnPlayer.setTroopsToPlace(this.turnPlayer.getTroopsToPlace() + numTroopsEarned);
        this.display.localTradeInSetDisplay(this.turnPlayer, numTroopsEarned);
        reinforcePhase();
    }

    private void battleUpdate(String[] code) {
        Util.debug("Entering controller.battleUpdate with msg: " + code);

        int attackerID = -1;
        int defenderID = -1;
        int attackRoll = -1;

        try {
            attackerID = Util.parseInt(code[1]);
            defenderID = Util.parseInt(code[2]);
            attackRoll = Util.parseInt(code[3]);

            

        } catch (Exception e) {
            Util.debug("READ BAD MESSAGE IN controller.battleUpdate: " + code + "\n" + e.getMessage());
            return;
        }

        Util.debug("Parsed args in controller.battleUpdate:" + 
        "\nattackerID=" + attackerID + 
        "\ndefenderID=" + defenderID +
        "\nattackRoll=" + attackRoll
        );

        //Act upon broadcast
        antebellum(board.getTerritoryByID(attackerID), 
        board.getTerritoryByID(defenderID), 
        attackRoll);

    }

    private void defenseUpdate(String[] code) {
        Util.debug("Entering controller.defenseUpdate with msg: " + code);

        int defenseRoll = -1;

        try {
            defenseRoll = Util.parseInt(code[1]);
        } catch (Exception e) {
            Util.debug("READ BAD MESSAGE IN controller.defenseUpdate: " + code + "\n" + e.getMessage());
            return;
        }

        Util.debug("Parsed args in controller.defenseUpdate:" + 
        "\ndefenseRoll=" + defenseRoll
        );

        //Act upon broadcast
        bellum(defenseRoll);


    }

    private void rollsUpdate(String[] code) {
        Util.debug("Entering controller.rollsUpdate with msg: " + code);

        String attacker = "000";
        String defender = "00";

        ArrayList<Integer> attackDice = new ArrayList<Integer>();
        ArrayList<Integer> defenseDice = new ArrayList<Integer>();
        
        try {
            attacker = code[1];
            defender = code[2];
        } catch (Exception e) {
            Util.debug("READ BAD MESSAGE IN controller.rollsUpdate: " + code + "\n" + e.getMessage());
            return;
        }

        int roll;
        for (int i = 0; i < attacker.length(); i++){
            roll = Util.parseInt(attacker.charAt(i));
            attackDice.add(roll);
        }

        for (int i = 0; i < defender.length(); i++){
            roll = Util.parseInt(defender.charAt(i));
            defenseDice.add(roll);
        }

        Util.debug("Parsed args in controller.rollsUpdate:" + 
        "\nattackRolls=" + attackDice + 
        "\ndefenseRolls=" + defenseDice
        );

        postbellum(attackDice, defenseDice);
        
    }

    private void transferUpdate(String[] code) {
        Util.debug("Entering controller.transferUpdate with msg: " + code);

        int srcID = -1;
        int dstID = -1;
        int numTroops = -1;
        
        try {
            srcID = Util.parseInt(code[1]);
            dstID = Util.parseInt(code[2]);
            numTroops = Util.parseInt(code[3]);
        } catch (Exception e) {
            Util.debug("READ BAD MESSAGE IN controller.transferUpdate: " + code + "\n" + e.getMessage());
            return;
        }

        Util.debug("Parsed args in controller.transferUpdate:" + 
        "\nsrcID=" + srcID + 
        "\ndstID=" + dstID +
        "\nnumTroops=" + numTroops
        );

        transfer(numTroops);
    }

    private void passUpdate() {
        Util.debug("Entering controller.passUpdate");
        endPhase();
    }

    private void disconnectedUpdate() {
        Util.debug("Entering controller.disconnectedUpdate");
        this.messageQueue.clear();
        triggerVictory(true);
    }

    private void victoryUpdate() {
        Util.debug("Entering controller.victoryUpdate");
        this.messageQueue.clear();
        triggerVictory(false);
    }







}