package com.example.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetailViewCallback {

    /**
     * 专辑详情内容加载出来了
     * @param tracks
     */
    void onDetailListLoad(List<Track> tracks);

    /**
     * 把Album传给UI
     * @param album
     */
    void onAlbumLoaded(Album album);

}
