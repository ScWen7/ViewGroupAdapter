package com.xxh.viewgroupadapter.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xxh.viewgroupadapter.R;
import com.xxh.viewgroupadapter.adapter.BaseAdapter;
import com.xxh.viewgroupadapter.adapter.OnItemClickListener;
import com.xxh.viewgroupadapter.adapter.OnItemLongClickListener;
import com.xxh.viewgroupadapter.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ll_horizontal;
    private List<TestBean> mDatas;
    private BaseAdapter<TestBean> mBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_horizontal = (LinearLayout) findViewById(R.id.ll_horizontal);

        initDatas();

        mBaseAdapter = new BaseAdapter<TestBean>(this, mDatas, R.layout.item_horizontal) {
            @Override
            protected void onBindViewHolder(ViewGroup parent, ViewHolder holder, TestBean item, final int pos) {
                holder.setImageResource(R.id.iv_icon, item.getImageId())
                        .setText(R.id.tv_title, item.getName())
                        .setOnClickListener(R.id.btn_test, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "按钮点击事件：" + pos, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        };
        mBaseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View itemView, int position) {
                Toast.makeText(MainActivity.this, "Item 的点击事件", Toast.LENGTH_SHORT).show();
            }
        }).setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(ViewGroup parent, View itemView, int position) {
                Toast.makeText(MainActivity.this, "Item的长按事件", Toast.LENGTH_SHORT).show();
                return true;
            }
        }).bindViews(ll_horizontal);

    }

    private void initDatas() {
        mDatas = new ArrayList<>();

        TestBean testBean = new TestBean(R.mipmap.image1, "美女1");
        mDatas.add(testBean);
        testBean = new TestBean(R.mipmap.image2, "美女2");
        mDatas.add(testBean);
        testBean = new TestBean(R.mipmap.image3, "美女3");
        mDatas.add(testBean);
        testBean = new TestBean(R.mipmap.image4, "美女4");
        mDatas.add(testBean);
        testBean = new TestBean(R.mipmap.image5, "美女5");
        mDatas.add(testBean);
        testBean = new TestBean(R.mipmap.image6, "美女6");
        mDatas.add(testBean);
        testBean = new TestBean(R.mipmap.image7, "美女7");
        mDatas.add(testBean);
        testBean = new TestBean(R.mipmap.image8, "美女8");
        mDatas.add(testBean);

    }

    public void textRefresh(View view) {
        TestBean testBean = new TestBean(R.mipmap.image1, "美女1");
        mDatas.add(testBean);
        testBean = new TestBean(R.mipmap.image2, "美女2");
        mDatas.add(testBean);
        testBean = new TestBean(R.mipmap.image3, "美女3");
        mDatas.add(testBean);
        mBaseAdapter.notifyDatasetChanged();
    }


    public void bindViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        for(int i = 0; i < mDatas.size(); i++) {
            TestBean item = mDatas.get(i);
            //初始化视图
            View view = layoutInflater.inflate(R.layout.item_horizontal,ll_horizontal,false);
            //初始化操作控件
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            Button  btn_test = (Button) view.findViewById(R.id.btn_test);
            //绑定数据
            iv_icon.setImageResource(item.getImageId());
            tv_title.setText(item.getName());
            //点击事件
            btn_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //item 的点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //item 的长按事件
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });

        }
    }
}
