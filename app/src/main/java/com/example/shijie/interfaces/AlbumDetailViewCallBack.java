package com.example.shijie.interfaces;

import com.example.shijie.beans.Poetry;
import com.example.shijie.beans.PoetryHistory;


import java.util.List;

public interface AlbumDetailViewCallBack {
    /**
     * 专辑详情内容加载
     * @param tracks
     */
    void onDetailListLoaded(List<Poetry> tracks);

    /**
     * 吧album 传给ui
     * @param album
     */
    void onAlbumLoaded(Poetry album);
    void onAlbumLoaded(PoetryHistory album);
}

