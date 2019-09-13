package com.example.shijie.presenters;

import android.app.VoiceInteractor;
import android.service.voice.VoiceInteractionSession;
import android.support.annotation.Nullable;
import android.util.Log;


import com.example.shijie.beans.Poetry;
import com.example.shijie.beans.PoetryHistory;
import com.example.shijie.interfaces.AlbumDetailViewCallBack;
import com.example.shijie.interfaces.IAlbumDetailPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {
    private Poetry mTargetAlbum;
    private PoetryHistory sTargetAlbum;
    private List<AlbumDetailViewCallBack>mCallBacks = new ArrayList<>();
    private String TAG= "tui";

    private AlbumDetailPresenter(){}
    private static AlbumDetailPresenter sintance = null;
    public static AlbumDetailPresenter getInstance(){
        if (sintance == null){
            synchronized (AlbumDetailPresenter.class){
                if(sintance == null){
                    sintance = new AlbumDetailPresenter();
                }
            }
        }
        return sintance;
    }
    @Override
    public void pullRefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumId, int page) {
//        Map<String,String> map =new HashMap<>();
//        map.put(DTransferConstants.ALBUM_ID, ""+albumId);
//        map.put(DTransferConstants.SORT,"asc");
//        map.put(DTransferConstants.PAGE, page+"");
//        map.put(DTransferConstants.PAGE_SIZE,Constants.Count_size+"");
//
//        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
//            @Override
//            public void onSuccess(@Nullable TrackList trackList) {
//                Log.d(TAG, "onSuccess: "+trackList);
//
//                if(trackList != null){
//                    List<Track >tracks = trackList.getTracks();
//                    LogUtil.d(TAG,"tracks size --->"+tracks.size());
//                    handleAlbumDetailResult(tracks);
//
//
//                }
//
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                LogUtil.d(TAG,"error --->"+i +"-- "+s);
//            }
//        });


    }

    private void handleAlbumDetailResult(List<Poetry> tracks) {
        for (AlbumDetailViewCallBack mcallback :mCallBacks){
            mcallback.onDetailListLoaded(tracks);
        }
    }

    @Override
    public void registerViewCallback(AlbumDetailViewCallBack callBack) {
        if (!mCallBacks.contains(callBack)){
            mCallBacks.add(callBack);
            if (mTargetAlbum != null){
                callBack.onAlbumLoaded(mTargetAlbum);
            }
            if(sTargetAlbum != null){
                callBack.onAlbumLoaded(sTargetAlbum);
            }
        }
    }

    @Override
    public void unregisterViewCallback(AlbumDetailViewCallBack callBack) {
        mCallBacks.remove(callBack);

    }

    public void setTargetAlbum(Poetry album){
        this.mTargetAlbum = album;
    }
    public void setTargetAlbum(PoetryHistory album){
        this.sTargetAlbum = album;
    }
}

