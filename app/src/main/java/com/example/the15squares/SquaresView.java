package com.example.the15squares;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

public class SquaresView extends SurfaceView implements View.OnTouchListener {
    private SquaresModel squaresModel;

    private ArrayList<Square> squareList;

    private static final int[][] neighborCoords = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };

    public SquaresView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        squareList = new ArrayList<>();

        squaresModel = new SquaresModel();

        int boardLen = squaresModel.boardLen;
        for(int x=0; x < boardLen; x++)
        {
            for(int y=0; y < boardLen; y++)
            {
                if( ((x * boardLen) + y) == (boardLen * boardLen-1) ){
                    //last tile should be NULL
                    squareList.add(null);
                } else {
                    Square square = new Square();
                    square.setCorner(x * squaresModel.length, y *squaresModel.length);
                    square.setId(squareList.size()+1);
                    squareList.add(square);
                }

            }
        }
        Collections.shuffle(squareList);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int boardLen = squaresModel.boardLen;
        if (squareList == null) {
            return;
        }
        for (int i = 0; i < boardLen * boardLen; i++) {
            Square square = squareList.get(i);
            if (square != null) {
                square.setCorner(i % boardLen * squaresModel.length, i / boardLen * squaresModel.length);
                square.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int boardLen = squaresModel.boardLen;
        int x = (int)motionEvent.getX();
        int y = (int)motionEvent.getY();
        if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < boardLen * boardLen; i++) {
                Square square = squareList.get(i);
                if (square != null) {
                    if (square.isClicked(x, y, i % boardLen , i / boardLen)) {
                        return tryMoving(i % boardLen, i / boardLen);
                    }
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        int boardLen = squaresModel.boardLen;
        for (int[] delta : neighborCoords) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < boardLen && nullY >= 0 && nullY < boardLen &&
                    squareList.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * squaresModel.boardLen;
    }

    protected void swapTiles(int i, int j) {
        Square temp = squareList.get(i);
        squareList.set(i, squareList.get(j));
        squareList.set(j, temp);
        invalidate();
    }

    protected void checkWin()
    {
        int boardLen = squaresModel.boardLen;
        for (int i = 1; i < boardLen * boardLen; i++)
        {
            if(i == squareList.get(i).getId())
            {
                // return true, celebrate, yay
            }
            else
            {
                // return false, bad, ew
            }
        }
    }

    public SquaresModel getSquaresModel()
    {
        return squaresModel;
    }
}
