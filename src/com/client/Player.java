/** File: Player.java 
 * @author Joseph Workoff
 * Creation Date: 09/24/2020
 * Major: CS/SW MS
 * Due Date: 11/06/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 2
 * Purpose: Player interface class
*/

package com.client;

import java.util.Random;
import java.util.ArrayList;

abstract class Player{
    /** Player ID */
    protected int ID;
    
    /** Player Name */
    protected String name;
    
    /** Player Color */
    protected String color;
    
    /** Player's Total Number of Units */
    protected int power;
    
    /** Player's state of having earned a card this turn or not */
    protected boolean earnedCard;
    
    /** Player's controlled territories */
    protected ArrayList<Territory> ownedTerritories;
    
    /** Player's controlled continents */
    protected ArrayList<Continent> ownedContinents;
    
    /** Player's cards in hand */
    protected ArrayList<Card> cards;

    /** DisplayController for printing to GUI */
    protected DisplayController displayController;

    /** Number of troops to place during this turn */
    protected int troopsToPlace;

    /** Cheat flag */
    protected boolean cheater;

    /** Number of left troops to place during initialallocation phase. */
    protected int initialTroopsToPlace;

    protected RiskGameController controller;

    protected int lobbyID;
    protected int lobbyPlayerID;

    /**
     * Player Default Constructor
     */
    protected Player(){
        this.ID = -1;
        this.name = "";
        this.color = "";
        this.power = 0;
        this.troopsToPlace = 0;
        this.initialTroopsToPlace = 0;
        this.earnedCard = false;
        this.cheater = false;
        this.ownedTerritories = new ArrayList<Territory>();
        this.ownedContinents = new ArrayList<Continent>();
        this.cards = new ArrayList<Card>();
        this.displayController = null;
        this.controller = null;
        this.lobbyID = -1;
        this.lobbyPlayerID = -1;
    }
    
    /**
     * Player Constructor
     * @param ID - Player ID 
     * @param name - Player Name
     * @param color - Player Color
     */
    protected Player(int ID, String name, String color){
        this.ID = ID;
        this.name = name;
        this.color = color;
        this.power = 0;
        this.troopsToPlace = 0;
        this.initialTroopsToPlace = 0;
        this.earnedCard = false;
        this.cheater = false;
        this.ownedTerritories = new ArrayList<Territory>();
        this.ownedContinents = new ArrayList<Continent>();
        this.cards = new ArrayList<Card>();
        this.displayController = null;
        this.controller = null;
        this.lobbyID = -1;
        this.lobbyPlayerID = -1;
    }
    
    /**
     * @return Player's ID
     */
    public int getID(){return this.ID;}
    /**
     * @return Player's Name
     */
    public String getName(){return this.name;}
    /**
     * @return Player's Color
     */
    public String getColor(){return this.color;}
    /**
     * @return Player's Power
     */
    public int getPower(){return this.power;}
    /**
     * @return Player's earned card status
     */
    public boolean getEarnedCard(){return this.earnedCard;}
    /**
     * @return Player's controlled territories
     */
    public ArrayList<Territory> getTerritories(){return this.ownedTerritories;}
    /**
     * @return Player's controlled continents
     */
    public ArrayList<Continent> getContinents(){return this.ownedContinents;}
    /**
     * @return Player's cards
     */
    public ArrayList<Card> getCards(){return this.cards;}
    /**
     * @param ID Player's ID
     */
    public void setID(int ID){this.ID = ID;}
    /**
     * 
     * @param name Player's Name
     */
    public void setName(String name){this.name = name;}
    /**
     * 
     * @param color Player's Color
     */
    public void setColor(String color){this.color = color;}
    /**
     * 
     * @param power Player's Power
     */
    public void setPower(int power){this.power = power;}
    /**
     * 
     * @param earnedCard Player's earned card status
     */
    public void setEarnedCard(boolean earnedCard){this.earnedCard = earnedCard;}
    /**
     * 
     * @param territories Player's territories
     */
    public void setTerritories(ArrayList<Territory> territories){this.ownedTerritories = territories;}
    /**
     * 
     * @param continents Player's Continents
     */
    public void setContinents(ArrayList<Continent> continents){this.ownedContinents = continents;}
    /**
     * 
     * @param cards Player's cards
     */
    public void setCards(ArrayList<Card> cards){this.cards = cards;}
    /**
     * 
     * @param power Increase to Player's power
     */
    public void gainPower(int power){this.power += power;}
    
    /**
     * DisplayController setter
     * @param displayController
     */
    public void setDisplayController(DisplayController displayController){this.displayController = displayController;}

    /**
     * Troopstoplace getter
     * @return troopsToPlace
     */
    public int getTroopsToPlace(){return this.troopsToPlace;}

    /**
     * TroopsToPlace setter
     * @param troopstoplace
     */
    public void setTroopsToPlace(int troops){this.troopsToPlace = troops;}

    /**
     * initialtroopstoplace getter
     * @return initialtroopstoplace
     */
    public int getInitialTroopsToPlace(){return this.initialTroopsToPlace;}

    /**
     * Initialtroopstoplace setter
     * @param troops
     */
    public void setInitialTroopsToPlace(int troops){this.initialTroopsToPlace = troops;}

    /**
     * Cheater flag setter
     * @param cheater
     */
    public void setCheater(boolean cheater){this.cheater = cheater;}

    /**
     * Cheater flag getter
     * @return cheater
     */
    public boolean getCheater(){return this.cheater;}

    public RiskGameController getController(){return this.controller;}
    public void setController(RiskGameController controller){this.controller = controller;}

    public int getLobbyPlayerID(){return this.lobbyPlayerID;}
    public void setLobbyPlayerID(int lobbyPlayerID){this.lobbyPlayerID = lobbyPlayerID;}

    public void setLobbyID(int lobbyID){this.lobbyID = lobbyID;}
    public int getLobbyID(){return this.lobbyID;}
    


    /**
     * Returns a unique string for Human and CPU players
     * @return "Human" or "CPU"
     */
    abstract String isA();





    /**
     * Adds one troop to a randomly chosen territory owned by the player.
     * Sets the territory's occupancy.
     * Sets the player's power.
     * Decrements troopsToPlace and initialTroopsToPlace.
     * @param terr - Territory to place in
     * @param numTroops - Number of troops to place
     */
    public void allocateTroop(){
        // Random rand = new Random();
        // int terrIdx = rand.nextInt(this.ownedTerritories.size());
        // Territory terr = this.ownedTerritories.get(terrIdx);

        // this.controller.reinforce(terr, 1);
        
        // terr.setOccupancy(terr.getOccupancy() + 1);
        // this.power += 1;
        // this.troopsToPlace -= 1;
        // if (this.initialTroopsToPlace >= 1){
        //     this.initialTroopsToPlace -= 1;
        // }
        
    }



    /**
     * Adds a number of troops to a territory owned by the player.
     * Sets the territory's occupancy.
     * Sets the player's power.
     * Decrements troopsToPlace and initialTroopsToPlace.
     * @param terr - Territory to place in
     * @param numTroops - Number of troops to place
     */
    public void allocateTroops(Territory terr, int numTroops){
        if (this.ownedTerritories.contains(terr)){
            terr.setOccupancy(terr.getOccupancy() + numTroops);
            this.power += numTroops;
            this.troopsToPlace -= numTroops;
            if (this.initialTroopsToPlace >= numTroops){
                this.initialTroopsToPlace -= numTroops;
            }
            Util.debug("Player.allocateTroops: Placed " + numTroops);
        }
    }


    
    /**
     * Calculates the total units being earned this turn, based on territories/continents owned.
     * @return Total number of units earned this turn.
     */
    protected int calculateReinforcements(){
        int newTroops = this.ownedTerritories.size() / 3;
        newTroops = (newTroops < 3) ? 3 : newTroops;

        if (!this.ownedContinents.isEmpty()){
            for (Continent cont: this.ownedContinents){
                newTroops += cont.getBonus();
            }
        }

        return newTroops;
    }



    /**
     * Returns a territory selected to attack with
     */
    abstract void chooseAttacker();

    /**
     * Selects cards from the player's hand to turn in.
     */
    abstract void chooseCards();

    /**
     * Selects a number of troops to place in a territory.
     */
    abstract void chooseNumberOfTroops();
    

    abstract void chooseNumberOfTroops(Territory terr);


    /**
     * Selects a territory for reinforcement.
     */
    abstract void chooseOwnedTerritory();

    abstract void chooseOwnedTerritory(boolean initial);

    /**
     * Selects whether to turn in cards in the player's hand.
     * @return True when turning in.
     */
    abstract void chooseToTurnInCards();

    /**
     * Chooses a territory to target for attack.
     * 
     * @param The selected attacking territory
     */
    abstract void chooseTarget(Territory terr);

    /**
     * Gets the number of dice to roll from the player.
     * @param terr - Attacking or defending territory.
     * @param maxDice - Maximum legal number of dice to roll.
     * @param defending - Whether the player is defending. If false, rolling 0 dice is valid.
     * @return Number of dice selected
     */
    abstract void chooseNumberOfDice(Territory terr, int maxDice, boolean defending);


    abstract void chooseNumberOfAttackDice(Territory attacker, Territory defender);
    abstract void chooseNumberOfDefenseDice(Territory attacker, Territory defender, int attackRoll);



    /**
     * Gets a number of troops to move into a newly captured territory from the player.
     * @param src - Territory to move from
     * @param dst - Territory to move to
     * @param min - Minimum number of troops to move
     */
    abstract void chooseNumberOfTroopsToMove(int min, Territory src, Territory dst);

    /**
     * Begins the player's turn.
     */
    abstract void takeTurn();

    abstract void startTurn();

    // protected void readyTurn(){
    //     this.displayController.readyTurnDisplay(this);
    // }


    // abstract void chooseOwnedTerritoryInitial();


    /**
     * Removes cards from the player's hand.
     * @param discard - Arraylist of cards to discard
     */
    protected void discardCards(ArrayList<Card> discard){
        for (Card c: discard){
            if (this.cards.contains(c))
                this.cards.remove(c);   
        }
    }


    
    /**
     * Adds a card to the player's hand
     * @param card - Card object to add to hand
     */
    protected void earnCard(Card card){
        card.setLocation(this.ID);
        this.cards.add(card);
    }


    
    /**
     * Adds territories to the player's territory array. Sets their owners, occupancies. Checks if they've conquered their continents. 
     * @param occupancy - The new occupancy of every captured territory.
     * @param territories - Array of territories being captured.
     */
    public void gainTerritory(int occupancy, Territory... territories){
        for (int i = 0; i < territories.length; i++){
            
            if (!this.ownedTerritories.contains(territories[i])){
                this.ownedTerritories.add(territories[i]);
                territories[i].capture(this, occupancy);
                
                if (territories[i].getContinent().checkDomination() && !this.ownedContinents.contains(territories[i].getContinent())){
                    this.ownedContinents.add(territories[i].getContinent());
                }
            }
        }
    }



    /**
     * Remove a continent from the player's control.
     * @param cont - Continent object to remove
     */
    public void loseContinent(Continent cont){
        if (this.ownedContinents.contains(cont)){
            this.ownedContinents.remove(cont);
        }
    }



    /**
     * Remove a territory from the player's control. Updates the player's power. Loses the territory's continent.
     * @param terr - Territory object to lose
     */
    public void loseTerritory(Territory terr){
        if (this.ownedTerritories.contains(terr)){
            this.ownedTerritories.remove(terr);
            this.power -= terr.getOccupancy();
            this.loseContinent(terr.getContinent());
        }
    }

    
}