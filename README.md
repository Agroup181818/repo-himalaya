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

已完成进度P110 要从107开始做                         我们做订阅，历史不做了。已经修改完成（由4个页面改为3个页面）
=======
P118BUG解决了
82集的重构代码做了，主界面播放控制没做82一半没做，83做了，84没做，85做了，之后正常敲就行了 联想词没有做
没有做的×p99,100,101,102,103,105

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

给控件设置相关事件
```
/**
     * 给控件设置相关事件
     */
    private void initEven() {
        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果现在的状态是播放的，就暂停
                if (mPlayerPresenter.isPlay()) {
                    mPlayerPresenter.pause();
                } else {
                    //如果是暂停状态，则播放
                    mPlayerPresenter.play();
                }
            }
        });

        mDurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if (isFromUser) {
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //手离开拖动进度条的时候更新进度
                mIsUserTouchProgressBar = false;
                mPlayerPresenter.seekTo(mCurrentProgress);

            }
        });

        mPlayPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放前一个节目
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });


        mPlayNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放下一个节目
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playNext();
                }
            }
        });


    }
```
P42获取标题
```
if (curModel instanceof Track) {
            Track currentTrack=(Track)curModel;
            //LogUtil.d(TAG,"title ==> "+currentTrack.getTrackTitle());
            //更新UI
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onTrackTitleUpdate(currentTrack.getTrackTitle());
            }
        }
```

P43设置适配器
```
mTrackPagerView = this.findViewById(R.id.track_pager_view);
        //创建适配器
        mTrackPagerAdapter = new PlayerTrackPagerAdapter();
        //设置适配器
        mTrackPagerView.setAdapter(mTrackPagerAdapter);
```

找到图片的链接并加载到布局上
```

public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_track_pager, container, false);
        LogUtil.d(TAG,"container.getContext()== "+container.getContext());

        container.addView(itemView);
        //设置数据
        //找到控件
        ImageView item = itemView.findViewById(R.id.track_pager_item);
        //设置图片
        Track track = mData.get(position);
        String coverUrlLarge = track.getCoverUrlLarge();
        Picasso.with(container.getContext()).load(coverUrlLarge).into(item);
        //Picasso.get( ).Load(url).into( ).
        return itemView;
    }
```

p44 在IPlayerCallback 接口中增加方法onPageSelected

并在PlayerActivity中回调、播放节目变动时，修改页面图片。

为播放页面设置监听、监听手势滑动。

```
@Override
public void onPageSelected(int position) {
    LogUtil.d(TAG , "position -------> " + position);
    //当页面被选择，就去切换播放内容
    if (mPlayerPresenter != null && mIsUserSlidePager) {
        mPlayerPresenter.playByIndex(position);
    }
    mIsUserSlidePager = false;
}
```

p45 存在bug：过早调用mPlayerManage.play()、导致如果一首节目正在播放、点击第二首无法正常播放。PlyaerManage 准备完成状态后prepared、调用play播放才正常。

解决：删除PlayerAcitivity中的play方法。

在PlayerPresenter 逻辑层 onSoundPrepared() 方法中调用play。

```
@Override
public void onSoundPrepared() {
    LogUtil.d(TAG, "onSoundPrepared..");
    if (mPlayerManager.getPlayerStatus() == PlayerConstants.STATE_PREPARED) {
        //播放器准备完成、可以去播放
        mPlayerManager.play();
    }
}
```

p46 增加动画效果

在drawable中增加点击切换icon实现动画:上一首、下一首、播放、暂停

```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@mipmap/next_pressed" android:state_pressed="true"/>
    <item android:drawable="@mipmap/next_normal"  />
</selector>
```

P47 播放模式的切换

点击事件

```
mPlayModeSwitchBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //根据当前的mode获取到下一个mode
        XmPlayListControl.PlayMode playMode = sPlayModeRule.get(mCurrentMode);
        //修改播放模式
        if (mPlayerPresenter != null) {
            mPlayerPresenter.switchPlayMode(playMode);
            mCurrentMode = playMode;
            updatePlayModeBtnImg();
        }
        mCurrentMode = playMode;
    }
});
```

图标切换

```
/**
 * 根据当前的状态，更新播放图标
 * PLAY_MODEL_LIST
 * ：PLAY_MODEL_LIST_LOOP
 * ：PLAY_MODEL_RANDOM
 * ：PLAY_MODEL_SINGLE_LOOP
 */
private void updatePlayModeBtnImg() {
    int resId = R.drawable.selector_paly_mode_list_order;
    switch (mCurrentMode){
        case PLAY_MODEL_LIST:
            resId = R.drawable.selector_paly_mode_list_order;
            break;
        case PLAY_MODEL_RANDOM:
            resId = R.drawable.selector_paly_mode_random;
            break;
        case PLAY_MODEL_LIST_LOOP:
            resId = R.drawable.selector_paly_mode_list_order_looper;
            break;
        case PLAY_MODEL_SINGLE_LOOP:
            resId = R.drawable.selector_paly_mode_single_loop;
            break;
    }
    mPlayModeSwitchBtn.setImageResource(resId);
}
```

P48 回显播放模式UI设计

呃 这个太tm乱了 不写了....

P49 回显模式播放器设置

P50 节目列表弹出

设置点击事件

```
mPlayListBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //TODO:展示播放列表
        mSobPopWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
    }
});
```

```
public SobPopWindow(){
    //设置它的宽高
    super(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    //载进来view
    View popView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null);
    //设置内容
    setContentView(popView);
}
```

P51 播放列表的关闭

```
//这里要注意，设置这个属性前要设置setBackGroundDrable
//否则点击外部无法关闭pop.
setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
setOutsideTouchable(true);
```
P52圆角列表样式
```
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">

    <corners android:topRightRadius="20dp"
        android:topLeftRadius="20dp"/>


    <solid android:color="#afafaf"/>
</shape>
```
P53处理pop窗体的透明度
```
mPlayListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:展示播放列表
                mSobPopWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                //处理一下背景，一点透明度
                updateBgAlpha(0.8f);
            }
        });
        mSobPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //pop窗体消失以后，恢复透明度
                updateBgAlpha(1.0f);

            }
        });
```

P54进入和退出的动画效果
```
<resources>

    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/main_color</item>
        <item name="colorPrimaryDark">@color/main_color</item>
        <item name="colorAccent">@color/main_color</item>
    </style>
    <style name="pop_animation" parent="android:Animation">
        <item name="android:windowEnterAnimation">
            @anim/pop_in
        </item>
        <item name="android:windowExitAnimation">
            @anim/pop_out
        </item>
    </style>
</resources>
```

pop_out.xml
```
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">

    <translate android:fromYDelta="0"
        android:toYDelta="100%"
        android:duration="300"
        />

    <alpha android:fromAlpha="1.0"
        android:duration="300"
        android:toAlpha="0.8"/>
</set>
```
P55加载动画initAnimation
```
private void initBgAnimation() {
        mEnterBgAnimator = ValueAnimator.ofFloat(1.0f, 0.7f);
        mEnterBgAnimator.setDuration(BG_ANIMATION_DURATION);
        mEnterBgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value  = (float) animation.getAnimatedValue();
                //处理一下背景，一点透明度
                updateBgAlpha(value);

            }
        });
        //退出的
        mOutBgAnimator = ValueAnimator.ofFloat(0.7f,1.0f);
        mOutBgAnimator.setDuration(BG_ANIMATION_DURATION);
        mOutBgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value  = (float) animation.getAnimatedValue();
                updateBgAlpha(value);

            }
        });
    }
```
P59列表布局设计
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:paddingTop="10dp"
    android:paddingBottom="10dp"
    android:orientation="vertical">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center_vertical"

        android:orientation="horizontal">

        <ImageView
            android:id="@+id/play_icon_iv"
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:layout_marginStart="10dp"
            android:src="@mipmap/playlist_playing_icon" />


        <TextView
            android:id="@+id/track_title_tv"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginStart="10dp"
            android:layout_marginEnd="20dp"
            android:maxLines="1"
            android:text="我是标题"
            android:textColor="@color/play_list_text_color"
            android:textSize="16sp" />
    </LinearLayout>

    <VideoView
        android:layout_width="match_parent"
        android:layout_height="1px"
        android:layout_marginTop="16dp"
        android:background="@color/play_list_line_color" />


</LinearLayout>
```


P60为列表设置位置改变的相应效果
```
public void setCurrentPlayPosition(int position){
        if (mPlayListAdapter != null) {
            //设置播放列表当前播放的位置
         mPlayListAdapter.setCurrentPlayPosition(position);

        }
    }
```

p65 实现pop列表切换播放模式：顺序循环、列表循环、随机循环、单曲循环

通过在SobPopWindow中暴露接口PlayListPlayModeClickListener实现点击切换播放模式

p66 图标icon和文字同时改变

改变播放模式方法中、实现更新图标UI和文字

```
@Override
public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {
    //更新播放模式并且修改UI
    mCurrentMode =playMode;
    //更新pop里的播放模式
    mSobPopWindow.updatePlayMode(mCurrentMode);
    updatePlayModeBtnImg();
}
```

p67 实现pop列表的顺序和逆序切换

修改SobPopWindow的接口PlayListPlayModeClickListener ==>PlayListActionListener

实现两个方法

```
public interface PlayListActionListener {
    //播放模式被点击
    void onPlayModeClick();

    //播放逆序、顺序切换按钮被点击
    void onOrderClick();
}
```

通过set 方法设置接口、并在PlayerActivity 实现点击逻辑。

```
mSobPopWindow.setPlayListActionListener(new SobPopWindow.PlayListActionListener() {
    @Override
    public void onPlayModeClick() {
        //todo:切换播放模式
        switchPlayMode();
    }

    @Override
    public void onOrderClick() {
        //点击了切换顺序和逆序
        Toast.makeText(PlayerActivity.this ,"切换列表顺序",Toast.LENGTH_SHORT).show();
        mSobPopWindow.updateOrderIcon(!testOrder);
        testOrder = !testOrder;
    }
});
```

P68播放列表顺序播放逻辑

```
@Override
public void reversePlayList() {
    //把播放列表反转
    List<Track> playList = mPlayerManager.getPlayList();
    Collections.reverse(playList);
    //第一个参数是播放列表，第二个参数是开始播放的下标
    //新的下标=总的内容个数-1-当前下标
    mCurrentIndex = playList.size()-1-mCurrentIndex;
    mPlayerManager.setPlayList(playList,mCurrentIndex);
    //跟新UI
    mCurrentTrack = (Track) mPlayerManager.getCurrSound();
    for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
        iPlayerCallback.onListLoaded(playList);
        iPlayerCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
    }
}
```

P69 专辑详情界面控制播放状态

UI

```
@Override
public void onPlayStart() {
    //开始播放，修改UI为暂停按键
    if (mControlBtn != null) {
        mControlBtn.setImageResource(R.drawable.selector_palyer_stop);
    }
}

@Override
public void onPlayPause() {
    if (mControlBtn != null) {
        mControlBtn.setImageResource(R.drawable.selector_palyer_play);
    }
}

@Override
public void onPlayStop() {
    if (mControlBtn != null) {
        mControlBtn.setImageResource(R.drawable.selector_palyer_play);
    }
}
```

控制

```
private void initListener() {
    mPlayControlBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //控制播放器的状态
            if (mPlayerPresenter.isPlaying()) {
                //正在播放，那么就暂停
                mPlayerPresenter.pause();
            }else {
                mPlayerPresenter.play();
            }
        }
    });
}
```

P70解决专辑详细默认播放状态的问题

在onCreate加入

```
updatePlayState(mPlayerPresenter.isPlaying());
```

将代码抽取，减少代码冗余

```
//根据播放状态修改图标和文字
private void updatePlayState(boolean playing) {
    if(mPlayControlBtn!=null&&mPlayControlTips!=null){
        mPlayControlBtn.setImageResource(playing?R.drawable.selector_play_control_pause:R.drawable.selector_play_control_play);
        mPlayControlTips.setText(playing?R.string.playing_tips_text:R.string.pause_tips_text);
    }
}
```

P71 详情界面播放默认内容

```
@Override
public void onClick(View view) {
    if (mPlayerPresenter != null) {
        //判断播放器是否有播放列表
        //
        boolean has = mPlayerPresenter.hasPlayList();
        if (has) {
            //控制播放器的状态
            handlePlayControl();
        }else {
            handleNoPlayList();
        }
    }
```

```
/**
 * 当播放器里面没有播放内容，我们要进行一个处理
 */
private void handleNoPlayList() {
    mPlayerPresenter.setPlayList(mCurrentTracks,DEFAULT_PLAY_INDEX);
}
```


P73添加刷新框架的依赖
```
    implementation 'com.lcodecorex:tkrefreshlayout:1.0.7'

```

更改之前的布局文件
```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
        android:id="@+id/refresh_Layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/album_detail_list"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
        </androidx.recyclerview.widget.RecyclerView>

    </com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout>


    </FrameLayout>
```
刷新事件添加监听
```
mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                BaseApplication.getsHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailActivity.this,"刷新成功...",Toast.LENGTH_SHORT).show();
                        mRefreshLayout.onFinishRefresh();
                    }
                },2000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);

                BaseApplication.getsHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailActivity.this,"开始上拉加载更多...",Toast.LENGTH_SHORT).show();
                        mRefreshLayout.onFinishLoadMore();

                    }
                },2000);


            }
        });
```
实现doLoad上拉加载更多的功能
```
private void doLoaded(final boolean isLoaderMore) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.ALBUM_ID, mCurrentAlbumId+"");
        map.put(DTransferConstants.PAGE, mCurrentPageIndex + "");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_DEFAULT + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    LogUtil.d(TAG, "tracks -->" + tracks);

                    if (isLoaderMore) {
                        //上拉加载，结果放到后面去
                    mTracks.addAll(tracks);

                    }
                    else {
                        //这个是下载加载，结果放到前面去
                        mTracks.addAll(0,tracks);
                    }
                    handlerAlbumDetailResult(mTracks);
                }
            }

            public void onError(int errorCode, String errorMsg) {
                if (isLoaderMore) {
                    mCurrentPageIndex--;
                }
                LogUtil.d(TAG, "errorCode -->  " + errorCode);
                LogUtil.d(TAG, "errorMsg -->  " + errorMsg);
                handleError(errorCode, errorMsg);
            }
        });

        }
```

实现跑马灯效果
```
android:ellipsize="marquee"
            android:marqueeRepeatLimit="marquee_forever"
```

根据播放状态修改名字
```
//根据播放状态修改图标和文字
    private void updatePlayState(boolean playing) {
        if(mPlayControlBtn!=null&&mPlayControlTips!=null){

            mPlayControlBtn.setImageResource(playing?R.drawable.selector_play_control_pause:R.drawable.selector_play_control_play);

            if (!playing) {
                mPlayControlTips.setText(R.string.click_play_tips_text);
            } else {
                if (!TextUtils.isEmpty(mCurrentTrackTitle)) {
                    mPlayControlTips.setText(mCurrentTrackTitle);

                }

            }

        }
    }
```
详情界面处理刷新结果
```
public void onLoaderMoreFinished(int size) {
        if (size > 0) {
            Toast.makeText(this, "成功加载" + size + "条节目", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "没有更多节目", Toast.LENGTH_SHORT).show();
        }
    }
```

P79-81 增加主界面底部的播放条、编写布局页面、init 控件。

```
//播放控制相关的
mRoundRectImageView = this.findViewById(R.id.main_track_cover);
mHeaderTitle = this.findViewById(R.id.main_head_title);
mHeaderTitle.setSelected(true);
mSubTitle = this.findViewById(R.id.main_sub_title);
mPlayControl = this.findViewById(R.id.main_play_control);
```

为播放条注册修改UI的接口、结束时销毁

```
private void initPresenter() {
    mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
    mPlayerPresenter.registerViewCallback(this)

}
```

```
@Override
protected void onDestroy() {
    super.onDestroy();
    if (mPlayerPresenter != null) {
        mPlayerPresenter.unRegisterViewCallback(this);
    }
}
```

播放歌曲时、为控件设置数据

```
@Override
public void onTrackUpdate(Track track, int playIndex) {
    if (track != null) {
        String trackTitle = track.getTrackTitle();
        String nickname = track.getAnnouncer().getNickname();
        String coverUrlMiddle = track.getCoverUrlMiddle();
        if (mHeaderTitle != null) {
            mHeaderTitle.setText(trackTitle);
        }
        if (mSubTitle != null) {
            mSubTitle.setText(nickname);
        }
        Picasso.with(this).load(coverUrlMiddle).into(mRoundRectImageView);

        LogUtil.d(TAG, "trackTitle -----> " + trackTitle);
        LogUtil.d(TAG, " nickname-----> " + nickname);
        LogUtil.d(TAG, "coverUrlMiddle -----> " + coverUrlMiddle);
    }
}
```

实现播放条上 播放、暂停 按钮

```
mPlayControl.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (mPlayerPresenter != null) {
            if (mPlayerPresenter.isPlaying()) {
                mPlayerPresenter.pause();
            } else {
                mPlayerPresenter.play();
            }
        }
    }
});
```
P82重构代码 14:00以后的功能没有做

```
package com.example.himalaya.api;

import com.example.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.HashMap;
import java.util.Map;

public class XimalayApi {
    private XimalayApi() {
    }

    private static XimalayApi sXimalayApi;

    public static XimalayApi getXimalayapi() {
        if (sXimalayApi == null) {
            synchronized (XimalayApi.class) {
                if (sXimalayApi == null) {
                    sXimalayApi = new XimalayApi();
                }
            }
        }
        return sXimalayApi;
    }

    /**
     * 获取推荐的内容
     *
     * @param callBack 请求结果的回调接口
     */
    public void getRecommendList(IDataCallBack<GussLikeAlbumList> callBack) {
        Map<String, String> map = new HashMap<>();
        //这个参数表示一页数据多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getGuessLikeAlbum(map, callBack);
    }


    /**
     * 根据专辑的id获取到专辑的内容
     *
     * @param callback  获取到专辑的回调接口
     * @param albumId   专辑的id
     * @param pageIndex 第几页
     */
    public void getAlbumDetail(IDataCallBack<TrackList> callback, long albumId, int pageIndex) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.PAGE, pageIndex + "");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_DEFAULT + "");
        CommonRequest.getTracks(map, callback);
    }

}

```


P83修复播放页面    播放按键和进度条的bug
```
    private void handlerPlayState(IPlayerCallback iPlayerCallback) {
        int playerStatus = mPlayerManager.getPlayerStatus();
        //根据状态调用接口的方法
        if (PlayerConstants.STATE_STARTED == playerStatus) {
            iPlayerCallback.onPlayStart();
        } else {
            iPlayerCallback.onPlayPause();
        }
    }
    
   public void registerViewCallback(IPlayerCallback iPlayerCallback) {

        //通知当前节目
        iPlayerCallback.onTrackUpdate(mCurrentTrack , mCurrentIndex);
        //更新状态栏progress
        iPlayerCallback.onProgressChange(mCurrentProgressPosition,mPrograssDuration);
        //更新状态
        handlerPlayState(iPlayerCallback);
        //从SP里拿
        int modeIndex = mPlayModSp.getInt(PLAY_MODE_SP_KEY, PLAY_MODEL_LIST_INT);
        mCurrentPlayMode = getModeByInt(modeIndex);
        iPlayerCallback.onPlayModeChange(mCurrentPlayMode);
        if (!mIPlayerCallbacks.contains(iPlayerCallback)) {
            mIPlayerCallbacks.add(iPlayerCallback);
        }
    }
```

P85搜索框的布局
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:gravity="center_vertical"
        android:orientation="horizontal">


        <ImageView
            android:layout_width="30dp"
            android:layout_height="26dp"
            android:layout_marginStart="10dp"
            android:layout_marginEnd="10dp"
            android:src="@drawable/selector_back_btn" />


        <EditText
            android:layout_width="0dp"
            android:layout_height="40dp"
            android:layout_weight="1"
            android:background="@drawable/shape_edit_text_bg" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="10dp"
            android:layout_marginEnd="10dp"
            android:paddingStart="6dp"
            android:paddingEnd="6dp"
            android:text="搜索"
            android:textColor="@color/second_color"
            android:textSize="15sp" />

    </LinearLayout>

    <FrameLayout
        android:id="@+id/search_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </FrameLayout>
</LinearLayout>
```

P86 修改搜索界面的UI

```
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:gravity="center_vertical"
    android:orientation="horizontal">


    <ImageView
        android:layout_width="40dp"
        android:layout_height="20dp"
        android:paddingLeft="10dp"
        android:paddingRight="10dp"
        android:src="@drawable/selector_back_btn" />


    <EditText
        android:layout_width="0dp"
        android:layout_height="40dp"
        android:paddingLeft="10dp"
        android:hint="请输入专辑关键字"
        android:textCursorDrawable="@drawable/shape_edit_text_cursor"
        android:singleLine="true"
        android:paddingRight="10dp"
        android:textSize="16sp"
        android:layout_weight="1"
        android:background="@drawable/shape_edit_text_bg" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="10dp"
        android:layout_marginEnd="10dp"
        android:paddingStart="6dp"
        android:paddingEnd="6dp"
        android:text="搜索"
        android:textColor="@color/main_color"
        android:textSize="18sp" />

</LinearLayout>
```

P87 定义搜索接口

ISearchPresenter:

```
/**
 * 进行搜素
 * @param keyword
 */
void doSearch(String keyword);

/**
 * 重新搜素
 */
void reSearch();

/**
 * 加载更多
 */
void loadMore();

/**
 * 获取热词
 */
void getHotWord();

/**
 * 获取推荐的关键字（相关的关键字）
 * @param keyword
 */
void getRecommendWord(String keyword);
```

callback:

```
/**
 * 搜索结果的回调方法
 * @param result
 */
void onSearchResultLoaded(List<Album> result);

/**
 * 获取推荐热词的结果回调
 * @param hotWords
 */
void onHotWordLoaded(List<HotWord> hotWords);

/**
 * 加载更多的结果返回
 * @param result 结果
 * @param isOkay true表示加载更多成功，false表示没有更多
 */
void onLoadMoreResult(List<Album> result,boolean isOkay);

/**
 * 联想关键字的结果回调方法
 * @param keyWordList
 */
void onRecommendWordLoaded(List<QueryResult> keyWordList);
```

P88 实现搜索逻辑

```
/**
 * 根据关键词进行搜素
 * @param keyword
 */
public void searchByKeyWord(String keyword, int page, IDataCallBack<SearchAlbumList> callBack) {
    Map<String, String> map = new HashMap<>();
    map.put(DTransferConstants.SEARCH_KEY, keyword);
    map.put(DTransferConstants.PAGE, page+"");
    map.put(DTransferConstants.PAGE_SIZE,Constants.COUNT_DEFAULT+"");
    CommonRequest.getSearchedAlbums(map, callBack);
}
```

```
@Override
public void doSearch(String keyword) {
    //用于得重新搜素
    //当网络不好的时候，用户会点击重新搜素
    this.mCurrentKeyWord = keyword;
    mXimalayapi.searchByKeyWord(keyword, mCurrenPage, new IDataCallBack<SearchAlbumList>() {
        @Override
        public void onSuccess(SearchAlbumList searchAlbumList) {
            List<Album> albums = searchAlbumList.getAlbums();
            if (albums != null) {
                LogUtil.d(TAG,"album size -- > " +albums.size());
            }else {
                LogUtil.d(TAG,"album si null..");
            }
        }

        @Override
        public void onError(int errorCode, String errorMsg) {
            LogUtil.d(TAG,"errorCode -- > " +errorCode);
            LogUtil.d(TAG,"errorMsg -- > " +errorMsg);
        }
    });
}
```

P89 实现热词搜索逻辑

```
@Override
public void getHotWord() {
    mXimalayapi.getHotWords(new IDataCallBack<HotWordList>() {
        @Override
        public void onSuccess(HotWordList hotWordList) {
            if (hotWordList != null) {
                List<HotWord> hotWords = hotWordList.getHotWordList();
                LogUtil.d(TAG,"hotWords size -- > "+ hotWords.size());
            }
        }

        @Override
        public void onError(int errorCode, String errorMsg) {
            LogUtil.d(TAG,"getHotWord errorCode -- > " +errorCode);
            LogUtil.d(TAG,"getHotWord errorMsg -- > " +errorMsg);
        }
    });
}

@Override
public void getRecommendWord(final String keyword) {
    mXimalayapi.getSuggestWord(keyword, new IDataCallBack<SuggestWords>() {
        @Override
        public void onSuccess(SuggestWords suggestWords) {
            if (suggestWords != null) {
                List<QueryResult> keyWordList = suggestWords.getKeyWordList();
                LogUtil.d(TAG,"keyWordList size -- > "+keyWordList.size());
            }
        }

        @Override
        public void onError(int errorCode, String errorMsg) {
            LogUtil.d(TAG,"getHotWord errorCode -- > " +errorCode);
            LogUtil.d(TAG,"getHotWord errorMsg -- > " +errorMsg);
        }
    });
}
```

P90 UI控件的初始化

```
private void initView(){
    mBackBtn = this.findViewById(R.id.search_back);
    mInputBox = this.findViewById(R.id.search_input);
    mSearchBtn = this.findViewById(R.id.search_btn);
    mResultContainer = this.findViewById(R.id.search_container);
}
```

```
private void initEvent() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //去调用搜索的逻辑
            }
        });
        mInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                LogUtil.d(TAG,"content --> "+charSequence);
//                LogUtil.d(TAG,"start --> "+start);
//                LogUtil.d(TAG,"before --> "+before);
//                LogUtil.d(TAG,"count --> "+count);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
```

P91 获取推荐的热词

```
@Override
public void onHotWordLoaded(List<HotWord> hotWordList) {
    LogUtil.d(TAG,"hotWordList -- > " + hotWordList.size());
    List<String> hotWords= new ArrayList<>();
    hotWords.clear();
    for (HotWord hotWord : hotWordList) {

        String searchWord = hotWord.getSearchword();
        hotWords.add(searchWord);
    }
    //更新UI，
    mFlowTextLayout.setTextContents(hotWords);
}
```

P92 搜索热词点击效果

导入各种文件 等等等等。。。
P94隐藏键盘
```
//隐藏键盘
        InputMethodManager imm= (InputMethodManager) this. getSystemService(Context. INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputBox.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
```
P96发起搜索
```
//1.把热词放到输入框里
                mInputBox.setText(text);
                mInputBox.setSelection(text.length());
                //2.发起搜索
                if (mSearchPresenter != null) {
                    mSearchPresenter.doSearch(text);
                }
                //改变UI状态
                if (mUILoader != null) {
                    mUILoader.updateStatus(UILoader.UIStatus.LOADING);
                }
```
P104 搜索结果进入详情页面
```
public void onItemClick(int position, Album album) {
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        //Item被点击了,跳转到详情界面
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }
```
P106解决搜索关键词为空的问题
```
if (TextUtils.isEmpty(keyword)) {
                    //提示
                    Toast.makeText(SearchActivity.this,"搜索关键词不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
```
