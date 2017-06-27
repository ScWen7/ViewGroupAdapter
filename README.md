# ViewGroupAdapter

##  动态的向Viewgroup添加布局，采用adapter 适配器模式

 ### 在开发过程中，经常会需要动态的向 ViewGroup 控件中添加 子布局 通过循环  `addView()`的形式来完成操作

类似于这样的界面，一个横向滑动的视图，外层控件使用的是 HorizontalScrollView 子布局为 LinearLayout

![示例](http://oqe10cpgp.bkt.clouddn.com/image/viewgroupadapter/shot.jpeg)

### 布局代码如下：

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

### 每一个item子视图的layout如下：

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

### 接着就是向ll_horizontal控件中添加子View

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

### Adapter 父类接口为：

```java
public interface IViewGroupAdapter  {

    View getView(ViewGroup parent, int pos); //获取视图

    int getCount(); //获取数据 count

    void notifyDatasetChanged();  

    void registerDataSetObserver(DataSetObserver dataSetObserver);

    void unregisterDataSetObserver(DataSetObserver dataSetObserver);
}
```

### 实现类BaseAdapter代码如下：

```java
public abstract class BaseAdapter<T> implements IViewGroupAdapter {
    protected List<T> mDatas; //数据源
    protected Context mContext; 

    protected DataSetObservable mDataSetObservable;

    private int mItemViewId;  //layoutId

    ViewGroup mParent; //需要绑定的ViewGroup对象

    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;


    DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
          //数据源发生变化时，刷新界面
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
     * 获取视图
     *
     * @param parent
     * @param pos
     * @return
     */
    @Override
    public View getView(ViewGroup parent, int pos) {
        //实现getView
        ViewHolder holder = onCreateViewHolder(parent, pos);
        convert(parent, holder, mDatas.get(pos), pos);
        return holder.itemView;

    }

    protected abstract void convert(ViewGroup parent, ViewHolder holder, T item, int pos);

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
        notifyDatasetChanged();
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
        int count = getCount();
        for (int i = 0; i < count; i++) {

            final View itemView = getView(mParent, i);
            mParent.addView(itemView);
            itemView.setTag(i);


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

```

### ViewHolder代码如下：

```java
public class ViewHolder {
    public  View itemView;

    private SparseArray<View> mChilds;

    private Context mContext;



    public static  ViewHolder carateViewHolder(Context context, ViewGroup parent, int itemViewId, int pos){

        return  new ViewHolder( context,  parent,  itemViewId, pos);
    }

    public ViewHolder(Context context, ViewGroup parent, int itemViewId, int pos) {
        this.mContext = context;
        this.mChilds = new SparseArray<>();
        itemView = LayoutInflater.from(context).inflate(itemViewId, parent, false);
    }

    public <V extends View> V findViewById(int id) {
        return getView(id);
    }

    public <V extends View> V getView(int id) {
        View child = mChilds.get(id);
        if (child == null) {
            child = itemView.findViewById(id);
            mChilds.put(id, child);
        }
        return (V) child;
    }

  
  //-------------------------------------------------------分割线-----------------------------------------------------------------------
  //--------------------------------------------------以下的代码与逻辑无关，提供了视图的基本操作---------------------------------------------
    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setSelected(int viewId, boolean selected) {
        View v = getView(viewId);
        v.setSelected(selected);
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(itemView.getContext().getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public ViewHolder setOnClickListener(int viewId,
                                         View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId,
                                         View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId,
                                             View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
```

### 封装过后使用的代码如下：

```java
 mBaseAdapter = new BaseAdapter<TestBean>(this, mDatas, R.layout.item_horizontal) {
            @Override
            protected void convert(ViewGroup parent, ViewHolder holder, TestBean item, final int pos) {
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
```

### 这时候使用起来是不是很爽？再也不用写那么多的重复代码了



