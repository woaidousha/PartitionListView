package org.bean.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class DragLayout extends ViewGroup {

    private static final int INDEX_CONTENT_VIEW = 0;
    private static final int INDEX_LEFT_MENU = 1;
    private static final int INDEX_RIGHT_MENU = 2;

    private static final int MIN_DRAWER_MARGIN = 64;

    private static final int[] LAYOUT_ATTRS = new int[] {
            android.R.attr.layout_gravity
    };

    private GestureDetectorCompat mGestureDetector;
    private ViewDragHelper mDragHelper;
    private ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View view, int i) {
            Log.d("lyl", "[tryCaptureView] view : " + view.getClass().getName() + ", mCapturedView:" + (view == mDragHelper.getCapturedView()) + ", result : " + (view == mLeftMenu || view == mRightMenu));
            return view == mLeftMenu || view == mRightMenu;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            Log.d("lyl", "[getViewHorizontalDragRange] child : " + child.getClass().getName());
            return child == mLeftMenu || child == mRightMenu ? child.getWidth() : 0;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.d("lyl", "[clampViewPositionHorizontal] left : " + left + ", dx:" + dx + ", child :" + child.getClass().getName());
            if (child == mLeftMenu) {
                Log.d("lyl", "[clampViewPositionHorizontal] left menu " + Math.max(-child.getWidth(), Math.min(left, 0)));
                return Math.max(-child.getWidth(), Math.min(left, 0));
            } else if (child == mRightMenu) {
                final int width = getWidth();
                Log.d("lyl", "[clampViewPositionHorizontal] right menu " + Math.max(width - child.getWidth(), Math.min(left, width)));
                return Math.max(width - child.getWidth(), Math.min(left, width));
            }
            return 0;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            Log.d("lyl", "[onEdgeDragStarted] edgeFlags :" + edgeFlags);
            if (edgeFlags == ViewDragHelper.EDGE_LEFT) {
                mDragHelper.captureChildView(mLeftMenu, pointerId);
            } else if (edgeFlags == ViewDragHelper.EDGE_RIGHT) {
                mDragHelper.captureChildView(mRightMenu, pointerId);
            }
        }

    };

    private View mContentView;
    private View mLeftMenu;
    private View mRightMenu;

    private int mMinDrawerMargin;

    public DragLayout(Context context) {
        super(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d("lyl", "distanceX : " + distanceX + ", distanceY :" + distanceY);
                return Math.abs(distanceX) > Math.abs(distanceY) * 2;
            }
        });
        mDragHelper = ViewDragHelper.create(this, 0.2f, mDragCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

        final float density = getResources().getDisplayMetrics().density;
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 3) {
            throw new IllegalStateException("child views count should less than 3");
        }

        mContentView = getChildAt(INDEX_CONTENT_VIEW);
        mLeftMenu = getChildAt(INDEX_LEFT_MENU);
        mRightMenu = getChildAt(INDEX_RIGHT_MENU);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            if (isInEditMode()) {
                if (widthMode == MeasureSpec.AT_MOST) {
                    widthMode = MeasureSpec.EXACTLY;
                } else if (widthMode == MeasureSpec.UNSPECIFIED) {
                    widthMode = MeasureSpec.EXACTLY;
                    widthSize = 300;
                }
                if (heightMode == MeasureSpec.AT_MOST) {
                    heightMode = MeasureSpec.EXACTLY;
                } else if (heightMode == MeasureSpec.UNSPECIFIED) {
                    heightMode = MeasureSpec.EXACTLY;
                    heightSize = 300;
                }
            } else {
                return;
            }
        }

        setMeasuredDimension(widthSize, heightSize);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (i == INDEX_CONTENT_VIEW) {
                int widthSpec = MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin,
                        MeasureSpec.EXACTLY);
                int heightSpec = MeasureSpec.makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin,
                        MeasureSpec.EXACTLY);
                child.measure(widthSpec, heightSpec);
            } else {
                int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                        mMinDrawerMargin + lp.leftMargin + lp.rightMargin, lp.width);
                int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.width);
                child.measure(childWidthSpec, childHeightSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = right - left;
        final int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (i == INDEX_CONTENT_VIEW) {
                child.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + child.getMeasuredWidth(),
                        lp.topMargin + child.getMeasuredHeight());
            } else if (i == INDEX_LEFT_MENU){
                child.layout(-child.getMeasuredWidth(), top + lp.topMargin, 0,
                        top + lp.topMargin + child.getMeasuredHeight());
            } else if (i == INDEX_RIGHT_MENU){
                child.layout(width, top + lp.topMargin, width + child.getWidth(),
                        top + lp.topMargin + child.getMeasuredHeight());
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (( action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        View view = mDragHelper.findTopChildUnder((int) ev.getX(), (int) ev.getY());
        int state = mDragHelper.getViewDragState();
        boolean should = mDragHelper.shouldInterceptTouchEvent(ev);
        boolean ontouch = mGestureDetector.onTouchEvent(ev);
        Log.d("lyl", "[onInterceptTouchEvent] view : " + (view == null ? " null " : view.getClass().getName()));
        Log.d("lyl", "[onInterceptTouchEvent] action:" + ev.getAction() + ",state : " + state);
        Log.d("lyl", "[onInterceptTouchEvent] should : " + should + ", ontouch : " + ontouch);
        return ontouch || should;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int state = mDragHelper.getViewDragState();
        View view = mDragHelper.getCapturedView();
        Log.d("lyl", "[onTouchEvent] view : " + (view == null ? " null " : view.getClass().getName()));
        Log.d("lyl", "[onTouchEvent] action:" + ev.getAction() + ",state : " + state);
        Log.d("lyl", "[onTouchEvent] slop:" + mDragHelper.getTouchSlop());
        mDragHelper.processTouchEvent(ev);

        return true;
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public int gravity = Gravity.NO_GRAVITY;
        float onScreen;
        boolean isPeeking;
        boolean knownOpen;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            final TypedArray a = c.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
            this.gravity = a.getInt(0, Gravity.NO_GRAVITY);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            this(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.gravity = source.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams
               ? new LayoutParams((LayoutParams) p)
               : p instanceof ViewGroup.MarginLayoutParams
                 ? new LayoutParams((MarginLayoutParams) p)
                 : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
}
