package com.example.shijie.interfaces;

import com.example.shijie.base.IbasePrecenter;

public interface ISearchprecenter extends IbasePrecenter<IsearchCallBack> {
    /*
    *搜索
     */
    void  doSearch(String keyword);
    /*
    *
    *重新搜索
     */
    void  research();
    /*
    加载更多
     */
    void  loadmore();
    /*
    获取热词
     */
    void  getHotMore();
    /*
    获取推荐的关键词
     */
    void  getRecommendMore(String keyword);

}
