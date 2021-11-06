/** File: Board.java 
 * @author Joseph Workoff
 * Creation Date: 09/24/2020
 * Major: CS/SW MS
 * Due Date: 11/06/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 2
 * Purpose: Board class for representing the collections of Territories and Continents
*/

package com.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import java.util.Collections;
import java.util.Arrays; 

/**
 * Board Class. Container for the Territories and Continents.
 */
public class Board implements Serializable{
    /**
     * Territory Array
     */
    private Territory[] territories;
    /**
     * Continents Array
     */
    private Continent[] continents;

    /**
     * Determines whether the board is displayed
     */
    private boolean invisible;
    /**
     * Determines whether there is a delay before printing the boa
     */
    private boolean nodelay;
    
    /**
     * Board Display
     */
    // private Display display;

    /**
     * Creates the 44 territories
     */
    private void createTerritories() {
        this.territories = new Territory[42]; //42
        // public Territory(int ID, String name, int continent, int... adjacencies)
        
        //North America:1
        
        //Alaska:1 -> Alberta:2, Northwest Territory:6, Kamchatka:32:32
        this.territories[0] = new Territory(1, "Alaska",  2, 6, 32);
        
        //Alberta:2 -> Alaska:1, Northwest Territory:6, Ontario: 7, Western United States:9
        this.territories[1] = new Territory(2, "Alberta",  1, 6, 7, 9);
        
        //Central America:3 -> Eastern United States: 4, Western United States: 9, Venezuela:13
        this.territories[2] = new Territory(3, "Central America",  4, 9, 13);

        //Eastern United States:4 -> Central America:3, Ontario:7, Quebec:8, Western United States: 9
        this.territories[3] = new Territory(4, "Eastern United States",  3, 7, 8, 9);

        //Greenland:5 -> Northwest Territory:6, Ontario:7, Quebec:8, Iceland:15
        this.territories[4] = new Territory(5, "Greenland",  6, 7, 8, 15);

        //Northwest Territory:6 -> Alaska:1, Alberta:2, Greenland:5, Ontario:7
        this.territories[5] = new Territory(6, "Northwest Territory",  1, 2, 5, 7);

        //Ontario:7 -> Alberta:2, Eastern United States:4, Greenland:5, Northwest Territory:6, Quebec:8, Western United States:9
        this.territories[6] = new Territory(7, "Ontario",  2, 4, 5, 6, 8, 9);

        //Quebec:8 -> Eastern United States, Greenland:5, Ontario:7
        this.territories[7] = new Territory(8, "Quebec",  4, 5, 7);

        //Western United States:9 -> Alberta:2, Central America:3, Eastern United States:4, Ontario, 7
        this.territories[8] = new Territory(9, "Western United States",  2, 3, 4, 7);
        


        //South America:2

        //Argentina:10 -> Brazil:11, Peru:12
        this.territories[9] = new Territory(10, "Argentina",  11, 12);

        //Brazil:11 -> Argentina:10, Peru:12, Venezuela:13, North Africa:25
        this.territories[10] = new Territory(11, "Brazil",  10, 12, 13, 25);

        //Peru:12 -> Argentina:10, Brazil:11, Venezuela:13
        this.territories[11] = new Territory(12, "Peru",  10, 11, 13);

        //Venezuela:13 -> Central America:3, Brazil:11, Peru:12
        this.territories[12] = new Territory(13, "Venezuela",  3, 11, 12);



        //Europe:3

        //Great Britain:14 -> Iceland:15, Northern Europe:16, Scandinavia:17, Western Europe: 20
        this.territories[13] = new Territory(14, "Great Britain",  15, 16, 17, 18, 20);
        
        //Iceland:15 -> Greenland:5, Great Britain:14, Scandinavia:17
        this.territories[14] = new Territory(15, "Iceland",  5, 14, 17 );

        //Northern Europe:16 -> Great Britain:14, Scandinavia:17, Southern Europe:18, Ukraine:19, Western Europe:20
        this.territories[15] = new Territory(16, "Northern Europe",  14, 17, 18, 19, 20);        
        
        //Scandinavia:17 -> Great Britain:14, Iceland:15, Northern Europe:16, Ukraine:19
        this.territories[16] = new Territory(17, "Scandinavia",  14, 15, 16, 19);

        //Southern Europe:18 -> Northern Europe:16, Ukraine:19, Western Europe:20, Egypt:23, North Africa:25, Middle East:33, 
        this.territories[17] = new Territory(18, "Southern Europe",  16, 19, 20, 23, 25, 33);
        
        //Ukraine:19 -> Northern Europe:16, Scandinavia:17, Southern Europe:18, Afghanistan:27, Middle East:33, Ural:37
        this.territories[18] = new Territory(19, "Ukraine",  16, 17, 18, 27, 33, 37);
        
        //Western Europe:20 -> Great Britain:14, Northern Europe:16, Southern Europe:18, North Africa:25
        this.territories[19] = new Territory(20, "Western Europe",  14, 16, 18, 25);



        //Africa: 4

        //Congo:21 ->  East Africa:22, North Africa:25, South Africa:26
        this.territories[20] = new Territory(21, "Congo",  22, 25, 26);

        //East Africa:22 -> Egypt:23, Madagascar:24, North Africa:25, South Africa:26, Middle East:33
        this.territories[21] = new Territory(22, "East Africa",  23, 24, 25, 26, 33);

        //Egypt:23 -> Southern Europe:18, East Africa:22, North Africa:25, Middle East:33
        this.territories[22] = new Territory(23, "Egypt", 18, 22, 25, 33);

        //Madagascar:24 -> East Africa:22, South Africa:26
        this.territories[23] = new Territory(24, "Madagascar",  22, 26);
        
        //North Africa:25 -> Brazil:11, Western Europe:20, Congo:21, East Africa:22, Egypt:23
        this.territories[24] = new Territory(25, "North Africa",  11, 20, 21, 22, 23);
        
        //South Africa:26 -> Congo:21, East Africa:22, Madagascar:24
        this.territories[25] = new Territory(26, "South Africa",  21, 22, 24);


        
        //Asia:5

        //Afghanistan:27 -> Ukraine:19, China:28, India:29, Middle East:33, Ural:37:,
        this.territories[26] = new Territory(27, "Afghanistan",  19, 28, 29, 33, 37);

        //China:28 -> Afghanistan:27, India:29, Mongolia:34, Siam:35, Siberia:36,  Ural:37
        this.territories[27] = new Territory(28, "China",  27, 29, 34, 35, 36, 37);

        //India:29 -> Afghanistan:27, China:28, Middle East:33, Siam:35
        this.territories[28] = new Territory(29, "India",  27, 28, 33, 35);

        //Irkutsk:30 -> Kamchatka:32, Mongolia:34, Siberia:36, Yakutsk:38 
        this.territories[29] = new Territory(30, "Irkustk",  32, 34, 36, 38);

        //Japan:31 -> Kamchatka:32, Mongolia:34
        this.territories[30] = new Territory(31, "Japan",  32, 34);

        //Kamchatka:32 -> Alaska:1, Irkutsk:30, Japan:31, Mongolia:34, Yakutsk:38
        this.territories[31] = new Territory(32, "Kamchatka",  1, 30, 31, 34, 38);

        //Middle East:33 -> Southern Europe:18, Ukraine:19, East Africa:22, Egypt:23, Afghanistan:27, India:29  
        this.territories[32] = new Territory(33, "Middle East",  18, 19, 22, 23, 27, 29);

        //Mongolia:34 -> China:28, Irkutsk:30, Japan:31, Siberia:36
        this.territories[33] = new Territory(34, "Mongolia",  28, 30, 31, 36);

        //Siam:35 -> China:28, India:29, Indonesia:40
        this.territories[34] = new Territory(35, "Siam",  28, 29, 40);

        //Siberia:36 -> China:28, Irkutsk:30, Mongolia:34, Ural:37, Yakutsk:38
        this.territories[35] = new Territory(36, "Siberia", 28, 30, 23, 37, 38);

        //Ural:37 -> Ukraine:19, Afghanistan:27, China:28, Siberia:36, 
        this.territories[36] = new Territory(37, "Ural",  19, 27, 28, 36);

        //Yakutsk:38 -> Irkutsk:30, Kamchatka:32, Siberia:36
        this.territories[37] = new Territory(38, "Yakutsk",  30, 32, 36);



        //Australia:6

        //Eastern Australia:39 -> New Guinea:41, Western Australia:42
        this.territories[38] = new Territory(39, "Eastern Australia",  41, 42);

        //Indonesia:40 -> Siam:35, New Guinea:41, Western Australia:42
        this.territories[39] = new Territory(40, "Indonesia",  35, 41, 42);

        //New Guinea:41 -> Eastern Australia:39, Indonesia:40
        this.territories[40] = new Territory(41, "New Guinea",  39, 40);

        //Western Australia:42 -> Eastern Australia:39, Indonesia:40
        this.territories[41] = new Territory(42, "Western Australia",  39, 40);


    }

    /**
     * Creates the 6 continents. Points them to the their respective territories. Points the territories to their repsective continents.
     */
    private void createContinents(){
        this.continents = new Continent[6];

        this.continents[0] = new Continent(1, "North America", 5, Arrays.copyOfRange(this.territories, 0, 9));
        this.continents[1] = new Continent(2, "South America", 2, Arrays.copyOfRange(this.territories, 9, 13));
        this.continents[2] = new Continent(3, "Europe", 5, Arrays.copyOfRange(this.territories, 13, 20));
        this.continents[3] = new Continent(4, "Africa", 3, Arrays.copyOfRange(this.territories, 20, 26));
        this.continents[4] = new Continent(5, "Asia", 7, Arrays.copyOfRange(this.territories, 26, 38));
        this.continents[5] = new Continent(6, "Australia", 2, Arrays.copyOfRange(this.territories, 38, 42));

        int j = 0;
        for (int i = 0; i < this.continents.length; i++){
            for (j = 0; j < this.continents[i].getTerritories().length; j++){
                this.continents[i].getTerritories()[j].setContinent(this.continents[i]);
            }
        }
    }

    /**
     * Points each territory to their neighbors.
     */
    private void createAdjacencies() {
        int temp, j = 0;
        for (int i = 0; i < this.territories.length; i++){
            for (j = 0; j < this.territories[i].getAdjacentIDs().length; j++){
                this.territories[i].addAdjacency(this.territories[this.territories[i].getAdjacentIDs()[j] - 1]);
            }
        }
    }

    
    /**
     * Board constructor. Creates the Territories, continents and display.
     */
    public Board(){
        this.invisible = true;
        this.nodelay = true;

        this.createTerritories();
        this.createContinents();
        this.createAdjacencies();

    }

    /**
     * Territories Getter
     * @return Territories - all territories in the game
     */
    public Territory[] getTerritories(){return this.territories;}

    /**
     * Continents Getter
     * @return Continents - All continents in the game
     */
    public Continent[] getContinents(){return this.continents;}



    /**
     * Checks if a player has conquered every continent, winning the game.
     * @return True if a player has won.
     */
    public boolean checkVictory(Player player) {
        for (Territory t: this.territories){
            if (t.getOwner() != player && t.getOwner().getColor() != "Grey"){
                return false;
            }
        }

        return true;
    }



    /**
     * Returns the territory object specified by the input Territory ID (1-42).
     * @param ID Territory ID
     * @return Territory object
     */
    public Territory getTerritoryByID(int ID){
        return this.territories[ID - 1];
    }



    /**
     * Returns the territory object specified by the input index in the territory array (0-41).
     * @param index Array Index
     * @return Territory Object
     */
    public Territory getTerritoryByIndex(int index){
        return this.territories[index];
    }


    /**
     * Moves troops from one territory to another.
     * Reduces occupancy of the src
     * Adds the territory to the new src owner and sets its occupancy
     * Checks for victory
     * @param src - Source territory
     * @param dst - Destination Territory
     * @param numTroops - Number of troops to move.
     * @return - True if this movement ends the game.
     */
    public boolean moveUnitsAfterVictory(Territory src, Territory dst, int numTroops){
        src.takeDamage(numTroops);
        src.getOwner().gainTerritory(numTroops, dst);

        return checkVictory(src.getOwner());

    }


}