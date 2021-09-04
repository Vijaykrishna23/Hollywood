package com.hemanth.hollywood;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Class file for keyboard.xml
 * Used for UI. attachToRoot is set to true
 */
public class MyKeyboard extends LinearLayout {

    public MyKeyboard(Context context) {
        this(context,null,0);
    }

    public MyKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.keyboard,this,true);

    }

}
