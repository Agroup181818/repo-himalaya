package com.example.himalaya.interfaces;

import com.example.himalaya.base.IBasePresenter;

public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {

    /**
     * 进行搜素
     * @param keyword
     */
    void doSearch(String keyword);

    /**
     * 重新搜素
     */
    void reSearch();

    /**
     * 加载更多
     */
    void loadMore();

    /**
     * 获取热词
     */
    void getHotWord();

    /**
     * 获取推荐的关键字（相关的关键字）
     * @param keyword
     */
    void getRecommendWord(String keyword);
}
