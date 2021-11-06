/** File: Util.java 
 * @author Joseph Workoff
 * Creation Date: 09/24/2020
 * Due Date: 12/09/2020
 * Professor: Dr. Spiegel
 * Course: CSC421
 * Assignment Number: 3
 * Purpose: Common utility methods class.
*/

package com.client;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Utility functions class
 */
public class Util {

    static Logger logger = Logger.getLogger("");
    
    /**
     * Parses and error checks an input string into an integer.
     * @param str String input
     * @return converted integer, or -999 if invalid
     */
    public static int parseInt(String str){
        int num = 0;
        try {
            num = Integer.parseInt(str);
            return num;
        }
        catch (NumberFormatException e){
            return -999;
        }
    }


    /**
     * Parses and error checks an input char into an integer.
     * @param c char input
     * @return converted integer, or -999 if invalid
     */
    public static int parseInt(char c){
        return Util.parseInt(String.valueOf(c)); 
    }


    public static void debug(String msg){
        logger.log(Level.SEVERE, msg);
    }
}