package com.example.shijie.interfaces;

public interface HistoryPresenter {
    /**
     * 获取推荐内容
     */
    void getRecommendList();

    /**
     * 下拉刷新内容
     */
    void pullRefreshMore();

    /**
     * 上拉加载更多
     */
    void loadMore();

    /**
     * 用于注册 ui 的回调实现类
     * @param callback
     */
    void registerViewCallback(HistoryViewCallback callback);

    /**
     * 取消ui的 回调注册
     * @param callback
     */
    void unRegisterCallback(HistoryViewCallback callback);

}

