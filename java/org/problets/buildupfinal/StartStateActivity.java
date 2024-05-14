//Name : Kristin Miyamoto
//Project: 02: Java Android
//Class: CMPS 366-01 - OPL
//Date: 4/29/23

package org.problets.buildupfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StartStateActivity extends AppCompatActivity{
    public final static String fileName = "directory"; //input load file name

    Button new_game, load;
    TextView inputText;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItem;

    String resume_file;

    /**
     * makes the main menu up on start of the app
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

        new_game = (Button)findViewById(R.id.new_game);

        File dir= new File("/data/user/0/org.problets.buildupfinal/files");

        File[] files =dir.listFiles();
        List<String> item=new ArrayList<>();
        for(File file: files){
            if(file.isFile()){
                item.add(file.getName());
            }
        }

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItem = new ArrayAdapter<String>(this,R.layout.list_items, item);
        autoCompleteTextView.setAdapter(adapterItem);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * to pass the intent or data from the startstateactivity to the mainactivity
             *              and in this case the resume file name (string)
             * @param adapterView The AdapterView where the click happened.
             * @param view The view within the AdapterView that was clicked (this
             *            will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                resume_file = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(StartStateActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();

                //passes data between activities
                Intent i = new Intent(StartStateActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("resume", resume_file);
                i.putExtras(bundle);
                finish();
                startActivity(i);

            }
        });


        new_game.setOnClickListener(new View.OnClickListener() {
            /**
             * to pass the intent or data from the startstateactivity to the mainactivity
             *              and in this case nothing because it goes into starting a new game
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                //passes data between activities
                Intent i = new Intent(StartStateActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                finish();
                startActivity(i);
            }
        });


    }

    /* *********************************************************************
    Function Name: displayTileImage
    Purpose: sets the correct drawable resource to be displayed in the gui to the imageview
    Parameters:
             ImageView myImage, represents the one of the images in the activity_main.xml (gui) to be changed.
             String tile, represents a string of the tile name for the imageview's
             image resource to be set to and appear in the gui
    Return Value: none
    Algorithm:
            1) uses switch statement to properly set the inputted imageview to the
            correct corresponding drawable resources
     Assistance Received: none
    ********************************************************************* */
    public static void displayTileImage(ImageView myImage, String tile) {
        switch(tile) {
            case "B00":
                myImage.setImageResource(R.drawable.b00);
                break;
            case "B01":
                myImage.setImageResource(R.drawable.b01);
                break;
            case "B02":
                myImage.setImageResource(R.drawable.b02);
                break;
            case "B03":
                myImage.setImageResource(R.drawable.b03);
                break;
            case "B04":
                myImage.setImageResource(R.drawable.b04);
                break;
            case "B05":
                myImage.setImageResource(R.drawable.b05);
                break;
            case "B06":
                myImage.setImageResource(R.drawable.b06);
                break;
            case "B11":
                myImage.setImageResource(R.drawable.b11);
                break;
            case "B12":
                myImage.setImageResource(R.drawable.b12);
                break;
            case "B13":
                myImage.setImageResource(R.drawable.b13);
                break;
            case "B14":
                myImage.setImageResource(R.drawable.b14);
                break;
            case "B15":
                myImage.setImageResource(R.drawable.b15);
                break;
            case "B16":
                myImage.setImageResource(R.drawable.b16);
                break;
            case "B22":
                myImage.setImageResource(R.drawable.b22);
                break;
            case "B23":
                myImage.setImageResource(R.drawable.b23);
                break;
            case "B24":
                myImage.setImageResource(R.drawable.b24);
                break;
            case "B25":
                myImage.setImageResource(R.drawable.b25);
                break;
            case "B26":
                myImage.setImageResource(R.drawable.b26);
                break;
            case "B33":
                myImage.setImageResource(R.drawable.b33);
                break;
            case "B34":
                myImage.setImageResource(R.drawable.b34);
                break;
            case "B35":
                myImage.setImageResource(R.drawable.b35);
                break;
            case "B36":
                myImage.setImageResource(R.drawable.b36);
                break;
            case "B44":
                myImage.setImageResource(R.drawable.b44);
                break;
            case "B45":
                myImage.setImageResource(R.drawable.b45);
                break;
            case "B46":
                myImage.setImageResource(R.drawable.b46);
                break;
            case "B55":
                myImage.setImageResource(R.drawable.b55);
                break;
            case "B56":
                myImage.setImageResource(R.drawable.b56);
                break;
            case "B66":
                myImage.setImageResource(R.drawable.b66);
                break;
            case "W00":
                myImage.setImageResource(R.drawable.w00);
                break;
            case "W01":
                myImage.setImageResource(R.drawable.w01);
                break;
            case "W02":
                myImage.setImageResource(R.drawable.w02);
                break;
            case "W03":
                myImage.setImageResource(R.drawable.w03);
                break;
            case "W04":
                myImage.setImageResource(R.drawable.w04);
                break;
            case "W05":
                myImage.setImageResource(R.drawable.w05);
                break;
            case "W06":
                myImage.setImageResource(R.drawable.w06);
                break;
            case "W11":
                myImage.setImageResource(R.drawable.w11);
                break;
            case "W12":
                myImage.setImageResource(R.drawable.w12);
                break;
            case "W13":
                myImage.setImageResource(R.drawable.w13);
                break;
            case "W14":
                myImage.setImageResource(R.drawable.w14);
                break;
            case "W15":
                myImage.setImageResource(R.drawable.w15);
                break;
            case "W16":
                myImage.setImageResource(R.drawable.w16);
                break;
            case "W22":
                myImage.setImageResource(R.drawable.w22);
                break;
            case "W23":
                myImage.setImageResource(R.drawable.w23);
                break;
            case "W24":
                myImage.setImageResource(R.drawable.w24);
                break;
            case "W25":
                myImage.setImageResource(R.drawable.w25);
                break;
            case "W26":
                myImage.setImageResource(R.drawable.w26);
                break;
            case "W33":
                myImage.setImageResource(R.drawable.w33);
                break;
            case "W34":
                myImage.setImageResource(R.drawable.w34);
                break;
            case "W35":
                myImage.setImageResource(R.drawable.w35);
                break;
            case "W36":
                myImage.setImageResource(R.drawable.w36);
                break;
            case "W44":
                myImage.setImageResource(R.drawable.w44);
                break;
            case "W45":
                myImage.setImageResource(R.drawable.w45);
                break;
            case "W46":
                myImage.setImageResource(R.drawable.w46);
                break;
            case "W55":
                myImage.setImageResource(R.drawable.w55);
                break;
            case "W56":
                myImage.setImageResource(R.drawable.w56);
                break;
            case "W66":
                myImage.setImageResource(R.drawable.w66);
                break;
        }
    }
    /* *********************************************************************
    Function Name: displayTileImage
    Purpose: sets the correct drawable resource to be displayed in the gui to the imageview
    Parameters:
             Button button, represents the one of the images in the activity_main.xml (gui) to be changed.
             String tile, represents a string of the tile name for the imageview's
             image resource to be set to and appear in the gui
    Return Value: none
    Algorithm:
            1) uses switch statement to properly set the inputted imageview to the
            correct corresponding drawable resources
     Assistance Received: none
    ********************************************************************* */
    public static void displayTileImage(Button button, String tile) {
        switch(tile) {
            case "B00":
                button.setBackgroundResource(R.drawable.b00);
                break;
            case "B01":
                button.setBackgroundResource(R.drawable.b01);
                break;
            case "B02":
                button.setBackgroundResource(R.drawable.b02);
                break;
            case "B03":
                button.setBackgroundResource(R.drawable.b03);
                break;
            case "B04":
                button.setBackgroundResource(R.drawable.b04);
                break;
            case "B05":
                button.setBackgroundResource(R.drawable.b05);
                break;
            case "B06":
                button.setBackgroundResource(R.drawable.b06);
                break;
            case "B11":
                button.setBackgroundResource(R.drawable.b11);
                break;
            case "B12":
                button.setBackgroundResource(R.drawable.b12);
                break;
            case "B13":
                button.setBackgroundResource(R.drawable.b13);
                break;
            case "B14":
                button.setBackgroundResource(R.drawable.b14);
                break;
            case "B15":
                button.setBackgroundResource(R.drawable.b15);
                break;
            case "B16":
                button.setBackgroundResource(R.drawable.b16);
                break;
            case "B22":
                button.setBackgroundResource(R.drawable.b22);
                break;
            case "B23":
                button.setBackgroundResource(R.drawable.b23);
                break;
            case "B24":
                button.setBackgroundResource(R.drawable.b24);
                break;
            case "B25":
                button.setBackgroundResource(R.drawable.b25);
                break;
            case "B26":
                button.setBackgroundResource(R.drawable.b26);
                break;
            case "B33":
                button.setBackgroundResource(R.drawable.b33);
                break;
            case "B34":
                button.setBackgroundResource(R.drawable.b34);
                break;
            case "B35":
                button.setBackgroundResource(R.drawable.b35);
                break;
            case "B36":
                button.setBackgroundResource(R.drawable.b36);
                break;
            case "B44":
                button.setBackgroundResource(R.drawable.b44);
                break;
            case "B45":
                button.setBackgroundResource(R.drawable.b45);
                break;
            case "B46":
                button.setBackgroundResource(R.drawable.b46);
                break;
            case "B55":
                button.setBackgroundResource(R.drawable.b55);
                break;
            case "B56":
                button.setBackgroundResource(R.drawable.b56);
                break;
            case "B66":
                button.setBackgroundResource(R.drawable.b66);
                break;
            case "W00":
                button.setBackgroundResource(R.drawable.w00);
                break;
            case "W01":
                button.setBackgroundResource(R.drawable.w01);
                break;
            case "W02":
                button.setBackgroundResource(R.drawable.w02);
                break;
            case "W03":
                button.setBackgroundResource(R.drawable.w03);
                break;
            case "W04":
                button.setBackgroundResource(R.drawable.w04);
                break;
            case "W05":
                button.setBackgroundResource(R.drawable.w05);
                break;
            case "W06":
                button.setBackgroundResource(R.drawable.w06);
                break;
            case "W11":
                button.setBackgroundResource(R.drawable.w11);
                break;
            case "W12":
                button.setBackgroundResource(R.drawable.w12);
                break;
            case "W13":
                button.setBackgroundResource(R.drawable.w13);
                break;
            case "W14":
                button.setBackgroundResource(R.drawable.w14);
                break;
            case "W15":
                button.setBackgroundResource(R.drawable.w15);
                break;
            case "W16":
                button.setBackgroundResource(R.drawable.w16);
                break;
            case "W22":
                button.setBackgroundResource(R.drawable.w22);
                break;
            case "W23":
                button.setBackgroundResource(R.drawable.w23);
                break;
            case "W24":
                button.setBackgroundResource(R.drawable.w24);
                break;
            case "W25":
                button.setBackgroundResource(R.drawable.w25);
                break;
            case "W26":
                button.setBackgroundResource(R.drawable.w26);
                break;
            case "W33":
                button.setBackgroundResource(R.drawable.w33);
                break;
            case "W34":
                button.setBackgroundResource(R.drawable.w34);
                break;
            case "W35":
                button.setBackgroundResource(R.drawable.w35);
                break;
            case "W36":
                button.setBackgroundResource(R.drawable.w36);
                break;
            case "W44":
                button.setBackgroundResource(R.drawable.w44);
                break;
            case "W45":
                button.setBackgroundResource(R.drawable.w45);
                break;
            case "W46":
                button.setBackgroundResource(R.drawable.w46);
                break;
            case "W55":
                button.setBackgroundResource(R.drawable.w55);
                break;
            case "W56":
                button.setBackgroundResource(R.drawable.w56);
                break;
            case "W66":
                button.setBackgroundResource(R.drawable.w66);
                break;
        }
    }



}
