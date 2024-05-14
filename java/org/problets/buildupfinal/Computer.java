//Name : Kristin Miyamoto
//Project: 02: Java Android
//Class: CMPS 366-01 - OPL
//Date: 4/29/23

package org.problets.buildupfinal;

import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;
import java.util.Collections;
public class Computer extends Player implements Serializable{

    private int board_position;
    private Tile computer_tile;

    private String computer_message;
    private String reasoning;

    /**
     * constructor for the computer class
     */
    public Computer(){
        super();
        try{
            Player computer_player = new Player("W");
            setPlayerBoneyard(computer_player.getPlayerBoneyard());
        }
        catch(Exception boneyardException){
            System.err.println(boneyardException.getMessage());
        }
    }

    /**
     *getter to get the reason why a move was done or selected
     * @return reasoning - string of why the move selected using computerAi
     *                      function was picked
     */
    public String getReasoning() {
        return reasoning;
    }

    /**
     * getter to get the board position
     * @return integer which represent the position of the board vector that was found from
     *          the computerAi function
     */
    public final int getBoardPosition(){
        return board_position;
    }

    /**
     *getter to get the computer_tile
     * @return A tile class object that represents the computer's tile
     */
    public Tile getComputerTile(){
        return computer_tile;
    }

    /**
     * getter to get the string from the computerAi function
     * @return string that represents the computer message (what computer played or recommended move)
     */

    public String getComputer_message() {
        return computer_message;
    }



    /* *********************************************************************
    Function Name: best_move
    Purpose: To calculate the best or a good valid move for the player to play
    Parameters:
                Tile playTile, an object of the tile class that represents the tile of the player to be tried to be placed on the board
                Board board, an object of the board class that is used to get the board vector in the class
    Return Value: The position or subscript of the board vector which represents the tile stacks, if
                    the position is -1 it represents that the computer skips turn
    Algorithm:	1) find the largest tile pip sum on the opponent's board stacks position and tile sum value
                2) returns the the board index of the best placement (if found and a valid move). if
                    move is not found moves on to find finds the smallest tile pip sum on player's board stacks
                3) returns the board index of the best placement on the player's stacks
                4) if none are found then it return -1 signifying that computer should pass turn
    Assistance Received: none
    ********************************************************************* */

    /**
     * To calculate the best or a good valid move for the player to play
     * @param playTile - an object of the tile class that represents the tile of the player to be tried to be placed on the board
     * @param board - an object of the board class that is used to get the board vector in the class
     * @return The position or subscript of the board vector which represents the tile stacks, if
     *                     the position is -1 it represents that the computer skips turn
     */
    public int best_move(Tile playTile, Board board){
        //holds the board tile scores of the temporary modified board
        Vector<Integer> board_score = new Vector<>();

        //used to hold the current board and will be modified to find the best move position
        //none of the changes want to effect the actual board vector so the changes are
        //only done to the temp
        Vector<Tile> temp_board = board.getBoard();

        //holds board's max position
        int max_index = 0;

        //holds board's min position
        int min_index = 0;

        //IF PLAYER IS "W" OR COMPUTER

        if(Objects.equals(playTile.getColor(), "W")) {
            for (int i = 0; i < 12; i++) {
                //loops through the board vector and when the tile is computer's changes the board score
                //value to -1 so that when it tries to find the largest sum of pips it does not look
                //at computer's tiles
                if(Objects.equals(temp_board.get(i).getColor(), "W")){
                    board_score.add(-1);
                }
                else{
                    board_score.add(temp_board.get(i).getTotalPips());
                }
            }
            for (int j = 0; j < 12; j++){
                //holds the maximum value of the vector board_score
                int max_tile = Collections.max(board_score);

                //holds the vector index of the variable max_tile
                max_index = board_score.indexOf(max_tile);

                if(board_score.get(max_index) != -1 && valid_move(playTile, temp_board.get(max_index))){
                    reasoning = "prioritize covering opponents tiles";
                    //if a valid move on the opponent's side (human) was found return board index
                    return max_index;
                }
                else{
                    //if the position of the max board score was not a valid move it sets the board score vale to -1
                    //so when it loops through the loop again it doesn't get stuck in an infinite loop
                    board_score.set(max_index,-1);

                }

            }
            //clears the board scores because no valid moves on the opponent's side was found
            //so now going to try to find best move one own stacks
            board_score.clear();

            for(int k = 0; k < 12; k++) {
                //loops through the board vector and when the tile is human's changes the board score
                //value to 100 so that when it tries to find the smallest sum of pips it does not look
                //at human's tiles
                if (Objects.equals(temp_board.get(k).getColor(), "B")) {
                    board_score.add(100);
                } else {
                    board_score.add(temp_board.get(k).getTotalPips());
                }
            }
            for(int l = 0; l < 12; l++){
                //holds the minimum value of the vector board_score
                int min_tile = Collections.min(board_score);

                //holds the vector index of the variable min_tile
                min_index = board_score.indexOf(min_tile);

                if(board_score.get(l) != 100 && valid_move(playTile,temp_board.get(min_index))){
                    reasoning = "Unable to play on opponent's tiles so increasing own score is best";

                    //if a valid move on the opponent's side (computer) was found return board index (min_index)
                    return min_index;
                }
                else{
                    //if the position of the min board score was not a valid move it sets the board score vale to 100
                    //so when it loops through the loop again it doesn't get stuck in an infinite loop
                    board_score.set(min_index,100);

                }
            }

        }

        //IF PLAYER IS "B" OR HUMAN
        if(Objects.equals(playTile.getColor(), "B")) {
            for (int i = 0; i < 12; i++) {
                if(Objects.equals(temp_board.get(i).getColor(), "B")){
                    //loops through the board vector and when the tile is human's changes the board score
                    //value to -1 so that when it tries to find the largest sum of pips it does not look
                    //at human's tiles
                    board_score.add(-1);
                }
                else{
                    board_score.add(temp_board.get(i).getTotalPips());
                }
            }
            for (int j = 0; j < 12; j++){
                //holds the maximum value of the vector board_score
                int max_tile = Collections.max(board_score);

                //holds the vector index of the variable max_tile
                max_index = board_score.indexOf(max_tile);

                if(board_score.get(max_index) != -1 && valid_move(playTile, temp_board.get(max_index))){
                    reasoning = "prioritize covering opponents tiles";

                    //if a valid move on the opponent's side (computer) was found return board index
                    return max_index;
                }
                else{
                    //if the position of the max board score was not a valid move it sets the board score vale to -1
                    //so when it loops through the loop again it doesn't get stuck in an infinite loop
                    board_score.set(max_index,-1);

                }

            }

            board_score.clear();

            for(int k = 0; k < 12; k++) {
                if (Objects.equals(temp_board.get(k).getColor(), "W")) {
                    board_score.add(100);
                } else {
                    board_score.add(temp_board.get(k).getTotalPips());
                }
            }
            for(int l = 0; l < 12; l++){
                //holds the minimum value of the vector board_score
                int min_tile = Collections.min(board_score);

                //holds the vector index of the variable min_tile
                min_index = board_score.indexOf(min_tile);

                if(board_score.get(l) != 100 && valid_move(playTile,temp_board.get(min_index))){
                    reasoning = "Unable to play on opponent's tiles so increasing own score is best";
                    //if a valid move on the opponent's side (computer) was found return board index
                    return min_index;
                }
                else{
                    //if the position of the max board score was not a valid move it sets the board score vale to 100
                    //so when it loops through the loop again it doesn't get stuck in an infinite loop
                    board_score.set(min_index,100);
                }
            }

            board_score.clear();


       }
        //if no valid move could be found returns -1 to signify that the turn should be passed
            return -1;


    }

    /* *********************************************************************
Function Name: computerAi
Purpose: To calculate the best or a good valid move for the player to play by going through
         the player's hand vector and trying to play each tile in the hand vector on the board until
         the best valid move is found. if no valid move could be found the function returns -1 to
Parameters:
            Board board, an object of the board class that is used to get the board vector in the class
Return Value: The position or subscript of the board vector which represents the tile stacks, if
                the position is -1 it represents that the computer skips turn
Algorithm:	1) checks to make sure the player's hand is not empty, if empty it will automatically return
            2) returns the the board index of the best placement (if found and a valid move). if
                move is not found moves on to find finds the smallest tile pip sum on player's board stacks
            3) returns the board index of the best placement on the player's stacks
            4) if none are found then it return -1 signifying that computer should pass turn
Assistance Received: none
********************************************************************* */

    public boolean computerAi(Board board){
        //used to store hand in a temp variable to make it easier to read
        Vector<Tile> hand = getHand();

        //used to hold the position of the board position or -1 if a valid move could not be found
        int check;


        if(isHandEmpty()){
            computer_message = "Computer passes turn";

            //if hand is empty returns true to skip turn
            return true;
        }

        for(int i = 0; i < getHandSize(); i++){

            //loops through hand vector and checks to see if the tile is not a double and will only try to play on the opponent's tiles
            if(!hand.get(i).getSamePips()){
                check = best_move_opp_side(hand.get(i), board);
                if(check != -1){
                    board_position = check;
                    computer_tile = hand.get(i);
                    System.out.println("Computer plays " + computer_tile.getTileName() + " on " + board.getBoard().get(check).getTileName());
                    computer_message = "Computer plays " + computer_tile.getTileName() + " on " + board.getBoard().get(check).getTileName();

                    //if a valid move is found the value of variable check is assigned to board_position and the tile in the hand vector is assigned to computer tile
                    return false;
                }
            }
        }

        for(int i = 0; i < getHandSize(); i++){
            //loops through hand vector and checks to see if the tile is not a double and will only try to play on the opponent's tiles and own tiles
            if(!hand.get(i).getSamePips()){
                check = best_move(hand.get(i), board);
                if(check != -1){
                    board_position = check;
                    computer_tile = hand.get(i);
                    System.out.println("Computer plays " + computer_tile.getTileName() + " on " + board.getBoard().get(check).getTileName());
                    computer_message = "Computer plays " + computer_tile.getTileName() + " on " + board.getBoard().get(check).getTileName();
                    return false;
                }
            }
        }
        for(int j = 0; j < getHandSize(); j++){
            //loops through hand vector and will only try to play on the opponent's tiles and own tiles
            //the reloop is necessary since in the previous loops doubles were skipped over or saved until the end
            // since the probability of being able to play them is higher than lower non double tiles
            check = best_move(hand.get(j),board);
            if(check != -1){
                board_position = check;
                computer_tile = hand.get(j);
                System.out.println("Computer plays " + computer_tile.getTileName() + " on " + board.getBoard().get(check).getTileName());
                computer_message = "Computer plays " + computer_tile.getTileName() + " on " + board.getBoard().get(check).getTileName();
                reasoning = "double tiles can be place on tiles greater than it (if other is non-double) or less than it";
                return false;
            }
        }
        computer_message = "Computer passes turn";
        reasoning = "No valid moves exist";

        //if not valid move is found returns true signify that the turn should be passed
        return true;
    }

    /* *********************************************************************
    Function Name: best_move_opp_side
    Purpose: To calculate the best or a good valid move for the player to play on opponent's tiles
    Parameters:
                Tile playTile, an object of the tile class that represents the tile of the player to be tried to be placed on the board
                Board board, an object of the board class that is used to get the board vector in the class
    Return Value: The position or subscript of the board vector which represents the tile stacks, if
                    the position is -1 it represents that the computer skips turn
    Algorithm:	1) find the largest tile pip sum on the opponent's board stacks position and tile sum value
                2) returns the the board index of the best placement (if found and a valid move). if
                    move is not found moves on to find finds the smallest tile pip sum on player's board stacks
                3) returns the board index of the best placement on the player's stacks
                4) if none are found then it return -1 signifying that computer should pass turn
    Assistance Received: none
    ********************************************************************* */
    public int best_move_opp_side(Tile playTile, Board board){
        Vector<Integer> board_score = new Vector<>();
        Vector<Tile> temp_board = board.getBoard();

        int max_index = 0;


        //IF PLAYER IS "W" OR COMPUTER
        if(Objects.equals(playTile.getColor(), "W")) {
            //loops through the board vector and when the tile is computer's changes the board score
            //value to -1 so that when it tries to find the largest sum of pips it does not look
            //at computer's tiles
            for (int i = 0; i < 12; i++) {
                if (Objects.equals(temp_board.get(i).getColor(), "W")) {
                    board_score.add(-1);
                } else {
                    board_score.add(temp_board.get(i).getTotalPips());
                }
            }
            for (int j = 0; j < 12; j++) {

                //holds the maximum value of the vector board_score
                int max_tile = Collections.max(board_score);

                //holds the vector index of the variable max_tile
                max_index = board_score.indexOf(max_tile);

                if (board_score.get(max_index) != -1 && valid_move(playTile, temp_board.get(max_index))) {
                    reasoning = "prioritize covering opponents tiles";

                    //if a valid move on the opponent's side (human) was found return board index
                    return max_index;
                } else {
                    //if the position of the max board score was not a valid move it sets the board score vale to -1
                    //so when it loops through the loop again it doesn't get stuck in an infinite loop
                    board_score.set(max_index, -1);

                }

            }
        }


            board_score.clear();



        //IF PLAYER IS "B" OR HUMAN
        if(Objects.equals(playTile.getColor(), "B")) {
            //loops through the board vector and when the tile is human's changes the board score
            //value to -1 so that when it tries to find the largest sum of pips it does not look
            //at human's tiles
            for (int i = 0; i < 12; i++) {
                if (Objects.equals(temp_board.get(i).getColor(), "B")) {
                    board_score.add(-1);
                } else {
                    board_score.add(temp_board.get(i).getTotalPips());
                }
            }
            for (int j = 0; j < 12; j++) {
                //holds the maximum value of the vector board_score
                int max_tile = Collections.max(board_score);

                //holds the vector index of the variable max_tile
                max_index = board_score.indexOf(max_tile);

                if (board_score.get(max_index) != -1 && valid_move(playTile, temp_board.get(max_index))) {
                    reasoning = "prioritize covering opponents tiles";
                    //if a valid move on the opponent's side (human) was found return board index

                    return max_index;
                } else {
                    //if the position of the max board score was not a valid move it sets the board score vale to -1
                    //so when it loops through the loop again it doesn't get stuck in an infinite loop
                    board_score.set(max_index, -1);

                }

            }
        }

        board_score.clear();

        //if no valid move could be found returns -1 to signify that the turn should be passed
        return -1;


    }


}
