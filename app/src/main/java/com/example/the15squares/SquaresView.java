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
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class SquaresView extends SurfaceView implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener {
    private SquaresModel squaresModel;

    private ArrayList<Square> squareList;
    private RelativeLayout background;
    private Paint backgroundPaint;
    private TextView progText;
    private boolean isFirstDraw = true;

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

    }

    // Void to create new board & arrayList holding it. Separate function from the constructor
    // so that we can run it in multiple places
    private void instantiateBoard()
    {
        int boardLen = squaresModel.boardLen;
        squareList.clear();
        background.setBackgroundColor(Color.WHITE);
        squaresModel.length = this.getWidth() / squaresModel.boardLen;
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
                    square.setSize(squaresModel.length);
                    square.setId(squareList.size()+1);
                    squareList.add(square);
                }

            }
        }
        randomizeList();
    }
    @Override
    public void onDraw(Canvas canvas) {
        // Check to see if variable isFirstDraw so we can know if the page has loaded fully
        // for the first time. If we don't check this, the board gets re-instantiated every time
        // the onDraw is called (which is every time we click on the board)
        if(isFirstDraw)
        {
            instantiateBoard();
            progText.setText(String.format("Current Game Size: %dx%d", squaresModel.boardLen, squaresModel.boardLen));
        }
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
        isFirstDraw = false;
    }

    // If we clicked on our square view, try moving it.
    // If we clicked on our reset switch, reset the board.
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int boardLen = squaresModel.boardLen;
        int x = (int)motionEvent.getX();
        int y = (int)motionEvent.getY();
        if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
            switch(view.getId()) {
                case R.id.squaresView:
                    for (int i = 0; i < boardLen * boardLen; i++) {
                        Square square = squareList.get(i);
                        if (square != null) {
                            if (square.isClicked(x, y, i % boardLen, i / boardLen)) {
                                return tryMoving(i % boardLen, i / boardLen);
                            }
                        }
                    }
                    break;
                case R.id.resetSwitch:
                    instantiateBoard();
                    invalidate();
                    break;
            }
        }
        return false;
    }

    // Test to see if move is possible (by doing it)
    // We use a int[] called neighborCoords to test all the pieces around the clicked piece
    // to see if they're the null piece.
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

    // Randomize list using the algorithm on https://mathworld.wolfram.com/15Puzzle.html.
    // Recursive function randomizes until board is solvable.
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

    // Update parameters of board based on seekbar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        squaresModel.boardLen = progress + 3;
        instantiateBoard();
        progText.setText(String.format("Current Game Size: %dx%d", squaresModel.boardLen, squaresModel.boardLen));
        invalidate();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setProgressText(TextView text)
    {
        progText = text;
    }
}
