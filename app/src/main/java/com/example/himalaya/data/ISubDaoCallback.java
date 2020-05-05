package com.example.himalaya.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface ISubDaoCallback {

    /**
     * 添加的结果回调方法
     * @param isSuccess
     */
    void onAddResult (Boolean isSuccess);


    /**
     * 删除是否成功
     * @param isSuccess
     */
    void onDelResult (Boolean isSuccess);

    /**
     * 获取订阅内容
     * @param result
     */
    void onSubListLoaded(List<Album> result);
}
