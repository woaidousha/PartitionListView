package org.bean.partition;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class Partition extends BaseAdapter {

    public static final class FixedViewInfo {
        public View view;
        public Object data;
        public boolean isSelectable;
    }

    private boolean mHidden;
    private final ArrayList<FixedViewInfo> mHeaderView = new ArrayList<FixedViewInfo>();
    private final ArrayList<FixedViewInfo> mFooterView = new ArrayList<FixedViewInfo>();

    public int getHeadersCount() {
        return mHeaderView.size();
    }

    public View getHeaderView(int position) {
        if (position < 0 || position >= mHeaderView.size()) {
            return null;
        }
        return mHeaderView.get(position).view;
    }

    public View getFooterView(int position) {
        if (position < 0 || position >= mFooterView.size()) {
            return null;
        }
        return mFooterView.get(position).view;
    }

    public int getFootersCount() {
        return mFooterView.size();
    }

    public void addHeaderView(View v) {
        addHeaderView(v, null, false);
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        removeHeader(v);
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        mHeaderView.add(info);
    }

    public void addFooterView(View v) {
        addFooterView(v, null, false);
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        removeFooter(v);
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        mFooterView.add(info);
    }

    public boolean removeHeader(View v) {
        for (int i = 0; i < mHeaderView.size(); i++) {
            FixedViewInfo info = mHeaderView.get(i);
            if (info.view == v) {
                mHeaderView.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean removeFooter(View v) {
        for (int i = 0; i < mFooterView.size(); i++) {
            FixedViewInfo info = mFooterView.get(i);
            if (info.view == v) {
                mFooterView.remove(i);
                return true;
            }
        }

        return false;
    }

    public void setHidden(boolean flag) {
        mHidden = flag;
    }

    public boolean isHidden() {
        return mHidden;
    }

    public int getCount(int partition) {
        if (mHidden) {
            return 0;
        }
        return getCount() + getHeadersCount() + getFootersCount();
    }

    public int getItemViewType(int partition, int position) {
        int numHeaders = getHeadersCount();
        if (position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = getCount();
            if (adjPosition < adapterCount) {
                return getItemViewType(adjPosition);
            }
        }
        // if the item is a header or footer, do not remove move views that should be ignored.
        // so needs AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER.
        return AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
    }

    public int getViewTypeCount(int partition) {
        return getViewTypeCount();
    }

    public boolean isEnabled(int partition, int position) {
        // Header
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return mHeaderView.get(position).isSelectable;
        }

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = getCount();
        if (adjPosition < adapterCount) {
            return isEnabled(adjPosition);
        }

        // Footer
        return mFooterView.get(adjPosition - adapterCount).isSelectable;
    }

    public Object getItem(int partition, int position) {
        // header
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return mHeaderView.get(position).data;
        }

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = getCount();
        if (adjPosition < adapterCount) {
            return getItem(adjPosition);
        }

        // Footer
        return mFooterView.get(adjPosition - adapterCount).data;
    }

    public long getItemId(int partition, int position) {
        return getItemId(position);
    }

    public View getView(int partition, int position, View convertView, ViewGroup parent) {
        // Header
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            View v1 = mHeaderView.get(position).view;
            bindHeaderView(position, v1, parent);
            return v1;
        }

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = getCount();
        if (adjPosition < adapterCount) {
            return getView(adjPosition, convertView, parent);
        }

        // Footer
        View v2 = mFooterView.get(adjPosition - adapterCount).view;
        bindFooterView(position, v2, parent);
        return v2;
    }

    protected void bindHeaderView(int position, View view, ViewGroup parent) {
    }

    protected void bindFooterView(int position, View view, ViewGroup parent) {
    }

    public abstract void clear();
}
