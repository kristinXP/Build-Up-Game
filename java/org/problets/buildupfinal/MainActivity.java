//Name : Kristin Miyamoto
//Project: 02: Java Android
//Class: CMPS 366-01 - OPL
//Date: 4/29/23

package org.problets.buildupfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    public final static String SAVE_FILE = "saveFile.txt";
    private final static float unselected = (float) 1.0;
    private final static float selected = (float) 0.5;

    private static Round round;

    private static Tile playerTile;
    private static Tile boardTile;
    private static int board_position;

    private static int human_hand_position;
    private static Tile zero;
    private static Tile one;
    private static Tile two;
    private static Tile three;
    private static Tile four;
    private static Tile five;

    private String fileName;

    private boolean human_hand_click = false;
    private boolean board_click = false;

    private int hands_count;
    private int EoT_human_score;
    private int EoT_computer_score;
    private int human_wins;
    private int computer_wins;
    private String EoT_winner;
    private String EoR_winner;
    private int EoR_human_score;
    private int EoR_computer_score;

    private String EoH_winner;
    private int EoH_human_score;
    private int EoH_computer_score;
    boolean isSwitched;

    String end_of_hand_reason;
    String round_of_reason;

    Dialog dialog;

    private static String save_file_name;


    public static int zero_position, one_position, two_position, three_position, four_position,five_position,  hand_adjustment_check;

    /**
     * creates the build up game once the main menu closes
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializes variables
        dialog = new Dialog(MainActivity.this);

        round = new Round();
        zero = new Tile();
        one = new Tile();
        two = new Tile();
        three = new Tile();
        four = new Tile();
        five = new Tile();
        save_file_name = "";
        playerTile = new Tile();
        boardTile = new Tile();
        board_position = 0;
        human_hand_position = 0;
        zero_position = 0;
        one_position = 1;
        two_position = 2;
        three_position = 3;
        four_position = 4;
        five_position = 5;
        hand_adjustment_check = 0;
        hands_count = 0;

        EoH_human_score = 0;
        EoH_computer_score = 0;

        EoR_human_score = 0;
        EoR_computer_score = 0;
        EoT_human_score = 0;
        EoT_computer_score = 0;
        human_wins = 0;
        computer_wins = 0;

        isSwitched = false;

        end_of_hand_reason = "";
        round_of_reason = "";


        // Get the Bundle from the Intent to check to see if it is a new game or resuming a game
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Get the message from the Bundle and load appropriate resume file
            fileName = bundle.getString("resume");
            load();
        }
        else{
            //start a new game
            openWhosGoingFirstDialog();

        }

        //refreshes the gui screen and initializes the buttons
        refreshBoard();
        setHumanImagesWithButtons();
        refreshHumanHand();
        refreshComputerHand();
        initializeHumanHandButtons();

        //computes the scores of the players to be displayed in the interface
        round.compute_endOfHand_score();
        ((TextView) findViewById(R.id.human_score)).setText(Integer.toString(round.getHuman_total_hand_score()));
        ((TextView) findViewById(R.id.cpu_score)).setText(Integer.toString(round.getComputer_total_hand_score()));

        System.out.println("Turn " + round.getTurn());

        //if computer is going first it immediately let computer make it's move
        if(round.getTurn().equals("Computer")){
            computerTurn();
        }

        //initializes variables for the switch, and buttons in the interface
        Switch cpuSwitch = findViewById(R.id.switchCPU);
        Button play_tile = (Button) findViewById(R.id.play_tile);
        Button help = (Button)findViewById(R.id.help);

        play_tile.setOnClickListener(new View.OnClickListener() {
            /**
             * used to listen for when human is ready to play a tile on the board
             * @param view The view that was clicked.
             */
             @Override
            public void onClick(View view) {
                //checks to make sure human player clicked on one human hand button and a board tile button before attempting to go to human turn function
                if (human_hand_click && board_click) {
                    // Both buttons have been clicked once goes to Turns function or human's turn
                    try {
                        Turns(view);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            /**
             * if the help button is clicked it prints the computer's recommendation for huma's move
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                helpMessage();
            }
        });

        cpuSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * toggle to turn on and off the visibility of computer's hand tiles and score. by default computer's hand tiles and score are invisibility
             * @param buttonView The compound button view whose state has changed.
             * @param isChecked  The new checked state of buttonView.
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cpuSwitch.isChecked()){
                    setComputerHandVisible();
                    findViewById(R.id.cpu_score).setVisibility(View.VISIBLE);
                    findViewById(R.id.cpu_score_label).setVisibility(View.VISIBLE);
                    isSwitched = true;

                }else{
                    setComputerHandInvisible();
                    findViewById(R.id.cpu_score).setVisibility(View.INVISIBLE);
                    findViewById(R.id.cpu_score_label).setVisibility(View.INVISIBLE);
                    isSwitched = false;
                }
            }
        });




    }

    /**
     * displays the move recommendation into the interface
     */
    public void helpMessage(){
        ((TextView)findViewById(R.id.message)).setText(round.helpMessage());
    }

    /**
     * sets the amount of wins human has (used for serialization)
     * @param human_wins - integer that represents the number of round wins human has
     */
    public void setHuman_wins(int human_wins) {
        this.human_wins = human_wins;
    }

    /**
     *  sets the amount of wins computer has (used for serialization)
     * @param computer_wins - integer that represents the number of round wins computer has
     */
    public void setComputer_wins(int computer_wins) {
        this.computer_wins = computer_wins;
    }

    /**
     * set the end of tournament human score
     * @param eoT_human_score - integer representing the human's score at the end of the tournament
     */
    public void setEoT_human_score(int eoT_human_score) {
        EoT_human_score = eoT_human_score;
    }
    /**
     * set the end of tournament computer score
     * @param eoT_computer_score - integer representing the computer's score at the end of the tournament
     */
    public void setEoT_computer_score(int eoT_computer_score) {
        EoT_computer_score = eoT_computer_score;
    }

    /**
     * used to create and display a pop up that displays the end of tournament scores and winner
     */
    private void endOfTournament(){
        Dialog EoT = new Dialog(MainActivity.this);

        View EoTLayout = getLayoutInflater().inflate(R.layout.end_of_tournament,null);
        TextView cpu_EoT_score = EoTLayout.findViewById(R.id.computer_score);
        TextView human_EoT_score = EoTLayout.findViewById(R.id.human_score);
        TextView cpu_EoT_wins = EoTLayout.findViewById(R.id.computer_wins);
        TextView human_EoT_wins = EoTLayout.findViewById(R.id.human_wins);
        TextView winner = EoTLayout.findViewById(R.id.winner);
        EoT.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));

        cpu_EoT_score.setText(Integer.toString(EoT_computer_score));
        human_EoT_score.setText(Integer.toString(EoT_human_score));
        cpu_EoT_wins.setText(Integer.toString(computer_wins));
        human_EoT_wins.setText(Integer.toString(human_wins));

        if(computer_wins > human_wins){
            EoT_winner = "COMPUTER";
        }
        else if(computer_wins < human_wins){
            EoT_winner = "HUMAN";
        }
        else{
            EoT_winner = "DRAW";
        }
        winner.setText(EoT_winner.toUpperCase(Locale.ROOT));



        EoT.setContentView(EoTLayout);
        EoT.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);


        Button okay = EoT.findViewById(R.id.ok_EoT);

        okay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EoT.dismiss();
                MainActivity.this.finish();
                System.exit(0);


            }
        });
        EoT.show();
    }

    /**
     * used to decide the turn order and display who is going first
     */
    private void openWhosGoingFirstDialog(){
        while(round.getTurn().equals("draw") || round.getTurn().equals("")){
            round.whoseGoingFirst();
        }
        hands_count += 1;
        View whosfirstLayout = getLayoutInflater().inflate(R.layout.whos_going_first,null);
        ImageView firstCPU = whosfirstLayout.findViewById(R.id.first_cpu);
        ImageView firstHuman = whosfirstLayout.findViewById(R.id.first_human);
        TextView firstTurn = whosfirstLayout.findViewById(R.id.first);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));

        firstTurn.setText(round.getTurn().toUpperCase(Locale.ROOT));
        StartStateActivity.displayTileImage(firstCPU,round.getComputer_first().tileToString());
        StartStateActivity.displayTileImage(firstHuman,round.getHuman_first().tileToString());

        dialog.setContentView(whosfirstLayout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        Button ok = dialog.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();

            }
        });
        dialog.show();


    }

    /**
     * used to adjust the button positions for human's hand when a tile is played
     */

    public void adjustHandPositions(){
        switch(hand_adjustment_check){
            case 0:
                zero_position = 0;
            case 1:
                one_position -= 1;
            case 2:
                two_position -= 1;
            case 3:
                three_position -= 1;
            case 4:
                four_position -= 1;
            case 5:
                five_position -= 1;
        }
    }

    /**
     * In the activity_main.xml file when the first hand button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition human_hand_click is set to true to let the play function know that
     * a human hand tile was selected if tile could be played
     * @param view - view object
     */

    public void zeroSelected(View view) {
        findViewById(R.id.hand_B1).setSelected(true);
        findViewById(R.id.hand_B2).setSelected(false);
        findViewById(R.id.hand_B3).setSelected(false);
        findViewById(R.id.hand_B4).setSelected(false);
        findViewById(R.id.hand_B5).setSelected(false);
        findViewById(R.id.hand_B6).setSelected(false);
        findViewById(R.id.hand_B1).setAlpha(selected);
        findViewById(R.id.hand_B2).setAlpha(unselected);
        findViewById(R.id.hand_B3).setAlpha(unselected);
        findViewById(R.id.hand_B4).setAlpha(unselected);
        findViewById(R.id.hand_B5).setAlpha(unselected);
        findViewById(R.id.hand_B6).setAlpha(unselected);

        try {
            playerTile.setTile(zero);
            human_hand_position = zero_position;
            hand_adjustment_check = 0;
            human_hand_click = true;
        } catch (Exception setException) {
            System.err.println(setException.getMessage());
        }
    }

    /**
     * In the activity_main.xml file when the second hand button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition human_hand_click is set to true to let the play function know that
     * a human hand tile was selected
     * @param view - view object
     */
    public void oneSelected(View view) {
        findViewById(R.id.hand_B1).setSelected(false);
        findViewById(R.id.hand_B2).setSelected(true);
        findViewById(R.id.hand_B3).setSelected(false);
        findViewById(R.id.hand_B4).setSelected(false);
        findViewById(R.id.hand_B5).setSelected(false);
        findViewById(R.id.hand_B6).setSelected(false);
        findViewById(R.id.hand_B1).setAlpha(unselected);
        findViewById(R.id.hand_B2).setAlpha(selected);
        findViewById(R.id.hand_B3).setAlpha(unselected);
        findViewById(R.id.hand_B4).setAlpha(unselected);
        findViewById(R.id.hand_B5).setAlpha(unselected);
        findViewById(R.id.hand_B6).setAlpha(unselected);

        try {
            playerTile.setTile(one);
            human_hand_position = one_position;
            hand_adjustment_check = 1;
            human_hand_click = true;
        } catch (Exception setException) {
            System.err.println(setException.getMessage());
        }
    }
    /**
     * In the activity_main.xml file when the third hand button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition human_hand_click is set to true to let the play function know that
     * a human hand tile was selected
     * @param view - view object
     */
    public void twoSelected(View view) {
        findViewById(R.id.hand_B1).setSelected(false);
        findViewById(R.id.hand_B2).setSelected(false);
        findViewById(R.id.hand_B3).setSelected(true);
        findViewById(R.id.hand_B4).setSelected(false);
        findViewById(R.id.hand_B5).setSelected(false);
        findViewById(R.id.hand_B6).setSelected(false);
        findViewById(R.id.hand_B1).setAlpha(unselected);
        findViewById(R.id.hand_B2).setAlpha(unselected);
        findViewById(R.id.hand_B3).setAlpha(selected);
        findViewById(R.id.hand_B4).setAlpha(unselected);
        findViewById(R.id.hand_B5).setAlpha(unselected);
        findViewById(R.id.hand_B6).setAlpha(unselected);

        try {
            playerTile.setTile(two);
            human_hand_position = two_position;
            hand_adjustment_check = 2;
            human_hand_click = true;
        } catch (Exception setException) {
            System.err.println(setException.getMessage());
        }
    }
    /**
     * In the activity_main.xml file when the fourth hand button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition human_hand_click is set to true to let the play function know that
     * a human hand tile was selected
     * @param view - view object
     */
    public void threeSelected(View view) {
        findViewById(R.id.hand_B1).setSelected(false);
        findViewById(R.id.hand_B2).setSelected(false);
        findViewById(R.id.hand_B3).setSelected(false);
        findViewById(R.id.hand_B4).setSelected(true);
        findViewById(R.id.hand_B5).setSelected(false);
        findViewById(R.id.hand_B6).setSelected(false);
        findViewById(R.id.hand_B1).setAlpha(unselected);
        findViewById(R.id.hand_B2).setAlpha(unselected);
        findViewById(R.id.hand_B3).setAlpha(unselected);
        findViewById(R.id.hand_B4).setAlpha(selected);
        findViewById(R.id.hand_B5).setAlpha(unselected);
        findViewById(R.id.hand_B6).setAlpha(unselected);

        try {
            playerTile.setTile(three);
            human_hand_position = three_position;
            hand_adjustment_check = 3;
            human_hand_click = true;
        } catch (Exception setException) {
            System.err.println(setException.getMessage());
        }
    }
    /**
     * In the activity_main.xml file when the fifth hand button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition human_hand_click is set to true to let the play function know that
     * a human hand tile was selected
     * @param view - view object
     */
    public void fourSelected(View view) {
        findViewById(R.id.hand_B1).setSelected(false);
        findViewById(R.id.hand_B2).setSelected(false);
        findViewById(R.id.hand_B3).setSelected(false);
        findViewById(R.id.hand_B4).setSelected(false);
        findViewById(R.id.hand_B5).setSelected(true);
        findViewById(R.id.hand_B6).setSelected(false);
        findViewById(R.id.hand_B1).setAlpha(unselected);
        findViewById(R.id.hand_B2).setAlpha(unselected);
        findViewById(R.id.hand_B3).setAlpha(unselected);
        findViewById(R.id.hand_B4).setAlpha(unselected);
        findViewById(R.id.hand_B5).setAlpha(selected);
        findViewById(R.id.hand_B6).setAlpha(unselected);

        try {
            playerTile.setTile(four);
            human_hand_position = four_position;
            hand_adjustment_check =4;
            human_hand_click = true;
        } catch (Exception setException) {
            System.err.println(setException.getMessage());
        }
    }
    /**
     * In the activity_main.xml file when the sixth hand button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition human_hand_click is set to true to let the play function know that
     * a human hand tile was selected
     * @param view - view object
     */
    public void fiveSelected(View view) {
        findViewById(R.id.hand_B1).setSelected(false);
        findViewById(R.id.hand_B2).setSelected(false);
        findViewById(R.id.hand_B3).setSelected(false);
        findViewById(R.id.hand_B4).setSelected(false);
        findViewById(R.id.hand_B5).setSelected(false);
        findViewById(R.id.hand_B6).setSelected(true);
        findViewById(R.id.hand_B1).setAlpha(unselected);
        findViewById(R.id.hand_B2).setAlpha(unselected);
        findViewById(R.id.hand_B3).setAlpha(unselected);
        findViewById(R.id.hand_B4).setAlpha(unselected);
        findViewById(R.id.hand_B5).setAlpha(unselected);
        findViewById(R.id.hand_B6).setAlpha(selected);

        try {
            playerTile.setTile(five);
            human_hand_position = five_position;
            hand_adjustment_check = 5;
            human_hand_click = true;
        } catch (Exception setException) {
            System.err.println(setException.getMessage());
        }
    }

    //BOARD

    /**
     * In the activity_main.xml file when the first board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardZero(View view) {
        board_click = true;
        board_position = 0;
        boardTile = round.getBoard_tile(0);

        findViewById(R.id.stack_W1).setAlpha(selected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the second board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardOne(View view) {
        board_click = true;
        board_position = 1;
        boardTile = round.getBoard_tile(1);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(selected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the third board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardTwo(View view) {
        board_click = true;
        board_position = 2;
        boardTile = round.getBoard_tile(2);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(selected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the fourth board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardThree(View view) {
        board_click = true;
        board_position = 3;
        boardTile = round.getBoard_tile(3);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(selected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the fifth board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardFour(View view) {
        board_click = true;
        board_position = 4;
        boardTile = round.getBoard_tile(4);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(selected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the sixth board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardFive(View view) {
        board_click = true;
        board_position = 5;
        boardTile = round.getBoard_tile(5);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(selected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the seventh board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardSix(View view) {
        board_click = true;
        board_position = 6;
        boardTile = round.getBoard_tile(6);


        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(selected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the eighth board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardSeven(View view) {
        board_click = true;
        board_position = 7;
        boardTile = round.getBoard_tile(7);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(selected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the ninth board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardEight(View view) {
        board_click = true;
        board_position = 8;
        boardTile = round.getBoard_tile(8);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(selected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the tenth board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardNine(View view) {
        board_click = true;
        board_position = 9;
        boardTile = round.getBoard_tile(9);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(selected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the eleventh board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardTen(View view) {
        board_click = true;
        board_position = 10;
        boardTile = round.getBoard_tile(10);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(selected);
        findViewById(R.id.stack_B6).setAlpha(unselected);

    }
    /**
     * In the activity_main.xml file when the twelve board button is clicked this function goes into effect.
     * When this button is clicked the button's opacity is reduced using the setAlpha to let the user
     * know what button is selected. In addition board_click is set to true to let the play function know that
     * a board tile was selected
     * @param view - view object
     */
    public void BoardEleven(View view) {
        board_click = true;
        board_position = 11;
        boardTile = round.getBoard_tile(11);

        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(selected);

    }

    /**
     * In the activity_main.xml file when the pass button is clicked this function goes into effect.
     * It sets Human pass boolean to tru and sets the turn to "computer"
     * @param view - view object
     */

    public void Pass(View view){
        round.setHumanPass(true);
        round.setTurn("Computer");
        Turns(view);
    }

    /**
     * refreshes the board images by getting the board vector of tile class objects in from the round class and converting it the tile into a string and then
     * using the displayTileImage function in StartStateActivity to get set the image of the board tile
     */
    private void refreshBoard() {
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_W1), round.getBoardTileAsString(0));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_W2), round.getBoardTileAsString(1));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_W3), round.getBoardTileAsString(2));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_W4), round.getBoardTileAsString(3));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_W5), round.getBoardTileAsString(4));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_W6), round.getBoardTileAsString(5));

        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_B1),round.getBoardTileAsString(6));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_B2),round.getBoardTileAsString(7));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_B3),round.getBoardTileAsString(8));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_B4),round.getBoardTileAsString(9));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_B5),round.getBoardTileAsString(10));
        StartStateActivity.displayTileImage((Button) findViewById(R.id.stack_B6),round.getBoardTileAsString(11));
    }

    /**
     * refreshes the computer hand images by getting the computer hand vector of tile class objects in from the round class and converting it the tile into a string and then
     * using the displayTileImage function in StartStateActivity to get set the image of the hand tile
     */
    private void refreshComputerHand() {

        switch(round.getComputer_hand_size()) {
            case 6:
                StartStateActivity.displayTileImage((ImageView) findViewById(R.id.hand_W6),round.getTileFromComputerHand(5));
            case 5:
                StartStateActivity.displayTileImage((ImageView) findViewById(R.id.hand_W5),round.getTileFromComputerHand(4));
            case 4:
                StartStateActivity.displayTileImage((ImageView) findViewById(R.id.hand_W4),round.getTileFromComputerHand(3));
            case 3:
                StartStateActivity.displayTileImage((ImageView) findViewById(R.id.hand_W3),round.getTileFromComputerHand(2));
            case 2:
                StartStateActivity.displayTileImage((ImageView) findViewById(R.id.hand_W2),round.getTileFromComputerHand(1));
            case 1:
                StartStateActivity.displayTileImage((ImageView) findViewById(R.id.hand_W1),round.getTileFromComputerHand(0));
                break;
            default:
                break;
        }
    }

    /**
     * used to remove the played tile from the computer's hand by changing the visibility of the image
     */
    private void adjustComputerHand(){

        switch(round.getComputer_hand_size()) {
            case 0:
                findViewById(R.id.hand_W1).setVisibility(View.GONE);
            case 1:
                findViewById(R.id.hand_W2).setVisibility(View.GONE);
            case 2:
                findViewById(R.id.hand_W3).setVisibility(View.GONE);

            case 3:
                findViewById(R.id.hand_W4).setVisibility(View.GONE);
            case 4:
                findViewById(R.id.hand_W5).setVisibility(View.GONE);

            case 5:
                findViewById(R.id.hand_W6).setVisibility(View.GONE);
            default:
                break;
        }
    }

    /**
     * used toggle the visibility of the computer's hand images when the switch is turn on so that the user can see computer's hand tiles
     */
    private void setComputerHandVisible(){

        switch(round.getComputer_hand_size()) {
            case 6:
                findViewById(R.id.hand_W6).setVisibility(View.VISIBLE);
            case 5:
                findViewById(R.id.hand_W5).setVisibility(View.VISIBLE);
            case 4:
                findViewById(R.id.hand_W4).setVisibility(View.VISIBLE);
            case 3:
                findViewById(R.id.hand_W3).setVisibility(View.VISIBLE);
            case 2:
                findViewById(R.id.hand_W2).setVisibility(View.VISIBLE);
            case 1:
                findViewById(R.id.hand_W1).setVisibility(View.VISIBLE);
            default:
                break;
        }
    }
    /**
     * used to toggle the computer's tile visibility so when the switch is turned off the user cannot see computer's tiles
     */
    private void setComputerHandInvisible(){

        switch(round.getComputer_hand_size()) {
            case 6:
                findViewById(R.id.hand_W6).setVisibility(View.INVISIBLE);
            case 5:
                findViewById(R.id.hand_W5).setVisibility(View.INVISIBLE);
            case 4:
                findViewById(R.id.hand_W4).setVisibility(View.INVISIBLE);
            case 3:
                findViewById(R.id.hand_W3).setVisibility(View.INVISIBLE);
            case 2:
                findViewById(R.id.hand_W2).setVisibility(View.INVISIBLE);
            case 1:
                findViewById(R.id.hand_W1).setVisibility(View.INVISIBLE);
            default:
                break;
        }
    }
    /**
     * refreshes the human hand images by getting the human hand vector of tile class objects in from the round class and converting it the tile into a string and then
     * using the displayTileImage function in StartStateActivity to get set the image of the hand tile
     */
    private void refreshHumanHand() {
        switch(round.getHuman_Hand_Size()) {
            case 6:
                StartStateActivity.displayTileImage(
                        (Button) findViewById(R.id.hand_B6),
                        round.getTileFromHumanHand(5));
            case 5:
                StartStateActivity.displayTileImage(
                        (Button) findViewById(R.id.hand_B5),
                        round.getTileFromHumanHand(4));
            case 4:
                StartStateActivity.displayTileImage(
                        (Button) findViewById(R.id.hand_B4),
                        round.getTileFromHumanHand(3));
            case 3:
                StartStateActivity.displayTileImage(
                        (Button) findViewById(R.id.hand_B3),
                        round.getTileFromHumanHand(2));
            case 2:
                StartStateActivity.displayTileImage(
                        (Button) findViewById(R.id.hand_B2),
                        round.getTileFromHumanHand(1));
            case 1:
                StartStateActivity.displayTileImage(
                        (Button) findViewById(R.id.hand_B1),
                        round.getTileFromHumanHand(0));
                break;
            default:
                break;
        }
    }

    /**
     * set's the tiles in humans current hand with the appropriate tile in mainActivity which is used to connected them with the appropriate button and images
     */
    private void setHumanImagesWithButtons() {
        try {
            switch(round.getHuman_Hand_Size()) {
                case 6:
                    five.setTile(round.getHuman_hand(5));
                case 5:
                    four.setTile(round.getHuman_hand(4));
                case 4:
                    three.setTile(round.getHuman_hand(3));
                case 3:
                    two.setTile(round.getHuman_hand(2));
                case 2:
                    one.setTile(round.getHuman_hand(1));
                case 1:
                    zero.setTile(round.getHuman_hand(0));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * used to remove hand tile from view when human makes a move
     */
    private void removeHandTile() {
        if (findViewById(R.id.hand_B1).isSelected()) {
            findViewById(R.id.hand_B1).setVisibility(View.GONE);

        } else if (findViewById(R.id.hand_B2).isSelected()) {
            findViewById(R.id.hand_B2).setVisibility(View.GONE);

        } else if (findViewById(R.id.hand_B3).isSelected()) {
            findViewById(R.id.hand_B3).setVisibility(View.GONE);

        } else if (findViewById(R.id.hand_B4).isSelected()) {
            findViewById(R.id.hand_B4).setVisibility(View.GONE);

        } else if (findViewById(R.id.hand_B5).isSelected()) {
            findViewById(R.id.hand_B5).setVisibility(View.GONE);

        } else if (findViewById(R.id.hand_B6).isSelected()) {
            findViewById(R.id.hand_B6).setVisibility(View.GONE);

        }
    }

    /**
     * used to initialize human's buttons by setting the visibility and resting the opacity to the unselected value
     */
    private void initializeHumanHandButtons() {
        switch(round.getHuman_Hand_Size()) {
            case 6:
                findViewById(R.id.hand_B6).setVisibility(View.VISIBLE);
                findViewById(R.id.hand_B6).setAlpha(unselected);

            case 5:
                findViewById(R.id.hand_B5).setVisibility(View.VISIBLE);
                findViewById(R.id.hand_B5).setAlpha(unselected);

            case 4:
                findViewById(R.id.hand_B4).setVisibility(View.VISIBLE);
                findViewById(R.id.hand_B4).setAlpha(unselected);

            case 3:
                findViewById(R.id.hand_B3).setVisibility(View.VISIBLE);
                findViewById(R.id.hand_B3).setAlpha(unselected);

            case 2:
                findViewById(R.id.hand_B2).setVisibility(View.VISIBLE);
                findViewById(R.id.hand_B2).setAlpha(unselected);

            case 1:
                findViewById(R.id.hand_B1).setVisibility(View.VISIBLE);
                findViewById(R.id.hand_B1).setAlpha(unselected);

                break;
            default:
                break;
        }

    }

    /**
     * used to deleted all the board tiles by setting the opacity back to the original unselected value
     */
    private void deselectBoardTiles() {
        findViewById(R.id.stack_W1).setAlpha(unselected);
        findViewById(R.id.stack_W2).setAlpha(unselected);
        findViewById(R.id.stack_W3).setAlpha(unselected);
        findViewById(R.id.stack_W4).setAlpha(unselected);
        findViewById(R.id.stack_W5).setAlpha(unselected);
        findViewById(R.id.stack_W6).setAlpha(unselected);
        findViewById(R.id.stack_B1).setAlpha(unselected);
        findViewById(R.id.stack_B2).setAlpha(unselected);
        findViewById(R.id.stack_B3).setAlpha(unselected);
        findViewById(R.id.stack_B4).setAlpha(unselected);
        findViewById(R.id.stack_B5).setAlpha(unselected);
        findViewById(R.id.stack_B6).setAlpha(unselected);
    }
    /**
     * used to deleted all the human hand tiles by setting the opacity back to the original unselected value and set the setSelected button built in function to false
     * the setSelected function is used to identify which button is to be removed from the view when human successfully played a valid move
     */
    private void deselectHandTiles() {
        findViewById(R.id.hand_W1).setSelected(false);
        findViewById(R.id.hand_W2).setSelected(false);
        findViewById(R.id.hand_W3).setSelected(false);
        findViewById(R.id.hand_W4).setSelected(false);
        findViewById(R.id.hand_W5).setSelected(false);
        findViewById(R.id.hand_W6).setSelected(false);
        findViewById(R.id.hand_W1).setAlpha(unselected);
        findViewById(R.id.hand_W2).setAlpha(unselected);
        findViewById(R.id.hand_W3).setAlpha(unselected);
        findViewById(R.id.hand_W4).setAlpha(unselected);
        findViewById(R.id.hand_W5).setAlpha(unselected);
        findViewById(R.id.hand_W6).setAlpha(unselected);
    }

    /**
     * used to play computer's turn and updates the view accordingly
     */

    public void computerTurn(){
        try {
            round.printCPUhand();
            round.setComputerPass(false);
            round.computer_turn();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ((TextView) findViewById(R.id.message)).setText(round.getComputer_message());
        round.compute_endOfHand_score();
        ((TextView) findViewById(R.id.human_score)).setText(Integer.toString(round.getHuman_total_hand_score()));
        ((TextView) findViewById(R.id.cpu_score)).setText(Integer.toString(round.getComputer_total_hand_score()));
        refreshBoard();
        round.getBoard().printBoard();
        adjustComputerHand();
        refreshComputerHand();
        round.setTurn("Human");


    }

    /**
     * used for human's and computer's turn
     * @param view - view object
     */
    public void Turns(View view) {
        //prints human hand
        round.printHumanhand();

        //checks to see if it's human's turn
        if(round.getTurn().equals("Human")) {
            //check to see if human's move is valid
            if (!round.HumanTurn(board_position, human_hand_position)) {
                //if human's move is not valid deselects hand tiles and board tiles and refreshes view and lets human try another move
                deselectHandTiles();
                deselectBoardTiles();
                return;
            } else {
                //if the move was valid the tile is removed from hand
                removeHandTile();

                //the hand position are readjusted so the buttons associated indexes do not go out of range
                adjustHandPositions();

                //resets the board opacity settings
                deselectBoardTiles();

                //changes the turn to computer
                round.setTurn("Computer");

                //computes the scores to update the new values displayed on the screen
                round.compute_endOfHand_score();
                ((TextView) findViewById(R.id.human_score)).setText(Integer.toString(round.getHuman_total_hand_score()));
                ((TextView) findViewById(R.id.cpu_score)).setText(Integer.toString(round.getComputer_total_hand_score()));
                refreshBoard();

                //checks to see if human hand vector is empty after the last move and if so set the the pass to true
                if (round.getHuman_Hand_Size() == 0) {
                    round.setHumanPass(true);
                }

            }

            //goes to computer turn
            round.getBoard().printBoard();
            round.setTurn("Computer");
            computerTurn();
        }
        else if(round.getTurn().equals("Computer")){
            //goes to computer turn this is here for when the function computerTurn cannot be called directly or it is unknown if computer is going first or not
            computerTurn();
        }

        //if both players pass it signals the end of the hand
        if (round.getComputerPass() == true && round.getHumanPass() == true) {
            newHand(view);

        }
        //resets human pass to false again so human can try to play a tile the next turn
        round.setHumanPass(false);

    }

    /**
     * used to make a new round by resetting everything except wins and end of tournament score of each player
     */
    public void newRound(){
        round.newRound();
        round.printCPUhand();
        round.printHumanhand();
        refreshBoard();
        setHumanImagesWithButtons();
        refreshHumanHand();
        refreshComputerHand();
        initializeHumanHandButtons();
        round.setComputerPass(false);
        round.setHumanPass(false);


    }

    /**
     * used to make preparation for a new hand
     * @param view - view object
     */
    public void newHand(View view){
        round.compute_endOfHand_score();
        EoH_human_score = round.getHuman_total_hand_score();
        EoH_computer_score= round.getComputer_total_hand_score();
        round.setComputerPass(false);
        round.setHumanPass(false);
        endOfHand(view);

    }

    /**
     * used to display the results of the winner at the end of hand
     * @param view - view object
     */
    private void endOfHand(View view){
        Dialog EoH = new Dialog(MainActivity.this);

        //dialog display settings
        View EoHLayout = getLayoutInflater().inflate(R.layout.end_of_hand,null);
        TextView cpu_EoH_score = EoHLayout.findViewById(R.id.computer_score_hand);
        TextView human_EoH_score = EoHLayout.findViewById(R.id.human_score_hand);
        TextView winner = EoHLayout.findViewById(R.id.hand_winner);
        TextView EoH_reason = EoHLayout.findViewById(R.id.end_hand_reason);
        EoH.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));

        cpu_EoH_score.setText(Integer.toString(EoH_computer_score));
        human_EoH_score.setText(Integer.toString(EoH_human_score));

        //figures out who is the winner of the hand
        if(EoH_computer_score > EoH_human_score){
            EoH_winner = "COMPUTER";
        }
        else if(EoH_computer_score < EoH_human_score){
            EoH_winner = "HUMAN";
        }
        else{
            EoH_winner = "DRAW";
        }
        winner.setText(EoH_winner.toUpperCase(Locale.ROOT));

        //figures out why the hand ended
        if(round.getComputer_hand_size() == 0 && round.getHuman_Hand_Size() == 0){
            end_of_hand_reason = "Hands are empty";
        }
        else{
            end_of_hand_reason = "No valid moves available";
        }

        EoH_reason.setText(end_of_hand_reason);

        EoH.setContentView(EoHLayout);
        EoH.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);


        Button okay = EoH.findViewById(R.id.ok_EoH);

        okay.setOnClickListener(new View.OnClickListener(){
            /**
             * when okay is clicked it start a new hand and resets the appropriate value to start a new hand if the boneyards are not empty
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v){
                EoH.dismiss();

                //if the boneyard sizes are 4 or more it creates another hand
                if(round.getHuman_boneyard_size() >=4 && round.getComputer_boneyard_size() >= 4 && hands_count <= 4){
                    //creates new hands for the players
                    round.drawNewHands();

                    //resetting and refreshing view and variables for new round
                    round.setComputerPass(false);
                    round.setHumanPass(false);
                    hands_count += 1;
                    zero_position = 0;
                    one_position = 1;
                    two_position = 2;
                    three_position = 3;
                    four_position = 4;
                    five_position = 5;
                    hand_adjustment_check = 0;
                    refreshHumanHand();
                    initializeHumanHandButtons();
                    refreshComputerHand();

                    if(isSwitched){
                        setComputerHandVisible();
                        findViewById(R.id.cpu_score).setVisibility(View.VISIBLE);
                        findViewById(R.id.cpu_score_label).setVisibility(View.VISIBLE);


                    }else{
                        setComputerHandInvisible();
                        findViewById(R.id.cpu_score).setVisibility(View.INVISIBLE);
                        findViewById(R.id.cpu_score_label).setVisibility(View.INVISIBLE);

                    }

                    round.compute_endOfHand_score();
                    ((TextView) findViewById(R.id.human_score)).setText(Integer.toString(round.getHuman_total_hand_score()));
                    ((TextView) findViewById(R.id.cpu_score)).setText(Integer.toString(round.getComputer_total_hand_score()));


                }
                else{
                    //if a new hand cannot be created it goes to end of round
                    hands_count = 0;
                    round.compute_endOfHand_score();
                    EoH_human_score = round.getHuman_total_hand_score();
                    EoH_computer_score= round.getComputer_total_hand_score();
                    EoR_human_score += round.getHuman_total_hand_score();
                    EoR_computer_score += round.getComputer_total_hand_score();
                    endOfRound(view);
                }

            }
        });
        EoH.show();
    }

    /**
     * used to display the result at the end of the round and asks if the player wants to create a new round
     * @param view - view object
     */
    public void endOfRound(View view){
        Dialog EoR = new Dialog(MainActivity.this);

        View EoRLayout = getLayoutInflater().inflate(R.layout.end_of_round,null);
        TextView cpu_EoR_score = EoRLayout.findViewById(R.id.computer_score_round);
        TextView human_EoR_score = EoRLayout.findViewById(R.id.human_score_round);
        TextView winner = EoRLayout.findViewById(R.id.round_winner);
        TextView round_reason = EoRLayout.findViewById(R.id.end_round_reason);
        EoR.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));

        cpu_EoR_score.setText(Integer.toString(EoR_computer_score));
        human_EoR_score.setText(Integer.toString(EoR_human_score));

        //calculates the winner of the round
        if(EoR_computer_score > EoR_human_score){
            EoR_winner = "COMPUTER";
            computer_wins += 1;
        }
        else if(EoR_computer_score < EoR_human_score){
            EoR_winner = "HUMAN";
            human_wins += 1;
        }
        else{
            EoR_winner = "DRAW";
        }
        winner.setText(EoR_winner.toUpperCase(Locale.ROOT));

        //figures out the reason why the round ended
        if(hands_count == 4){
            round_of_reason = "Four hands were played";
        }
        else{
            round_of_reason = "Boneyards are empty";
        }

        round_reason.setText(round_of_reason);

        EoR.setContentView(EoRLayout);
        EoR.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);


        Button okay = EoR.findViewById(R.id.ok_EoR);

        okay.setOnClickListener(new View.OnClickListener(){
            /**
             * when okay is click a new round is created and all of the appropriate values are reset back to the original value at the start of the game
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v){
                EoR.dismiss();
                EoT_human_score += EoR_human_score;
                EoT_computer_score += EoR_computer_score;

                round = new Round();
                zero = new Tile();
                one = new Tile();
                two = new Tile();
                three = new Tile();
                four = new Tile();
                five = new Tile();

                playerTile = new Tile();
                boardTile = new Tile();
                board_position = 0;
                human_hand_position = 0;
                zero_position = 0;
                one_position = 1;
                two_position = 2;
                three_position = 3;
                four_position = 4;
                five_position = 5;
                hand_adjustment_check = 0;
                hands_count = 0;

                EoH_human_score = 0;
                EoH_computer_score = 0;

                EoR_computer_score = 0;
                EoR_human_score = 0;
                newRound();
                round.drawNewHands();

                if(isSwitched){
                    setComputerHandVisible();
                    findViewById(R.id.cpu_score).setVisibility(View.VISIBLE);
                    findViewById(R.id.cpu_score_label).setVisibility(View.VISIBLE);


                }else{
                    setComputerHandInvisible();
                    findViewById(R.id.cpu_score).setVisibility(View.INVISIBLE);
                    findViewById(R.id.cpu_score_label).setVisibility(View.INVISIBLE);

                }

                refreshBoard();
                setHumanImagesWithButtons();
                refreshHumanHand();
                refreshComputerHand();
                initializeHumanHandButtons();
                if(round.getTurn().equals("Human")){
                    Turns(view);
                }
                else{
                    computerTurn();
                }

                //Toast.makeText(MainActivity.this,"Ok", Toast.LENGTH_SHORT).show();

            }
        });

        Button cancel = EoR.findViewById(R.id.no_EoR);

        cancel.setOnClickListener(new View.OnClickListener(){
            /**
             * when cancel button is clicked it computes human's and computer's end of tournament scores and then brings up the end of tournament window
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v){
                EoR.dismiss();
                EoT_human_score += EoR_human_score;
                EoT_computer_score += EoR_computer_score;
                endOfTournament();
            }
        });
        EoR.show();

    }

    /**
     * used to serialize out the game information when the save button is pressed and then closes the game safely
     * @param view - view object
     */
    private void saveFile(View view){
        Dialog savedFile = new Dialog(MainActivity.this);

        View enterSaveFile = getLayoutInflater().inflate(R.layout.savefile,null);
        EditText fileName = enterSaveFile.findViewById(R.id.savedfile);
        savedFile.setContentView(enterSaveFile);
        savedFile.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        Button okay = enterSaveFile.findViewById(R.id.ok_save);

        okay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                if(fileName.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Empty fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    save_file_name = fileName.getText().toString() + ".txt";

                    savedFile.dismiss();
                    savingFile();
                }

            }
        });
        savedFile.show();
    }

    /**
     * when the save button is clicked it prompts the saveFile function to start serializing out the game
     * because saveFile has the user input the name of the file and that it creates a dialog, it cannot be
     * directly called using android:onClick so this was made to be like a buffer.
     * @param view - view object
     */
    public void Save(View view)  {
        saveFile(view);
    }

    /**
     * used to serialize out the game information into a text file (.txt)
     */
    public void savingFile(){
        round.Save();
        try {

            FileOutputStream fos = openFileOutput(save_file_name, MODE_PRIVATE);
            fos.write("Computer:".getBytes());
            fos.write("\n   Stack: ".getBytes());
            fos.write(round.getSerial_out_boardL().getBytes());
            fos.write("\n   Boneyard: ".getBytes());
            fos.write(round.getSerial_Computer_boneyard().getBytes());
            fos.write("\n   Hand: ".getBytes());
            fos.write(round.getSerial_Computer_hand().getBytes());
            fos.write("\n   Score: ".getBytes());
            fos.write(Integer.toString(EoR_computer_score).getBytes());
            fos.write("\n   Rounds Won: ".getBytes());
            fos.write(Integer.toString(computer_wins).getBytes());
            fos.write("\n".getBytes());

            fos.write("\nHuman:".getBytes());
            fos.write("\n   Stack: ".getBytes());
            fos.write(round.getSerial_out_boardR().getBytes());
            fos.write("\n   Boneyard: ".getBytes());
            fos.write(round.getSerial_Human_boneyard().getBytes());
            fos.write("\n   Hand: ".getBytes());
            fos.write(round.getSerial_Human_hand().getBytes());
            fos.write("\n   Score: ".getBytes());
            fos.write(Integer.toString(EoR_human_score).getBytes());
            fos.write("\n   Rounds Won: ".getBytes());
            fos.write(Integer.toString(human_wins).getBytes());
            fos.write("\n".getBytes());
            fos.write("\nTurn: ".getBytes());
            fos.write(round.getTurn().getBytes());


            System.out.println("Saved to" + getFilesDir() + "/" + save_file_name);

            //closes the MainActivity and safely exits app
            MainActivity.this.finish();
            System.exit(0);



        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * used resume a game and serialize in a .txt file
     */

    public void load()  {
        FileInputStream fis = null;
        try {
            //used to have access to function available in the tile class mainly converting string to tile
            Tile t = new Tile();

            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);

            BufferedReader br = new BufferedReader(isr);

            //used to hold the contents of the txt file
            Vector<String> content = new Vector<>();

            //used as a temporary string to extract each line of the txt file into one string array
            String text;

            while((text = br.readLine()) !=null){
                content.add(text);
            }

            //Computer
            //created a temporary string to get the computer stacks
            String computer_stacks = content.get(1);

            //splits the string into separate string when there is a space and stores them in an array of strings
            String[] stack_temp = computer_stacks.split("\\s+");

            //creates a temporary vector to be used when the string of tiles are converted into tiles
            Vector<Tile> temp_stack = new Vector<>();

            //used to sort the labels from the actual tiles
            Vector<String> temp_string = new Vector<>();
            for (int i = 0; i < stack_temp.length; i++) {
                    temp_string.add(stack_temp[i]);
                }

            temp_string.remove(1);
            if(!temp_string.isEmpty()) {
                temp_string.remove(0);
                if (!temp_string.isEmpty()) {
                    for (int i = 2; i < stack_temp.length; i++) {
                        //if the string is not empty after removing the labels it stores the tiles in the temporary vector
                        temp_stack.add(t.stringToTile(stack_temp[i]));
                    }
                }
            }

            //computer boneyard
            // splits the string into separate tile and labels into an array of string
            String computer_boneyard = content.get(2);
            String[] boneyard_temp = computer_boneyard.split("\\s+");
            Vector<Tile> temp_computer_boneyard = new Vector<>();
            Vector<String> temp_string1 = new Vector<>();
            for (int i = 0; i < boneyard_temp.length; i++) {
                    temp_string1.add(boneyard_temp[i]);
            }

            //removes the labels from the array of string holding the computer's boneyard
            temp_string1.remove(1);
            if(!temp_string1.isEmpty()) {
                temp_string1.remove(0);
                if (!temp_string1.isEmpty()) {
                    for (int i = 2; i < boneyard_temp.length; i++) {
                        //if the string is not empty after removing the labels it stores the tiles in the temporary vector
                        temp_computer_boneyard.add(t.stringToTile(boneyard_temp[i]));
                    }
                    //sets the newly converted tile vector created from the text file to the computer's boneyard
                    round.setComputer_Boneyard(temp_computer_boneyard);
                }
            }

            //computer hand
            // splits the string into separate tile and labels into an array of string
            String computer_hand = content.get(3);
            String[] hand_temp = computer_hand.split("\\s+");
            Vector<Tile> temp_computer_hand = new Vector<>();
            Vector<String> temp_string2 = new Vector<>();
            for (int i = 0; i < hand_temp.length; i++) {
                temp_string2.add(hand_temp[i]);
            }

            //removes the labels from the array of string holding the computer's hand
            temp_string2.remove(1);
            if(!temp_string2.isEmpty()) {
                temp_string2.remove(0);
                if (!temp_string2.isEmpty()) {
                    for (int i = 2; i < hand_temp.length; i++) {
                        //if the string is not empty after removing the labels it stores the tiles in the temporary vector
                        temp_computer_hand.add(t.stringToTile(hand_temp[i]));
                    }
                    //sets the newly converted tile vector created from the text file to the computer's hand
                    round.setComputer_Hand(temp_computer_hand);

                }
            }

            //computer's score
            String computer_score = content.get(4);
            String[] score_temp = computer_score.split("\\s+");
            setEoT_computer_score(Integer.parseInt(score_temp[2]));

            //computer's wins
            String computer_won = content.get(5);
            String[] won_temp = computer_won.split("\\s+");
            setComputer_wins(Integer.parseInt(won_temp[3]));


            //serializes human

            //created a temporary string to get the human's stacks
            String human_stacks = content.get(8);

            //splits the string into separate string when there is a space and stores them in an array of strings
            stack_temp = human_stacks.split("\\s+");

            //creates a temporary vector to be used when the string of tiles are converted into tiles
            temp_string = new Vector<>();

            //used to sort the labels from the actual tiles
            for (int i = 0; i < stack_temp.length; i++) {
                temp_string.add(stack_temp[i]);
            }

            temp_string.remove(1);
            if(!temp_string.isEmpty()) {
                temp_string.remove(0);
                if (!temp_stack.isEmpty()) {
                    for (int i = 2; i < stack_temp.length; i++) {
                        //if the string is not empty after removing the labels it stores the tiles in the temporary vector
                        temp_stack.add(t.stringToTile(stack_temp[i]));
                    }
                    //sets the newly converted tile vector created from the text file to the board vector
                    round.setBoardTiles(temp_stack);
                }
            }

            //human boneyard
            // splits the string into separate tile and labels into an array of string
            String human_boneyard = content.get(9);
            String[] boneyard_temp1 = human_boneyard.split("\\s+");
            Vector<Tile> temp_human_boneyard = new Vector<>();
            Vector<String> temp_string3 = new Vector<>();
            for (int i = 0; i < boneyard_temp1.length; i++) {
                //if the string is not empty after removing the labels it stores the tiles in the temporary vector
                temp_string3.add(boneyard_temp1[i]);
            }

            //removes the labels from the array of string holding the human's boneyard
            temp_string3.remove(1);
            if(!temp_string3.isEmpty()) {
                temp_string3.remove(0);
                if (!temp_string3.isEmpty()) {
                    for (int i = 2; i < boneyard_temp1.length; i++) {
                        //if the string is not empty after removing the labels it stores the tiles in the temporary vector
                        temp_human_boneyard.add(t.stringToTile(boneyard_temp1[i]));
                    }
                    //sets the newly converted tile vector created from the text file to the human boneyard
                    round.setHuman_Boneyard(temp_human_boneyard);
                }
                else{
                    round.clearBoneyard();
                }

            }

            //human hand
            // splits the string into separate tile and labels into an array of string
            String human_hand = content.get(10);
            String[] hand_temp1 = human_hand.split("\\s+");
            Vector<Tile> temp_human_hand = new Vector<>();
            Vector<String> temp_string4 = new Vector<>();
            for (int i = 0; i < hand_temp1.length; i++) {
                temp_string4.add(hand_temp1[i]);
            }

            temp_string4.remove(1);
            if(!temp_string4.isEmpty()) {
                temp_string4.remove(0);
                if (!temp_string4.isEmpty()) {
                    for (int i = 2; i < hand_temp1.length; i++) {
                        //if the string is not empty after removing the labels it stores the tiles in the temporary vector
                        temp_human_hand.add(t.stringToTile(hand_temp1[i]));
                    }
                    //sets the newly converted tile vector created from the text file to the human hand
                    round.setHuman_Hand(temp_human_hand);

                }
            }

            //human score
            String human_score = content.get(11);
            String[] score_temp1 = human_score.split("\\s+");
            setEoT_human_score(Integer.parseInt(score_temp1[2]));

            //human wins
            String human_won = content.get(12);
            String[] won_temp1 = human_won.split("\\s+");
            setHuman_wins(Integer.parseInt(won_temp1[3]));

            //serializes turn
            String temp_turn = content.get(14);
            String[] turn_temp = temp_turn.split("\\s+");

            //checks to see if turn is empty by checking the length of the string
            if(temp_turn.length() > 6) {
                round.setTurn(turn_temp[1]);
                round.sortHumanHand();
                round.sortComputerHand();
            }
            else{
                openWhosGoingFirstDialog();
            }


        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

    }



}