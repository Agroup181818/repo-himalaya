# repo-himalaya
这是一个仓库-喜马拉雅仿

连接测试

this is a test

yu test

# git强制覆盖本地代码和强制推送本地到远程仓库

1.git强制覆盖本地文件（与git远程仓库保持一致）：
git fetch --all
git reset --hard origin/master
git pull

已完成进度P37
=======
配置build.gradle 中阿里镜像

```java
// 添加阿里云 maven 地址
maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
```

配置build.gradle:app 中依赖

```
implementation files('libs/TingPhoneOpenSDK_7.1.6.jar')
implementation files('libs/XmDownload_1.7.jar')
implementation files('libs/XmlyOAuth2SDK_V1.11.jar')
implementation 'com.google.code.gson:gson:2.8.1'
implementation 'com.squareup.okhttp3:okhttp:3.11.0'
implementation 'com.squareup.okio:okio:1.15.0'
implementation 'androidx.legacy:legacy-support-v4:1.0.0'
```



##### 喜马拉雅集成sdk

从喜马拉雅sdk平台下载最新版sdk、参考开发文档

初始化接口-设置app_secret(密钥) 

并获取每个类别categroies

```java
Map<String, String> map = new HashMap<>();
CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {   
    @Override    
    public void onSuccess(@Nullable CategoryList categoryList) {        
        List<Category> categories = categoryList.getCategories();        
        if (categories != null) {            
            int size = categories.size();            
            Log.d(TAG, "categories size ---- <<" + size);            
            for (Category category : categories) {               
                Log.d(TAG, "category ------>" + category.getCategoryName());            
            }        
        }   
    }   
    @Override    
    public void onError(int code, String message) {        
        Log.d(TAG , "error code --" + code + "error message" + message);    
    }});
```


###### 问题：安卓SDK升级后，V4包会找不到报错,更新后android.support.v4.view.ViewPager被androidx.viewpager.widget.ViewPager所取代，所有关于v4的包都要替换掉。

解决方案：修改XML文件中的android.support.v4.view.ViewPager为androidx.viewpager.widget.ViewPager。删除引入的所有关于v4的包，设置Android Studio自动导包，自动补全包即可。

###### 问题：因为android版本27以上http明文请求不被允许、访问喜马拉雅接口被拒绝，callback 回调连接失败，错误代码eor406。

解决方案：在res目录下新建xml/network_security_config.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>    
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
```
###### 问题: AndroidX变化												

解决方案：android.support.v7.widget.RecyclerView  
---->androidx.recyclerview.widget.RecyclerView

并在Manifests声明 android:roundIcon="@mipmap/ic_launcher_round"

增加Logutil工具类、方便管理打印日志、在BaseApplication.java中添加

```
//初始化 false 打印log ; true 取消打印
logLogUtil.init(this.getPackageName(),false);
```

使用github上 MagicIndictor 实现导航栏

方法：indicator+Viewpager+fragment

```java
mMagicIndicator = this.findViewById(R.id.main_indicator);mMagicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));
//创建indicator的适配器
IndicatorAdapter adapter = new IndicatorAdapter(this);CommonNavigator commonNavigator = new CommonNavigator(this);commonNavigator.setAdapter(adapter);
//viewPager
mContentPager = this.findViewById(R.id.content_pager);
//创建内容适配器
FragmentManager supportFragmentManager = getSupportFragmentManager();MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);mContentPager.setAdapter(mainContentAdapter);//把viewPager和indicator绑定在一起
mMagicIndicator.setNavigator(commonNavigator);ViewPagerHelper.bind(mMagicIndicator, mContentPager);
```

MainContent适配器设置对应的Fragment，通过utils->FragmentCreator生成

```java
public final static int INDEX_RECOMMEND =0;
public final static int INDEX_SUBSCRIPTION=1;
public final static int INDEX_HISTORY=2;
public final static int PAGE_COUNT=3;
private  static Map<Integer, BaseFragment> sCache=new HashMap<>();
public  static BaseFragment getFragment(int index){    
    BaseFragment baseFragment = sCache.get(index);    
    if (baseFragment != null) {        
        return baseFragment;    
    }    
    switch (index) {        
        case INDEX_RECOMMEND:            
            baseFragment=new RecommendFragment();            
            break;        
        case INDEX_SUBSCRIPTION:            
            baseFragment=new SubscriptionFragment();            
            break;        
        case INDEX_HISTORY:            
            baseFragment =new HistoryFragment();            
            break;    
    }    
    sCache.put(index, baseFragment);    
    return baseFragment;}
```

配置Indicator的监听事件，实现点击切换、在IndicatorAdapter中暴露一个接口OnIndicatorTapClickListener。



//P10 获取内容数据

导包 RecyclerView

```
implementation 'com.android.support:recyclerview-v7:29.0.0-rc01'
```

//P11 推荐UI 显示

导包picasso

```
implementation 'com.squareup.picasso:picasso:2.5.2'
```


配置圆角矩阵的样式
```
public class RoundRectImageView extends AppCompatImageView {

    private float roundRatio = 0.1f;
    private Path path;

    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (path == null) {
            path = new Path();
            path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), roundRatio * getWidth(), roundRatio * getHeight(), Path.Direction.CW);
        }
        canvas.save();
        canvas.clipPath(path);
        super.onDraw(canvas);
        canvas.restore();
    }
}
```

用到了一个工具类UIUtil
```
public final class UIUtil {

    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
```

获取从喜马拉雅SDK获取数据、返回状态有四种情况：成功、网络错误、内容为空、正在加载

使用UILoader类实现四种情况的切换。

```
private void switchUIByCurrentStatus() {    
//加载中    
if (mLoadingView == null) {        
mLoadingView = getLoadingView();        
addView(mLoadingView);    
}    
// 根据状态设置可见    
mLoadingView.setVisibility(mCurrentStatus == UIStatus.LOADING ? VISIBLE : GONE);    
//成功    
if (mSuccessView == null) {        
mSuccessView = getSuccessView(this);        
addView(mSuccessView);    
}    
// 根据状态设置可见    
mSuccessView.setVisibility(mCurrentStatus == UIStatus.SUCCESS ? VISIBLE : GONE);    
//网络错误页面    
if (mNetworkErrorView == null) {        
mNetworkErrorView = getNetWorkErrorView();        addView(mNetworkErrorView);    
}    
// 根据状态设置可见    
mNetworkErrorView.setVisibility(mCurrentStatus == UIStatus.NETWORK_ERROE ? VISIBLE : GONE);    
//数据为空界面    
if (mEmptyView == null) {        
mEmptyView = getEmptyView();        
addView(mEmptyView);    
}    
// 根据状态设置可见    
mEmptyView.setVisibility(mCurrentStatus == UIStatus.EMPTY ? VISIBLE : GONE);}
```

如果出现网络错误、加载初始化页面时、为UI设置监听器、实现点击重新获取数据。



//p16 UILoading加载中界面

编写LoadingView类，在LoadingView类中实现图标的旋转

```
@Override
protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    mNeedRotate = true;
    //绑定到windows的时候
    post(new Runnable() {
        @Override
        public void run() {
            rotateDegree += 30;
            rotateDegree = rotateDegree <= 360 ? rotateDegree:0;
            invalidate();
            //是否继续旋转
            if(mNeedRotate){
                postDelayed(this,100);
            }

        }
    });
}
```

P17 UILoader空页面

当页面为空时显示页面

```
<LinearLayout
    android:layout_width="wrap_content"
    android:orientation="vertical"
    android:gravity="center_horizontal"
    android:layout_centerInParent="true"
    android:layout_height="wrap_content">

        <ImageView
            android:layout_width="75dp"
            android:src="@mipmap/content_empty"
            android:layout_height="75dp" />

        <TextView
            android:layout_marginTop="9dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="呜呜~ 没有内容.."/>

</LinearLayout>
```

P18 页面跳转

在RecommendListAdapter中添加监听

```
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(mItemClickListerer !=null){
            mItemClickListerer.onItemClick((Integer) view.getTag());
        }
        Log.d(TAG,"holder.itemView clicke -- > "+view.getTag());
    }
});
```

当点击时，跳转界面

```
@Override
public void onItemClick(int position) {
    //Item被点击了,跳转到详情界面
    Intent intent = new Intent(getContext(), DetailActivity.class);
    startActivity(intent);

}
```

P19 详情界面

在DetailActivity中显示专辑的标题、作者、封面信息

```
@Override
public void onAlbumLoaded(Album album) {
    if(mAlbumTitle!=null){
        mAlbumTitle.setText(album.getAlbumTitle());
    }
    if(malubmAuthor != null){
        malubmAuthor.setText(album.getAnnouncer().getNickname());
    }
    if (mlargeCover != null) {
        Picasso.with(this).load(album.getCoverUrlLarge()).into(mlargeCover);
    }
    if (msmallCover != null) {
        Picasso.with(this).load(album.getCoverUrlLarge()).into(msmallCover);
    }
}
```

详情页面

```
<LinearLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginLeft="20dp"
    android:layout_marginTop="-30dp"
    android:layout_toRightOf="@+id/viv_samll_cover"
    android:layout_below="@+id/iv_large_cover"
    android:orientation="vertical">
    <TextView
        android:layout_width="wrap_content"
        android:text="这是标题"
        android:textSize="18sp"
        android:id="@+id/tv_album_title"
        android:textColor="#ffffff"
        android:layout_height="wrap_content" />
    <TextView
        android:layout_width="wrap_content"
        android:text="这是作者"
        android:textSize="12sp"
        android:id="@+id/tv_album_author"
        android:layout_marginTop="10dp"
        android:textColor="#ffffff"
        android:layout_height="wrap_content" />
</LinearLayout>
```

P20 跟换logo

在manifests里修改logo

```
android:roundIcon="@mipmap/logo"
```


P21引入高斯模糊工具类
```
public class ImageBlur {
    public static void makeBlur(ImageView imageview, Context context) {
        BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap blurred = blurRenderScript(bitmap, 10, context); //second parametre is radius max:25
        imageview.setImageBitmap(blurred); //radius decide blur amount
    }


    private static Bitmap blurRenderScript(Bitmap smallBitmap, int radius, Context context) {
        smallBitmap = RGB565toARGB888(smallBitmap);
        Bitmap bitmap = Bitmap.createBitmap(smallBitmap.getWidth(), smallBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript renderScript = RenderScript.create(context);
        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);
        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        return bitmap;

    }

    private static Bitmap RGB565toARGB888(Bitmap img) {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }
}
```

p27 ==> 在DetailListAdapter 中实现 为每个详情页面itemUI设置数据

```java
@Overridepublic void onBindViewHolder(@NonNull InnerHolder holder, int position) {    
    //找到控件、设置数据    
    View itemView = holder.itemView;    
    //顺序ID    
    TextView orderTv = itemView.findViewById(R.id.order_text);    
    //标题    
    TextView titleTv = itemView.findViewById(R.id.detail_item_title);    //播放次数    
    TextView playCountTv = itemView.findViewById(R.id.detail_item_play_count);    
    //时长    
    TextView durationTv = itemView.findViewById(R.id.detail_item_duration);    
    //更新日期    
    TextView updateDateTv = itemView.findViewById(R.id.detail_item_update_time);    
    //设置数据    
    Track track = mdetailData.get(position);    
    orderTv.setText(position + "");    
    titleTv.setText(track.getTrackTitle());    playCountTv.setText(track.getPlayCount() + "");    
    int durationMil = track.getDuration() * 1000;    
    String duration = mDurationFormat.format(durationMil);    durationTv.setText(duration);    
    String updateTimeText = mUpdateDateFormat.format(track.getUpdatedAt());    updateDateTv.setText(updateTimeText);}
```



p28>为实现详情页DetailActivity 数据加载四种状态：正在加载、成功、数据为空、网络错误

将RecycleView 放到 Framlayout 帧布局中 作为container，并且将Framlayout.inlfater(mUiLoader) 通过判断数据的状态、改变UiLoader的状态。

最后将 mUiLoader 加载到DetailActivity中。

P29 设置item的点击事件

```
//设置item的点击事件
itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(),"you click "+position+" item",Toast.LENGTH_SHORT).show();
    }
});
```

P30 从详情列表界面跳转到播放器界面

在点击监听里设置监听事件

```
//设置item的点击事件
itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(),"you click "+position+" item",Toast.LENGTH_SHORT).show();
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick();
        }
    }
});
```

在DetailActivity里设置跳转到PlayActivity

```
@Override
public void onItemClick() {
    //TODO;跳转到播放器界面
    Intent intent = new Intent(this,PlayerActivity.class);
    startActivity(intent);
}
```

在PlayActivity里指向activity_player.xml布局文件

```
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
}
```

P31编写播放器布局界面

```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="match_parent">

    <TextView
        android:layout_marginTop="30dp"
        android:textSize="20sp"
        android:gravity="center"
        android:layout_marginLeft="10dp"
        android:layout_marginRight="20dp"
        android:maxLines="2"
        android:id="@+id/track_title"
        android:ellipsize="end"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="这是标题内容"
        />
    <androidx.viewpager.widget.ViewPager
        android:background="#ff00ff"
        android:layout_below="@+id/track_title"
        android:layout_width="match_parent"
        android:layout_marginTop="30dp"
        android:layout_marginBottom="100dp"
        android:layout_height="match_parent" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:orientation="vertical">

        <!--进度条和时间-->
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <TextView
                android:id="@+id/current_position"
                android:layout_width="50dp"
                android:layout_height="wrap_content"
                android:layout_centerVertical="true"
                android:gravity="center"
                android:text="00:00"
                android:textSize="16sp" />

            <SeekBar
                android:id="@+id/track_seek_bar"
                style="@style/Widget.AppCompat.ProgressBar.Horizontal"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_centerInParent="true"
                android:layout_marginLeft="50dp"
                android:layout_marginRight="50dp" />

            <TextView
                android:id="@+id/track_duration"
                android:layout_width="50dp"
                android:layout_height="wrap_content"
                android:layout_alignParentRight="true"
                android:layout_centerVertical="true"
                android:gravity="center"
                android:text="00:00"
                android:textSize="16sp" />

        </RelativeLayout>
        <!--播放控制-->
        <LinearLayout
            android:gravity="center_vertical"
            android:layout_marginBottom="10dp"
            android:layout_marginTop="20dp"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="horizontal">

            <ImageView
                android:layout_width="0dp"
                android:layout_weight="1"
                android:src="@mipmap/sort_descending"
                android:layout_height="20dp" />
            <ImageView
                android:layout_width="0dp"
                android:layout_weight="1"
                android:src="@mipmap/previous"
                android:layout_height="20dp" />
            <ImageView
                android:layout_width="0dp"
                android:layout_weight="1"
                android:src="@mipmap/play"
                android:layout_height="35dp" />
            <ImageView
                android:layout_width="0dp"
                android:layout_weight="1"
                android:src="@mipmap/next_press"
                android:layout_height="20dp" />
            <ImageView
                android:layout_width="0dp"
                android:layout_weight="1"
                android:src="@mipmap/player_icon_list_press"
                android:layout_height="20dp" />
        </LinearLayout>
        </LinearLayout>
</RelativeLayout>
```

P32 介绍播放器和阅读文档

P33 逻辑层接口定义

抽取接口presenter的共同代码至IBasePresenter，减少代码冗余

```
public interface IBasePresenter<T> {

    /**
     * 注册UI的回调接口
     * @param t
     */
    void registerViewCallback(T t);

    /**
     * 取消注册
     * @param t
     */
    void unregisterViewCallback(T t);

}
```

定义逻辑层接口

```
public interface IPlayerPresenter extends IBasePresenter<IPlayerCallback> {

    /**
     * 播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 上一首
     */
    void playPre();

    /**
     * 播放下一首
     */
    void playNest();

    /**
     * 切换播放类型
     * @param mode
     */
    void switchPlayMode(XmPlayListControl.PlayMode mode);

    /**
     * 获取播放列表
     */
    void getPlayList();

    /**
     * 根据节目在列表中的位置播放
     * @param index
     */
    void playByIndex(int index);

    /**
     * 切换播放进度
     * @param progress
     */
    void seekTo(int progress);
}
```

P34 播放器的UI回调接口

```
/**
 * 开始播放
 */
void onPlayStart();

/**
 * 播放暂停
 */
void onPlayPause();

/**
 * 播放停止
 */
void onPlayStop();

/**
 * 播放错误
 */
void onPlayError();

/**
 * 下一首播放
 */
void nextPlay(Track track);

/**
 * 上一首播放
 */
void prePlay();

/**
 * 播放列表数据加载成功
 * @param list
 */
void onListLoaded(List<Track> list);

/**
 * 播放器模式改变
 * @param playMode
 */
void onPlayModeChange(XmPlayListControl.PlayMode playMode);

/**
 * 季度条的改变
 * @param currentProgress
 * @param total
 */
void onProgressChange(long currentProgress,long total);

/**
 * 广告正在加载
 */
void onAdLoading();

/**
 * 广告结束
 */
void onAdFinished();
```

P35 逻辑层播放列表数据设置和测试播放

添加播放器权限

```
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>

<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
```

配置service进程

```
<service
    android:name="com.ximalaya.ting.android.opensdk.player.service.XmPlayerService"
android:process=":player"/>
```

播放

```
@Override
public void play() {
    if (isPlayListSet) {
        mPlayerManager.play();
    }
}
```

bug:

在抽取Presenter时，由于各个Presenter里的方面命名有点不一样，例如unRegisterViewCallback和unregisterViewCallback成错误。

解决方法：将各个presenter的命名统一为BasePresenter里的命名即可。

P36 添加广告物料的监听

```
mPlayerManager.addAdsStatusListener(this);
```

P37播放器状态相关的接口方法

```
//注册播放器状态相关的接口
mPlayerManager.addPlayerStatusListener(this);
```

