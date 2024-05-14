//Name : Kristin Miyamoto
//Project: 02: Java Android
//Class: CMPS 366-01 - OPL
//Date: 4/29/23

package org.problets.buildupfinal;

import java.util.Vector;
import java.io.Serializable;
import java.util.Collections;

public class Player implements Serializable{
    private Boneyard boneyard;
    private Vector<Tile> hand;

    private int score;

    private boolean pass;

    private String serial_boneyard = "";
    private String serial_hand = "";

    /**
     * default constructor for the player class
     */
    public Player() {
        boneyard = new Boneyard();
        hand = new Vector<Tile>();
        score = 0;
        pass = false;
    }

    /**
     * constructor for the player class
     * @param color - string that represents the color of the tiles (black for human, white for computer)
     */
    public Player(String color){
        boneyard = new Boneyard(color);
        hand = new Vector<Tile>();
        score = 0;
        pass = false;
    }

    /**
     * getter for the boneyard serialization
     * @return string of the boneyard vector
     */
    public String getSerial_boneyard() {
        return serial_boneyard;
    }

    /**
     * getter for the hand serialization string
     * @return string of the hand vector
     */
    public String getSerial_hand() {
        return serial_hand;
    }

    /**
     * getter for the player's hand score
     * @return integer that represents the players hand score
     */
    public final int getScore(){
        return score;
    }

    /**
     * getter for whether the player passed turn or not
     * @return boolean that represent whether the player passes turn or not
     */
    public final boolean getPass(){
        return pass;
    }

    /**
     * setter to set player's hand score
     * @param score - integer that represents the player's hand score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * setter to st player's hand vector
     * @param hand - a vector of tile class objects that represent the tiles in a player's hand
     */
    public void setHand(Vector<Tile> hand) {
        this.hand = hand;
    }

    /**
     * getter for player's hand vector
     * @return vector of tile class objects that represents a player's hand
     */
    public Vector<Tile> getHand(){
        return hand;
    }

    /**
     * getter for boneyard vector
     * @return vector of tile class objects that represent a player's boneyard
     */
    public Vector<Tile> getBoneyard(){
        return boneyard.getBoneyard();
    }

    /**
     * getter to get the player's boneyard size
     * @return integer that represents the vector of tile class objects, boneyard, size
     */
    public final int getBoneyardSize(){
        return boneyard.getBoneyardSize();
    }

    /**
     * setter to set boneyard vector from the Boneyard class
     * @param boneyard - a vector of tile class objects that represents a player's boneyard
     */
    public void setBoneyard(Vector<Tile> boneyard){
        this.boneyard.setBoneyard(boneyard);
    }

    /**
     * setter to set boneyard vector without calling the boneyard class
     * @param boneyard - a vector of tile class objects that represents a player's boneyard
     */
    public void setPlayerBoneyard(Boneyard boneyard){
        this.boneyard = boneyard;
    }

    /**
     * getter to get boneyard vector
     * @return a vector of tile class objects that represents a player's boneyard
     */
    public Boneyard getPlayerBoneyard(){
        return boneyard;
    }

    /**
     * getter to get player's hand size
     * @return integer that represent the size of the hand vector
     */
    public final int getHandSize(){
        return hand.size();
    }

    /**
     * setter to set pass boolean
     * @param pass - boolean that represents whether a player passes turn or not
     */
    public void setPass(boolean pass) {
        this.pass = pass;
    }


    /**
     * to check if the player's move is valid or not if it is valid the function
     *             returns true if the move is invalid the function returns false
     * @param playerTile - a tile class object that represents the player's tile
     * @param boardTile - a tile class object that represents the board's tile
     * @return
     */
    public boolean valid_move(Tile playerTile, Tile boardTile){
        int playerPipsScore = playerTile.getTotalPips();
        int boardPipsScore = boardTile.getTotalPips();

        //checks to see if player tile is greater than tile on board
        if (playerPipsScore > boardPipsScore) {
            return true;
        }
        //checks to see if player tile is equal to board tile and that board tile is a non double
        else if (playerPipsScore == boardPipsScore && !playerTile.getSamePips() && !playerTile.getSamePips()){
            return true;
        }
        //checks to see if player tile is a double and board tile is a non doubler
        else if (playerTile.getSamePips() && !boardTile.getSamePips()) {
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * to remove Tile class object from hand vector<tile>
     * @param playerTile - tile class object that represents the player's tile that
     *           is to be removed. If the tile class object can not be removed from hand
     *           an exception is thrown
     * @throws Exception - let's user know that the tile could not be removed
     */
    public void removeFromHand(Tile playerTile) throws Exception {
        if(!hand.remove(playerTile)){
            throw new Exception("Tile couldn't be removed");
        }
    }

    /**
     * to check if the hand vector<Tile> is empty or not
     * @return boolean that represents if hand vector is empty of not
     */
    public boolean isHandEmpty(){
        if(!hand.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * clears hand contents of vector
     */
    public void clearHand(){
        hand.clear();
    }

    /**
     * clears boneyard contents of vector
     */
    public void clearBoneyard(){
        boneyard.newBoneyard();
    }

    /**
     * to calculate the total number of pips of the tiles in the hand vector.
     *           the score is then added to the score variable
     */
    public void hand_score(){
        setScore(0);
        for(int i = 0; i < hand.size();i++){
            score += hand.get(i).getTotalPips();
        }
    }

    /**
     * to sort hand by total number of pips and reoragnize the order of hand
     *          in ascending order of total number of pips on each tile
     */
    public void sort_hand(){
        for(int i = 0; i < hand.size(); i++){
            for(int j = 0; j < hand.size(); j++){
                if (hand.get(i).getTotalPips() < hand.get(j).getTotalPips()){
                    Tile temp = hand.get(i);
                    hand.set(i, hand.get(j));
                    hand.set(j,temp);

                }
            }
        }
    }

    /**
     * to add 6 or 4 tiles from boneyard class object depending on how many tiles are
     *         left
     */
    public void draw_hand(){
        try {
            if (boneyard.getBoneyardSize() >= 6) {
                for (int i = 0; i < 6; i++) {
                    hand.add(boneyard.removeTile());
                }
            } else if (boneyard.getBoneyardSize() >= 4) {
                for (int i = 0; i < 4; i++) {
                    hand.add(boneyard.removeTile());
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    /**
     * shuffles boneyard vector
     */
    public void shuffleBoneyard(){
        boneyard.shuffle();
    };

    /**
     * To place tile class object on board
     * @param board - a board class object that represents the board tiles
     * @param board_position - an integer that represents the index of the element
     *                     in the board vector<Tile>
     * @param tile - a tile class object that represents the player tile to be placed
     *                     onto board class object
     * @return the updated board class object
     * @throws Exception - throws runtimeexception
     */
    public Board playTile(Board board, int board_position, Tile tile) throws Exception {
        Vector<Tile> temp_board = board.getBoard();
        temp_board.set(board_position, tile);

        try {

            removeFromHand(tile);
            board.setBoard(temp_board);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return board;

    }

    /**
     * To print the contents of the vector<Tile> hand
     */
    public void print_hand(){
        if(isHandEmpty() == true){
            System.out.println("Empty");
        }
        else {
            for (int i = 0; i < hand.size(); i++) {
                System.out.print(hand.get(i).getTileName() + " ");
            }
            System.out.println("");
        }

    }


    /**
     * To prepare hand vector for serialization by converting the Tile class objects to
     *          string and adding it to a new variable
     */
    public void serialize_hand(){
        for(int i = 0; i < hand.size(); i++){
            serial_hand += hand.get(i).tileToString() + " ";
        }
    }

    /**
     * To prepare boneyard class object for serialization by converting the tile
     *         elements on the class object to strings
     */
    public void serialize_boneyard(){
        Vector<Tile> temp = new Vector<>();
        temp = getBoneyard();
        for(int i = 0; i < boneyard.getBoneyardSize(); i++){
            serial_boneyard += temp.get(i).tileToString() + " ";
        }
    }

    /**
     * To get the hand vector element at position index and convert it to string
     * @param index - integer that represent the hand vector position
     * @return string that represents the name of the tile
     */
    public String getHandTileAsString(int index) {
        if(index <12){
            return hand.get(index).tileToString();
        }
        else{
            return "Invalid";
        }

    }


}