/** File: MessageComparator.java 
 * @author Joseph Workoff
 * Major: CS/SW MS
 * Creation Date: 12/08/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: Custom comparator class for Message Priority Queue.
*/


package com.client;

import java.util.Comparator;


public class MessageComparator implements Comparator<String> { 

    /**
     * Calculates a numeric priority for a message's code.
     * @param str - Coded message from server
     * @return int 0:10
     */
    private int calcPriority(String str){
        
        //A message of higher priority must be resolve before messages of lower priority to maintain turn order.

        switch (str.split(":")[0]) {
            case "VICTORY":
                return 10;                
            case "DISCONNECTED":
                return 9;                
            case "TURNIN":
                return 8;                
            case "REINFORCE":
                return 7;                              
            case "BATTLE":
                return 6;                
            case "DEFENSE":
                return 5;                
            case "ROLLS":
                return 4;                
            case "TRANSFER":
                return 3;                
            case "PASS":
                return 2;                
            case "NONE":
                return 1;                
            default:
                return 0;
                
        }

    }


    /**
     * Overridden compare function for comparator
     * @param str1 String to compare
     * @param str2 String to compare
     */
    public int compare(String str1, String str2){ 
        int prio1 = calcPriority(str1);
        int prio2 = calcPriority(str2);

        return prio2 - prio1;
            
    } 
} 