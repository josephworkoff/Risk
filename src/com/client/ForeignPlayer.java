/** File: ForeignPlayer.java 
 * @author Joseph Workoff
 * Creation Date: 09/24/2020
 * Due Date: 11/06/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 2
 * Purpose: ForeignPlayer class representing a human player whose decisions are retrieved from the server.
*/

package com.client;

import java.util.Random;
import java.util.ArrayList;


/**
 * Human Player class.
 * Choose methods return static values.
 */
public class ForeignPlayer extends Player{

    /**
     * Player Default Constructor
     */
    public ForeignPlayer(){

        super();
    }
    


    /**
     * Player Constructor
     * @param ID - Player ID 
     * @param name - Player Name
     * @param color - Player Color
     */
    public ForeignPlayer(int ID, String name, String color){

        super(ID, name, color);
    }



    /**
     * Decides whether to turn in cards or not.
     * @return false
     */
    void chooseToTurnInCards(){
        ;
    }



    /**
     * Gets 3-pair of cards to turn in for extra troops.
     * @return null, to ready GUI for selection.
     */
    void chooseCards(){
        ;
    }


    
    /**
     * Chooses a territory to attack with.
     * @return null, to ready GUI for selection.
     */
    void chooseAttacker(){
        this.displayController.foreignChooseAttackerDisplay(this);
    }

     

    /**
     * Gets a number of troops to place
     * @return -1, to ready GUI for selection.
     */
    void chooseNumberOfTroops(){
        this.displayController.displayGUI.displayInputPanel(true);
    }



    /**
     * Selects a territory that the Player owns.
     */
    void chooseOwnedTerritory(){
        this.displayController.foreignChooseAttackerDisplay(this);
    }



    void chooseOwnedTerritory(boolean initial){
        this.displayController.foreignAllocationDisplay(this, initial);
    }

    /**
     * Accepts a territory selection from the player, and determines whether it is a valid target for attack.
     * @return null, to ready GUI for selection.
     */
    void chooseTarget(Territory terr){
        this.displayController.displayGUI.activateTargetableEnemyButtons(terr);
    }



    /** 
     * Gets a number of dice to roll.
     * @param maxDice - maximum number of dice that can be rolled.
     * @param defending - True if defending, and disables retreating.
     */
    void chooseNumberOfDice(Territory terr, int maxDice, boolean defending){
        this.displayController.displayGUI.displayRollOptions(terr, !defending, maxDice);
    }


    /**
     * Gets a number of troops to move into a newly captured territory from the player.
     * @param min - Minimum number of troops to move
     * @return -1, to ready GUI for selection.
     */
    void chooseNumberOfTroopsToMove(int min){
        return;
    }



    /**
     * 
     */
    void chooseNumberOfTroopsToMove(int min, Territory src, Territory dst){
        this.displayController.foreignChooseMoveDisplay(src, dst, min);
    }



    /**
     * From another world.
     * @return "Foreign"
     */
    String isA(){
        return "Foreign";
    }



    /**
     * Readies the display for the player's turn.
     * @return True if the player has won the game this turn, false otherwise.
     */
    void takeTurn(){
        boolean pass = false;
        int action = 1;
        int cheat = 0;
        this.cheater = false;
        
        this.troopsToPlace = this.calculateReinforcements();
        this.earnedCard = false;
        discardCards(this.cards);

        this.controller.reinforcePhase();
        this.displayController.foreignTakeTurnDisplay(this);
    }

    void startTurn(){
        ;
    }

    void chooseNumberOfTroops(Territory terr){
        ;
    }

    void chooseNumberOfAttackDice(Territory attacker, Territory defender){
        this.displayController.foreignBattleResultsDisplay();
    }

    void chooseNumberOfDefenseDice(Territory attacker, Territory defender, int attackRoll){
        this.displayController.foreignChooseDefenseRollDisplay(attacker, defender, attackRoll);
    }

}