//Name : Kristin Miyamoto
//Project: 02: Java Android
//Class: CMPS 366-01 - OPL
//Date: 4/29/23

package org.problets.buildupfinal;

import static android.content.Context.MODE_PRIVATE;

import android.os.Environment;

import java.io.*;
import java.util.Vector;
import java.util.Scanner;

public class Round implements Serializable{
    private static Human human;
    private static Computer computer;
    private static Computer helper;
    private Board board;

    private String turn;

    private int computer_total_hand_score;

    private int human_total_hand_score;
    private Tile human_first;
    private Tile computer_first;




    private int computer_won;
    private int human_won;

    /**
     * Round class constructor to initialize the variables in the round class
     */
    public Round(){
        human = new Human();
        computer = new Computer();
        helper = new Computer();
        board = new Board();
        turn = "";
        computer_total_hand_score = 0;
        human_total_hand_score = 0;
        computer_won = 0;
        human_won = 0;
        computer_first = new Tile();
        human_first = new Tile();
    }

    /**
     * getter for the first tile human drawn for it's hand tiles
     * @return tile class object
     */
    public Tile getHuman_first() {
        return human_first;
    }
    /**
     * getter for the first tile computer drawn for it's hand tiles
     * @return tile class object
     */
    public Tile getComputer_first() {
        return computer_first;
    }

    /**
     * getter for the total computed end of hand score for computer
     * @return integer representing computer's end of hand score
     */
    public int getComputer_total_hand_score() {
        return computer_total_hand_score;
    }
    /**
     * getter for the total computed end of hand score for human
     * @return integer representing human's end of hand score
     */
    public int getHuman_total_hand_score() {
        return human_total_hand_score;
    }

    /**
     * prints the tiles of human's hand
     */

    public void printHumanhand(){
        System.out.println("Human hand: ");
        human.print_hand();
    }
    /**
     * prints the tiles of computer's hand
     */
    public void printCPUhand(){
        System.out.println("Computer hand: ");
        computer.print_hand();}

    /**
     * gets a specific tile at a certain position from human's hand
     * @param index - integer that represents the position in human's hand vector
     * @return tile class object that represents the tile from human's hand vector at the position, index
     */
    public Tile getHuman_hand(int index) {
        Vector<Tile> temp = human.getHand();

        return temp.get(index);
    }

    /**
     * used to set human boneyard vector (used to serialize in human boneyard vector from text file)
     * @param temp_HB - vector of tile class objects to be set to human's boneyard vector
     */
    public void setHuman_Boneyard(Vector<Tile> temp_HB){
        human.setBoneyard(temp_HB);
    }

    /**
     * used to set computer boneyard vector (used to serialize in computer boneyard vector from text file)
     * @param temp_CPU - vector of tile class objects to be set to computer's boneyard vector
     */
    public void setComputer_Boneyard(Vector<Tile> temp_CPU){
        computer.setBoneyard(temp_CPU);
    }

    /**
     * setter to set human hand (used to serialize in human hand vector from text file)
     * @param humanHand - vector of tile class objects to be set to human's hand vector
     */
    public void setHuman_Hand(Vector<Tile> humanHand){
        human.setHand(humanHand);
    }

    /**
     * setter to set computer hand (used to serialize in computer hand vector from text file)
     * @param computerHand - vector of tile class objects to be set to computer's hand vector
     */
    public void setComputer_Hand(Vector<Tile> computerHand){
        computer.setHand(computerHand);
    }

    /**
     * getter to get human's boneyard size
     * @return integer that represents human's boneyard(vector of tile class objects) size
     */
    public int getHuman_boneyard_size(){
        return human.getBoneyardSize();
    }

    /**
     * getter to get computer's boneyard size
     * @return integer that represents computer's boneyard(vector of tile class objects) size
     */
    public int getComputer_boneyard_size(){
        return computer.getBoneyardSize();
    }

    /**
     * getter to get human's hand size
     * @return integer that represent human's hand size (vector of tile class objects)
     */
    public int getHuman_Hand_Size(){
        return human.getHandSize();
    }

    /**
     * to get a specific board tile given the position at which the board tile is in the vector
     * @param index - integer representing the board vector's subscript
     * @return tile class object which represents the specific board tile at position index
     */
    public Tile getBoard_tile(int index){
        return board.getBoard_tile(index);
    }

    /**
     * getter for turn (either draw, human, or computer)
     * @return string which represents the turn of the player
     */
    public String getTurn() {
        return turn;
    }

    /**
     * setter to set the turn of the play order or turn
     * @param turn - string representing which player's turn it is
     */
    public void setTurn(String turn) {
        this.turn = turn;
    }

    /**
     * getter for board that gets the board class
     * @return
     */
    public Board getBoard(){
        return board;
    }

    /**
     * setter to set board class object
     * @param board - a board class object
     */
    public void setBoard(Board board){
        this.board = board;
    }

    /**
     * setter to set board tiles
     * @param temp_board - a vector of tile class objects that represent the tiles on the board
     */
    public void setBoardTiles(Vector<Tile> temp_board){
        board.setBoard(temp_board);
    }

    /**
     * getter to get computer's hand vector size
     * @return integer that represents the size of the computer hand (how many tiles are left in the hand)
     */
    public int getComputer_hand_size(){
        return computer.getHandSize();
    }

    /**
     * To allow human to place valid tiles onto the board vector and then remove
     *          valid play tile from hand vector
     * @param board_position - integer that represent the board vector subscript that the human wishes to play hand tile on
     * @param hand_position - integer that represent the hand vector subscript (the tile human wants to play)
     * @return boolean that represents where or not human passed turn
     */
    public boolean HumanTurn(int board_position, int hand_position) {
        if(!human.valid_move(human.getHand().get(hand_position), board.getBoard_tile(board_position))){
            return false;
        }
        else{
            try{
                setBoard(human.playTile(board,board_position,human.getHand().get(hand_position)));

            } catch (Exception invalidMove) {
                System.err.println(invalidMove.getMessage());
                return false;
            }
            return true;
        }
    }

    /**
     * sets whether or not human passes turn
     * @param bool - represent whether human passed turn or not
     */
    public void setHumanPass(boolean bool){
        human.setPass(bool);
    }

    /**
     * getter to get whether human passed or not
     * @return boolean representing if human passed or not
     */
    public boolean getHumanPass(){
        return human.getPass();
    }

    /**
     * getter to get whether computer passed or not
     * @return boolean that represents whether or not computer passed
     */
    public boolean getComputerPass(){
        return computer.getPass();
    }

    /**
     * setter to or not human passes turn
     * @param bool - represent whether human passed turn or not
     */
    public void setComputerPass(boolean bool){
        computer.setPass(bool);
    }

    /**
     * used to sort computer's hand (used in serializing computer hand)
     */
    public void sortComputerHand(){
        computer.sort_hand();
    }
    /**
     * used to sort human's hand (used in serializing human hand)
     */
    public void sortHumanHand(){
        human.sort_hand();
    }

    /**
     * getter to get the board tile as a string to used to update the board image buttons
     * @param index - integer that represents the position in the board vector
     * @return string that represents the name of the tile at index of the board vector
     */
    public String getBoardTileAsString(int index) {
        return board.getBoardTileAsString(index);
    }

    /**
     * used to get a tile from human hand as a string to be used to update human's hand button's images
     * @param index - integer that represents the position in the hand (human) vector
     * @return string that represents the name of the tile at index of the human hand vector
     */
    public String getTileFromHumanHand(int index){
        return human.getHandTileAsString(index);
    }

    /**
     * used to get a tile from computer hand as a string to be used to update computer's hand button's images
     * @param index - integer that represents the position in the hand (computer) vector
     * @return string that represents the name of the tile at index of the computer hand vector
     */
    public String getTileFromComputerHand(int index){
        return computer.getHandTileAsString(index);
    }

    /**
     * gets the message and reasoning from the computer class that explains what the computer did and why
     * @return string that represents the move and why the move was done or suggested
     */
    public String getComputer_message(){
        return computer.getComputer_message() + ": " + computer.getReasoning();
    }

    /* *********************************************************************
Function Name: whoseGoingFirst
Purpose: To decide what player is going first and make players' hands
Parameters:none
Return Value: none
Algorithm:	1) checks to see if boneyards are empty or not, if boneyards are empty it will create new boneyards
            2) checks to see if hands are empty and then makes players' hand by drawing 6 or 4 tiles
                from the players' boneyards and placing the tile class objects into the appropriate
                hand vector
			3) compares first human's tile pip sum with computer's first tile pip sum
			4) sets the player with the larger first tile pip sum to variable 'turn', which is the player who will go first
			5) if score are the same, 'turn' is set to draw
Assistance Received: none
********************************************************************* */
    public void whoseGoingFirst(){
        if(computer.getBoneyard().isEmpty() && human.getBoneyard().isEmpty()){
            human = new Human();
            computer = new Computer();
            computer.shuffleBoneyard();
            human.shuffleBoneyard();
            board.clearBoard();
            computer.setBoneyard(board.dealBoard(computer.getBoneyard()));
            human.setBoneyard(board.dealBoard(human.getBoneyard()));

        }
        if(human.isHandEmpty()){
            human.draw_hand();
        }
        if(computer.isHandEmpty()){
            computer.draw_hand();
        }

        Tile humanFirst = human.getHand().get(0);
        Tile computerFirst = computer.getHand().get(0);
        if(humanFirst.getTotalPips() > computerFirst.getTotalPips()){
            //human goes first
            turn = "Human";
            computer_first = computer.getHand().get(0);
            human_first = human.getHand().get(0);
            computer.sort_hand();
            human.sort_hand();
            System.out.println("Turn Order: Human");
            board.printBoard();

        }
        else if(humanFirst.getTotalPips() < computerFirst.getTotalPips()){
            //computer goes first
            turn = "Computer";
            computer_first = computer.getHand().get(0);
            human_first = human.getHand().get(0);
            computer.sort_hand();
            human.sort_hand();
            System.out.println("Turn Order: Computer");
            board.printBoard();
        }
        else{
            //draw
            turn = "draw";
            System.out.println("Turn Order: DRAW");
            board.printBoard();
            computer.clearBoneyard();
            computer.clearHand();
            human.clearBoneyard();
            human.clearHand();

        }

    }

    /**
     * clears the boneyard of human and computer which are both vectors of tile class objects
     */
    void clearBoneyard(){
        human.clearBoneyard();
        computer.clearBoneyard();
    }

    /**
     * To get the message from computerAi to be printed in the gui to
     *               that prints the computer's recommended move and why
     * @return string that describes the recommend move and why it was recommended
     */
    public String helpMessage(){

        Vector<Tile> temp = new Vector<>();
        temp = human.getHand();
        helper.setHand(temp);

        boolean tempp = helper.computerAi(board);


        if(!tempp){
            return "CPU recommends placing " + helper.getComputerTile().tileToString() + " on " + board.getBoard_tile(helper.getBoardPosition()).tileToString() + ": (" + helper.getReasoning() + ")";
        }
        else{
            return "CPU recommends passing turn: No valid moves exist";
        }


    }
    /* *********************************************************************
Function Name: computer_turn
Purpose: To allow computer to place valid tiles onto the board vector and then remove
         valid play tile from hand vector
Parameters: none
Return Value: none
Algorithm:	1) check to see if computer hand (vector) is empty and if it is
                sets pass boolean to true
			2) if computer does not pass turn, it goes to the function computerAi
			    and tries to find the best valid tile from computer's hand and the best position
			    on the board. if there are no valid tile moves computerAi
			    will return true and effectively set the pass boolean to true. if there is a
			    valid move the function, computer_turn, will return false and the pass boolean will be set to false
			3) checks again to see if computer's pass boolean changed, and if it is false
			    the function setBoard and playTile are called. These functions play computer's valid
			    calculated move that was computed in the computerAi function.
			3) checks if hand is empty and set pass boolean to appropriate value
			4) changes the turn to "human"
Assistance Received: none
********************************************************************* */
    public void computer_turn() throws Exception {
        if(computer.isHandEmpty() == true){
            computer.setPass(true);
        }

        if(computer.getPass() == false){
            computer.setPass(computer.computerAi(board));


            if(computer.getPass() == false) {
                setBoard(computer.playTile(board, computer.getBoardPosition(), computer.getComputerTile()));
            }

        }

        if(computer.isHandEmpty() == true){
            computer.setPass(true);
        }

        turn = "Human";

    }

    /**
     * To draw new hands (6 or 4 tiles from the boneyard)
     */

    public void drawNewHands(){
        human.clearHand();
        computer.clearHand();
        human.draw_hand();
        computer.draw_hand();
        human.sort_hand();
        computer.sort_hand();

        System.out.println("Computer Hand: ");
        computer.print_hand();
        System.out.println("Human Hand: ");
        human.print_hand();
    }

    /**
     * To clear the boneyard and hand vectors of the players and the board vector
     *             to prepare for a new round. As well as, to create and shuffle new tiles and then
     *             add them to the players' respective boneyards
     */
    public void newRound(){
        computer.clearBoneyard();
        computer.clearHand();
        human.clearBoneyard();
        human.clearHand();

        human = new Human();
        computer = new Computer();
        computer.shuffleBoneyard();
        human.shuffleBoneyard();

        board.clearBoard();
        computer.setBoneyard(board.dealBoard(computer.getBoneyard()));
        human.setBoneyard(board.dealBoard(human.getBoneyard()));

    }

    /**
     * To compute the end of hand scores of each player
     */

    public void compute_endOfHand_score(){
        board.total_board_scores();
        computer.hand_score();
        human.hand_score();
        computer_total_hand_score = board.getComputer_score() - computer.getScore();
        human_total_hand_score = board.getHuman_score() - human.getScore();
        System.out.println("Human Score: " + Integer.toString( human_total_hand_score));
        System.out.println("Computer Score: " + Integer.toString(computer_total_hand_score));

    }

    /**
     * getter for the string of the serialization of the first 6 board tiles
     * @return string of the first 6 board tiles
     */
    public String getSerial_out_boardL(){
         return board.getSerial_board_L();
    }
    /**
     * getter for the string of the serialization of the last 6 board tiles
     * @return string of the last 6 board tiles
     */
    public String getSerial_out_boardR(){
        return board.getSerial_board_R();
    }

    /**
     * getter for the string of the serialization of computer's boneyard tiles
     * @return string of computer's boneyard tiles
     */
    public String getSerial_Computer_boneyard(){
        return computer.getSerial_boneyard();
    }
    /**
     * getter for the string of the serialization of computer's hand tiles
     * @return string of computer's hand tiles
     */
    public String getSerial_Computer_hand(){;
        return  computer.getSerial_hand();
    }
    /**
     * getter for the string of the serialization of human's boneyard tiles
     * @return string of human's boneyard tiles
     */
    public String getSerial_Human_boneyard(){
        return human.getSerial_boneyard();
    }
    /**
     * getter for the string of the serialization of human's hand tiles
     * @return string of human's hand tiles
     */
    public String getSerial_Human_hand(){
        return  human.getSerial_hand();
    }

    /**
     * To prepare the appropriate fields to be serialized out by calling the
     *             respective functions
     */

    public void Save(){
        board.serial_out_board();
        computer.serialize_boneyard();
        computer.serialize_hand();
        human.serialize_boneyard();
        human.serialize_hand();
    }


}