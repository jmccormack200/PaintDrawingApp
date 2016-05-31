package com.example.jmack.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jmack on 5/31/16.
 */
public class DrawView extends View {

    private Path path = new Path();
    private Paint paint = new Paint();

    public ConcurrentLinkedQueue<Pair> queue = new ConcurrentLinkedQueue<>();


    public DrawView(Context context) {
        super(context);
        setupDrawView();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawView();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupDrawView();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupDrawView();
    }

    private void setupDrawView(){
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawPath(path, paint);
    }

    public void clear(){
        path.reset();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float eventX = event.getX();
        float eventY = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(eventX, eventY);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
}
