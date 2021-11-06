/** File: HumanPlayer.java 
 * @author Joseph Workoff
 * Creation Date: 09/24/2020
 * Due Date: 11/06/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 2
 * Purpose: HumanPlayer class for representing a human player making manual decisions.
*/

package com.client;

import java.util.Random;
import java.util.ArrayList;


/**
 * Human Player class.
 * Choose methods return static values.
 */
public class HumanPlayer extends Player{

    /**
     * Player Default Constructor
     */
    public HumanPlayer(){

        super();
    }
    


    /**
     * Player Constructor
     * @param ID - Player ID 
     * @param name - Player Name
     * @param color - Player Color
     */
    public HumanPlayer(int ID, String name, String color){

        super(ID, name, color);
    }



    /**
     * Decides whether to turn in cards or not.
     * @return false
     */
    void chooseToTurnInCards(){
        this.displayController.localChooseToTurnInCards(this);
    }



    /**
     * Gets 3-pair of cards to turn in for extra troops.
     * @return null, to ready GUI for selection.
     */
    void chooseCards(){
        this.displayController.localChooseCardsDisplay(this);
    }



    
    /**
     * Chooses a territory to attack with.
     * @return null, to ready GUI for selection.
     */
    void chooseAttacker(){
        this.displayController.localChooseAttackerDisplay(this);
    }

    

    /**
     * Gets a number of troops to place
     * @return -1, to ready GUI for selection.
     */
    void chooseNumberOfTroops(){
        ;
    }


    void chooseNumberOfTroops(Territory terr){
        this.displayController.localChooseTroopsDisplay(terr);
    }


    void chooseOwnedTerritory(){
        this.displayController.localAllocationDisplay(this, false);
    }

    /**
     * Selects a territory that the Player owns.
     */
    void chooseOwnedTerritory(boolean initial){
        this.displayController.localAllocationDisplay(this, initial);
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
        this.displayController.localChooseRollDisplay(defending);
        // this.displayController.displayGUI.displayRollOptions(terr, prevRoll, defending, maxDice);
    }


    void chooseNumberOfAttackDice(Territory attacker, Territory defender){
        this.displayController.localChooseAttackRollDisplay(attacker, defender);
    }

    void chooseNumberOfDefenseDice(Territory attacker, Territory defender, int attackRoll){
        this.displayController.localChooseDefenseRollDisplay(attacker, defender, attackRoll);
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
        this.displayController.localChooseMoveDisplay(src, dst, min);
    }



    /**
     * Flesh and blood.
     * @return "Human"
     */
    String isA(){
        return "Human";
    }



    /**
     * Readies the display for the player's turn.
     */
    void takeTurn(){
        Util.debug(this.color +  " Entering HP.taketurn.");

        boolean pass = false;
        int action = 1;
        int cheat = 0;
        this.cheater = false;
        
        this.troopsToPlace = this.calculateReinforcements();
        this.earnedCard = false;

        this.displayController.localTakeTurnDisplay(this);
    }


    /**
     * Readies the display for the start of the player's turn.
     */
    void startTurn(){
        Util.debug(this.color +  " Entering HP.startturn.");
        this.displayController.localStartTurnDisplay();
    }


}