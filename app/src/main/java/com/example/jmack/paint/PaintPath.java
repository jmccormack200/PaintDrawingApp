package com.example.jmack.paint;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by jmack on 6/2/16.
 */
public class PaintPath {
    public Paint paint;
    public Path path;

    public PaintPath(Paint paint, Path path){
        this.paint = paint;
        this.path = path;
    }

    public Path getPath(){
        return this.path;
    }

    public Paint getPaint(){
        return this.paint;
    }

    public void setPath(Path path){
        this.path = path;
    }

    public void setPaint(Paint paint){
        this.paint = paint;
    }
}
