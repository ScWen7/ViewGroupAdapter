package com.xxh.viewgroupadapter.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;




public interface IViewGroupAdapter  {

    View getView(ViewGroup parent, int pos);


    int getCount();

    void notifyDatasetChanged();

    void registerDataSetObserver(DataSetObserver dataSetObserver);

    void unregisterDataSetObserver(DataSetObserver dataSetObserver);
}
