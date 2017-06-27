package com.xxh.viewgroupadapter.adapter;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**

 */

public abstract class BaseAdapter<T> implements IViewGroupAdapter {
    protected List<T> mDatas;
    protected Context mContext;

    protected DataSetObservable mDataSetObservable;

    private int mItemViewId;


    ViewGroup mParent;

    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;



    DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            refreshUI();
        }

        @Override
        public void onInvalidated() {
        }
    };



    public BaseAdapter(Context context, List<T> datas, int itemViewId) {
        mDatas = datas;
        mContext = context;
        this.mItemViewId = itemViewId;
        mDataSetObservable = new DataSetObservable();
    }

    @Override
    public void notifyDatasetChanged() {
        mDataSetObservable.notifyChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        mDataSetObservable.registerObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        mDataSetObservable.unregisterObserver(dataSetObserver);
    }

    /**
     * ViewGroup调用获取ItemView,create bind一起做
     *
     * @param parent
     * @param pos
     * @return
     */
    @Override
    public View getView(ViewGroup parent, int pos) {
        //实现getView
        ViewHolder holder = onCreateViewHolder(parent, pos);
        onBindViewHolder(parent, holder, mDatas.get(pos), pos);
        return holder.itemView;

    }

    protected abstract void onBindViewHolder(ViewGroup parent, ViewHolder holder, T item, int pos);

    private ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
        return ViewHolder.carateViewHolder(mContext, parent, mItemViewId, pos);
    }

    /**
     * ViewGroup调用，得到ItemCount
     *
     * @return
     */
    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public BaseAdapter setDatas(List<T> datas) {
        mDatas = datas;
        return this;
    }


    public void bindViews(ViewGroup parent) {
      this.mParent = parent;
        mDataSetObservable.unregisterAll();
        mParent = parent;
        registerDataSetObserver(mDataSetObserver);
        refreshUI();
    }

    private BaseAdapter<T> refreshUI() {
        if (mParent == null ) {
            return this;
        }
        mParent.removeAllViews();
        //Step 2, begin add views
        int count = getCount();
        for (int i = 0; i < count; i++) {
            //Get itemView by adapter
            final View itemView = getView(mParent, i);
            mParent.addView(itemView);
            itemView.setTag(i);

            //Step 3 (Optional),
            //If item has not set click listener before, add click listener for each item.
            //If in refresh , reset click listener
            if (null != mOnItemClickListener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer tag = (Integer) v.getTag();
                        mOnItemClickListener.onItemClick(mParent,v,tag);
                    }
                });
            }
            if (null != mOnItemLongClickListener) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Integer tag = (Integer) v.getTag();
                        return mOnItemLongClickListener.onItemLongClick(mParent,v,tag);
                    }
                });
            }

        }
        return this;
    }


    public BaseAdapter<T> setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public BaseAdapter<T> setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
        return this;
    }
}
