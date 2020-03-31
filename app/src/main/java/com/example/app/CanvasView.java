package com.example.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView  extends View {

    private Paint mPaint;
    private Path mPath;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(Color.RED);
        mPath = new Path();
    }

    void setColor(int color){
        mPaint.setColor(color);
        invalidate();//IWR
    }

    void setStyle(Paint.Style style){
        mPaint.setStyle(style);
        invalidate();
    }

    void setSize(int size){
        mPaint.setStrokeWidth(size);
        invalidate();
    }

    void setCap(Paint.Cap cap){
        mPaint.setStrokeCap(cap);
    }

    void clear() {
        mPath.reset();
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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

    public void toggleStroke() {
        if(mPaint.getStyle() == Paint.Style.STROKE){

        }
    }

    public int getColor() {
        return mPaint.getColor();
    }
}