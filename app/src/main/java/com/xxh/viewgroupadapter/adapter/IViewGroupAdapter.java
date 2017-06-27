package com.xxh.viewgroupadapter.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;


/**
 */

public interface IViewGroupAdapter  {
    /**
     * ViewGroup调用获取ItemView
     *
     * @param parent
     * @param pos
     * @return
     */
    View getView(ViewGroup parent, int pos);

    /**
     * ViewGroup调用，得到ItemCount
     *
     * @return
     */
    int getCount();

    /**
     * 用户调用，刷新ViewGroup界面
     */
    void notifyDatasetChanged();

    void registerDataSetObserver(DataSetObserver dataSetObserver);

    void unregisterDataSetObserver(DataSetObserver dataSetObserver);
}
