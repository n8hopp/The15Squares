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
    private Paint borderPaint;

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
        borderPaint = new Paint();
        borderPaint.setStrokeWidth(length/20);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas)
    {
        stringPaint.setTextSize(length/2);
        canvas.drawRect(x, y, x+length, y+length, paint);
        canvas.drawRect(x, y, x+length, y+length, borderPaint);
        canvas.drawText(String.valueOf(id), x+length/2, y+length/2 - (stringPaint.descent() + stringPaint.ascent()) / 2, stringPaint );
    }

    public void setCorner(int _x, int _y)
    {
        x = _x;
        y = _y;
    }

    public void setSize(int len)
    {
        length = len;
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
