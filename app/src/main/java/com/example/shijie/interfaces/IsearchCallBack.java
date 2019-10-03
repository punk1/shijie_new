package com.example.shijie.interfaces;

import com.example.shijie.beans.Poetry;

import java.util.List;

public interface IsearchCallBack {
    void  onResultsearchLoad(List<Poetry> tracks);

    /**
     * 加载更多的结果饭返回
     * @param tracks  结果
     * @param isok  true  表示加载更多，false  表示没有更多
     */
    void onloadmoreResult(List<Poetry> tracks,boolean isok);

    /**
     * 获取关键字的结果回调方法
     * @param tracks
     */
    void  onrecommendload(List<Poetry> tracks);

    void onError(int errorcode,String errorMsg);

}
