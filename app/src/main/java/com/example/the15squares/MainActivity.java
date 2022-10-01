/* @author Nate Hopper
 9/29/2022
 15Square puzzle game where you have to rearrange puzzle cubes to get them in numerical order

 Unfortunately, had to hardcode some values for text size and layout width+height.
 Could not figure out the mixture of layout_weight AND having the squares inside the view being
 dynamically sized. It maxed out the size of the grid to the screen every time.

 Optional enhancements implemented:
 -Only play boards that are mathematically possible to complete
 -Puzzle size change
*/
package com.example.the15squares;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // To hide the top action bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView squaresText = findViewById(R.id.squaresText);
        SeekBar squaresBar = findViewById(R.id.squaresBar);
        Button resetSwitch = findViewById(R.id.resetSwitch);

        RelativeLayout bg = findViewById(R.id.background);
        SquaresView sv = findViewById(R.id.squaresView);
        SquaresController sc = new SquaresController(sv);
        sv.setOnTouchListener(sv);
        sv.setBackground(bg);
        squaresBar.setOnSeekBarChangeListener(sv);
        resetSwitch.setOnTouchListener(sv);
        sv.setProgressText(squaresText);

    }
}