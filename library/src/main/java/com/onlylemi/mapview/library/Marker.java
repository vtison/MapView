package com.onlylemi.mapview.library;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * @brief Marker element, contain point coordinates for the marker, label, and mark icons
 *
 * @author vtison
 */

public class Marker extends PointF {
    PointF mPoint = new PointF();
    String mLabel;
    Bitmap mBtmp;
    Bitmap mBtmpSelect;

    public Marker(){

    }

    public Marker(PointF pointF, String label){
        mPoint.set(pointF);
        mLabel = label;
        mBtmp = null;
        mBtmpSelect = null;
    }

    public Marker(float x, float y, String label){
        mPoint.set(x, y);
        mLabel = label;
        mBtmp = null;
        mBtmpSelect = null;
    }

    public Marker(PointF pointF, String label, Bitmap btmp, Bitmap btmpSelect){
        mPoint.set(pointF);
        mLabel = label;
        mBtmp = btmp;
        mBtmpSelect = btmpSelect;
    }

    public Marker(float x, float y, String label, Bitmap btmp, Bitmap btmpSelect){
        mPoint.set(x,y);
        mLabel = label;
        mBtmp = btmp;
        mBtmpSelect = btmpSelect;
    }

    public PointF getPoint(){
        return mPoint;
    }

    public String getLabel(){
        return mLabel;
    }

    public void setBtmp(Bitmap btmp){
        mBtmp =btmp;
    }

    public void setBtmpSelect(Bitmap btmpSelect){
        mBtmpSelect = btmpSelect;
    }

    public Bitmap getBtmp(){
        return mBtmp;
    }

    public Bitmap getBtmpSelect(){
        return mBtmpSelect;
    }
}
