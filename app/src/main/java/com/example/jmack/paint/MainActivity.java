package com.example.jmack.paint;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawView mDrawView;

    private TypedArray styledAttributes;
    private int mActionBarSize;

    //TODO: Convert this to a list

    private ArrayList<View> paletteArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        styledAttributes = this.getTheme().obtainStyledAttributes(
                new int[]{
                        android.R.attr.actionBarSize});
        mActionBarSize = (int) styledAttributes.getDimension(0,0);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        final ViewGroup fabContainer = (ViewGroup) findViewById(R.id.sdpallete);
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.palette_parent);

        collapseFAB(fabContainer, paletteArrayList, rl);

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

    public void collapseFAB(final ViewGroup fabContainer,
                            final ArrayList<View> fabArrayList,
                            RelativeLayout rl){

        //TODO I think we can remove this part
        for (int i=0; i < rl.getChildCount(); i++) {
            fabArrayList.add(rl.getChildAt(i));
        }

        fabContainer.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw(){
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                for(int i = 0; i < fabArrayList.size() - 1; i++){
                    float offset = fabArrayList.get(i).getY() - fabArrayList.get(i + 1).getY();
                    fabArrayList.get(i + 1).setTranslationY(offset);
                }
                return true;
            }
        });
    }
}
