/** File: Territory.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 09/24/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: Territory class for representing a single territory on the board.
*/

package com.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Territory Class
 */
public class Territory implements Serializable{
    /** Integer ID 1 - 42 */
    private int ID;

    /** Territory Name */
    private String name;

    /** Territory's continent */
    private Continent continent;

    /** Territory's current owner */
    private Player owner;

    /** Number of troops current in this Territory */
    private int occupancy;

    /** Array of IDs of territories adjacent to this */
    private int[] adjacentIDs;

    /** List of Territory objects adjacent to this */
    private ArrayList<Territory> adjacencies;

    /** Territory's button on the GUI */
    private Button button;

    /** Territory's occupancy text box on the GUI */
    private TextBox occTextBox;


    /**
     * Default constructor
     */
    public Territory(){
        this.ID = 0;
        this.name = "";
        this.continent = null;
        this.owner = null;
        this.occupancy = 0;
        this.adjacencies = new ArrayList<Territory>();
        this.adjacentIDs = new int[0];
        this.button = null;
        this.occTextBox = null;

    }

    /**
     * Constructor
     * @param ID - Territory ID [1-42]
     * @param name - Territory name
     * @param IDs - Array of IDs of adjacent territories
     */
    public Territory(int ID, String name, int... IDs){
        this.ID = ID;
        this.name = name;
        this.continent = null;
        this.owner = null;
        this.occupancy = 0;
        this.adjacencies = new ArrayList<Territory>();
        this.button = null;
        this.occTextBox = null;

        this.adjacentIDs = new int[IDs.length];
        System.arraycopy(IDs, 0, this.adjacentIDs, 0, this.adjacentIDs.length);
    }

    /**
     * Adds a territory to adjacencies
     * @param territory
     */
    public void addAdjacency(Territory territory){
        this.adjacencies.add(territory);
    }

    /**
     * ID getter
     * @return ID
     */
    public int getID() {return this.ID;}

    /**
     * Name Getter
     * @return Name
     */
    public String getName() {return this.name;}

    /**
     * Continent Getter
     * @return Continent
     */
    public Continent getContinent() {return this.continent;}

    /**
     * Owner Getter
     * @return Owner
     */
    public Player getOwner() {return this.owner;}

    /**
     * Occupancy Getter
     * @return Occupancy
     */
    public int getOccupancy() {return this.occupancy;}

    /**
     * AdajcentIDs getter
     * @return AdjacentIDs
     */
    public int[] getAdjacentIDs(){return this.adjacentIDs;}

    /**
     * Adjacencies Getter
     * @return Adjacencies
     */
    public ArrayList<Territory> getAdjacencies(){return this.adjacencies;}

    /**
     * Continent Setter
     * @param continent
     */
    public void setContinent(Continent continent){this.continent = continent;}

    /**
     * Adds to occupancy
     * @param troops
     */
    public void changeOccupancy(int troops){this.occupancy += troops;}
    
    public void setOcc(int occupancy){this.occupancy = occupancy;}

    /**
     * Occupancy Setter
     * @param occupancy
     */
    public void setOccupancy(int occupancy){
        this.occupancy = occupancy;
        this.updateOccBox();
    }

    /**
     * Button Setter
     * @param button
     */
    public void setButton(Button button){this.button = button;}

    /**
     * Button Getter
     * @return Button
     */
    public Button getButton(){return this.button;}

    /**
     * OccBox Setter
     * @param box
     */
    public void setOccTextBox(TextBox box){this.occTextBox = box;}

    /**
     * OccBox Getter
     * @return OccTextBox
     */
    public TextBox getOccTextBox(){return this.occTextBox;}

    /**
     * Updates and colors the occupancy display
     */
    public void updateOccBox(){
        if (this.occTextBox != null){
            this.occTextBox.setText(String.valueOf(this.occupancy));
            this.occTextBox.setStyleName("owned" + this.owner.color);
        }
    }

    /**
     * Subtracts from occupancy.
     * @param damage
     * @return True if occupancy reduced to 0
     */
    public boolean takeDamage(int damage){
        if (damage >= this.occupancy){
            this.setOccupancy(0);
            return true;
        }
        else{
            this.setOccupancy(this.occupancy - damage);
            return false;
        }
    }

    /**
     * Checks whether a territory is adjacent to this one
     * @param territory
     * @return True if adjacent.
     */
    public boolean checkAdjacency(Territory territory){
        return this.adjacencies.contains(territory);
    }

    /**
     * Removes this territory from its owner.
     * Checks the continent for ownership.
     * Updates with a new owner and occupancy.
     * Updates Occupancy Box
     * @param player
     * @param occupancy
     */
    public void capture(Player player, int occupancy){
        if (this.owner != null){
            this.owner.loseTerritory(this);
        }
        this.continent.checkDomination();
        this.owner = player;
        this.occupancy = occupancy;
        this.updateOccBox();
    }
    
    /**
     * Checks if this territory is adjacent to an enemy-controlled territory.
     * @return - True if next to an enemy.
     */
    public boolean enemyAdjacent() {
        for (Territory t: this.adjacencies){
            if (t.getOwner() != this.owner){
                return true;
            }
        }
        return false;
    }



    /**
     * Calculates the maximum amount of dice rollable based on this territory's occupancy.
     * When attacking: Occ - 1 up to 3
     * When defending: Occ up to 2
     * @param defending True if defending
     * @return int 1:3
     */
    public int calcMaxDice(boolean defending){
        if (defending){
            return (this.occupancy >= 2) ? 2 : 1;
        }
        else{
            return (this.occupancy >= 4) ? 3 : this.occupancy - 1;
        }
    }

}