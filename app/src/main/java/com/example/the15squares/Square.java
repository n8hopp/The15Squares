package com.example.the15squares;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Square {
    private int x,y;
    private int length;
    private int id;
    private Paint paint;
    private Paint stringPaint;

    public Square()
    {
        x = 300;
        y = 300;
        length = 200;
        id = 0;

        paint = new Paint();
        paint.setARGB(255, 0, 0,0);

        stringPaint = new Paint();
        stringPaint.setColor(Color.WHITE);
        stringPaint.setTextAlign(Paint.Align.CENTER);
        stringPaint.setTextSize(length/2);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(x, y, x+length, y+length, paint);
        canvas.drawText(String.valueOf(id), x+length/2, y+length/2, stringPaint );
    }

    public void setCorner(int _x, int _y)
    {
        x = _x;
        y = _y;
    }

    public boolean isClicked(float clickX, float clickY, int tileX, int tileY) {
        int tileX0 = tileX * length;
        int tileX1 = (tileX + 1) * length;
        int tileY0 = tileY * length;
        int tileY1 = (tileY + 1) * length;
        return (clickX >= tileX0) && (clickX < tileX1) && (clickY >= tileY0) && (clickY < tileY1);
    }

    public void setId(int _id)
    {
        id = _id;
    }

    public int getId()
    {
        return id;
    }


}
