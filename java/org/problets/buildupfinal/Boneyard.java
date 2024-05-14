//Name : Kristin Miyamoto
//Project: 02: Java Android
//Class: CMPS 366-01 - OPL
//Date: 4/29/23

package org.problets.buildupfinal;

import java.io.Serializable;
import java.util.Vector;
import java.util.Collections;

public class Boneyard implements Serializable{
    private Vector<Tile> boneyard;

    /**
     * Boneyard class default constructor
     */
    Boneyard(){
        boneyard = new Vector<Tile>(28);
    }
    /**
     * constructor for the Boneyard class
     * @param color - String color, represents the color of the tile either black or white ("B" and "W")
     */
    public Boneyard(String color){
        boneyard = new Vector<Tile>(28);
        int pip1 = 0;
        int pip2 = 0;
        color = color.toUpperCase();

        for (int i = 0; i < 7; i++) {
            for (int j = pip1; j < 7; j++) {
                String tile = color + String.valueOf(pip1) + String.valueOf(pip2);

                Tile temp = new Tile(color, pip1, pip2);
                boneyard.add(temp);
                pip2++;
            }

            pip1++;
            pip2 = pip1;

        }
    }

    /**
     * getter for the boneyard vector's size
     * @return integer, represents the size of the boneyard vector
     */
    public final int getBoneyardSize(){
        return boneyard.size();
    }

    /**
     * getter for the boneyard vector
     * @return vector<Tile>, represents the boneyard vector
     */
    public Vector<Tile> getBoneyard(){
        return boneyard;
    }

    /**
     * setter for the boneyard vector
     * @param boneyard - vector of tile objects representing the boneyard
     */
    public void setBoneyard(Vector<Tile> boneyard){
        this.boneyard = boneyard;
    }


    /* *********************************************************************
 Function Name: removeTile
 Purpose: To remove tile from boneyard vector
 Parameters:
               none
 Return Value: none
 Assistance Received: none
 ********************************************************************* */

    /**
     * To remove tile from boneyard vector
     * @return returns the tile object that was removed from the boneyard vector
     */
    public Tile removeTile(){
        Tile temp_return = new Tile();
        temp_return = boneyard.get(0);
        boneyard.remove(temp_return);
        return temp_return;
    }


    /**
     * Used to shuffle the boneyard vector of Tile class objects
     */
    public void shuffle(){
        Collections.shuffle(boneyard);
    }

    /**
     * Used to clear the boneyard vector of Tile class objects
     */
    public void newBoneyard(){
        boneyard.clear();
    }
}