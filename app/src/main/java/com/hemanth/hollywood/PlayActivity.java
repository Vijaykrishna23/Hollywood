package com.hemanth.hollywood;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import static android.support.annotation.Dimension.SP;

public class PlayActivity extends AppCompatActivity{

    // the empty boxes
    FlowLayout boxesLayout;

    // HollyWood Text
    TextView hollyWoodTextView;

    // BackGroundButtons for the keys in keyboard
    Drawable redBackGroundButton, greenBackGroundButton;

    // List of the boxes displayed and the spaces where boxes are not displayed
    List<TextView> allBoxesList;

    String currentMovie;
    int lengthOfCurrentMovie;

    // same as allBoxesList but contains dummy values for the size.
    Stack<Integer> individualCharactersStack;

    // contains the string 'HollyWood'
    Stack<Character> hollyWoodTextStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        initVariables();

        currentMovie = getRandomMovieFromMoviesFile();
        lengthOfCurrentMovie = currentMovie.length();

        // displaying empty boxes
        for (int i = 0; i < lengthOfCurrentMovie; i++) {

            // creating new box
            TextView textView = new TextView(this);
            textView.setText(String.valueOf(currentMovie.charAt(i)));

            // displays a-z and 0-9 as _ | rest are displayed as the same
            if (Character.isLetter(currentMovie.charAt(i)) || Character.isDigit(currentMovie.charAt(i))) {
                textView.setText("_");
                textView.setBackground(getDrawable(R.drawable.button_green));
                individualCharactersStack.push(i);
            }

            // for ui
            textView.setPadding(10, 10, 10, 10);
            textView.setTextSize(SP, 20);

            addTextViewToLayout(textView);
        }

    }

    /**
     * Method to initialize the class-level variables.
     */

    public void initVariables() {

        boxesLayout = findViewById(R.id.flow);
        hollyWoodTextView = findViewById(R.id.hollywood);
        redBackGroundButton = getDrawable(R.drawable.button_red);
        greenBackGroundButton = getDrawable(R.drawable.button_green);

        allBoxesList = new ArrayList<>();

        individualCharactersStack = new Stack<>();
        hollyWoodTextStack = new Stack<>();

        // populating hollywood stack
        for (char letter : "Hollywood".toCharArray()) {
            hollyWoodTextStack.push(letter);
        }


    }

    /**
     *  Opens the movies.txt file and converts to list and returns a random movie.
     */
    public String getRandomMovieFromMoviesFile() {
        List<String> movies = new ArrayList<>();

        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.movies));

        while (scanner.hasNext()) {
            String movie = scanner.nextLine();
            movies.add(movie.toUpperCase());
        }

        scanner.close();

        int randomIndex = (int) (Math.random() * movies.size());

        return movies.get(randomIndex);
    }

    /**
     * Given a textview, it adds to the end of the boxes.
     * It also adds the textview to allBoxes list.
     */
    public void addTextViewToLayout(TextView textView) {
        boxesLayout.addView(textView);
        allBoxesList.add(textView);
    }

    /**
     * This function gets executed when any key on custom keyboard is pressed.
     * Handles changing of the background of the key and ending the game if the user wins or loses.
     */
    public void onKeyPressed(View view) {

        // tag = 0 | not pressed yet
        if (view.getTag().equals("0")) {

            String pressedLetter = ((Button) view).getText().toString();

            // letter is in the movie
            if (currentMovie.contains(pressedLetter)) {

                // tag = 1 | correct answer
                changeTagAndBackgroundColor(view, "1", greenBackGroundButton, Color.BLACK);

                for (int i = 0; i < lengthOfCurrentMovie; i++) {

                    if (pressedLetter.equals(String.valueOf(currentMovie.charAt(i)))) {

                        allBoxesList.get(i).setText(pressedLetter);
                        individualCharactersStack.pop();

                        if (isWinner()) {
                            showDialog("YOU WON");
                        }
                    }
                }

            } else {

                // tag = -1 | wrong answer
                changeTagAndBackgroundColor(view, "-1", redBackGroundButton,Color.WHITE);

                hollyWoodTextStack.pop();

                if (isGameOver()) {
                    showDialog("YOU LOSE");
                }
                hollyWoodTextView.setText(stackToString());

            }
        }
        else {
            Toast.makeText(this,"Already Pressed!",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Given the view , tag, backGroundColor, and textColor
     * the view is modified according to the parameters.
     * Used when onKeyPressed.
     */
    public void changeTagAndBackgroundColor(View view, String tag, Drawable buttonColor, int textColor) {
        view.setTag(tag);
        ((Button) view).setTextColor(textColor);
        view.setBackground(buttonColor);

    }

    /**
     * Utility Function to check if game is over i.e. user loses.
     */
    public boolean isGameOver() {
        return hollyWoodTextStack.empty();
    }

    /**
     * Utility Function to check if game is won i.e. user wins.
     */
    public boolean isWinner() {
        return individualCharactersStack.empty();
    }

    /**
     * Given a message, opens a dialog with choices of 'PLAY AGAIN'
     * and 'CANCEL'. This is used when user wins or loses the game.
     */
    public void showDialog(String message) {
         new AlertDialog.Builder(this)
                .setTitle("Hollywood")
                .setPositiveButton("PLAY AGAIN", (dialog, which) -> {
                    startActivity(new Intent(PlayActivity.this, PlayActivity.class));
                    finish();
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    startActivity(new Intent(PlayActivity.this, PlayActivity.class));
                    finish();
                })
                .setMessage(message)
                .show()
                .setCanceledOnTouchOutside(false);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void logText(String message) {
        Log.d("playactivity", message);
    }

    /**
     * Takes the stack of characters and converts to string.
     * Used to display the text HollyWood.
     */
    public String stackToString() {

        StringBuilder string = new StringBuilder();
        for (char letter : hollyWoodTextStack) {
            string.append(letter);
        }
        return String.valueOf(string);
    }


}
