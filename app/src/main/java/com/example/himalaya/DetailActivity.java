package com.example.himalaya;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.views.RoundRectImageView;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private ImageView mlargeCover;
    private RoundRectImageView msmallCover;
    private TextView mAlbumTitle;
    private TextView malubmAuthor;
    private AlbumDetailPresenter mAblumDetailPresenter;

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
        msmallCover = this.findViewById(R.id.viv_samll_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        malubmAuthor = this.findViewById(R.id.tv_album_author);
    }


    @Override
    public void onDetailListLoad(List<Track> tracks) {

    }

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
}
