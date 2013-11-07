package com.example.dragandzoomimage;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class TouchImageView extends ImageView {

    Matrix matrix;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int viewWidth, viewHeight;
    int mode = NONE;
    int count;
    long lastClickTime;

    PointF lastTouchPoint = new PointF();
    PointF startTouchPoint = new PointF();

    static final int MINMOVEDIS = 3;        //移动距离小于3则判定为点击
    float saveScale = 1f;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;

    ScaleGestureDetector mScaleDetector;

    Context context;

    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);        //在构造函数加入scaleListener和onTouchListener
    }

    //在调用setContentView显示TouchImageView时会调用到下面的构造函数
    public TouchImageView(Context context,AttributeSet attrs) {
        super(context,attrs);
        sharedConstructing(context);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        count = 0;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix = new Matrix();
        setImageMatrix(matrix);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                PointF currentPoint = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchPoint.set(currentPoint);
                        startTouchPoint.set(currentPoint);
                        mode = DRAG;
                        count++;
                        if(count == 1){
                            lastClickTime = System.currentTimeMillis();

                        } else if (count == 2){
                            if(System.currentTimeMillis() - lastClickTime < 200){
                                setNormalScale();
                            }
                            count = 0;
                            lastClickTime = 0;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            float deltaX = currentPoint.x - lastTouchPoint.x;
                            float deltaY = currentPoint.y - lastTouchPoint.y;
                            matrix.postTranslate(deltaX, deltaY);
                            lastTouchPoint.set(currentPoint);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        int xDis = (int) Math.abs(currentPoint.x - startTouchPoint.x);
                        int yDis = (int) Math.abs(currentPoint.y - startTouchPoint.y);
                        if (xDis < MINMOVEDIS && yDis < MINMOVEDIS)
                            performClick();
                        else
                            count = 0;
                        break;

                    case MotionEvent.ACTION_POINTER_UP:     //ACTION_POINTER_UP：当屏幕上有多个点被按住，松开其中一个点时触发
                        mode = NONE;
                        break;
                }

                setImageMatrix(matrix);
                invalidate();
                return true;
            }

        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            count = 0;
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            saveScale *= mScaleFactor;

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
            else
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());

            return true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            setNormalScale();
            setImageMatrix(matrix);
        }
    }

    private void setNormalScale(){
        float scale;
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
            return;
        int bmWidth = drawable.getIntrinsicWidth();
        int bmHeight = drawable.getIntrinsicHeight();
        float scaleX = (float) viewWidth / (float) bmWidth;
        float scaleY = (float) viewHeight / (float) bmHeight;
        scale = Math.min(scaleX, scaleY);
        matrix.setScale(scale, scale);

        float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
        float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
        redundantYSpace /= (float) 2;
        redundantXSpace /= (float) 2;

        origWidth = viewWidth - 2 * redundantXSpace;
        origHeight = viewHeight - 2 * redundantYSpace;

        matrix.postTranslate(redundantXSpace, redundantYSpace);
    }
}