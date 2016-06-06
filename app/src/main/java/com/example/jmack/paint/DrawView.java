package com.example.jmack.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by jmack on 5/31/16.
 */
public class DrawView extends View {

    private Path path = new Path();
    private Paint paint = new Paint();
    private int color = Color.BLACK;
    private float stroke = 4f;

    public ArrayList<PaintPath> paintPaths = new ArrayList<>();


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

    public void changeColor(int color) {
        this.color = color;
        setupDrawView();
    }

    public void changeStrokeWidth(float size) {
        this.stroke = size;
        setupDrawView();
    }

    private void setupDrawView() {
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(stroke);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (PaintPath paintPath : paintPaths) {
            canvas.drawPath(paintPath.path, paintPath.paint);
        }
        canvas.drawPath(path, paint);
    }

    public void clear() {
        path.reset();
        if (paintPaths.size() > 0) {
            paintPaths.remove(paintPaths.size() - 1);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            path.moveTo(eventX, eventY);
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_MOVE) {
            int historySize = event.getHistorySize();
            for (int i = 0; i < historySize; i++) {
                float historicalX = event.getHistoricalX(i);
                float historicalY = event.getHistoricalY(i);
                path.lineTo(historicalX, historicalY);
            }
            path.lineTo(eventX, eventY);
        } else {
            return false;
        }
        if (action == MotionEvent.ACTION_UP) {
            paintPaths.add(new PaintPath(paint, path));
            paint = new Paint();
            path = new Path();
            setupDrawView();
        }
        invalidate();
        return true;
    }
}
