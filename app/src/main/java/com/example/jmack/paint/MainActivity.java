package com.example.jmack.paint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        drawView = new DrawView(this);
        setContentView(drawView);
    }

    @Override
    public void onResume(){
        super.onResume();
        drawView.resume();
    }

    @Override
    public void onPause(){
        super.onPause();
        drawView.pause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:
                Pair pair = new Pair(x, y);
                drawView.queue.add(pair);
        }
        return true;

    }
}
