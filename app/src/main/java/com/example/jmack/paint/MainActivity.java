package com.example.jmack.paint;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    DrawView drawView;

    private TypedArray styledAttributes;
    private int mActionBarSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        styledAttributes = this.getTheme().obtainStyledAttributes(
                new int[]{
                        android.R.attr.actionBarSize});
        mActionBarSize = (int) styledAttributes.getDimension(0,0);


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
        int x = (int)e.getX();
        int y = (int)e.getY() - mActionBarSize;

        Pair pair;

        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:

                pair = new Pair(x, y);
                drawView.queue.add(pair);
                break;
        }
        return false;

    }
}
