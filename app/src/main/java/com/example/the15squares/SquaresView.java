package com.example.the15squares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;

public class SquaresView extends SurfaceView implements View.OnTouchListener {
    private SquaresModel squaresModel;

    private ArrayList<Square> squareList;
    private RelativeLayout background;
    private Paint backgroundPaint;

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

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
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
        //randomizeList();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
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
                if(checkWin())
                {
                    backgroundPaint.setColor(Color.GREEN);
                    background.setBackgroundColor(Color.GREEN);
                } else
                {
                    backgroundPaint.setColor(Color.WHITE);
                    background.setBackgroundColor(Color.WHITE);
                }
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

    protected boolean checkWin()
    {
        int boardLen = squaresModel.boardLen;
        boolean found = true;
        for (int i = 0; i < (boardLen * boardLen) - 1; i++)
        {
            if(squareList.get(i) != null)
            {
                if(!(i+1 == squareList.get(i).getId())) {
                    found = false;
                }
            } else { found = false; }

        }

        return found;
    }

    private boolean isRandomizationValid(ArrayList<Square> ar)
    {
        int boardLen = squaresModel.boardLen;
        int alternatingSum = 0;
        for(int i = 0; i < boardLen * boardLen; i++)
        {
            if(squareList.get(i) == null)
            {
                continue;
            }
            for(int j = 0; j < i; j++)
            {
                if(squareList.get(j) == null)
                {
                    continue;
                }
                if(squareList.get(j).getId() > squareList.get(i).getId())
                {
                    alternatingSum++;
                }
            }
        }
        return (alternatingSum % 2 == 0);
    }

    private void randomizeList()
    {
        int boardLen = squaresModel.boardLen;
        ArrayList<Square> randomized = new ArrayList<>();
        for(int i = 0; i < boardLen*boardLen - 1; i++)
        {
            randomized.add(squareList.get(i));
        }
        Collections.shuffle(randomized);
        for(int i = 0; i < boardLen*boardLen - 1; i++)
        {
            squareList.set(i, randomized.get(i));
        }
        if(!isRandomizationValid(squareList))
        {
            randomizeList();
        }

    }

    public void setBackground(RelativeLayout bg)
    {
        background = bg;
    }

    public SquaresModel getSquaresModel()
    {
        return squaresModel;
    }
}
