package com.hemanth.hollywood;

import android.content.DialogInterface;
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

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import static android.support.annotation.Dimension.SP;

public class PlayActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    ArrayList<String> movies;
    ArrayList<TextView> individualCharacterList;
    String currentMovie;
    FlowLayout flowLayout;
    TextView hollyWoodTextView;
    char[] hollyWood;
    int hollyWoodLength, lengthOfCurrentMovie;
    int dummyIndividualCharacterListLength;
    Drawable buttonRed, buttonGreen;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        MyKeyboard keyboard = findViewById(R.id.keyboard);
        int random;
        Scanner scanner;

        init();


        scanner = new Scanner(getResources().openRawResource(R.raw.movies));
        while (scanner.hasNext()) {
            String movie = scanner.nextLine();
            movies.add(movie.toUpperCase());
        }
        scanner.close();


        random = (int) (Math.random() * movies.size());

        currentMovie = movies.get(random);
        lengthOfCurrentMovie = currentMovie.length();


        for (int i = 0; i < lengthOfCurrentMovie; i++) {
            TextView textView = new TextView(this);

            if (Character.isLetter(currentMovie.charAt(i))) {
                textView.setText("_");
                textView.setBackground(getDrawable(R.drawable.button_green));
                dummyIndividualCharacterListLength++;
            }

            textView.setPadding(10, 10, 10, 10);
            textView.setTextSize(SP, 20);

            addTextViewToLayout(textView);
        }

    }

    public void init() {
        movies = new ArrayList<>();
        individualCharacterList = new ArrayList<>();
        flowLayout = findViewById(R.id.flow);
        hollyWoodTextView = findViewById(R.id.hollywood);
        hollyWood = hollyWoodTextView.getText().toString().toCharArray();
        hollyWoodLength = hollyWood.length;
        buttonRed = getDrawable(R.drawable.button_red);
        buttonGreen = getDrawable(R.drawable.button_green);
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Hollywood")
                .setPositiveButton("PLAY AGAIN", this)
                .setNegativeButton("CANCEL",this);



    }

    public void addTextViewToLayout(TextView textView) {
        flowLayout.addView(textView);
        individualCharacterList.add(textView);
    }


    public void onKeyPressed(View view) {
        if (view.getTag().equals("0")) {
            String pressedLetter = ((Button) view).getText().toString();
            if (currentMovie.contains(pressedLetter)) {
                changeTagBackgroundFor(view, "1", buttonGreen);

                for (int i = 0; i < lengthOfCurrentMovie; i++) {
                    if (pressedLetter.equals(String.valueOf(currentMovie.charAt(i)))) {
                        individualCharacterList.get(i).setText(pressedLetter);
                        dummyIndividualCharacterListLength--;
                        logText(dummyIndividualCharacterListLength + "");
                        if (checkWinner()) {
                            showDialog("YOU WON");
                        }
                    }
                }

            } else {
                changeTagBackgroundFor(view, "-1", buttonRed);
                hollyWoodLength--;
                if (checkGameOver()) {
                    hollyWoodTextView.setText(hollyWood, 0, 0);
                    showDialog("YOU LOSE");
                } else {
                    hollyWoodTextView.setText(hollyWood, 0, hollyWoodLength);
                }
            }
        }
    }

    public void changeTagBackgroundFor(View view, String tag, Drawable buttonColor) {
        view.setTag(tag);
        ((Button) view).setTextColor(Color.WHITE);
        view.setBackground(buttonColor);

    }

    public boolean checkGameOver() {
        return hollyWoodLength < 1;
    }

    public boolean checkWinner() {
        return dummyIndividualCharacterListLength < 1;
    }

    public void showDialog(String message) {
        builder.setMessage(message).show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PlayActivity.this, MainActivity.class));

    }

    public void logText(String message) {
        Log.d("vj",message);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //logText("which=" + which);
        if(which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(PlayActivity.this, PlayActivity.class));
        }
        else {
            startActivity(new Intent(PlayActivity.this, MainActivity.class));
        }


    }
}
