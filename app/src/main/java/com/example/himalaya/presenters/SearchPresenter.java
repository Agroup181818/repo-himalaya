package com.example.himalaya.presenters;

import com.example.himalaya.data.XimalayApi;
import com.example.himalaya.interfaces.ISearchCallback;
import com.example.himalaya.interfaces.ISearchPresenter;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements ISearchPresenter {

    private static final String TAG = "SearchPresenter";
    //当前的搜索关键字
    private String mCurrentKeyWord = null;
    private XimalayApi mXimalayapi;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;


    private SearchPresenter() {
        mXimalayapi = XimalayApi.getXimalayapi();
    }

    private static SearchPresenter sSearchPresenter = null;

    public static SearchPresenter getSearchPresenter() {
        if (sSearchPresenter == null) {
            synchronized (SearchPresenter.class) {
                if (sSearchPresenter == null) {
                    sSearchPresenter = new SearchPresenter();
                }
            }
        }
        return sSearchPresenter;
    }

    private List<ISearchCallback> mCallback = new ArrayList<>();

    @Override
    public void doSearch(String keyword) {
        //用于得重新搜素
        //当网络不好的时候，用户会点击重新搜素
        this.mCurrentKeyWord = keyword;
        search(keyword);
    }

    private void search(String keyword) {
        mXimalayapi.searchByKeyWord(keyword, mCurrentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                List<Album> albums = searchAlbumList.getAlbums();
                if (albums != null) {
                    LogUtil.d(TAG, "album size -- > " + albums.size());
                    for (ISearchCallback iSearchCallback : mCallback) {
                        iSearchCallback.onSearchResultLoaded(albums);
                    }
                } else {
                    LogUtil.d(TAG, "album si null..");
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "errorCode -- > " + errorCode);
                LogUtil.d(TAG, "errorMsg -- > " + errorMsg);
                for (ISearchCallback iSearchCallback : mCallback) {
                    iSearchCallback.onError(errorCode, errorMsg);
                }
            }
        });
    }

    @Override
    public void reSearch() {
        search(mCurrentKeyWord);
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getHotWord() {

        mXimalayapi.getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(HotWordList hotWordList) {
                if (hotWordList != null) {
                    List<HotWord> hotWords = hotWordList.getHotWordList();
                    LogUtil.d(TAG, "hotWords size -- > " + hotWords.size());
                    for (ISearchCallback iSearchCallback : mCallback) {
                        iSearchCallback.onHotWordLoaded(hotWords);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getHotWord errorCode -- > " + errorCode);
                LogUtil.d(TAG, "getHotWord errorMsg -- > " + errorMsg);

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
                    LogUtil.d(TAG, "keyWordList size -- > " + keyWordList.size());
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getHotWord errorCode -- > " + errorCode);
                LogUtil.d(TAG, "getHotWord errorMsg -- > " + errorMsg);
                for (ISearchCallback iSearchCallback : mCallback) {
                    iSearchCallback.onError(errorCode, errorMsg);
                }
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchCallback iSearchCallback) {
        if (!mCallback.contains(iSearchCallback)) {
            mCallback.add(iSearchCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(ISearchCallback iSearchCallback) {
        mCallback.remove(iSearchCallback);
    }
}
