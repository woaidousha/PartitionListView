package org.bean.widget;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by liuyulong on 15-2-10.
 */
public class DragLayout extends FrameLayout {

    private GestureDetectorCompat mGestureDetector;
    private ViewDragHelper mDragHelper;
    private ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View view, int i) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.d("lyl", "child : " + child.getId() + ", left :" + left + ", dx : " + dx);
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - child.getWidth();
            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            return newLeft;
        }
    };

    private VelocityTracker mVelocityTracker;

    public DragLayout(Context context) {
        super(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d("lyl", "distanceX : " + distanceX + ", distanceY :" + distanceY);
                return Math.abs(distanceX) > Math.abs(distanceY);
            }
        });
        mVelocityTracker = VelocityTracker.obtain();
        mDragHelper = ViewDragHelper.create(this,100.0f, mDragCallback);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        boolean should = mDragHelper.shouldInterceptTouchEvent(ev);
        boolean ontouch = mGestureDetector.onTouchEvent(ev);
        return should || ontouch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        return false;
    }
}
