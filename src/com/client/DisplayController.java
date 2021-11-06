/** File: DisplayController.java 
 * @author Joseph Workoff
 * Creation Date: 09/24/2020
 * Major: CS/SW MS
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: DisplayController Class for interacting with the GUI.
*/

package com.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Timer; 

import com.google.gwt.media.client.Audio;

import java.util.ArrayList; 
import java.util.Random; //Dice Rolling
import java.util.Collections; //Sorting

import java.util.logging.Logger;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * DisplayController class. The main controller for interacting with the GUI.
 */
public class DisplayController{

    /** GUI layout object */
    public DisplayGUI displayGUI;

    /** Game controller */
    private RiskGameController controller;

    /** Board Object */
    private Board board;

    /** Deck object */
    private Deck deck;

    /** Players object */
    private Player[] players;

    /** Owner player for grey territories */
    private Player neutralPlayer;

    /** Player currently taking their turn */
    private Player turnPlayer;

    /** Player whose actions are submitted through the GUI */
    private Player localPlayer;

    /** Territory currently selected for attack */
    private Territory selectedTerritory;

    /** Territory current targeted for attack */
    private Territory targetedTerritory;

    /** Player array index for current turn player */
    private int playerIndex;
    
    /** Current phase of the turn */
    private Phase phase;
    
    /** Current number of attack dice being rolled */
    private int attackRoll;
    
    /** Current number of defense dice being rolled */
    private int defenseRoll;
    
    /** List of cards being turned in */
    private ArrayList<Card> turnInCards;
    
    /** True if the game has been ended. */
    private boolean gameOver;
    
    /** Enum for turn phase*/
    enum Phase{
        BEGIN,
        INITIAL_ALLOCATION,
        INITIAL_PLACING,
        ALLOCATING,
        PLACING,
        SELECTING,
        TARGETING,
        ROLLING_ATTACK,
        ROLLING_DEFENSE,
        TRANSFERING
    }

    /** For writing to javascript console */
    Logger logger;


    /**
     * Default constructor.
     */
    public DisplayController(){
        this.controller =   null;
        this.board =        null;
        this.deck =         null;
        this.players =      null;
        this.neutralPlayer =null;
        this.localPlayer = null;

        this.displayGUI = null;

        this.playerIndex = 0;
        this.turnInCards = new ArrayList<Card>();
        this.gameOver = false;

        this.logger = Logger.getLogger("");
    }

    /**
     * Constructor. Creates the DisplayGUI.
     * @param controller - Game Contoller obj
     * @param board - Game Board obj
     * @param deck - Deck obj
     * @param players - Array of Players
     */
    public DisplayController(RiskGameController controller, Board board, Deck deck, Player[] players){        
        this.controller = controller;
        this.board = board;
        this.deck = deck;
        this.players = players;
        this.neutralPlayer = controller.getNeutralPlayer();
        this.localPlayer = null;


        this.displayGUI = new DisplayGUI(board);

        this.playerIndex = 0;
        this.turnInCards = new ArrayList<Card>();
        this.gameOver = false;

        this.logger = Logger.getLogger("");
    }



    /**
     * Adds the main layout panel to the passed rootpanel.
     * @param rootPanel
     */
    public void render(RootLayoutPanel rootPanel){
        rootPanel.add(this.displayGUI.getLayoutPanel());
    }
    


    /**
     * Performs start-of-game housekeeping
     * Initializes all buttons handlers.
     * Sets the local player.
     * @param localPlayer -  Local player.
     */
    public void readyGame(Player localPlayer){
        Util.debug(" Entering DC.readyGame");
        initButtons();

        setLocalPlayer(localPlayer);
    }



    /**
     * Sets the click handlers for every button.
     */
    private void initButtons() {

        //Territories
        for (final Territory t: board.getTerritories()){
            t.getButton().setEnabled(false);
            t.getButton().addClickHandler(new ClickHandler(){
                public void onClick(ClickEvent e){
                    territoryButtonHandler(t);
                }
            });
        }

        //Cards
        for (final Button b: this.displayGUI.getCardButtons()){
            b.addClickHandler(new ClickHandler(){
                public void onClick(ClickEvent e){
                    cardButtonHandler(b.getText());
                }
            });
        }

        //Card Turn In Submit
        this.displayGUI.getCardTurnInButton().addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                cardTurnInButtonHandler();
            }
        });


        // Action Buttons /

        Button[] actionButtons = this.displayGUI.getActionButtons();

        //Start
        actionButtons[0].addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                controller.startTurn();
            }
        });

        //Reinforce
        actionButtons[1].addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                controller.reinforcePhase();
            }
        });

        //Battle
        actionButtons[2].addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                controller.battlePhase();
            }
        });

        //Pass
        actionButtons[3].addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                controller.endPhase();
            }
        });

        //Turn in
        actionButtons[4].addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                controller.turnInPhase();
            }
        });

        // /Actions Buttons


        // Cheats /

        //Cheats
        this.displayGUI.getCheatMenuButton().addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                cheatMenuHandler();
            }
        });

        Button[] cheatButtons = this.displayGUI.getCheatButtons();

        //AutoWin Cheat
        cheatButtons[0].addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                autowinCheat();
            }
        });

        //Card Cheat Menu
        cheatButtons[1].addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                displayCardCheatMenu();
            }
        });
        
        //Card Cheat Submit
        this.displayGUI.getCardCheatSubmit().addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                addCardCheat();
            }
        });
        
        //End Game Cheat Button
        cheatButtons[2].addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                winGameCheat();
            }
        });


        // /Cheats 



        //Allocation Submit
        this.displayGUI.getSubmitButton().addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                submitHandler();
            }
        });

        //Roll
        this.displayGUI.getDiceSubmitButton().addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                diceSubmitHandler();
            }
        });

        //test button
        this.displayGUI.getDevButton().addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                devHandler();
            }
        });

    }



    /**
     * Click handler for Territory Buttons.
     * During ALLOCATING Phase: Calls selectForAllocation
     * During PLACING Phase: Calls reinforcePhase
     * During SELECTING Phase: Calls selectTerritory
     * During TARGETING Phase: Calls targetTerrory
     * @param terr - Selected Territory
     */
    private void territoryButtonHandler(Territory terr){

        Util.debug(turnPlayer.getColor() +  " Entering DC.territoryButtonHandler in phase " + this.phase);


        switch (this.phase){
            case ALLOCATING:
            case INITIAL_ALLOCATION:
                localChooseTroopsDisplay(terr);
                break;

            case PLACING: //deselect
                controller.reinforcePhase();
                break;

            case INITIAL_PLACING: //deselect
                controller.initialAllocation();
                break;

            case SELECTING:
                localChooseTargetDisplay(terr);
                break;

            case TARGETING:
                if (terr == this.selectedTerritory){
                    controller.battlePhase();
                }
                else{
                    this.targetedTerritory = terr;
                    localChooseAttackRollDisplay(this.selectedTerritory, this.targetedTerritory);
                }
                break;

            default:
                break;
        }
    }



    /**
     * Click handler for Card Turn-In UI Submit button
     * Checks that turnInCards match
     * Calculates troops bonus, adds to turn player
     * Clears turnIn list
     * Removes Turn In UI
     * Moves game to Reinforcement Phase
     */
    private void cardTurnInButtonHandler(){
        Util.debug(turnPlayer.getColor() +  " Entering DC.cardturninbuttonhandler");

        //match
        if (Deck.checkCardsMatch(this.turnInCards)){
            controller.turnInSet(this.turnInCards);
        }
        //don't match
        else{
            this.turnInCards.clear();
            this.displayGUI.printCards("Those cards don't match.");
            this.displayGUI.displayCardTurnInMenu(this.turnPlayer.getCards().size(), this.turnInCards);
        }
    }



    /**
     * Button handler for the 5 card buttons.
     * @param cardString - The identifying card string of the card selected
     */
    private void cardButtonHandler(String cardString){
        Util.debug(turnPlayer.getColor() +  " Entering DC.cardbuttonhandler");

        for (Card c: this.turnPlayer.getCards()){
            if (c.getCardString() == cardString){
                
                //deselect
                if (this.turnInCards.contains(c)){
                    this.turnInCards.remove(c);
                }
                //select
                else{
                    this.turnInCards.add(c);
                }
            }
            this.displayGUI.displayCardTurnInMenu(this.turnPlayer.getCards().size(), this.turnInCards);
        }
    }



    /**
     * Click handler for Placement Input Submit button
     * Retrieves and error checks the number entered.
     * PLACING/INITIAL_PLACING Phase: 
     *      Calls Reinforce.
     * TRANSFERING Phase:
     *      Calls transfer.
     */
    private void submitHandler(){

        Util.debug(turnPlayer.getColor() +  " Entering DC.submitHandler");

        int troops = 0;

        troops = this.displayGUI.getInputInt(0);

        switch (this.phase) {
            case ALLOCATING:
            case INITIAL_ALLOCATION:
            case PLACING:
            case INITIAL_PLACING:
                localReinforce(troops);
                break;

            case TRANSFERING:
                if (troops < this.attackRoll || troops > this.selectedTerritory.getOccupancy() - 1){
                    this.displayGUI.printPlaced("Invalid Selection: Min:" + this.attackRoll + ", Max:" 
                    + (this.selectedTerritory.getOccupancy() - 1) );
                }
                else{
                    controller.transfer(troops);
                }
                break;

            default:
                Util.debug(turnPlayer.getColor() +  " Error: Entered DC.submitHandler during Phase " + this.phase);
                break;
        }
    }



    /**
     * Click Handler for Dice Submit button.
     * Gets the selected roll.
     * During ROLLING_ATTACK Phase: 
     *      Returns to the battle phase upon Retreat
     *      Otherwise sets attackRoll, moves to ROLLING_DEFENSE Phase, redisplays roll options.
     * During ROLLING_Defense Phase: 
     *      Sets defenseRoll
     *      Calls bellum to begin battle
     */
    private void diceSubmitHandler(){
        Util.debug(turnPlayer.getColor() +  " Entering DC.dicesubmithandler");

        String opt = this.displayGUI.getRoll();
        int roll;
        switch (this.phase) {
            case ROLLING_ATTACK:
                if (opt == "Retreat"){
                    controller.battlePhase();
                    return;
                }
                roll = Integer.parseInt(opt);
                // setAttackRoll(roll);
                controller.antebellum(this.selectedTerritory, this.targetedTerritory, roll);
                break;
            case ROLLING_DEFENSE:
                roll = Integer.parseInt(opt);
                controller.bellum(roll);
                break;

            default:
                break;
        }
    }



    /**
     * Click handler for the Card Turn In button.
     * Toggles display of the Cheat Option buttons
     */
    private void cheatMenuHandler(){
        this.displayGUI.toggleCheatMenu();
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
        
        this.phase = Phase.BEGIN;
        
        this.setTurnPlayer(player);
        
        Util.debug(this.turnPlayer.getColor() + " DC.readyTurn");
        
        //clean territory buttons
        this.displayGUI.cleanTerritoryButtons();

        //clean action buttons
        this.displayGUI.cleanActionButtons();

        this.attackRoll = 0;
        this.defenseRoll = 0;

        this.selectedTerritory = null;
        this.targetedTerritory = null;

        this.displayGUI.cleanDisplay();

        this.turnInCards.clear();
        localCardsDisplay();

        this.displayGUI.printPlayer(player.getColor() + "'s Turn");
    }


    // public void readyTurnDisplay(Player player){
    //     // this.phase = Phase.BEGIN;
    //     this.displayGUI.cleanTerritoryButtons();
    //     this.displayGUI.cleanActionButtons();
    //     this.displayGUI.cleanDisplay();
        
    // }



    /**
     * Returns "Your" or player's color for prompts.
     * @param player Turn player
     * @return
     */
    private String yourOrColor(Player player){
        if (this.localPlayer == null){
            return player.getColor() + "'s";
        }
        else{
            return "Your";
        }
    }




    /**
     * Sets the display when the local player starts their turn.
     * @param player Local player
     */
    public void localTakeTurnDisplay(Player player){
        Util.debug(player.getColor() +  " Entering DC.localTakeTurnDisplay");
        readyTurn(player);
        this.displayGUI.toggleStartButton(true);
        this.displayGUI.print(yourOrColor(player) + " Turn: Click Start Turn");
    }


    /**
     * Sets the display when the foreign player starts their turn.
     * @param player Foreign player
     */
    public void foreignTakeTurnDisplay(Player player){
        Util.debug(player.getColor() +  " Entering DC.foreignTakeTurnDisplay");
        readyTurn(player);
        this.displayGUI.cleanActionButtons();
        this.displayGUI.print(player.getColor() + " is starting their turn.");
    }




    /**
     * Sets the display when the local player is allocating troops.
     * @param player Turn player
     * @param initial True during the initial allocation phase
     */
    public void localAllocationDisplay(Player player, boolean initial){
        this.displayGUI.cleanDisplay();
        Util.debug("DC.localAllocationdisplay");

        this.displayGUI.cleanActionButtons();

        this.selectedTerritory = null;

        String msg = "";
        if (initial){
            this.phase = Phase.INITIAL_ALLOCATION;
            msg = "(" + player.getInitialTroopsToPlace() + " total)";
        }
        else{
            this.phase = Phase.ALLOCATING;
        }

        this.displayGUI.displaySelectionDisplay(true);
            
        this.displayGUI.print(player.getColor() + ": You have " 
            + player.getTroopsToPlace() 
            + " troops to place. " + msg);
        this.displayGUI.printPlaced("Select a Territory to place troops in.");

        this.displayGUI.activateOwnedButtons(player);
    }


    
    /**
     * Sets the display when the foreign player is allocating troops.
     * @param player Foreign player
     * @param initial True during the initial allocation phase.
     */
    public void foreignAllocationDisplay(Player player, boolean initial){
        Util.debug("DC.foreignlinitialallocationdisplay");

        this.displayGUI.cleanActionButtons();

        String msg = "";
        if (initial){
            this.phase = Phase.INITIAL_ALLOCATION;
            msg = "(" + player.getInitialTroopsToPlace() + " total)";
        }
        else{
            this.phase = Phase.ALLOCATING;
        }

        this.displayGUI.displaySelectionDisplay(true);
            
        this.displayGUI.print(player.getColor() + " has " 
            + player.getTroopsToPlace() 
            + " troops to place. " + msg);
    }






    /**
     * Sets the display when the local player is choose a number of troops to place.
     * @param terr Territory being placed in.
     */
    public void localChooseTroopsDisplay(Territory terr){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localChooseTroopsDisplay in phase " + this.phase);

        this.phase = (this.phase == Phase.INITIAL_ALLOCATION) ? Phase.INITIAL_PLACING : Phase.PLACING;

        this.selectedTerritory = terr;

        this.displayGUI.cleanTerritoryButtons(terr);
        this.displayGUI.displayInputPanel(true);
        this.displayGUI.printPlaced("Enter number of troops to place in " + terr.getName());
    }



    /**
     * Logs when an allocation has occured.
     * @param player Turn Player
     * @param terr Allocated Territory
     * @param numTroops Number of troops placed
     */
    public void localReinforcementsPlacedDisplay(Player player, Territory terr, int numTroops){
        this.displayGUI.print(player.getColor() + " placed " + numTroops +
        " in " + terr.getName());
        this.displayGUI.logEvent(player.getColor() + " placed " + numTroops +
        " in " + terr.getName());
    }


    /**
     * Error checks a reinforcement before committing. 
     * @param troops Number of troops being placed.
     */
    public void localReinforce(int troops) {
        Util.debug(turnPlayer.getColor() + " Entering DC.localReinforce during phase " + this.phase);
        if (troops > this.turnPlayer.getTroopsToPlace() || troops < 1){
            Util.debug("DC.localReinforce: Bad input");
            this.displayGUI.printPlaced("Invalid Selection");
        }
        else{
            Util.debug("DC.localReinforce: Placing " + troops + "in " + this.selectedTerritory.getName());
            this.controller.reinforce(this.selectedTerritory, troops);
        }
    }



    /**
     * Sets the display when the local player begins their turn.
     */
    public void localStartTurnDisplay(){
        this.displayGUI.logEvent(this.turnPlayer.getColor() + "'s Turn.");

        this.displayGUI.cleanDisplay();
        this.displayGUI.cleanActionButtons();
        this.displayGUI.toggleReinforceButton(true);
    }



    /**
     * Sets the display when the local player has the option to turn in cards.
     */
    public void localChooseToTurnInCards(Player player){
        this.displayGUI.toggleTurnInButton(true);
        this.displayGUI.print(player.getColor() + ": Turn in cards, or move to Reinforcement.");
    }


    /**
     * Displays the card turn in menu to the local player.
     */
    public void localChooseCardsDisplay(Player player){
        this.displayGUI.displayCardTurnInMenu(player.getCards().size(), new ArrayList<Card>());
    }

    

    /**
     * Sets the display when the turn player trades in cards.
     * @param player
     * @param earned
     */
    public void localTradeInSetDisplay(Player player, int earned){
        this.displayGUI.cleanDisplay();
        this.displayGUI.logEvent(this.turnPlayer.getColor() + " traded cards for " + earned + " extra troops.");
        localCardsDisplay();
    }




    /**
     * Sets the display when the local player enters the reinforcement phase.
     */
    public void localReinforcePhaseDisplay(){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localReinforcePhaseDisplay.");

        this.displayGUI.cleanActionButtons();
        this.displayGUI.cleanDisplay();
        this.displayGUI.displaySelectionDisplay(true);
        this.selectedTerritory = null;

        this.phase = Phase.ALLOCATING;
        this.displayGUI.print(this.turnPlayer.getColor() + ": You have " 
            + turnPlayer.getTroopsToPlace() + " troops to place.");
        this.displayGUI.printPlaced("Select a Territory to place troops in.");
    }



    /**
     * Sets the display when the local player enters the battle phase.
     */
    public void localBattlePhaseDisplay(){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localBattlePhaseDisplay");
        this.displayGUI.cleanActionButtons();
        this.displayGUI.cleanDisplay();
        this.displayGUI.displayBattlePanel(true);
        this.displayGUI.togglePassButton(true);

        this.selectedTerritory = null;
        this.targetedTerritory = null;

        this.phase = Phase.SELECTING;

        this.displayGUI.print(this.turnPlayer.getColor() + ": Select a Territory to Attack With, or Pass");
  
    }


 
    /**
     * Sets the display when the local player is choosing an attacker
     * @param player Turn player
     */
    public void localChooseAttackerDisplay(Player player){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localchooseattackerdisplay");

        this.displayGUI.cleanDisplay();
        this.displayGUI.cleanActionButtons();
        this.displayGUI.displayBattlePanel(true);

        this.selectedTerritory = null;
        this.targetedTerritory = null;

        this.phase = Phase.SELECTING;

        this.displayGUI.print(this.turnPlayer.getColor() + ": Select a Territory to Attack With, or Pass");

        this.displayGUI.togglePassButton(true);
        this.displayGUI.activateSelectableButtons(player);
    }



    /**
     * Sets the display when the foreign player is choosing an attacker
     * @param player Turn player
     */
    public void foreignChooseAttackerDisplay(Player player){
        Util.debug(turnPlayer.getColor() +  " Entering DC.foreignChooseAttackerDisplay");

        this.displayGUI.cleanDisplay();
        this.displayGUI.cleanActionButtons();
        this.displayGUI.displayBattlePanel(true);

        this.selectedTerritory = null;
        this.targetedTerritory = null;

        this.phase = Phase.SELECTING;

        this.displayGUI.print(player.getColor() + " is planning their attack.");
    }


    /**
     * Sets the display when the turn player is choosing a target for attack.
     * @param attacker Attacking territory.
     */
    public void localChooseTargetDisplay(Territory attacker){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localChooseTargetDisplay.");
        this.displayGUI.cleanDisplay();
        this.displayGUI.displaySelectionDisplay(true);
        this.displayGUI.printSelected("Selected: " + attacker.getName());

        this.selectedTerritory = attacker;

        this.phase = Phase.TARGETING;

        this.displayGUI.print("Select an enemy Territory to Attack with " + attacker.getName());
        this.displayGUI.activateTargetableEnemyButtons(attacker);
    }


    /**
     * Sets the display when the local player is choosing a number of attack dice to roll.
     * Sets Phase to ROLLING_ATTACK
     * @param attacker Attacking territory
     * @param defender Defending territory
     */
    public void localChooseAttackRollDisplay(Territory attacker, Territory defender) {
        Util.debug(turnPlayer.getColor() +  " Entering DC.localChooseAttackRolltDisplay.");
        // this.displayGUI.cleanDisplay();
        this.displayGUI.displaySelectionDisplay(false);
        this.displayGUI.cleanTerritoryButtons();
        this.displayGUI.displayBattlePanel(false);
        this.displayGUI.displayRollPanel(true);

        this.displayGUI.print(attacker.getName() + 
            " is attacking " + defender.getName());

        this.phase = Phase.ROLLING_ATTACK;

        this.displayGUI.displayRollOptions(attacker, true, attacker.calcMaxDice(false));
    }


    /**
     * Sets the display when the local player is choosing a number of defense dice to roll.
     * Sets Phase to ROLLING_DEFENSE
     * @param attacker Attacking territory
     * @param defender Defending territory
     * @param attackRoll Number of attack dice rolled
     */
    public void localChooseDefenseRollDisplay(Territory attacker, Territory defender, int attackRoll){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localChooseDefenseRolltDisplay.");
        // this.displayGUI.cleanDisplay();
        this.displayGUI.displaySelectionDisplay(false);
        this.displayGUI.cleanTerritoryButtons();
        this.displayGUI.displayBattlePanel(false);
        this.displayGUI.displayRollPanel(true);

        this.attackRoll = attackRoll;

        this.displayGUI.print(attacker.getName() + 
            " is attacking " + defender.getName() + 
            " with " + attackRoll + " dice");

        this.phase = Phase.ROLLING_DEFENSE;

        this.displayGUI.displayRollOptions(defender, false, defender.calcMaxDice(true));
    }


    /**
     * Sets the display when the foreign player is choosing a number of defense dice to roll
     * @param attacker Attacking territory
     * @param defender Defending territory
     * @param attackRoll Number of attack dice rolled
     */
    public void foreignChooseDefenseRollDisplay(Territory attacker, Territory defender, int attackRoll){
        this.displayGUI.cleanDisplay();

        this.displayGUI.print(defender.getOwner().getColor() + " is choosing how to defend.");

        this.phase = Phase.ROLLING_DEFENSE;
    }



    /**
     * Sets the display when the local player is choosing a number of dice to roll
     * @param defending True when defending
     */
    public void localChooseRollDisplay(boolean defending){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localChooseRolltDisplay.");
        this.displayGUI.cleanDisplay();
        this.displayGUI.cleanTerritoryButtons();
        this.displayGUI.displayBattlePanel(false);
        this.displayGUI.displayRollPanel(true);

        if (defending){
            this.displayGUI.print(this.selectedTerritory.getName() + 
                " is attacking " + this.targetedTerritory.getName() + 
                " with " + this.attackRoll + " dice");

            this.phase = Phase.ROLLING_DEFENSE;
        }
        else{
            this.displayGUI.print(this.selectedTerritory.getName() + 
            " is attacking " + this.targetedTerritory.getName());
            this.phase = Phase.ROLLING_ATTACK;
        }

        this.displayGUI.displayRollOptions(this.selectedTerritory, !defending, this.selectedTerritory.calcMaxDice(defending));
    }



    /**
     * Displays battle results.
     * @param attacker Attacking territory
     * @param attackerDamage Damage taken by attacker
     * @param defender Defending territory
     * @param defenderDamage Damage taken by defender
     */
    public void localBattleResultsDisplay(Territory attacker, int attackerDamage, Territory defender, int defenderDamage){
        this.displayGUI.print(attacker.getName() + " took " + 
            attackerDamage + " and dealt " + defenderDamage + 
            " to " + defender.getName());
            
        this.displayGUI.logEvent(attacker.getName() + " took " + 
            attackerDamage + " and dealt " + defenderDamage + 
            " to " + defender.getName());
    }

    
    public void foreignBattleResultsDisplay(){
        this.displayGUI.displaySelectionDisplay(false);
        this.displayGUI.displayRollPanel(false);
    }


    /**
     * Displays dice rolls
     * @param attackDice Attack Rolls
     * @param defenseDice Defense Rolls
     */
    public void localDisplayRolls(ArrayList<Integer> attackDice, ArrayList<Integer> defenseDice){
        this.displayGUI.printRolls(attackDice, defenseDice);
    }



    /**
     * Sets the display when a territory has been conquered.
     * @param player Conquering player
     * @param target Conquered territoru
     */
    public void localConquerDisplay(Player player, Territory target){
        // this.displayGUI.cleanDisplay();
        this.displayGUI.print(player.getColor() + " has conquered " + target.getName() + "!");
        this.displayGUI.logEvent(player.getColor() + " has conquered " + target.getName() + "!");
    }



    /**
     * Sets the display when the local player is choosing a number of troops to move into a conquered territory
     * Sets phase to TRANSFERING
     * @param src Source territory
     * @param dst Destination territory
     * @param min Minimum number to move
     */
    public void localChooseMoveDisplay(Territory src, Territory dst, int min){
        // this.displayGUI.cleanDisplay();
        this.displayGUI.displaySelectionDisplay(true);
        this.displayGUI.displayInputPanel(true);
        this.displayGUI.displayRollPanel(false);
        this.displayGUI.cleanActionButtons();

        this.phase = Phase.TRANSFERING;

        this.displayGUI.printPlaced("Enter a number of troops to move from " 
            + src.getName() + " to " + dst.getName() + " - Minimum: " + min 
            + ", Maximum: " + (src.getOccupancy() - 1) );
    }



    
    /**
     * Sets the display when the foreign player is choosing a number of troops to move into a conquered territory
     * Sets phase to TRANSFERING
     * @param src Source territory
     * @param dst Destination territory
     * @param min Minimum number to move
     */
    public void foreignChooseMoveDisplay(Territory src, Territory dst, int min){
        this.displayGUI.displayRollPanel(false);
        this.displayGUI.cleanActionButtons();

        this.phase = Phase.TRANSFERING;

        this.displayGUI.printPlaced(src.getOwner().getColor() + "is moving troops from " 
            + src.getName() + " to " + dst.getName());
    }


    /**
     * Sets the display when troops have been moved.
     * @param player Turn player
     * @param src Source territory
     * @param dst Destination territory
     * @param numTroops Number of troops moved
     * @param continent True if the continent was conquered in the battle
     */
    public void localTransferDisplay(Player player, Territory src, Territory dst, int numTroops, boolean continent){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localTransferDisplay");
        this.displayGUI.logEvent(player.getColor() + " moved " + numTroops + 
            " from " + src.getName() + " to " + dst.getName());

        if (continent){
            this.displayGUI.logEvent(player.getColor() + 
                " has conquered the continent of " + dst.getContinent().getName() + "!");
        }
    }




    /**
     * Sets the display when the game ends.
     * @param player Winning player
     * @param dc True if the opponent disconnected
     * @param timestamp Duration of the game
     */
    public void localVictoryDisplay(Player player, boolean dc, String timestamp){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localvictorydisplay");
        this.displayGUI.cleanActionButtons();
        this.displayGUI.cleanTerritoryButtons();
        this.displayGUI.cleanDisplay();

        if (dc){
            this.displayGUI.print("Opponent disconnected.");
            this.displayGUI.logEvent("Opponent disconnected.");
        }
        else{
            this.displayGUI.print(player.getColor() + " has won the game!");
            this.displayGUI.logEvent(player.getColor() + " has won the game!");
        }

        this.displayGUI.displayVictory(player, timestamp);
    }



    /**
     * Performs end of turn housekeeping.
     * Deals a card if turnplayer conquered a territory this turn.
     * Sets turnPlayer + turnIndex to the next player.
     */
    public void endPhase(){

        Util.debug(turnPlayer.getColor() +  " Entering endPhase");

        this.displayGUI.cleanActionButtons();

        this.displayGUI.cleanDisplay();
        
        if (this.turnPlayer.getEarnedCard()){
            turnPlayer.earnCard(this.deck.draw());
            localCardsDisplay();
        }

        this.playerIndex = (playerIndex + 1) % this.players.length;
        this.players[playerIndex].takeTurn();
    }


    /**
     * Cleans the display at the end of turn.
     */
    public void localEndPhaseDisplay(){
        Util.debug(turnPlayer.getColor() +  " Entering DC.localEndPhaseDisplay");
        this.displayGUI.cleanActionButtons();
        this.displayGUI.cleanDisplay();
        localCardsDisplay();

    }



    /**
     * Click handler for the developer's test button.
     */
    private void devHandler(){


        return;
    }



    /**
     * Click Handler for the Add Card Cheat Button.
     * Displays the Card Cheat UI
     */
    private void displayCardCheatMenu(){
        this.displayGUI.displayCardCheatMenu(this.turnPlayer.getCards().size());
    }



    /**
     * Click Handler for Win Next Battles cheat button
     * Sets player's cheater flag.
     */
    private void autowinCheat(){
        if (this.turnPlayer != null){
            this.turnPlayer.setCheater(true);
            this.displayGUI.print(this.turnPlayer.getColor() + " troops have become stronger!");
        }
    }



    /**
     * Click handler for Win The Game cheat button.
     * Takes all territories, then checks for victory.
     */
    private void winGameCheat() {
        for (Territory t: this.board.getTerritories()){
            this.turnPlayer.gainTerritory(100, t);
        }

        if (this.board.checkVictory(this.turnPlayer)){
            controller.triggerVictory(false);
            return;
        }
    }



    /**
     * Click handler for Create Card button. 
     */
    private void addCardCheat(){
        String design = "";
        Territory terr = null;
        Card card = null;


        design = this.displayGUI.getCardRadioButtons();

        int terrID = this.displayGUI.getInputInt(1);

        if (design == "WILD"){
            terr = null;
        }
        else if (terrID > 0 && terrID <= 42){
            terr = board.getTerritoryByID(terrID);
        }

        if ((design != "" && terr != null) || design == "WILD"){
            this.turnPlayer.earnCard(this.deck.createNewCard(terr, Card.convertStringToDesign(design) ));
            this.displayGUI.displayCardCheatPanel(false);
            localCardsDisplay();
        }

    }



    /**
     * Sets the turn player, and displays it in the Player Display.
     */
    public void setTurnPlayer(Player player) {
        this.displayGUI.printPlayer(player.getColor() + "'s Turn");
        this.turnPlayer = player;
    }


    /**
     * Sets the local player. If it's a local game, states that in the Playing As display. Otherwise print the local player's color.
     * @param player Local player
     */
    public void setLocalPlayer(Player player){
        Util.debug(" Entering DC.setLocalPlayer");
        this.localPlayer = player;
        if (this.localPlayer == null){
            this.displayGUI.printPlayAs("Local Game");
            Util.debug("Local game");
        }
        else{
            Util.debug("Playing as: " + this.localPlayer.getColor());
            this.displayGUI.printPlayAs("Playing as: " + this.localPlayer.getColor());
        }
    }



    /**
     * Sets the displayed cards.
     * If the game is local, display the turn player's cards.
     * If the game is online, display the local player's cards.
     */
    public void localCardsDisplay(){
        Util.debug(" Entering DC.localCardsDisplay");
        if (this.localPlayer == null){
            this.displayGUI.displayCards(this.turnPlayer.getCards());
        }
        else{
            this.displayGUI.displayCards(this.localPlayer.getCards());
        }
    }




}