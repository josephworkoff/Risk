/** File: Continent.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 11/06/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 2
 * Purpose: Continent Class for representing a single Continent
*/

package com.client;

import java.awt.Color;
import java.io.Serializable;


/**
 * Continent Class
 */
public class Continent implements Serializable{

    /** Integer Continent ID 1 - 6 */
    private int ID;

    /** Continent's name */
    private String name;

    /** Troop bonus for controlling this continent */
    private int controlBonus;

    /** Continent's current owner */
    private Player owner;

    /** Territories contained in this continent */
    private Territory[] territories;

    
    /**
     * Default Constructor
     */
    public Continent(){
        this.ID = 0;
        this.name = "";
        this.owner = null;
        this.territories = new Territory[0];
        // this.continentColor = null;
    }

    /**
     * Constructor
     * @param ID - Numeric ID [1-6]
     * @param name - Continent Name
     * @param controlBonus - Troop bonus for controlling this continent
     * @param territories - Array of Territories inside this continent
     */
    public Continent(int ID, String name, int controlBonus, Territory... territories){
        this.ID = ID;
        this.name = name;
        this.controlBonus = controlBonus;
        this.owner = null;

        this.territories = new Territory[territories.length];
        System.arraycopy(territories, 0, this.territories, 0, this.territories.length);
    }
    
    /**
     * ID Getter
     * @return ID
     */
    public int getID() {return this.ID;}

    /**
     * Name Getter
     * @return name
     */
    public String getName() {return this.name;}
    
    /**
     * Bonus Getter
     * @return Bonus
     */
    public int getBonus() {return this.controlBonus;}

    /**
     * Owner Getter
     * @return owner
     */
    public Player getOwner() {return this.owner;}

    /**
     * Territories Getter
     * @return Territories
     */
    public Territory[] getTerritories(){return this.territories;}


    /**
     * Checks if all territories in this continent are owned by the same player.
     * If so, updates its ownership.
     * If not, removes itself from its last owner if necessary.
     * Returns true if dominated.
     */
    public boolean checkDomination(){

        Player temp = this.territories[0].getOwner();

        for (Territory terr: this.territories){
            if (terr.getOwner() != temp){
                if (this.owner != null){
                    this.owner.loseContinent(this);
                }
                this.owner = null;
                return false; 
            }
        }
        this.owner = temp;
        return true;
    }
}