package com.example.jmack.paint;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawView mDrawView;

    private TypedArray styledAttributes;
    private int mActionBarSize;

    private boolean collapsePal = true;
    private ViewGroup paletteContainer;
    private View rootPalette;

    //TODO: Convert this to a list

    private ArrayList<View> paletteArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        styledAttributes = this.getTheme().obtainStyledAttributes(
                new int[]{
                        android.R.attr.actionBarSize});
        mActionBarSize = (int) styledAttributes.getDimension(0,0);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        paletteContainer = (ViewGroup) findViewById(R.id.sdpallete);
        rootPalette = paletteContainer.getChildAt(paletteContainer.getChildCount() -1);
        initCollapseFAB(paletteContainer, paletteArrayList);


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

    public void initCollapseFAB(final ViewGroup fabContainer,
                                final ArrayList<View> fabArrayList){

        //TODO I think we can remove this part
        for (int i=0; i < fabContainer.getChildCount(); i++) {
            fabArrayList.add(fabContainer.getChildAt(i));
        }

        fabContainer.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw(){
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                for(int i = (fabArrayList.size() - 1); i > 0; i--){
                    Log.v("HELP", String.valueOf(i));
                    float offset = fabArrayList.get(i).getY() - fabArrayList.get(i - 1).getY();
                    fabArrayList.get(i - 1).setTranslationY(offset);
                }
                return true;
            }
        });
    }

    public void onClickExpand(View view){
        collapsePal = !collapsePal;
        if (collapsePal) {
            expandFAB();
        } else {
            //collapseFAB();
        }
    }

    private static final String TRANSLATION_Y = "translationY";

    private Animator createCollapseAnimator(View view, float offset){
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset){
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 1)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private void expandFAB(){
        AnimatorSet animatorSet = new AnimatorSet();

        ArrayList<Animator> animatorArrayList = new ArrayList<>();
        animatorSet.playTogether();
    }
}


