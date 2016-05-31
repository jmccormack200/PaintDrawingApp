package com.example.jmack.paint;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    private DrawView mDrawView;

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

        mDrawView = (DrawView) findViewById(R.id.drawview);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void clearAll(View view){
        mDrawView.clear();
    }

}
