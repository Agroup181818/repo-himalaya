package com.example.himalaya.interfaces;

public interface IRecommendPresenter {
    /**
     * 获取推荐内容
     */
    void getRecommendList();


    /**
     * 下拉刷新更多内容
     */
    void pull2RefreshMore();


    /**
     * 上划加载更多
     */
    void loadMore();


    /**
     * 这个方法用于注册UI的回调
     * @param callback
     */
    void registerViewCallback(IRecommendViewCallback callback);



    /**
     * 这个方法用于取消注册UI的回调
     * @param callback
     */
    void unRegisterViewCallback(IRecommendViewCallback callback);




}