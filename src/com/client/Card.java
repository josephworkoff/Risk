/** File: Card.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 11/06/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 2
 * Purpose: Card class for representing a single territory card.
*/

package com.client;

import java.io.Serializable;

/**
 * Card class. Represents a single territory card.
 */
public class Card implements Serializable{

    public enum Design{
        INFANTRY,
        CAVALRY,
        ARTILLERY,
        WILD,
        NONE
    }


    /** Integer card ID */
    private int ID;

    /** Card's corresponding Territory. Null for Wild cards. */
    private Territory territory;

    /**
     * Numeric card location.
     * -1 = Discarded/Out of the game
     * 0  = In the deck
     * >0 = In that Player's hand.
     */
    private int location;

    /**
     * Card's design.
     * INFANTRY/CAVALRY/ARTILLERY/WILD
     */
    private Design design;


    /**
     * Card constructor
     */
    public Card(){
        this.ID = 0;
        this.territory = null;
        this.location = -1;
        this.design = Design.NONE;
    }

    /**
     * Card Constructor
     * @param ID Card ID
     * @param territory Corresponding Territory
     * @param design INFANTRY/CAVALRY/ARTILLERY
     */
    public Card(int ID, Territory territory, Design design){
        this.ID = ID;
        this.territory = territory;
        this.location = 0;
        this.design = design;
    }


    /**
     * ID getter
     * @return Card ID
     */
    public int getID(){return this.ID;}

    /**
     * Design Getter
     * @return Card's Design
     */
    public Design getDesign(){return this.design;}

    /**
     * Location Getter
     * @return Card's location code
     */
    public int getLocation(){return this.location;}

    /**
     * Territory Getter
     * @return Card's territory
     */
    public Territory getTerritory(){return this.territory;}

    /**
     * Returns a single string of the territory's ID, Territory name, and Design
     * @return Card String
     */
    public String getCardString(){
        return (this.territory == null) ? Integer.toString(this.ID) + ". " + this.design : Integer.toString(this.ID) + ". " + this.territory.getName() + "/" + this.design;
    }

    /**
     * ID setter
     * @param ID New Card ID
     */
    public void setID(int ID){this.ID = ID;}

    /**
     * Design Setter
     * @param design New Card Design
     */
    public void setDesign(Design design){this.design = design;}

    /**
     * Location Setter
     * @param location New Card Location
     */
    public void setLocation(int location){this.location = location;}
    
    /**
     * Territory Setter
     * @param territory New Card Territory
     */
    public void setTerritory(Territory territory){this.territory = territory;}
    
    /**
     * Sets Card's location to -1, out of the game.
     */
    public void discard(){this.location = -1;}


    /**
     * Converts a string design to a Design enum.
     * @param design String card design
     * @return Design type
     */
    public static Design convertStringToDesign(String design) {
        if (design.equalsIgnoreCase("INFANTRY")){
            return Design.INFANTRY;
        }
        else if (design.equalsIgnoreCase("CAVALRY")){
            return Design.CAVALRY;
        }
        else if (design.equalsIgnoreCase("ARTILLERY")){
            return Design.ARTILLERY;
        }
        else if (design.equalsIgnoreCase("WILD")){
            return Design.WILD;
        }
        else {
            return Design.NONE;
        }
    }

    


}