package com.example.himalaya.presenters;

import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.data.ISubDaoCallback;
import com.example.himalaya.data.SubscriptionDao;

import com.example.himalaya.interfaces.ISubscriptionCallback;
import com.example.himalaya.interfaces.ISubscriptionPresenter;
import com.example.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SubscriptionPresenter implements ISubscriptionPresenter, ISubDaoCallback {

    private final SubscriptionDao mSubscriptionDao;

    private Map<Long, Album> mData = new HashMap<>();


    private List<ISubscriptionCallback> mCallbacks = new ArrayList<>();

    private SubscriptionPresenter() {
        mSubscriptionDao = SubscriptionDao.getInstance();
        mSubscriptionDao.setCallback(this);
    }

    private void listSubscriptions() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //只调用不处理结果
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.listAlbums();

                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private static SubscriptionPresenter sInstance = null;

    public static SubscriptionPresenter getInstance() {
        if (sInstance == null) {
            synchronized (SubscriptionPresenter.class) {
                sInstance = new SubscriptionPresenter();
            }
        }
        return sInstance;
    }


    @Override
    public void addSubscription(final Album album) {
        //判断当前的订阅数量,不能超过100
        if (mData.size() >= Constants.MAX_SUB_COUNT) {
            //给出提示
            for (ISubscriptionCallback callback : mCallbacks) {
                callback.onSubFull();
            }
            return;
        }
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.addAlbum(album);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void deleteSubscription(final Album album) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.deleteAlbum(album);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void getSubscriptionList() {
        listSubscriptions();
    }

    @Override
    public boolean isSub(Album album) {
        Album result = mData.get(album.getId());
        //不为空，表示已经订阅
        return result != null;
    }

    @Override
    public void registerViewCallback(ISubscriptionCallback iSubscriptionCallback) {
        if (!mCallbacks.contains(iSubscriptionCallback)) {
            mCallbacks.add(iSubscriptionCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(ISubscriptionCallback iSubscriptionCallback) {
        mCallbacks.remove(iSubscriptionCallback);
    }


    @Override
    public void onAddResult(final Boolean isSuccess) {
        listSubscriptions();
        //添加结果的回调
        BaseApplication.getsHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onAddResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onDelResult(final Boolean isSuccess) {
        listSubscriptions();

        //删除结果的回调
        BaseApplication.getsHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onDeleteResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onSubListLoaded(final List<Album> result) {
        //加载数据的回调
        mData.clear();
        for (Album album : result) {
            mData.put(album.getId(), album);
        }
        //通知UI更新
        BaseApplication.getsHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onSubscriptionsLoaded(result);
                }
            }
        });
    }

}
