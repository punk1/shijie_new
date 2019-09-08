package com.example.shijie.presenters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.shijie.R;
import com.example.shijie.beans.Poetry;
import com.example.shijie.interfaces.IRecommendPresenter;
import com.example.shijie.interfaces.IRecommendViewCallback;
import com.example.shijie.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class RecommendPresenter implements IRecommendPresenter {

    private int page ;
    private int limitCount = 10;//10条数据
    private static final String TAG = "tui";
    private List<IRecommendViewCallback> mcallbacks = new ArrayList<>();
    private List<Poetry> mlist = new ArrayList<>();
    private Poetry mpoetry;

    private RecommendPresenter(){}

    private static RecommendPresenter sInstance = null ;

    /**
     * 获取单例对象
     * @return
     */

    public static RecommendPresenter getInstance(){
        if (sInstance == null){
            synchronized (RecommendPresenter.class){
                if (sInstance == null){
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }
    private void doLoaed(final boolean isLoaderMore){
        BmobQuery<Poetry> query=new BmobQuery<Poetry>();
        query.setLimit(limitCount);
        if (page != 0) {
            query.setSkip(limitCount * page);
        }
        query.findObjects(new FindListener<Poetry>() {
            @Override
            public void done(List<Poetry> list, BmobException e) {
                if(e ==null){
                    Log.d(TAG, "done: poetrty --->"+list.size());
                    for (int i = 0; i < list.size(); i++) {
                        Log.d(TAG, "done: poetry"+i+list.get(i));
                    }
                    if(isLoaderMore){
                        // 上啦加载 放到后面
                        mlist.addAll(mlist.size()-1,list);
                    }else{
                        //下拉加载 放到前面
                        mlist.addAll(0,list);
                    }

                    handlerRecommendResunt(mlist);
                }else{
                    if (isLoaderMore){
                        page--;
                    }
                    LogUtil.d("tui", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
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
            for (IRecommendViewCallback callback :mcallbacks){
                callback.onNetworkerror();
            }
        }
    }


    private void handlerRecommendResunt(List<Poetry> albumList) {
        if (albumList != null){
            if (albumList.size() == 0){
                for (IRecommendViewCallback callback : mcallbacks){
                    callback.onEmpty();
                }
            }else {
                //通知 UI
                for (IRecommendViewCallback callback :mcallbacks){
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
        for(IRecommendViewCallback callback :mcallbacks){
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
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (!mcallbacks.contains(callback)&& mcallbacks!=null){
            mcallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterCallback(IRecommendViewCallback callback) {
        if (mcallbacks != null){
            mcallbacks.remove(callback);
        }

    }
    public void setTargetAlbum(Poetry album){
        this.mpoetry = album;
    }

}
