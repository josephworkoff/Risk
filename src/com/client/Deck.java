/** File: Deck.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 11/06/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 2
 * Purpose: Deck class for representing the deck of territory cards.
*/

package com.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Deck Class. Acts as container for Card objects. Provides methods for operating on multiple cards.
 */
public class Deck implements Serializable{

    /**
     * The current bonus units received for trading in cards.
     */
    private int bonus = 0;
    /**
     * Number of sets of cards traded in throughout the game.
     */
    private int setsTraded = 0;
    /**
     * Array of all Card objects in the game.
     */
    private ArrayList<Card> cards;
    /**
     * 
     * @return Current Bonus
     */
    public int getBonus(){return this.bonus;}
    /**
     * 
     * @return Number of sets traded in
     */
    public int getSetsTraded(){return this.setsTraded;}
    /**
     * 
     * @return ArrayList of Cards
     */
    public ArrayList<Card> getCards(){return this.cards;}
    /**
     * 
     * @param bonus New bonus
     */
    public void setBonus(int bonus){this.bonus = bonus;}
    /**
     * 
     * @param setsTraded New Sets Traded In
     */
    public void setSetsTraded(int setsTraded){this.setsTraded = setsTraded;}
    /**
     * 
     * @param cards New ArrayList of Cards.
     */
    public void setCards(ArrayList<Card> cards){this.cards = cards;}



    public Deck(){
        this.cards = new ArrayList<Card>();
    }


    /**
     * Deck constructor.
     * @param board The board object
     */
    public Deck(Board board){
        this.cards = new ArrayList<Card>();
        createCards(board);
    }



    /**
     * Checks whether 3 cards match.
     * Matching means having 3 of the same or 1 of each Card.Design.
     * Only applies to sets of 3 cards.
     * @param cards ArrayList of 3 cards.
     * @return True if the 3 cards match. False otherwise.
     */
    public static boolean checkCardsMatch(ArrayList<Card> cards){
        Card.Design firstDesign = cards.get(0).getDesign();
        Card.Design secondDesign = cards.get(1).getDesign();
        Card.Design thirdDesign = cards.get(2).getDesign();
        
        //If theres a wild, the other two can be the same or different and still match.
        //e.g. WILD/INFANTRY/INFANTRY - match through 3 same
        //     WILD/INFANTRY/ARTILLERY - match through 3 different
        if (firstDesign == Card.Design.WILD || secondDesign == Card.Design.WILD || thirdDesign == Card.Design.WILD){
            return true;
        }
        //No wilds
        //Everything must be the same or different
        //e.g. INFANTRY/INFANTRY/INFANTRY
        //     INFANTRY/CAVALRY/ARTILLERY
        else{
            if (firstDesign == secondDesign){
                return (secondDesign == thirdDesign);
            }
            else{
                return (firstDesign != thirdDesign && secondDesign != thirdDesign);
            }
        }
    }



    /**
     * Determines whether a hand has a match in it.
     * @param cards - Player's hand
     * @return - True if there is a match in hand
     */
    public static boolean matchInHand(ArrayList<Card> cards){
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

        
        return (sameDesignMatch(inf, cav, art, wil) != Card.Design.NONE) || differentDesignsMatch(inf, cav, art, wil);
        
    }
    
    
    
    /**
     * Determines whether a group of cards has a match from 3 same designs.
     * @param inf - number of infantry cards 
     * @param cav - number of cavalary cards
     * @param art - number of artillery cards
     * @param wil - number of wild cards
     * @return Design.NONE: No match
     *         Design.INFANTRY: Match through 3 infantries
     *         Design.CAVALRY: Match through 3 cavalaries
     *         Design.ARTILLERY: Match through 3 artilleries
     *         Design.WILD: Match through >2 wilds
     */
    public static Card.Design sameDesignMatch(int inf, int cav, int art, int wil){
        Util.debug("Deck.sameDesignMatch");
        //If a design's total > 2, there's a match. Wilds count to lower this amount.

        if (inf > 2 - wil){
            return Card.Design.INFANTRY;
        }
        else if (cav > 2 - wil){
            return Card.Design.CAVALRY;
        }
        else if (art > 2 - wil){
            return Card.Design.ARTILLERY;
        }
        else if (wil > 2){
            return Card.Design.WILD;
        }
        else{
            return Card.Design.NONE;
        }

    }


    /**
     * Determines whether a group of cards has a match from 3 different designs.
     * @param inf - number of infantry cards 
     * @param cav - number of cavalary cards
     * @param art - number of artillery cards
     * @param wil - number of wild cards
     * @return true if match exists
     */
    public static boolean differentDesignsMatch(int inf, int cav, int art, int wil){
        
        if (wil == 0){
            return ((inf > 0) &&
                    (cav > 0) &&
                    (art > 0) );
        }

        if (wil == 1){
            return ( (inf > 0 && cav > 0) || (inf > 0 && art > 0) || (cav > 0 && art > 0) );
        }

        if (wil == 2){
            return ( (inf > 0) || (cav > 0) || (art > 0) );
        }
        return true;
    }



    /**
     * Creates 44 cards.
     * @param board Board object to link to its territories.
     */
    private void createCards(Board board){
        Card.Design design;

        //Create 42 cards with a specific Territory and Design
        for (int i = 0; i < 42; i++){
            switch (i % 3){
                case 0:
                    design = Card.Design.INFANTRY;
                    break;
                case 1:
                    design = Card.Design.CAVALRY;
                    break;
                case 2:
                    design = Card.Design.ARTILLERY;
                    break;
                default:
                    design = Card.Design.NONE;
            }
            this.cards.add(new Card(i, board.getTerritoryByIndex(i), design));
        }

        //Create two Wild Cards
        this.cards.add(new Card(42, null, Card.Design.WILD));
        this.cards.add(new Card(43, null, Card.Design.WILD));

    }



    /**
     * Creates a single card by calling the Card constructor.
     * Used for player cheats when creating a new card.
     * @param territory The Card's Territory
     * @param design The Card's design
     * @return New Card object
     */
    public Card createNewCard(Territory territory, Card.Design design){
        Card card = new Card(this.cards.size(), territory, design);
        this.cards.add(card);
        return card;
    }



    /**
     * Selects a random Card that is still in the deck.
     * @return Selected Card.
     */
    public Card draw(){
        Random rand = new Random();
        int cardID;

        //Make sure the card is still in the deck.
        do{
            cardID = rand.nextInt(this.cards.size());
        } while (this.cards.get(cardID).getLocation() != 0);

        Card card = this.cards.get(cardID);
        return card; //player needs to update location

    }



    /**
     * Prints the details of every card in the game.
     */
    public void printDeck(){
        for (Card c: cards){
            System.out.println(c.getCardString());
        }
    }



    /**
     * Trades in a set of 3 matching cards. Assumes the cards are a proper set.
     * Removes the cards from the game by calling Card.discard.
     * Increments setsTraded. Calculates the bonus and returns it. 
     * @param cards ArrayList of cards.
     * @return Bonus troops
     */
    public int tradeIn(ArrayList<Card> cards){
        setsTraded++;
        //Each set is worth 2 more than the last.
        if (setsTraded < 6){
            bonus = (setsTraded * 2) + 2;
        }
        //After the 6th set, each set is worth 5 more.
        else{
            bonus = ( (setsTraded - 6) * 5) + 15;
        }
        for (Card c: cards){
            c.discard();
        }
        return bonus;
    }






}