package com.example.himalaya;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.adapters.DetailListAdapter;
import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.interfaces.IPlayerCallback;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.presenters.PlayerPresenter;
import com.example.himalaya.utils.ImageBlur;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.RoundRectImageView;
import com.example.himalaya.views.UILoader;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, UILoader.OnRetrayClickListener, DetailListAdapter.ItemClickListener, IPlayerCallback {

    private static final String TAG = "DetailActivity";
    private ImageView mlargeCover;
    private RoundRectImageView msmallCover;
    private TextView mAlbumTitle;
    private TextView malubmAuthor;
    private AlbumDetailPresenter mAblumDetailPresenter;
    private int mCurrentPage=1;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;
    private FrameLayout mDetailListContainer;
    private UILoader mUiLoader;
    private long mCurrentId = -1;
    private ImageView mPlayControlBtn;
    private TextView mPlayControlTips;
    private PlayerPresenter mPlayerPresenter;
    private List<Track> mCurrentTracks = null;
    private final static int DEFAULT_PLAY_INDEX = 0;
    private TwinklingRefreshLayout mRefreshLayout;
    private String mCurrentTrackTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        //这个是专辑详情的Presenter
        mAblumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAblumDetailPresenter.registerViewCallback(this);
        //播放器的Presenter.
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        updatePlayState(mPlayerPresenter.isPlaying());
        initListener();

    }

    private void initListener() {
        mPlayControlBtn.setOnClickListener(new View.OnClickListener() {
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

            }

            /**
             * 当播放器里面没有播放内容，我们要进行一个处理
             */
            private void handleNoPlayList() {
                mPlayerPresenter.setPlayList(mCurrentTracks,DEFAULT_PLAY_INDEX);
            }
            private void handlePlayControl() {
                if (mPlayerPresenter.isPlaying()) {
                    //正在播放，那么就暂停
                    mPlayerPresenter.pause();
                }else {
                    mPlayerPresenter.play();
                }
            }
        });
    }

    private void initView() {
        mDetailListContainer = this.findViewById(R.id.detail_list_container);
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(mUiLoader);
            mUiLoader.setOnRetrayListener(DetailActivity.this);
        }
        mlargeCover = this.findViewById(R.id.iv_large_cover);
        msmallCover = this.findViewById(R.id.viv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        malubmAuthor = this.findViewById(R.id.tv_album_author);
        //播放控制的图标
        mPlayControlBtn = this.findViewById(R.id.detail_play_control);
        mPlayControlTips = this.findViewById(R.id.play_control_tv);
        mPlayControlTips.setSelected(true);
    }

    private boolean mIsLoaderMore=false;


    private View createSuccessView(ViewGroup container) {
        View detailListView = LayoutInflater.from(this).inflate(R.layout.item_detail_list ,container , false);
        mDetailList = detailListView.findViewById(R.id.album_detail_list);
        mRefreshLayout = detailListView.findViewById(R.id.refresh_Layout);
        //Recycleview的使用步骤
        //1.设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDetailList.setLayoutManager(layoutManager);
        //2.设置适配器
        mDetailListAdapter = new DetailListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        //3.设置item上下间距
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect. top= UIUtil. dip2px(view. getContext(), 2);
                outRect. bottom=UIUtil. dip2px(view. getContext(), 2);
                outRect. left=UIUtil. dip2px(view. getContext(), 2);
                outRect. right=UIUtil. dip2px(view. getContext(), 2);
            }
        });

        mDetailListAdapter.setItemClickListener(this);
        BezierLayout headerView = new BezierLayout(this);
        mRefreshLayout.setHeaderView(headerView);
        mRefreshLayout.setMaxHeadHeight(140);

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                BaseApplication.getsHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailActivity.this,"刷新成功...",Toast.LENGTH_SHORT).show();
                        mRefreshLayout.finishRefreshing();
                    }
                },2000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                //去加载更多内容
                if (mAblumDetailPresenter != null) {
                    mAblumDetailPresenter.loadMore();
                    mIsLoaderMore=true;
                }

            }
        });
        return detailListView;
    }


    @Override
    public void onDetailListLoad(List<Track> tracks) {

        if (mIsLoaderMore&&mRefreshLayout!=null) {
            mRefreshLayout.finishLoadmore();
            mIsLoaderMore=false;
        }

        this.mCurrentTracks = tracks;
        //判断数据结果、根据结果显示UI控制
        if (tracks == null || tracks.size() == 0) {
            if (mUiLoader != null) {
                mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
            }
        }

        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
        //更新/设置UI数据
        mDetailListAdapter.setData(tracks);
    }

    @Override
    public void onNetworkError(int errorCode, String errorMsg) {
        //请求发生错误、显示网络异常状态
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROE);
    }

    @Override
    public void onAlbumLoaded(Album album) {

        long id = album.getId();

        LogUtil.d(TAG , "album --->" + id);
        mCurrentId = id;
        //获取专辑的详情内容
        if (mAblumDetailPresenter != null) {
            mAblumDetailPresenter.getAlbumDetail((int)id,mCurrentPage);
        }

        //拿数据、显示Loading状态
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        }

        if(mAlbumTitle!=null){
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if(malubmAuthor != null){
            malubmAuthor.setText(album.getAnnouncer().getNickname());
        }
        LogUtil.d(TAG,"mlargeCover---->>" + mlargeCover);
        //做毛玻璃效果
        if (mlargeCover != null && null != mlargeCover) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mlargeCover, new Callback() {
                @Override
                public void onSuccess() {
                    Drawable drawable = mlargeCover.getDrawable();
                    if (drawable != null) {
                        //到这里才说明这是有图片的
                        LogUtil.d(TAG,"加载毛玻璃---->>onSuccess");
                        ImageBlur.makeBlur(mlargeCover,DetailActivity.this);                    }
                }

                @Override
                public void onError() {
                    LogUtil.d(TAG,"onError");
                }
            });

        }
        if (msmallCover != null) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(msmallCover);
        }
    }

    @Override
    public void onLoaderMoreFinished(int size) {
        if (size > 0) {
            Toast.makeText(this, "成功加载" + size + "条节目", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "没有更多节目", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefreshFinished(int size) {

    }


    @Override
    public void onRetryClick() {
        //这里面表示用户网络不佳 重新加载
        if (mAblumDetailPresenter != null) {
            mAblumDetailPresenter.getAlbumDetail((int) mCurrentId,mCurrentPage);
        }
    }

    @Override
    public void onItemClick(List<Track> mdetailData, int position) {
        //设置播放器的数据
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        playerPresenter.setPlayList(mdetailData,position);
        //跳转到播放器界面
        Intent intent = new Intent(this,PlayerActivity.class);
        startActivity(intent);
    }

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

    @Override
    public void onPlayStart() {
        //修改图片为暂停的，文字修改为正在播放
        updatePlayState(true);
    }

    @Override
    public void onPlayPause() {
        //设置成播放图片，文字修改已暂停
        updatePlayState(false);
    }

    @Override
    public void onPlayStop() {
        //设置成播放图片，文字修改已暂停
        updatePlayState(false);
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void prePlay() {

    }

    @Override
    public void onListLoaded(List<Track> list) {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {

        if (track != null) {
            mCurrentTrackTitle = track.getTrackTitle();
            if (!TextUtils.isEmpty(mCurrentTrackTitle)&&mPlayControlTips!=null) {
                mPlayControlTips.setText(mCurrentTrackTitle);
            }
        }


    }

    @Override
    public void updateListOrder(boolean isReverse) {

    }
}
