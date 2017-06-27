package com.xxh.viewgroupadapter.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 解晓辉 on 2017/5/27.
 * 作用：
 */

public class VGUtil {
    ViewGroup mParent;
    IViewGroupAdapter mAdapter;



    DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            refreshUI();
        }

        @Override
        public void onInvalidated() {
        }
    };

    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;


    public VGUtil(ViewGroup parent, IViewGroupAdapter adapter) {
        this(parent,adapter,null,null);
    }


    public VGUtil(ViewGroup parent, IViewGroupAdapter adapter, OnItemClickListener onItemClickListener) {
        this(parent, adapter,  onItemClickListener, null);
    }

    public VGUtil(ViewGroup parent, IViewGroupAdapter adapter, OnItemLongClickListener onItemLongClickListener) {
        this(parent, adapter, null, onItemLongClickListener);
    }

    public VGUtil(ViewGroup parent, IViewGroupAdapter adapter, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        if (parent == null || adapter == null) {
            throw new IllegalArgumentException("ViewGroup or Adapter can't be null! ");
        }
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mParent = parent;
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataSetObserver);
        mOnItemClickListener = onItemClickListener;
        mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * Begin bind views for {@link #mParent}
     */
    public VGUtil bind() {
        return bind(false);
    }

    /**
     * Refresh ui for {@link #mParent}.
     * This method will reset {@link OnItemClickListener} and {@link OnItemLongClickListener}
     *
     * @return
     */
    public VGUtil refreshUI() {
        return bind(true);
    }

    private VGUtil bind(boolean refresh) {
        if (mParent == null || mAdapter == null) {
            return this;
        }
        mParent.removeAllViews();
        //Step 2, begin add views
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            //Get itemView by adapter
            final View itemView = mAdapter.getView(mParent, i);
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
}
