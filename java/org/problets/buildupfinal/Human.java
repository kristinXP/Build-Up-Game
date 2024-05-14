//Name : Kristin Miyamoto
//Project: 02: Java Android
//Class: CMPS 366-01 - OPL
//Date: 4/29/23

package org.problets.buildupfinal;

import java.io.Serializable;
import java.util.Vector;
public class Human extends Player implements Serializable{

    /**
     * constructor for the Human class
     */
    public Human() {
        super();
        try {
            Player human_player = new Player("B");
            setPlayerBoneyard(human_player.getPlayerBoneyard());
        }
        catch(Exception setException) {
            System.err.println(setException.getMessage());
        }
    }


}