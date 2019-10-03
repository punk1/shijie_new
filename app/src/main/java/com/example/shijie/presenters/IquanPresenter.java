package com.example.shijie.presenters;

import android.util.Log;

import com.example.shijie.beans.DynamicItem;
import com.example.shijie.beans.Poetry;
import com.example.shijie.interfaces.IRecommendViewCallback;
import com.example.shijie.interfaces.IquanViewCallback;
import com.example.shijie.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class IquanPresenter implements com.example.shijie.interfaces.IquanPresenter {
    private List<IquanViewCallback>mcallbacks = new ArrayList<>();
    private int limitCount = 3;//10条数据
    private int page = 0;
    private List<DynamicItem>    dynamicItemList = new ArrayList<>();


    private IquanPresenter(){}
    private static IquanPresenter sintance = null;
    public static IquanPresenter getInstance(){
        if (sintance == null){
            synchronized (IquanPresenter.class){
                if(sintance == null){
                    sintance = new IquanPresenter();
                }
            }
        }
        return sintance;
    }

    @Override
    public void getRecommendList() {
        loadingUpdat();
        doLoaed(false);


    }

    private void doLoaed(final boolean isLoaderMore){
        BmobQuery<DynamicItem> query=new BmobQuery<DynamicItem>();
        query.order("-updatedAt");
        query.setLimit(limitCount);
        if (page != 0) {
            query.setSkip(limitCount * page);
        }
        query.findObjects(new FindListener<DynamicItem>() {
            @Override
            public void done(List<DynamicItem> list, BmobException e) {
                if(e ==null){

                    if(isLoaderMore){
                        // 上啦加载 放到后面
                        Log.d("quan", "done: 1");
                        dynamicItemList.addAll(dynamicItemList.size()-1,list);
                    }else{
                        //下拉加载 放到前面
                        Log.d("quan", "done: 2");
                        dynamicItemList.addAll(0,list);

                    }
                    Log.d("quan", "done: 准备加载");
                    handlerquanResunt(dynamicItemList);

                }else{
                        page--;
                    LogUtil.d("tui", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });

    }

    private void handlerquanResunt(List<DynamicItem> albumList){
        Log.d("quan", "handlerRecommendResunt: "+albumList.size());
        if (albumList != null){
            if (albumList.size() == 0){
                for (IquanViewCallback callback : mcallbacks){
                    callback.onEmpty();
                }
            }else {
                //通知 UI

                Log.d("quan", "handlerRecommendResunt: 更新ui");
                for (IquanViewCallback callback :mcallbacks){
                    callback.onRecommendListload(albumList);
                }
            }
        }

    }



    private void loadingUpdat() {
        for (IquanViewCallback callback :mcallbacks){
            callback.onLoad();
        }
    }

    @Override
    public void pullRefreshMore() {
        page = 0;
        doLoaed(false);

    }

    @Override
    public void loadMore() {
        ++page;
        doLoaed(true);
    }

    @Override
    public void registerViewCallback(IquanViewCallback callback) {
        if (!mcallbacks.contains(callback)&& mcallbacks!=null){
            mcallbacks.add(callback);
        }

    }

    @Override
    public void unRegisterCallback(IquanViewCallback callback) {
        if (mcallbacks != null){
            mcallbacks.remove(callback);
        }

    }
}
