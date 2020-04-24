package com.example.himalaya.presenters;

import com.example.himalaya.interfaces.IRecommendPresenter;
import com.example.himalaya.interfaces.IRecommendViewCallback;
import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter {


    private static final String TAG="RecommendPresenter";

    private List<IRecommendViewCallback> mCallbacks=new ArrayList();

    private  RecommendPresenter(){}



    private  static RecommendPresenter sInstance=null;


    /**
     * 获取单例对象
     * @return
     */
    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                        sInstance=new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }
    //获取推荐内容，其实就是“猜你喜欢”
    @Override
    public void getRecommendList() {
        //获取推荐内容
        //封装参数
        updateLoading();
        Map<String, String> map = new HashMap<>();
        //这个参数表示一页数据多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT+"");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG," thread name -- > " +Thread.currentThread().getName());
                //数据返回成功
                if(gussLikeAlbumList != null){
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    //数据回来 更新UI
                    //UpRecommendUI(albumList);
                    handlerRecommendResult(albumList);

                }
            }

            @Override
            public void onError(int i, String s) {
                //数据返回出错
                LogUtil.d(TAG," error -- > "+i);
                LogUtil.d(TAG," errorMsg -- > "+s);
                handlerError();
            }
        });

    }

    private void handlerError() {
        if (mCallbacks != null) {
            for (IRecommendViewCallback callback : mCallbacks) {
                callback.onNetworkError();
            }
        }
    }



    private void handlerRecommendResult(List<Album> albumList) {
        //通知UI更新
        if (albumList != null) {
            //测试 清空一下让界面显示空
            //albumList.clear();
            if (albumList.size() == 0) {
                for (IRecommendViewCallback callback : mCallbacks){
                    callback.onEmpty();
                }
            }else {
                if (mCallbacks != null) {
                    for (IRecommendViewCallback callback : mCallbacks) {
                        callback.onRecommendListLoaded(albumList);
                    }
                }
            }
        }

    }

    private void updateLoading() {
        for (IRecommendViewCallback callback : mCallbacks){
            callback.onLoading();
        }
    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks!=null&&!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks!=null) {
            mCallbacks.remove(callback);
        }
    }
}
