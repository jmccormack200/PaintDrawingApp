package com.example.jmack.paint;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The main activity lays out the UI and sets up the animations.
 *
 * @author John McCormack
 * @version 1.0
 * @since 2016-06-06
 */
public class MainActivity extends AppCompatActivity {

    private DrawView mDrawView;

    private static final boolean COLLAPSE = true;
    private static final float strokeWidth = 8.0f;
    private static final String TRANSLATION_Y = "translationY";
    private static final String TRANSLATION_X = "translationX";


    private boolean collapsePal = COLLAPSE;
    private ArrayList<ViewOffsetHolder> paletteArrayList = new ArrayList<>();

    private boolean collapseBrush = COLLAPSE;
    private ViewGroup brushContainer;
    private ArrayList<ViewOffsetHolder> brushArrayList = new ArrayList<>();

    private String mTranslation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mTranslation = TRANSLATION_Y;
        } else {
            mTranslation = TRANSLATION_X;
        }

        ViewGroup paletteContainer = (ViewGroup) findViewById(R.id.sdpallete);
        initCollapseFAB(paletteContainer, paletteArrayList);

        brushContainer = (ViewGroup) findViewById(R.id.sdbrush);
        initCollapseFAB(brushContainer, brushArrayList);


        mDrawView = (DrawView) findViewById(R.id.drawview);
    }


    public void initCollapseFAB(final ViewGroup fabContainer,
                                final ArrayList<ViewOffsetHolder> fabArrayList) {

        fabContainer.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = (fabContainer.getChildCount() - 1); i > 0; i--) {
                            View button = fabContainer.getChildAt(i);
                            View prevButton = fabContainer.getChildAt(i - 1);
                            float offset;
                            if (Objects.equals(mTranslation, TRANSLATION_Y)) {
                                offset = button.getY() - prevButton.getY();
                                prevButton.setTranslationY(offset);
                            } else {
                                offset = button.getX() - prevButton.getX();
                                prevButton.setTranslationX(offset);
                            }
                            ViewOffsetHolder viewOffsetHolder =
                                    new ViewOffsetHolder(fabContainer.getChildAt(i - 1), offset);
                            fabArrayList.add(viewOffsetHolder);
                        }
                        return true;
                    }
                });
    }

    public void onClickPalette(View view) {
        collapsePal = !collapsePal;
        collapseExpandSpeedDial(paletteArrayList, collapsePal);
    }

    public void onClickBrush(View view) {
        collapseBrush = !collapseBrush;
        collapseExpandSpeedDial(brushArrayList, collapseBrush);
    }

    public void onClickColor(View view) {
        ColorStateList colorStateList = view.getBackgroundTintList();
        assert colorStateList != null;
        mDrawView.changeColor(colorStateList.getDefaultColor());
        onClickPalette(view);
    }

    //TODO: I'd like to take another look at this as the solution feels hacked together.
    public void onClickBrushSize(View view) {
        for (int i = 0; i < brushContainer.getChildCount(); i++) {
            if (brushContainer.getChildAt(i) == view) {
                mDrawView.changeStrokeWidth(i * strokeWidth);
            }
        }
        onClickBrush(view);
    }

    private Animator collapseExpandAnimator(View view, float offset, boolean collapse) {
        float start = (collapse) ? 0.0f : offset;
        float end = (collapse) ? offset : 0.0f;
        return ObjectAnimator.ofFloat(view, mTranslation, start, end)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private void collapseExpandSpeedDial(ArrayList<ViewOffsetHolder> viewOffsetHolderArrayList, boolean collapse) {
        AnimatorSet animatorSet = new AnimatorSet();

        ArrayList<Animator> animatorArrayList = new ArrayList<>();
        for (int i = 0; i < viewOffsetHolderArrayList.size(); i++) {
            Animator animator = collapseExpandAnimator(
                    viewOffsetHolderArrayList.get(i).getView(),
                    viewOffsetHolderArrayList.get(i).getOffset(),
                    collapse);
            animatorArrayList.add(animator);
        }
        animatorSet.playTogether(animatorArrayList);
        animatorSet.start();
    }

    public void clearAll(View view){
        mDrawView.clear();
    }

}


