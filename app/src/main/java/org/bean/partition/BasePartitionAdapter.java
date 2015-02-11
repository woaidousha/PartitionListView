package org.bean.partition;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BasePartitionAdapter extends BaseAdapter {

    private static final int INITIAL_CAPACITY = 2;
    private Partition[] mPartitions;
    private int mSize = 0;
    private int mCount = 0;
    private int mViewTypeCount = 0;
    private boolean mCacheValid = true;

    public BasePartitionAdapter() {
        mPartitions = new Partition[INITIAL_CAPACITY];
    }

    public void addPartition(Partition partition) {
        if (mSize >= mPartitions.length) {
            int newCapacity = mSize + 2;
            Partition[] newPartitions = new Partition[newCapacity];
            System.arraycopy(mPartitions, 0, newPartitions, 0, mSize);
            mPartitions = newPartitions;
        }

        partition.registerDataSetObserver(mDataSetObserver);

        mPartitions[mSize] = partition;
        mCount += partition.getCount(mSize);
        mViewTypeCount += partition.getViewTypeCount(mSize);
        mSize++;

        notifyDataChanged();
    }

    public int getPositionForPartition(int partitionIndex) {
        ensureCacheValid();
        int position = 0;
        for (int i = 0; i < partitionIndex; i++) {
            position += mPartitions[i].getCount(i);
        }
        return position;
    }

    private void ensureCacheValid() {
        if (mCacheValid) {
            return;
        }

        mCount = 0;
        mViewTypeCount = 0;
        for (int i = 0; i < mSize; i++) {
            mCount += mPartitions[i].getCount(i);
            mViewTypeCount += mPartitions[i].getViewTypeCount(i);
        }

        mCacheValid = true;
    }

    @Override
    public int getCount() {
        ensureCacheValid();
        return mCount;
    }

    @Override
    public Object getItem(int position) {
        ensureCacheValid();
        int start = 0;
        for (int i = 0; i < mPartitions.length; i++) {
            int end = start + mPartitions[i].getCount(i);
            if (position >= start && position < end) {
                return mPartitions[i].getItem(i, position - start);
            }
            start = end;
        }

        throw new ArrayIndexOutOfBoundsException(position);
    }

    @Override
    public long getItemId(int position) {
        ensureCacheValid();
        int start = 0;
        for (int i = 0; i < mPartitions.length; i++) {
            int end = start + mPartitions[i].getCount(i);
            if (position >= start && position < end) {
                return mPartitions[i].getItemId(i, position - start);
            }
            start = end;
        }

        throw new ArrayIndexOutOfBoundsException(position);
    }

    @Override
    public int getViewTypeCount() {
        ensureCacheValid();
        return mViewTypeCount;
    }

    @Override
    public int getItemViewType(int position) {
        ensureCacheValid();
        int start = 0;
        int viewTypeOffset = 0;
        for (int i = 0; i < mPartitions.length; i++) {
            int end = start + mPartitions[i].getCount(i);
            if (position >= start && position < end) {
                int itemViewType = mPartitions[i].getItemViewType(i, position - start);
                // if type is negative number, it should be ignored.
                if (itemViewType >= 0) itemViewType += viewTypeOffset;
                return itemViewType;
            }
            viewTypeOffset += mPartitions[i].getViewTypeCount(i);
            start = end;
        }

        throw new ArrayIndexOutOfBoundsException(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ensureCacheValid();
        int start = 0;
        for (int i = 0; i < mPartitions.length; i++) {
            int end = start + mPartitions[i].getCount(i);
            if (position >= start && position < end) {
                return mPartitions[i].getView(i, position - start, convertView, parent);
            }
            start = end;
        }

        throw new ArrayIndexOutOfBoundsException(position);
    }

    @Override
    public boolean isEnabled(int position) {
        ensureCacheValid();
        int start = 0;
        for (int i = 0; i < mPartitions.length; i++) {
            int end = start + mPartitions[i].getCount(i);
            if (position >= start && position < end) {
                return mPartitions[i].isEnabled(i, position - start);
            }
            start = end;
        }

        throw new ArrayIndexOutOfBoundsException(position);
    }

    public void notifyDataChanged() {
        invalidate();
        if (getCount() > 0) {
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }
    }

    public void invalidate() {
        mCacheValid = false;
    }

    private DataSetObserver mDataSetObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            notifyDataChanged();
        }

        @Override
        public void onInvalidated() {
            notifyDataChanged();
        }
    };

    public void clear() {
        for (int i = 0; i < mPartitions.length; i++) {
            mPartitions[i].clear();
        }
        notifyDataChanged();
    }
}
