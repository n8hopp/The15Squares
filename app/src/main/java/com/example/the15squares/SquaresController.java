package com.example.the15squares;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

// @author Nate Hopper
// UNUSED CLASS - found it wildly more easier to implement in SquaresView so we didn't need to
// pass over every variable.
public class SquaresController implements View.OnClickListener, View.OnTouchListener{

    private SquaresView squaresView;
    private SquaresModel squaresModel;
    private View view;


    public SquaresController(SquaresView sv)
    {
        squaresView = sv;
        squaresModel = squaresView.getSquaresModel();
    }
    @Override
    public void onClick(View view) {

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
