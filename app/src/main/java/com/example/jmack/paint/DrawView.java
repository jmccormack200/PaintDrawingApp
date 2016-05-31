package com.example.jmack.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jmack on 5/31/16.
 */
public class DrawView extends SurfaceView implements Runnable, SurfaceHolder.Callback  {
    private Thread drawloop = null;
    private SurfaceHolder surface;
    volatile boolean running = false;

    private boolean clearDrawing = true;

    private int motion = 0;

    private Canvas mCanvas;
    private int mCanvasW;
    private int mCanvasH;
    private Bitmap mBitmap;

    private Matrix identityMatrix;

    private Path path = new Path();

    public ConcurrentLinkedQueue<Pair> queue = new ConcurrentLinkedQueue<>();


    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void run() {
        while (running){
            if (!surface.getSurface().isValid()){
                continue;
            }

            Canvas canvas = surface.lockCanvas();

            canvas.drawColor(Color.rgb(25, 0, 100));


            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(3);

            while (queue.peek() != null){
                Pair pair = queue.poll();
                float x = (float)pair.first;
                float y = (float)pair.second;

                if (x != 0 && y != 0) {
                    path.lineTo()
                    //mCanvas.drawCircle(x, y, 10, paint);
                    //mCanvas.drawPoint(x, y, paint);
                }
            }
            canvas.drawBitmap(mBitmap, identityMatrix, null);

            surface.unlockCanvasAndPost(canvas);
        }
    }

    public void resume(){
        running = true;
        surface = getHolder();
        getHolder().addCallback(this);

        drawloop = new Thread(this);
        drawloop.start();
    }

    public void pause(){
        running = false;

        while(running == false){
            try{
                drawloop.join();
                running = true;
            } catch (InterruptedException e){
                Log.v("DrawView", e.getMessage());
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCanvasW = getWidth();
        mCanvasH = getHeight();
        Log.v("WIDTH", String.valueOf(mCanvasH));
        mBitmap = Bitmap.createBitmap(mCanvasW, mCanvasH, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);

        identityMatrix = new Matrix();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
