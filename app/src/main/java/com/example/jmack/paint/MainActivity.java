package com.example.jmack.paint;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawView mDrawView;

    private TypedArray styledAttributes;
    private int mActionBarSize;

    private boolean collapsePal = false;
    private ViewGroup paletteContainer;
    private ArrayList<ViewOffsetHolder> paletteArrayList = new ArrayList<>();

    private boolean collapseBrush = false;
    private ViewGroup brushContainer;
    private ArrayList<ViewOffsetHolder> brushArrayList = new ArrayList<>();

    //TODO: Convert this to a list




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
        initCollapseFAB(paletteContainer, paletteArrayList);

        brushContainer = (ViewGroup) findViewById(R.id.sdbrush);
        initCollapseFAB(brushContainer, brushArrayList);


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
                                final ArrayList<ViewOffsetHolder> fabArrayList){

        fabContainer.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw(){
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                for(int i = (fabContainer.getChildCount() - 1); i > 0; i--){

                    float offset = fabContainer.getChildAt(i).getY()
                            - fabContainer.getChildAt(i - 1).getY();
                    fabContainer.getChildAt(i - 1).setTranslationY(offset);
                    ViewOffsetHolder viewOffsetHolder =
                            new ViewOffsetHolder(fabContainer.getChildAt(i-1), offset);
                    fabArrayList.add(viewOffsetHolder);
                }
                return true;
            }
        });
    }

    public void onClickPalette(View view){
        collapsePal = !collapsePal;
        if (collapsePal) {
            expandFAB(paletteArrayList);
        } else {
            collapseFAB(paletteArrayList);
        }
    }

    public void onClickBrush(View view){
        collapseBrush = !collapseBrush;
        if (collapseBrush) {
            expandFAB(brushArrayList);
        } else {
            collapseFAB(brushArrayList);
        }
    }

    public void onClickColor(View view){
        ColorStateList colorStateList = view.getBackgroundTintList();
        mDrawView.changeColor(colorStateList.getDefaultColor());
        onClickPalette(view);
    }

    //TODO: I'd like to take another look at this as the solution feels hacked together.
    public void onClickBrushSize(View view){
        for (int i = 0; i < brushContainer.getChildCount(); i++){
            if (brushContainer.getChildAt(i) == view){
                mDrawView.changeStrokeWidth(i * 4.0f);
            }
        }
        onClickBrush(view);
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

    //TODO It should be possible to collapse these two into one function
    private void expandFAB(ArrayList<ViewOffsetHolder> viewOffsetHolderArrayList){
        AnimatorSet animatorSet = new AnimatorSet();


        ArrayList<Animator> animatorArrayList = new ArrayList<>();
        for (int i = 0; i < viewOffsetHolderArrayList.size(); i++){
            Animator animator = createExpandAnimator(
                    viewOffsetHolderArrayList.get(i).getView(),
                    viewOffsetHolderArrayList.get(i).getOffset());
            animatorArrayList.add(animator);
        }
        animatorSet.playTogether(animatorArrayList);
        animatorSet.start();
    }

    private void collapseFAB(ArrayList<ViewOffsetHolder> viewOffsetHolderArrayList){
        AnimatorSet animatorSet = new AnimatorSet();

        ArrayList<Animator> animatorArrayList = new ArrayList<>();
        for (int i = 0; i < viewOffsetHolderArrayList.size(); i++){
            Animator animator = createCollapseAnimator(
                    viewOffsetHolderArrayList.get(i).getView(),
                    viewOffsetHolderArrayList.get(i).getOffset()
            );
            animatorArrayList.add(animator);
        }
        animatorSet.playTogether(animatorArrayList);
        animatorSet.start();
    }


}


