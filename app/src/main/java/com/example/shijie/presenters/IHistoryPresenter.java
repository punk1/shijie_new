package com.example.shijie.presenters;

import android.util.Log;

import com.example.shijie.beans.Config;
import com.example.shijie.beans.Poetry;
import com.example.shijie.beans.PoetryHistory;
import com.example.shijie.interfaces.HistoryPresenter;
import com.example.shijie.interfaces.HistoryViewCallback;
import com.example.shijie.interfaces.IRecommendPresenter;
import com.example.shijie.interfaces.IRecommendViewCallback;
import com.example.shijie.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class IHistoryPresenter implements HistoryPresenter {

    private int page ;
    private int limitCount = 10;//10条数据
    private static final String TAG = "tui";
    private List<HistoryViewCallback> mcallbacks = new ArrayList<>();
    private List<PoetryHistory> mlist = new ArrayList<>();
    private PoetryHistory mpoetry;

    private IHistoryPresenter(){}

    private static IHistoryPresenter sInstance = null ;

    /**
     * 获取单例对象
     * @return
     */

    public static IHistoryPresenter getInstance(){
        if (sInstance == null){
            synchronized (IHistoryPresenter.class){
                if (sInstance == null){
                    sInstance = new IHistoryPresenter();
                }
            }
        }
        return sInstance;
    }
    private void doLoaed(final boolean isLoaderMore){
        BmobQuery<PoetryHistory> query=new BmobQuery<>();
        Log.d("xiang", "doLoaed: 加载 历史");
        query.addWhereEqualTo("u_id", Config.getInstance().user.getObjectId());
        Log.d("cha", "initData: Objectid"+Config.getInstance().user.getObjectId());
        query.order("-updatedAt");
        query.findObjects(new FindListener<PoetryHistory>() {
            @Override
            public void done(List<PoetryHistory> list, BmobException e) {
                if(e == null){
                    if (list != null && list.size() > 0) {
                        handlerRecommendResunt(list);
                    }else{

                    }
                }else{

                }
            }
        });

    }

    @Override
    public void getRecommendList() {
        updataloading();
        Log.d(TAG, "getRecommendList: 获取 诗内容");
//        Map<String, String > map = new HashMap<>();
//        map.put(DTransferConstants.LIKE_COUNT, Constants.recommend_count+"");
//        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
//            @Override
//            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
//                LogUtil.d(TAG,"thread name --- >" + Thread.currentThread().getName());
//                if(gussLikeAlbumList != null){
//                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
//                    for (int i = 0; i < albumList.size(); i++) {
//                        LogUtil.d(TAG,"album  ---->"+ albumList.get(i));
//                        LogUtil.d(TAG,"album  ---->"+"/n");
//                    }
//
//
//                    //更新 ui
//                    // upRecommendUI(albumList);
//                    handlerRecommendResunt(albumList);
//                }
//            }

//            @Override
//            public void onError(int i, String s) {
//                LogUtil.d(TAG,"error --->"+ i+"---"+s);
//                handlerror();
//            }
//        });
        doLoaed(false);

    }



    private void handlerror() {
        if (mcallbacks!=null){
            for (HistoryViewCallback callback :mcallbacks){
                callback.onNetworkerror();
            }
        }
    }


    private void handlerRecommendResunt(List<PoetryHistory> albumList) {
        if (albumList != null){
            if (albumList.size() == 0){
                for (HistoryViewCallback callback : mcallbacks){
                    callback.onEmpty();
                }
            }else {
                //通知 UI
                for (HistoryViewCallback callback :mcallbacks){
                    callback.onRecommendListload(albumList);
                }
            }
        }
//        //通知 UI
//        if (mcallbacks!=null){
//            for (IRecommendViewCallback callback :mcallbacks){
//                callback.onRecommendListload(albumList);
//            }
//        }
    }

    private void  updataloading(){
        for(HistoryViewCallback callback :mcallbacks){
            callback.onLoad();
        }

    }

    @Override
    public void pullRefreshMore() {

    }

    @Override
    public void loadMore() {
        Log.d(TAG, "loadMore: 加载更多");
        ++page;
        Log.d(TAG, "loadMore: page"+page);
        doLoaed(true);

    }

    @Override
    public void registerViewCallback(HistoryViewCallback callback) {
        if (!mcallbacks.contains(callback)&& mcallbacks!=null){
            mcallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterCallback(HistoryViewCallback callback) {
        if (mcallbacks != null){
            mcallbacks.remove(callback);
        }

    }
    public void setTargetAlbum(PoetryHistory album){
        this.mpoetry = album;
    }

}

