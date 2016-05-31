package com.example.jmack.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jmack on 5/31/16.
 */
public class DrawView extends SurfaceView implements Runnable {
    private Thread drawloop = null;
    private SurfaceHolder surface;
    volatile boolean running = false;

    private boolean clearDrawing = true;

    private int motion = 0;

    public ConcurrentLinkedQueue<Pair> queue = new ConcurrentLinkedQueue<>();


    public DrawView(Context context) {
        super(context);

        surface = getHolder();

    }

    @Override
    public void run() {
        while (running){
            if (!surface.getSurface().isValid()){
                continue;
            }

            Canvas canvas = surface.lockCanvas();

            if (clearDrawing) {
                canvas.drawColor(Color.rgb(25, 0, 100));
                clearDrawing = false;
            }

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);

            if (queue.peek() != null){
                Pair pair = queue.poll();
                float x = (float)pair.first;
                float y = (float)pair.second;
                canvas.drawCircle(x, y, 100, paint);
            }


            surface.unlockCanvasAndPost(canvas);
        }
    }

    public void resume(){
        running = true;
        drawloop = new Thread(this);
        drawloop.start();
    }

    public void pause(){
        running = false;

        while(true){
            try{
                drawloop.join();
            } catch (InterruptedException e){
                Log.v("DrawView", e.getMessage());
            }
        }
    }

}
