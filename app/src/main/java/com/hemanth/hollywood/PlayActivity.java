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
import android.widget.Toast;

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
    int lengthOfCurrentMovie;
    Drawable buttonRed, buttonGreen;
    AlertDialog.Builder builder;
    Stack<Integer> dummyStack;
    Stack<Character> hollyWoodStack;

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
            textView.setText(String.valueOf(currentMovie.charAt(i)));

            if (Character.isLetter(currentMovie.charAt(i)) || Character.isDigit(currentMovie.charAt(i))) {
                textView.setText("_");
                textView.setBackground(getDrawable(R.drawable.button_green));
                dummyStack.push(i);
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
        buttonRed = getDrawable(R.drawable.button_red);
        buttonGreen = getDrawable(R.drawable.button_green);
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Hollywood")
                .setPositiveButton("PLAY AGAIN", this)
                .setNegativeButton("CANCEL", this);

        dummyStack = new Stack<>();
        hollyWoodStack = new Stack<>();

        for (char letter : "Hollywood".toCharArray()) {
            hollyWoodStack.push(letter);
        }


    }

    public void addTextViewToLayout(TextView textView) {
        flowLayout.addView(textView);
        individualCharacterList.add(textView);
    }


    public void onKeyPressed(View view) {
        if (view.getTag().equals("0")) {
            String pressedLetter = ((Button) view).getText().toString();
            if (currentMovie.contains(pressedLetter)) {
                changeTagBackgroundColorFor(view, "1", buttonGreen, Color.BLACK);

                for (int i = 0; i < lengthOfCurrentMovie; i++) {
                    if (pressedLetter.equals(String.valueOf(currentMovie.charAt(i)))) {
                        individualCharacterList.get(i).setText(pressedLetter);
                        dummyStack.pop();
                        //logText(dummyIndividualCharacterListLength + "");
                        if (checkWinner()) {
                            showDialog("YOU WON");
                        }
                    }
                }

            } else {
                changeTagBackgroundColorFor(view, "-1", buttonRed,Color.WHITE);
                hollyWoodStack.pop();
                if (checkGameOver()) {
                    showDialog("YOU LOSE");
                }
                hollyWoodTextView.setText(stackToString());

            }
        }
        else if(view.getTag().equals("1")) {
            Toast.makeText(this,"Already Pressed!",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Already Pressed!",Toast.LENGTH_SHORT).show();
        }
    }

    public void changeTagBackgroundColorFor(View view, String tag, Drawable buttonColor,int textColor) {
        view.setTag(tag);
        ((Button) view).setTextColor(textColor);
        view.setBackground(buttonColor);

    }

    public boolean checkGameOver() {
        return hollyWoodStack.empty();
    }

    public boolean checkWinner() {
        return dummyStack.empty();
    }

    public void showDialog(String message) {
        builder.setMessage(message).show().setCanceledOnTouchOutside(false);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PlayActivity.this, MainActivity.class));

    }

    public void logText(String message) {
        Log.d("vj", message);
    }


    public String stackToString() {
        StringBuilder string = new StringBuilder();
        for (char letter : hollyWoodStack) {
            string.append(letter);
        }
        return String.valueOf(string);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        //logText("which=" + which);
        if (which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(PlayActivity.this, PlayActivity.class));
        } else {
            startActivity(new Intent(PlayActivity.this, MainActivity.class));
        }


    }
}
