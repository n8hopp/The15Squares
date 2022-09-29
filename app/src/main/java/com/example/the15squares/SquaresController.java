package com.example.the15squares;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

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
