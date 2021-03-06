package com.example.everyware.ui.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class EditorCanvas extends View {


    public int width;
    public  int height;
    private Rect area;
    private Bitmap mBitmap;
    private Canvas canvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    Context context;

    public EditorCanvas(Context context, AttributeSet attrs){
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mPath = new Path();
    }

    public void setArea(Rect area){
        this.area = area;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(area == null){
            super.onDraw(canvas);
            return;
        }
        canvas.save();
        canvas.clipRect(area);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.onDraw(canvas);
    }

    public void clearDrawing()
    {
        mPath = null;
        mPath = new Path();
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!area.contains((int) event.getX(),(int) event.getY())){
            super.onTouchEvent(event);
            return false;
        }


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

}
