package org.bean.partition;

import java.util.ArrayList;

public abstract class GridPartition<T> extends Partition {

    public static final int DEFAULT_COLUMN = 2;

    public ArrayList<T> mDatas;
    public boolean mHasMore = true;
    private int mColumn;

    public GridPartition() {
        this(DEFAULT_COLUMN);
    }

    public GridPartition(int column) {
        mDatas = new ArrayList<T>();
        mColumn = column;
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        int size = mDatas.size();
        return size == 0 ? 0 : (size - 1) / mColumn + 1;
    }

    @Override
    public ArrayList<T> getItem(int position) {
        int index = position * mColumn;
        T entry = mDatas.get(index);
        ArrayList<T> entries = new ArrayList<T>();
        entries.add(entry);
        for (int i = entries.size(); i < mColumn; i++) {
            if (++index < mDatas.size()) {
                entries.add(mDatas.get(index));
            }
        }
        return entries;
    }

    public int getColumn() {
        return mColumn;
    }

    public void appendData(ArrayList<T> data) {
        mDatas.addAll(data);
    }

    @Override
    public void clear() {
        mDatas.clear();
        mHasMore = true;
    }

    public int getDataCount() {
        return mDatas.size();
    }

    public boolean hasMore() {
        return mHasMore;
    }

    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
    }
}
