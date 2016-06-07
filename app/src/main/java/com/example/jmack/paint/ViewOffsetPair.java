package com.example.jmack.paint;

import android.view.View;

/**
 * Created by jmack on 6/1/16.
 */
public class ViewOffsetPair {
    public View view;
    public float offset;

    public ViewOffsetPair(View view, float offset) {
        this.view = view;
        this.offset = offset;
    }

    public View getView() {
        return this.view;
    }

    public float getOffset() {
        return this.offset;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }
}
