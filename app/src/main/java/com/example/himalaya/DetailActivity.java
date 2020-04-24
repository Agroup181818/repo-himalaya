package com.example.himalaya;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.adapters.DetailListAdapter;
import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.utils.ImageBlur;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.RoundRectImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private static final String TAG = "DetailActivity";
    private ImageView mlargeCover;
    private RoundRectImageView msmallCover;
    private TextView mAlbumTitle;
    private TextView malubmAuthor;
    private AlbumDetailPresenter mAblumDetailPresenter;
    private int mCurrentPage=1;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        mAblumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAblumDetailPresenter.registerViewCallback(this);




    }

    private void initView() {

        mlargeCover = this.findViewById(R.id.iv_large_cover);
        msmallCover = this.findViewById(R.id.viv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        malubmAuthor = this.findViewById(R.id.tv_album_author);
        mDetailList = this.findViewById(R.id.album_detail_list);



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






    }


    @Override
    public void onDetailListLoad(List<Track> tracks) {
        //更新/设置UI数据
        mDetailListAdapter.setData(tracks);
    }

    @Override
    public void onAlbumLoaded(Album album) {


        long id = album.getId();

        //获取专辑的详情内容
        mAblumDetailPresenter.getAlbumDetail((int)id,mCurrentPage);




        if(mAlbumTitle!=null){
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if(malubmAuthor != null){
            malubmAuthor.setText(album.getAnnouncer().getNickname());
        }

        //做毛玻璃效果
        if (mlargeCover != null &&null!=mlargeCover) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mlargeCover, new Callback() {
                @Override
                public void onSuccess() {
                    Drawable drawable = mlargeCover.getDrawable();
                    if (drawable != null) {
//到这里才说明这是有图片的
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
}
