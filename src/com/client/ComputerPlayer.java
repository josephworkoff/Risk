/** File: ComputerPlayer.java 
 * @author Joseph Workoff
 * Creation Date: 09/24/2020
 * Major: CS/SW MS
 * Due Date: 11/06/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 2
 * Purpose: ComputerPlayer class for representing an AI player.
*/

package com.client;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;

import java.lang.Math;

import com.google.gwt.user.client.Timer;


/**
 * AI Player Class
 */
public class ComputerPlayer extends Player{
    
    private int BOT_DELAY;

    /** Random object for RNG */
    private Random rand;
    
    /** Territory currently selected */
    private Territory selectedTerritory = null;

    /** Territory currently targeted */
    private Territory targetTerritory = null;

    /** Map of integer priorities to owned territories. Used for deciding which territories to reinforce. */
    TreeMap<Integer, Territory> reinforcementPriority;

    /**
     * Computer Player Default Constructor
     */
    public ComputerPlayer(){
        super();
    }
    

    /**
     * Computer Player Constructor
     * @param ID - Player ID 
     * @param name - Player Name
     * @param color - Player Color
     */
    public ComputerPlayer(int ID, String name, String color, int botDelay){
        super(ID, name, color);
        this.rand = new Random();
        this.BOT_DELAY = botDelay;

        this.reinforcementPriority = new TreeMap<Integer, Territory>();
    }



    /**
     * Calculates a numeric priority for reinforcement for all owned territories.
     * Fills reinforcementPriority with priority:territory pairs.
     */
    private void prioritizeTerritories() {
        Util.debug(this.color +  " Entering CPU.prioritizeTerritories.");
        this.reinforcementPriority.clear();

        int prio = 0;

        //Loop through all owned territories
        for (Territory t: this.ownedTerritories){
            prio = evaluateTerritoryPriority(t);

            //Add territory to prio map

            //Have to avoid collisions
            //This means that lower id/longer held territories will be priotized
            while (this.reinforcementPriority.containsKey(prio) ){
                prio--;
            }

            this.reinforcementPriority.put(prio, t);
            
        }

        for (int keys : this.reinforcementPriority.keySet()){
            Util.debug(keys + ":"+ this.reinforcementPriority.get(keys).getName());
        }

    }


    /**
     * Calculates a numeric priority for reinforcement for a single territory.
     * @param terr - Territory to evaluate
     * @return integer priority
     */
    private int evaluateTerritoryPriority(Territory terr) {
        Util.debug(this.color +  " Entering CPU.evaluateTerritoryPriority.");
        /**
         * Priorize based on:
         * Territory's strength
         *      Weaker territories need to be reinforced
         * Number of adjacent enemies
         *      Surrounded territories need to be reinforced
         * Strength of adjacent enemies
         *      Territories with strong neighbors need to be reinforced
         * Strategic value
         *      Territories with important location (chokepoints, continent access points)
         */

        int prio = 0;

        //Prioritize weak territories
        //Do this by deprioritizing strong territories
        prio -= (terr.getOccupancy() * 5);

        //10 power terr -> -50 prio
        //1 power terr -> -5 prio

        //Next, prioritize territiories based on their surroundings
        for (Territory n: terr.getAdjacencies()){

            //Increase priority based on number and strength of enemy neighbors
            if (n.getOwner() != this){
                prio += 10; //Flat amount for being next to an enemy

                //Increase based on strength of the enemy
                if (n.getOccupancy() - terr.getOccupancy() > 0){
                    prio += 5 * ( n.getOccupancy() - terr.getOccupancy() );
                }

                //1 power next to 5 power -> prio + 20
                
                //Prioritize entryways into the continent
                if (n.getContinent() != terr.getContinent()){
    
                    if (n.getContinent().getOwner() != this){
                        if (n.getOwner() == this){
                            prio += 20;
                        }
                        else{
                            prio += 10;
                        }
                    }
                }
            } 
        }

        return prio;

    }



    /**
     * Chooses a territory to attack with.
     * Chooses the CPU's strongest territory that has a valid target.
     * Calls displayController.selectTerritory()
     */
    void chooseAttacker() {
        Util.debug(this.color +  " Entering CPU.chooseAttacker.");
        Territory attacker = null;

        //Strongest territory will attack its weakest neighbor.
        //To be declared the attacker, the territory must:
        //Be the CPU's strongest territory
        //Be at least 3 strong
        //Have a weaker enemy neighbor
        for (Territory t: this.ownedTerritories){

            //Strongest than previous strongest
            if (t.getOccupancy() > 2 && ( attacker == null || (t.getOccupancy() > attacker.getOccupancy())   )   ){
                //Have weaker enemy neighbor
                for (Territory n: t.getAdjacencies()){
                    if ( (n.getOwner() != this) &&  (n.getOccupancy() < t.getOccupancy()) ){
                        attacker = t;
                    }
                }
            }
        }

        //If there is no valid attacker, null will be selected, turn passed.

        this.selectedTerritory = attacker;

        if (attacker == null){
            controller.endPhase();
        }
        else{
            chooseTarget(selectedTerritory); 
        }


        // Timer t = new Timer() {
        //     @Override
        //     public void run(){
        //         displayController.selectTerritory(selectedTerritory); 
        //     }
        // };  
        // t.schedule(BOT_DELAY);
    }


    /**
     * Chooses cards to turn in.
     * Assumes a match exists.
     * Calls displayController.turnInSet()
     */
    void chooseCards() {
        Util.debug(this.color +  " Entering CPU.chooseCards");
        //Count number of each design held

        int inf = 0;
        int cav = 0;
        int art = 0;
        int wil = 0;

        for (Card c: cards){
            if (c.getDesign() == Card.Design.INFANTRY){
                inf++;
            }
            else if (c.getDesign() == Card.Design.CAVALRY){
                cav++;
            }
            else if (c.getDesign() == Card.Design.ARTILLERY){
                art++;
            }
            else if (c.getDesign() == Card.Design.WILD){
                wil++;
            }
        }


        //Choose cards to turn in
        final ArrayList<Card> turnIn = new ArrayList<Card>();

        if (Deck.differentDesignsMatch(inf, cav, art, wil)){

            //If there is a match by having one of each design,
            //Search for 3 cards with different designs

            boolean i = false;
            boolean c = false;
            boolean a = false;

            for (Card card: this.cards){

                //If a design is not already in the list, add to list
                if ( (card.getDesign() == Card.Design.INFANTRY) && !i ){
                    turnIn.add(card);
                    i = true;
                }
                else if ( (card.getDesign() == Card.Design.CAVALRY) && !c ){
                    turnIn.add(card);
                    c = true;
                }
                else if ( (card.getDesign() == Card.Design.ARTILLERY) && !a ){
                    turnIn.add(card);
                    a = true;
                }
            }
        }
        else{
            
            //If there is a match from having 3 of the same design,
            //search for 3 cards with that design
            Card.Design match = Deck.sameDesignMatch(inf, cav, art, wil);
            if (match != Card.Design.NONE){
                for (Card c: this.cards){
                    if (  (c.getDesign() == match) && turnIn.size() < 3  ){
                        turnIn.add(c);
                    }
                }
            }
        }

        Timer t = new Timer() {
            @Override
            public void run() {
                controller.turnInSet(turnIn);
            }
        };  
        t.schedule(BOT_DELAY);

    }

    /**
     * Chooses a number of troops to place.
     * Chooses half the current placeable troops.
     * Calls displayController.reinforce(num)
     */
    void chooseNumberOfTroops() {
        Util.debug(this.color +  " Entering CPU.chooseNumberOfTroops.");
        final int num = Math.max(this.troopsToPlace / 2, 1);

        Timer t = new Timer() {
            @Override
            public void run() {
                controller.reinforce(selectedTerritory, num);
            }
        };  
        t.schedule(BOT_DELAY);
    }


    void chooseNumberOfTroops(Territory terr){
        Util.debug(this.color +  " Entering CPU.chooseNumberofTroops(Territory).");
        final int num = Math.max(this.troopsToPlace / 2, 1);

        controller.reinforce(terr, num);
    }


    /**
     * Chooses a territory to reinforce.
     * Calls selectForAllocation with the highest priority territory.
     */
    void chooseOwnedTerritory() {
        Util.debug(this.color +  " Entering CPU.chooseOwnedTerritory.");
        if (this.reinforcementPriority.size() == 0){
            prioritizeTerritories();
        }

        final Territory terr = this.reinforcementPriority.get(this.reinforcementPriority.lastKey());
        this.reinforcementPriority.remove(this.reinforcementPriority.lastKey());

        this.selectedTerritory = terr;
        chooseNumberOfTroops(terr);

        // Timer t = new Timer() {
        //     @Override
        //     public void run() {
        //         displayController.selectForAllocation(terr);
        //     }
        // };  
        // t.schedule(BOT_DELAY);

    }

    void chooseOwnedTerritory(boolean initial){
        Util.debug(this.color +  " Entering CPU.chooseOwnedTerritory(bool).");
        this.displayController.foreignAllocationDisplay(this, initial);
        if (this.reinforcementPriority.size() == 0){
            prioritizeTerritories();
        }

        // Util.debug("A");
        
        final Territory terr = this.reinforcementPriority.get(this.reinforcementPriority.lastKey());
        // Util.debug("B");
        this.reinforcementPriority.remove(this.reinforcementPriority.lastKey());
        // Util.debug("C");
        
        final int num = Math.max(this.troopsToPlace / 2, 1);
        // Util.debug("D");
        
        Timer t = new Timer() {
            @Override
            public void run() {
                // Util.debug("E");
                controller.reinforce(terr, num);
            }
        };  
        t.schedule(BOT_DELAY);

    }

    /**
     * Choose whether to turn in cards.
     * CPU will always turn in cards if they have a match in hand.
     */
    void chooseToTurnInCards() {
        Util.debug(this.color +  " Entering CPU.chooseToTurnInCards.");

        Timer t = new Timer() {
            @Override
            public void run() {
                if (Deck.matchInHand(cards)){
                    controller.turnInPhase();
                }
                else{
                    controller.reinforcePhase();
                }
            }
        };  
        t.schedule(BOT_DELAY);
        

    }


    /**
     * Chooses a territory to attack
     * Chooses the weakest enemy neighbor of the previously selected attacker.
     * Assumes selectedTerritory is set.
     * Calls displayController.targetTerritory(), or displayController.endPhase() on error.
     */
    void chooseTarget(Territory terr) {
        Util.debug(this.color +  " Entering CPU.chooseTarget.");

        //chooseAttacker has already confirmed that the attacker has a valid target.
        //Choose the weakest enemy neighbor.

        Territory target = null;
        for (Territory t: terr.getAdjacencies()){
            if ( t.getOwner() != this && (t.getOccupancy() < terr.getOccupancy()) && (target == null || target.getOccupancy() > t.getOccupancy()) ){
                target = t;
            }
        }

        this.targetTerritory = target;

        if (targetTerritory == null){
            Util.debug("ctchose=null");
            displayController.endPhase();
        }
        else{
            Util.debug("ctchose=" + targetTerritory.getName());
            int maxDice = (this.selectedTerritory.getOccupancy() >= 4) ? 3 : this.selectedTerritory.getOccupancy() - 1;
            chooseNumberOfDice(this.selectedTerritory, maxDice, false);
        }


        

        // Timer t = new Timer() {
        //     @Override
        //     public void run() {
        //         if (targetTerritory == null){
        //             Util.debug("ctchose=null");
        //             displayController.endPhase();
        //         }
        //         else{
        //             Util.debug("ctchose=" + targetTerritory.getName());
        //             displayController.targetTerritory(targetTerritory);
        //         }
        //     }
        // };  
        // t.schedule(BOT_DELAY);


    }


    

    /**
     * Choose a number of dice to roll.
     * CPU will always roll the maximum dice it can without becoming the weaker territory.
     * Calls displayController.setDefenseRoll() if defending, displayController.battlePhase() if retreating, displayController.setAttackRoll() if attacking.
     * @param maxDice - Maximum number of dice rollable.
     * @param defending - True if defending. CPU will always roll max when defending.
     * @return Maximum number of dice to roll.
     */
    void chooseNumberOfDice(Territory terr, int maxDice, boolean defending){
        
        Util.debug(this.color + " Entering CPU.chooseNumberOfDice" + 
        "\nterr=" + terr.getName() + 
        "\n maxDice=" + maxDice +
        "\n defending="+ defending);

        //Can't retreat. Defend to the end.
        if (defending){
            controller.bellum(maxDice);
            return;
        }

        //CPU will continue to attack until:
        //Attacking would risk the attacker becoming weaker than the defender, or
        //Attacking would risk the attacker dropping below 2 power.

        //CPU will always roll max dice, or the maximum that the attacker can lose and still remain equal strength.

        int roll;
        int strDiff = this.selectedTerritory.getOccupancy() - this.targetTerritory.getOccupancy();

        if (strDiff <= 0){
            roll = 0;
        }
        else{
            roll = (strDiff > 3) ? 3 : strDiff;
        }


        if (roll > maxDice){
            Util.debug("ERROR: CPU chose to roll higher than maxDice");
            roll = maxDice;
        }

        final int r = roll;

        Timer t = new Timer() {
            @Override
            public void run() {
                if (r == 0){
                    controller.battlePhase();
                }
                else{
                    controller.antebellum(selectedTerritory, targetTerritory, r);
                }
            }
        };  
        t.schedule(BOT_DELAY);


    }

    void chooseNumberOfAttackDice(Territory attacker, Territory defender){
        chooseNumberOfDice(attacker, attacker.calcMaxDice(false), false);
    }

    void chooseNumberOfDefenseDice(Territory attacker, Territory defender, int attackRoll){
        chooseNumberOfDice(defender, defender.calcMaxDice(true), true);  
    }
    
    /**
     * Choose a number of troops to move into a conquered territory.
     * CPU chooses a number to move based on the reinforcement priorities of the source and destination.
     * Calls this.displayController.transfer()
     * @param min Minimum allowable troops to move.
     * @param src Territory moving troops from.
     * @param dst Territory moving troops to.
     */
    void chooseNumberOfTroopsToMove(int min, Territory src, Territory dst) {
        Util.debug(this.color +  " Entering CPU.choosenumberofTroopsToMove.");
        if (src == null){
            Util.debug("CPU" + this.ID + " src = null in tomove ");
            controller.transfer(min);
            return;
        }
        if (dst == null){
            Util.debug("CPU" + this.ID + " dst = null in tomove ");
            controller.transfer(min);
            return;
        }

        

        //Choose number to move based on priorities of the two territories
        //dst will be heavily favored because it is at 0 power at calculation time

        int srcPrio = evaluateTerritoryPriority(src);
        int dstPrio = evaluateTerritoryPriority(dst);

        int move = 0;

        //src is no longer relevant
        if (srcPrio < 0 && dstPrio > 0){
            move = src.getOccupancy() - 2;
        }
        //dst is irrelevant
        else if (srcPrio > 0 && dstPrio < 0){
            move = min;
        }
        //src is less important
        else if (srcPrio < dstPrio - 20){
            move = (int)(src.getOccupancy() * .75);
        }
        //dst is less important
        else if (dstPrio < srcPrio - 20){
            move = (int)(src.getOccupancy() * .25);
        }
        //about the same
        else{
            move = src.getOccupancy() / 2;
        }

        //Prefer that it doesn't move too much
        move = ( (src.getOccupancy() - move) >= 2) ? move : src.getOccupancy() - 2;

        //Error check that it doesn't move less than the minimum
        move = (move < min) ? min : move;

        final int m = move;

        Util.debug("tomove=" + move + " ");

        Timer t = new Timer() {
            @Override
            public void run() {
                controller.transfer(m);
            }
        };  
        t.schedule(BOT_DELAY);

    }



    /**
     * Reveals my robotic nature.
     * @return "CPU"
     */
    String isA(){
        return "CPU";
    }



    /**
     * Begins a turn.
     * Resets selected/target territories.
     * Calculates reinforcements for this turn.
     * Evaluates reinforcement priorities.
     * Calls DisplayController.startTurn
     */
    void takeTurn() {
        Util.debug(this.color +  " Entering CPU.taketurn.");
        this.selectedTerritory = null;
        this.targetTerritory = null;
        this.troopsToPlace = this.calculateReinforcements();

        prioritizeTerritories();

        this.displayController.readyTurn(this);

        Timer t = new Timer() {
            @Override
            public void run() {
                controller.startTurn();
            }
        };  
        t.schedule(BOT_DELAY);
        
    }


    /**
     * Starts a turn.
     */
    void startTurn(){
        Util.debug(this.color +  " Entering CPU.startTurn.");
    }

}