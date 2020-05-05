package com.example.himalaya.api;

import com.example.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

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

    /**
     * 根据关键词进行搜素
     *
     * @param keyword
     */
    public void searchByKeyWord(String keyword, int page, IDataCallBack<SearchAlbumList> callBack) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, keyword);
        map.put(DTransferConstants.PAGE, page + "");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_DEFAULT + "");
        CommonRequest.getSearchedAlbums(map, callBack);
    }

    /**
     * 获取推荐的热词
     *
     * @param callback
     */
    public void getHotWords(IDataCallBack<HotWordList> callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.TOP, Constants.COUNT_HOT_WORD + "");
        CommonRequest.getHotWords(map, callback);
    }

    /**
     * 根据关键字获取联想词
     *
     * @param keyword  关键字
     * @param callBack 回调
     */
    public void getSuggestWord(String keyword, IDataCallBack<SuggestWords> callBack) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, keyword);
        CommonRequest.getSuggestWord(map, callBack);
    }
}
