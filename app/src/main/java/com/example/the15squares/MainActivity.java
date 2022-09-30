package com.example.the15squares;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        TextView tv = new TextView(this);
        tv.setText("Hello world!");

        RelativeLayout bg = findViewById(R.id.background);
        SquaresView sv = findViewById(R.id.squaresView);
        SquaresController sc = new SquaresController(sv);
        sv.setOnTouchListener(sv);
        sv.setBackground(bg);


    }
}