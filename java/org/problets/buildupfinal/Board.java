//Name : Kristin Miyamoto
//Project: 02: Java Android
//Class: CMPS 366-01 - OPL
//Date: 4/29/23

package org.problets.buildupfinal;

import java.util.Vector;
import java.io.Serializable;

public class Board implements Serializable{
    private static final long serialVersionUID = 1L;
    private transient Vector<Tile> board;

    private String serial_board_L = "";
    private String serial_board_R = "";

    private transient int computer_score;
    private transient int human_score;

    /**
     * constructor for board class
     */
    public Board(){
        final int board_size = 12;
        board = new Vector<Tile>();
    }

    /**
     * getter for computer's board score
     * @return integer represents computer's board score
     */
    public int getComputer_score() {
        return computer_score;
    }

    /**
     * getter for the first 6 board tiles as strings
     * @return returns string of 6 tiles in the board
     */
    public String getSerial_board_R() {
        return serial_board_R;
    }

    /**
     * getter for the last 6 board tiles as strings
     * @return returns string of 6 tiles in the board
     */
    public String getSerial_board_L() {
        return serial_board_L;
    }

    /**
     * getter for human's board score
     * @return integer, represents the total board score of human's stacks
     */
    public int getHuman_score() {
        return human_score;
    }

    /**
     *
     * @param index - integer that represents the index of the board vector
     * @return Tile class object that represents the tile at board position, index
     */
    public Tile getBoard_tile(int index){
        return board.get(index);
    }

    /**
     * sets computer's board score
     * @param computer_score- integer, represents the total board score of computer's stacks
     */
    public void setComputer_score(int computer_score) {
        this.computer_score = computer_score;
    }

    /**
     * sets human's board score
     * @param human_score - integer, represents the total board score of human's stacks
     */
    public void setHuman_score(int human_score) {
        this.human_score = human_score;
    }

    /**
     * getter for the vector board
     * @return Vector<Tile>, copy of board vector
     */
    public Vector<Tile> getBoard(){
        Vector<Tile> copyBoard = new Vector<Tile>(board);
        return copyBoard;
    }

    public void setBoard(Vector<Tile> board){
        this.board = board;
    }

    /**
     *To add tiles from the vector of tiles, boneyard, to the board vector
     *              of tiles
     * @param boneyard -  Vector<Tile>, a vector of tile class objects that holds the boneyard
     * @return - Vector<Tile> that represents the updated Vector<Tile> parameter that
     *                    was passed in initially
     */
    public Vector<Tile> dealBoard(Vector<Tile> boneyard){
        for (int i = 0; i < 6; i++){
            board.add(boneyard.get(0));
            boneyard.remove(0);
        }
        return boneyard;
    }

    /**
     * clears the board vector<Tile>
     */
    public void clearBoard(){
        board.clear();
    }


    /**
     * To add the board score or the sum of the tiles to the appropriate
     *             player based on color or the tile
     */
    public void total_board_scores(){
        setComputer_score(0);
        setHuman_score(0);
        for (int i =0; i<board.size();i++){
            if(board.get(i).getColor().equals("W")){
                computer_score += board.get(i).getTotalPips();
            }
            else if(board.get(i).getColor().equals("B")){
                human_score += board.get(i).getTotalPips();
            }
        }
    }



    /**
     * to print out the contents of the tile vector, board
     */
    public void printBoard(){
        for(int i = 0; i < board.size(); i++){
            System.out.print(board.get(i).getTileName() + " ");

        }
        System.out.println(" ");
    }


    /**
     * To prepare the board vector<Tile> for serialization by changing each of the tile elements in the vector to a string and storing them in a new vector of strings
     */
    public void serial_out_board(){
        for(int i = 0; i<6; i++){
            serial_board_L += board.get(i).tileToString() + " ";

        }
        for(int i = 6; i<12; i++){
            serial_board_R += board.get(i).tileToString() + " ";

        }

    }


    /**
     * To return the board vector elements as a string rather than a tile class object
     *             (used to update the images of the buttons)
     * @param index - integer, represents the position of the tile in the board vector
     * @return string of the board tile class object
     */
    public String getBoardTileAsString(int index) {
        if(index <12){
            return board.get(index).tileToString();
        }
        else{
            return "Invalid";
        }

    }

}