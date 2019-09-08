package com.example.shijie.interfaces;

public interface IAlbumDetailPresenter  {
    /**
     * 下拉刷新 更多
     */
    void pullRefreshMore();
    /**
     * 上啦加载更多
     */
    void loadMore();

    /**
     * 获取专辑列表
     * @param albumId
     * @param page
     */
    void getAlbumDetail(int albumId,int page);

    void registerViewCallback(AlbumDetailViewCallBack callBack);
    void unregisterViewCallback(AlbumDetailViewCallBack callBack);
}

