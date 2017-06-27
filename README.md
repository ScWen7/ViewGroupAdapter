# ViewGroupAdapter

##  动态的向Viewgroup添加布局，采用adapter 适配器模式

 ### 在开发过程中，经常会需要动态的向 ViewGroup 控件中添加 子布局 通过循环  `addView()`的形式来完成操作

类似于这样的界面，一个横向滑动的视图，外层控件使用的是 HorizontalScrollView 子布局为 LinearLayout

![示例](http://oqe10cpgp.bkt.clouddn.com/image/viewgroupadapter/shot.jpeg)

布局代码如下：

```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:orientation="vertical"
    tools:context="com.xxh.viewgroupadapter.demo.MainActivity">

    <HorizontalScrollView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
      >
        <LinearLayout
            android:id="@+id/ll_horizontal"
            android:layout_width="wrap_content"
            android:orientation="horizontal"
            android:layout_height="wrap_content"></LinearLayout>
    </HorizontalScrollView>

    <Button
        android:text="测试刷新"
        android:onClick="textRefresh"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
</LinearLayout>

```

每一个item子视图的layout如下：

```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="160dp"
              android:layout_height="300dp"
              android:layout_marginLeft="10dp"
              android:layout_marginRight="10dp"
              android:gravity="center_horizontal"
              android:orientation="vertical">

    <ImageView
        android:id="@+id/iv_icon"
        android:layout_width="150dp"
        android:layout_height="220dp"
        android:scaleType="centerCrop"/>

    <TextView
        android:id="@+id/tv_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="测试文本"/>

    <Button
        android:id="@+id/btn_test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="点击我"/>
</LinearLayout>
```

接着就是向ll_horizontal控件中添加子View

```java
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
```

看似这段代码没什么问题，但是如果 Item 中有多个控件需要绑定数据，绑定点击事件，就需要我们写很多重复的 代码，本着打死不重复的原则，对其进行封装

参考List View 的形式进行Adapter 封装，三个角色 ：ViewGroup控件，Adapter ,ViewHolder

Adapter 提供 数据与视图的绑定，ViewHolder 提供视图的创建和视图的基本操作



