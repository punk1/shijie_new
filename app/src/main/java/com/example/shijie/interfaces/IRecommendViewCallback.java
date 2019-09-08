package com.example.shijie.interfaces;



import com.example.shijie.beans.Poetry;

import java.util.List;

public interface IRecommendViewCallback {
    /**
     * 获取到推荐内容的结果
     */
    void onRecommendListload(List<Poetry> result);

    /**
     * 加载更多
     */
    void onLoaderMore(List<Poetry> result);

    /**
     * 下拉加载更多的 结果
     * @param result
     */
    void onRefreshMore(List<Poetry> result);

    /**
     * 网络错误
     */
    void onNetworkerror();

    /**
     * 数据为空
     */
    void onEmpty();

    /**
     * 加载中
     */
    void onLoad();


}

