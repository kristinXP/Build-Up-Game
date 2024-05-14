//Name : Kristin Miyamoto
//Project: 02: Java Android
//Class: CMPS 366-01 - OPL
//Date: 4/29/23

package org.problets.buildupfinal;

import java.io.Serializable;

public class Tile implements Serializable{

    private static final long serialVersionUID = 1L;
    String color;
    int leftPip, rightPip;

    /**
     * default constructor the Tile class
     */
    public Tile(){
        color = "B";
        leftPip = 0;
        rightPip = 0;

    }

    /**
     * constructor for the Tile class
     * @param c - string representing the color of the tile
     * @param L - integer representing the amount of pips on the left side
     * @param R - integer representing the amount of pips on the right side
     */
    public Tile(String c, int L, int R){
        color = c;
        leftPip = L;
        rightPip = R;


    }

    /* *********************************************************************
    Function Name: setTile
    Purpose: sets Tile class initial variables
    Parameters:
             Tile tile, a tile class object
    Return Value: none
     Assistance Received: none
    ********************************************************************* */

    /**
     * sets Tile class initial variables
     * @param tile - a tile class object
     * @throws Exception - if tile could not be created it throws and exception (Invalid color)
     */
    public void setTile(Tile tile) throws Exception{
        setColor(tile.getColor());
        setLeftPip(tile.getLeftPip());
        setRightPip(tile.getRightPip());

    }



    public final String getColor() {
        return color;
    }

    /**
     * sets color of tile to "W" or "B" if not it throws error
     * @param c - string representing the color of the tile
     * @throws Exception - invalid color if color is not "B" or "W"
     */
    public void setColor(String c) throws Exception{
        c = c.toUpperCase();
        if(c != "B" && c != "W"){
            Exception colorException = new Exception("Invalid color");
            throw colorException;
        }
        else{
            color = c;
        }

    }

    /**
     * getter to get the total sum of the pips of the tile
     * @return sum of the left and right side of the tile
     */
    public final int getTotalPips(){
        return leftPip + rightPip;
    }

    /**
     * to return if tile has the same amount on the right and left side
     * @return boolean value if tile has the same pips on both sides
     */
    public final boolean getSamePips(){
        boolean samePips = false;
        if(leftPip == rightPip){
            samePips = true;
        }
        return samePips;
    }

    /**
     * get the name of the tile name
     * @return string of the tile name the color, left pip, and right pip combined
     */
    public final String getTileName(){
        return color + leftPip + rightPip;
    }

    /**
     * used to set the left pip value
     * @param leftPip - integer representing the left pip value
     */
    public void setLeftPip(int leftPip) {
        this.leftPip = leftPip;
    }
    /**
     * used to set the right pip value
     * @param rightPip - integer representing the right pip value
     */
    public void setRightPip(int rightPip){
        this.rightPip = rightPip;
    }

    /**
     * getter to get the left pip vale
     * @return integer represents the left pip value
     */
    public int getLeftPip(){
        return leftPip;
    }

    /**
     * getter to get the right pip vale
     * @return integer represents the right pip value
     */
    public int getRightPip(){
        return rightPip;
    }
    /**
     * To converts tile class object to string
     * @return string name of the tile class object
     */
    public String tileToString(){
        StringBuffer temp = new StringBuffer();
        temp.append(color);
        temp.append(String.valueOf(leftPip));
        temp.append(String.valueOf(rightPip));
        String name = temp.toString();
        return name;
    }

    /**
     * To convert tile string to a tile class object (used mainly for serialization)
     * @param tile - a string that holds the name of the tile from
     *                 the serialization file.
     * @return the newly created tile class object
     */
    public Tile stringToTile(String tile){
        String temp_leftPip, temp_rightPip,temp_color;
        temp_color = tile.substring(0,1);
        temp_leftPip = tile.substring(1,2);
        temp_rightPip = tile.substring(2,3);
        int leftPip =  Integer.parseInt(temp_leftPip);
        int rightPip = Integer.parseInt(temp_rightPip);
        Tile temp = new Tile(temp_color,leftPip,rightPip);
        return temp;


    }



}