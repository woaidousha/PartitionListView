package org.bean.widget;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by liuyulong on 15-2-9.
 */
public class AutoLoadListView extends ListView implements AbsListView.OnScrollListener {

    public interface LoadListener {
        public void load();
    }

    private LoadListener mLoadListener;
    private ViewDragHelper mDragHelper;

    public AutoLoadListView(Context context) {
        super(context);
    }

    public AutoLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setOnScrollListener(this);
    }

    public void setLoadListener(LoadListener loadListener) {
        this.mLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
            if (mLoadListener != null) {
                mLoadListener.load();
            }
        }
    }
}
