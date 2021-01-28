package com.luo.slidingscollview;

/**
 * author：lg on 2021/1/27 15:28
 * desc: 横向手势滑动控件，子view有button需clickable="false"
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class SlidingScollView extends LinearLayout {
    private static final String TAG = "SlidingScollView";
    private int lastX;
    private int lastY;
    private int firstX;
    private int xLenght;
    private int mTranslationLenght = 400;//位移长度
    private GestureDetector mGestureDetector;
    private OnClickListener mClickListener;
    private boolean mAlreadyMove;//是否已经手动滑动
    private int mScreenWidth;
    private int mScreenHeight;

    public SlidingScollView(Context context) {
        super(context);
        init(context);
    }

    public SlidingScollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlidingScollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 单击事件
     */
    public void setOnSingleTapUp(@Nullable OnClickListener l) {
        this.mClickListener = l;
    }

    private void init(Context context) {

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            //当手指按下的时候触发下面的方法
            @Override
            public boolean onDown(MotionEvent e) {
                firstX = (int) e.getRawX();//获取第一次触摸事件触摸位置的原始X坐标
                lastX = (int) e.getRawX();//获取触摸事件触摸位置的原始X坐标
                lastY = (int) e.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }

            //当手指在屏幕上轻轻点击的时候触发下面的方法
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.w(TAG, "onSingleTapUp");
                if (mClickListener != null) {
                    mClickListener.onClick(SlidingScollView.this);
                }
                return true;
            }

            //当手指在屏幕上滚动的时候触发这个方法
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (e1.getX() < e2.getX()) {//向右滑动
                    dispathEvent(e2);
                }
                return true;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
       /* if (!mAlreadyMove) {//防止父控件调用requestLayout()方法后，该view回到初始位置
            super.onLayout(changed, l, t, r, b);
        }*/

    }


    private void moveLayout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }
        //拖动完成后恢复到原位，单击事件不执行
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (xLenght >= mTranslationLenght) {//位移400回调
                clickActionUp.onClickActionUp();
            }
            requestLayout();
        }
        return true;
    }

    private void dispathEvent(MotionEvent event) {
        int ea = event.getAction();
        switch (ea) {
            case MotionEvent.ACTION_MOVE:
                //event.getRawX();获得移动的位置
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                xLenght = (int) event.getRawX() - firstX;
                int l = this.getLeft() + dx;
                int b = this.getBottom();
//                int b = this.getBottom() + dy;//可上下滑动
                int r = getRight() + dx;
                int t = getTop();
//                int t = getTop() + dy;//可上下滑动
                //下面判断移动是否超出屏幕
                /*if (l < 0) {
                    l = 0;
                    r = l + this.getWidth();
                }
                if (t < 0) {
                    t = 0;
                    b = t + this.getHeight();
                }
               if (r > mScreenWidth) {
                    r = mScreenWidth;
                    l = r - this.getWidth();
                }
                if (b > mScreenHeight) {
                    b = mScreenHeight;
                    t = b - this.getHeight();
                }*/
                if (!mAlreadyMove) {//判断是否已经随手势滑动
                    if (Math.abs(dx) > 30 || Math.abs(dy) > 30) {
                        mAlreadyMove = true;
                    }
                }
                moveLayout(l, t, r, b);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;

        }
    }

    public interface OnClickActionUp {
        void onClickActionUp();
    }

    private OnClickActionUp clickActionUp;

    public void setClickActionUp(OnClickActionUp clickActionUp) {
        this.clickActionUp = clickActionUp;
    }


}