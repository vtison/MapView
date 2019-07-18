package com.onlylemi.mapview.library.layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.Marker;
import com.onlylemi.mapview.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * MarkLayer
 *
 * @author: onlylemi
 */
public class MarkLayer extends MapBaseLayer {

    private List<Marker> marks;
    private MarkIsClickListener listener;

    private Bitmap bmpMark, bmpMarkTouch;

    private float radiusMark;
    private boolean isClickMark = false;
    private int num = -1;
    private boolean replaceMarkIconOnClick = false;
    private boolean isResizing = false;

    private Paint paint;
    /** Contains the associated Rect for each marks draw in the map. Used to find click event on all the mark bitmap */
    private ArrayList<Rect> mBitmapRectList;

    public MarkLayer(MapView mapView) {
        this(mapView, null);
    }

    public MarkLayer(MapView mapView, List<Marker> marks) {
        super(mapView);
        this.marks = marks;

        initLayer();
    }

    private void initLayer() {
        radiusMark = setValue(10f);

        bmpMark = BitmapFactory.decodeResource(mapView.getResources(), R.mipmap.mark);
        bmpMarkTouch = BitmapFactory.decodeResource(mapView.getResources(), R.mipmap.mark_touch);
        mBitmapRectList = new ArrayList<>(marks.size());
        for (int i = 0; i < marks.size(); i++)
            mBitmapRectList.add(null);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (marks != null) {
            if (!marks.isEmpty()) {
                for (int i = 0; i < marks.size(); i++) {
                    if (mBitmapRectList.get(i).contains((int) event.getX(), (int) event.getY())){
                        num = i;
                        isClickMark = true;
                        break;
                    }

                    if (i == marks.size() - 1) {
                        isClickMark = false;
                    }
                }
            }

            if (listener != null) {
                if (isClickMark)
                    listener.markIsClick(num);
                mapView.refresh();
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float
            currentRotateDegrees) {
        if (isVisible && marks != null) {
            canvas.save();
            if (!marks.isEmpty()) {
                for (int i = 0; i < marks.size(); i++) {
                    Bitmap bmpMarkUse, bmpMarkTouchUse;
                    PointF mark = marks.get(i).getPoint();
                    float[] goal = {mark.x, mark.y};
                    currentMatrix.mapPoints(goal);

                    if (marks.get(i).getBtmp() != null)
                        bmpMarkUse = marks.get(i).getBtmp();
                    else
                        bmpMarkUse = bmpMark;

                    if (marks.get(i).getBtmpSelect() != null)
                        bmpMarkTouchUse = marks.get(i).getBtmpSelect();
                    else
                        bmpMarkTouchUse = bmpMarkTouch;


                    paint.setColor(Color.BLACK);
                    paint.setTextSize(radiusMark);
                    //mark name
                    if (mapView.getCurrentZoom() > 1.0 && marks != null) {
                        canvas.drawText(marks.get(i).getLabel(), goal[0] - radiusMark, goal[1] -
                                radiusMark / 2, paint);
                    }
                    Rect tpmBitmapRect;
                    if (isResizing){
                        if (replaceMarkIconOnClick) {
                            if (i == num && isClickMark) {
                                tpmBitmapRect = new Rect((int) (goal[0] - bmpMarkTouchUse.getWidth() * currentZoom / 3),
                                        (int) (goal[1] - bmpMarkTouchUse.getHeight() * currentZoom), (int) (goal[0] + 2 * bmpMarkTouchUse.getWidth() * currentZoom / 3), (int) (goal[1]));
                                canvas.drawBitmap(bmpMarkTouchUse, null, tpmBitmapRect, paint);
                            } else {
                                tpmBitmapRect = new Rect((int) (goal[0] - bmpMarkUse.getWidth() * currentZoom / 3),
                                        (int) (goal[1] - bmpMarkUse.getHeight() * currentZoom), (int) (goal[0] + 2 * bmpMarkUse.getWidth() * currentZoom / 3), (int) goal[1]);
                                canvas.drawBitmap(bmpMarkUse, null, tpmBitmapRect, paint);
                            }
                            mBitmapRectList.set(i, tpmBitmapRect);
                        } else {
                            tpmBitmapRect = new Rect((int) (goal[0] - bmpMarkUse.getWidth() * currentZoom / 3), (int) (goal[1] - bmpMarkUse.getHeight() * currentZoom / 2),
                                    (int) (goal[0] + 2 * bmpMarkUse.getWidth() * currentZoom / 3), (int) (goal[1] + bmpMarkUse.getHeight() * currentZoom /2));
                            canvas.drawBitmap(bmpMarkUse, null, tpmBitmapRect, paint);
                            mBitmapRectList.set(i, tpmBitmapRect);

                            if (i == num && isClickMark) {
                                canvas.drawBitmap(bmpMarkTouchUse, null, new Rect((int) (goal[0] - bmpMarkTouchUse.getWidth() * currentZoom / 3),
                                        (int) (goal[1] - bmpMarkTouchUse.getHeight() * currentZoom), (int) (goal[0] + 2 * bmpMarkTouchUse.getWidth() * currentZoom / 3), (int) (goal[1])), paint);
                            }
                        }//replaceIconOnClick
                    } else {
                        if (replaceMarkIconOnClick) {
                            if (i == num && isClickMark) {
                                tpmBitmapRect = new Rect((int) goal[0] - bmpMarkTouchUse.getWidth() / 3, (int) goal[1] - bmpMarkTouchUse.getHeight(),
                                        (int) (goal[0] + 2 * bmpMarkTouchUse.getWidth() / 3), (int) (goal[1]));
                                canvas.drawBitmap(bmpMarkTouchUse, null, tpmBitmapRect, paint);
                            } else {
                                tpmBitmapRect = new Rect((int) goal[0] - bmpMarkTouchUse.getWidth() / 3, (int) goal[1] - bmpMarkTouchUse.getHeight(),
                                        (int) (goal[0] + 2 * bmpMarkTouchUse.getWidth() / 3), (int) (goal[1]));
                                canvas.drawBitmap(bmpMarkUse, null, tpmBitmapRect, paint);
                            }
                            mBitmapRectList.set(i, tpmBitmapRect);
                        } else {
                            tpmBitmapRect = new Rect((int) (goal[0] - bmpMarkUse.getWidth() / 3), (int) (goal[1] - bmpMarkUse.getHeight() / 2),
                                    (int) (goal[0] + 2 * bmpMarkUse.getWidth() / 3), (int) (goal[1] + bmpMarkUse.getHeight() / 2));
                            canvas.drawBitmap(bmpMarkUse, null, tpmBitmapRect, paint);
                            mBitmapRectList.set(i, tpmBitmapRect);

                            if (i == num && isClickMark) {
                                canvas.drawBitmap(bmpMarkTouchUse, goal[0] - (float) bmpMarkTouchUse.getWidth() / 3,
                                        goal[1] - bmpMarkTouchUse.getHeight(), paint);
                            }
                        }//replaceIconOnClick
                    }//isResizing
                }//for
            }//if !mark.isEmpty()
            canvas.restore();
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Marker> getMarks() {
        return marks;
    }

    public void setMarks(List<Marker> marks) {
        this.marks = marks;
    }

    public boolean isClickMark() {
        return isClickMark;
    }

    public void setMarkIsClickListener(MarkIsClickListener listener) {
        this.listener = listener;
    }

    public interface MarkIsClickListener {
        void markIsClick(int num);
    }

    /**
     * @brief Allow us to change the mark icon by another bitmap
     * @param bmpMark The new bitmap file we want as mark icon
     *
     * @author vtison
     */
    public void setBmpMark(Bitmap bmpMark){
        this.bmpMark = bmpMark;
    }

    /**
     * @brief Allow us to change the mark touch icon by another bitmap
     * @param bmpMarkTouch The new bitmap file we want as mark touch icon
     *
     * @author vtison
     */
    public void setBmpMarkTouch(Bitmap bmpMarkTouch){
        this.bmpMarkTouch = bmpMarkTouch;
    }

    /**
     * @brief Enable/Disable the replacement of mark icon when it's touch or not
     * @param replaceMarkIconOnClick if false, the mark touch icon will be displayed with mark icon,
     *                               else, only one will be displayed (either mark icon, either mark touch icon)
     *
     * @author vtison
     */
    public void setReplaceMarkIconOnClick(boolean replaceMarkIconOnClick){
        this.replaceMarkIconOnClick = replaceMarkIconOnClick;
    }

    /**
     * @brief allow to set click mark at false
     *
     * @author vtison
     */
    public void setUnclickMark(){
        isClickMark = false;
    }

    /**
     * @brief Allow to set resize for marks
     * @param isResizing true to dynamically resize marks, else false and marks got always the same size
     *
     * @author vtison
     */
    public void setResizing(boolean isResizing){
        this.isResizing = isResizing;
    }

    /**
     * @brief Return if we resize the marks or not
     * @return true if the marks are dynamically resize, else false
     *
     * @author vtison
     */
    public boolean isResizing(){
        return isResizing;
    }
}
