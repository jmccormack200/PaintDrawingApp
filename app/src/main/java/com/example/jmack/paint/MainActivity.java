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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * The main activity lays out the UI and sets up the animations.
 */
public class MainActivity extends AppCompatActivity {

    private static final boolean COLLAPSE = true;
    private static final float strokeWidth = 8.0f;
    private static final String TRANSLATION_Y = "translationY";
    private static final String TRANSLATION_X = "translationX";


    private boolean collapsePal = COLLAPSE;
    private ArrayList<ViewOffsetPair> paletteArrayList = new ArrayList<>();

    private boolean collapseBrush = COLLAPSE;
    private ArrayList<ViewOffsetPair> brushArrayList = new ArrayList<>();

    private float mStartLocation;

    private String mTranslation;

    @InjectView(R.id.drawview)
    DrawView mDrawView;

    @InjectView(R.id.sdbrush)
    ViewGroup brushContainer;

    @InjectView(R.id.sdpallete)
    ViewGroup paletteContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mTranslation = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
                TRANSLATION_Y : TRANSLATION_X;

        initCollapseFAB(paletteContainer, paletteArrayList);
        initCollapseFAB(brushContainer, brushArrayList);


    }


    public void initCollapseFAB(final ViewGroup fabContainer,
                                final ArrayList<ViewOffsetPair> fabArrayList) {


        View rootView = fabContainer.getChildAt(0);
        mStartLocation = Objects.equals(mTranslation, TRANSLATION_Y) ? rootView.getY() : rootView.getX();

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
                            ViewOffsetPair viewOffsetPair =
                                    new ViewOffsetPair(fabContainer.getChildAt(i - 1), offset);
                            fabArrayList.add(viewOffsetPair);
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


    private void collapseExpandSpeedDial(ArrayList<ViewOffsetPair> viewOffsetPairArrayList, boolean collapse) {
        AnimatorSet animatorSet = new AnimatorSet();

        ArrayList<Animator> animatorArrayList = new ArrayList<>();
        for (int i = 0; i < viewOffsetPairArrayList.size(); i++) {
            Animator animator = collapseExpandAnimator(
                    viewOffsetPairArrayList.get(i).getView(),
                    viewOffsetPairArrayList.get(i).getOffset(),
                    collapse);
            animatorArrayList.add(animator);
        }
        animatorSet.playTogether(animatorArrayList);
        animatorSet.start();
    }

    private Animator collapseExpandAnimator(View view, float offset, boolean collapse) {
        float start = (collapse) ? mStartLocation : offset;
        float end = (collapse) ? offset : mStartLocation;
        return ObjectAnimator.ofFloat(view, mTranslation, start, end)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    public void undoLastLineDrawn(View view) {
        mDrawView.clear();
    }

}


