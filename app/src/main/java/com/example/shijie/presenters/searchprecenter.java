package com.example.shijie.presenters;

import android.util.Log;

import com.example.shijie.beans.Poetry;
import com.example.shijie.interfaces.ISearchprecenter;
import com.example.shijie.interfaces.IsearchCallBack;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class searchprecenter implements ISearchprecenter {
    //当前的搜索关键字
    private String mCurrentKeyword= null;
    private List<Poetry> queryData = new ArrayList<>();

    private searchprecenter(){}
    private static searchprecenter searchprecenter =null;
    public static searchprecenter getSearchprecenter(){
        if (searchprecenter ==null){
            synchronized (searchprecenter.class){
                if(searchprecenter==null){
                    searchprecenter = new searchprecenter();
                }

            }
        }
        return searchprecenter;
    }

    private List<IsearchCallBack> mCallBack = new ArrayList<>();

    @Override
    public void doSearch(String keyword) {
        this.mCurrentKeyword = keyword;
        Log.d("tui", "doSearch: "+keyword);
        /**
         * 搜索
         */
        query(mCurrentKeyword);

    }

    private void query(String keyword) {
        BmobQuery <Poetry> bmobQuery= new BmobQuery<>();
        // 按题目查询
        bmobQuery.addWhereEqualTo("p_name",keyword);
        bmobQuery.findObjects(new FindListener<Poetry>() {
            @Override
            public void done(List<Poetry> list, BmobException e) {
                if (e==null){
                    Log.d("tui", "doSearch: "+list.size());
                    if (list!=null &list.size()>0){
                        queryData.clear();
                        Log.d("tui", "doSearch: "+list.size());
                        for (int i = 0; i < list.size(); i++) {
                            Log.d("tui", "doSearch: "+list.get(i).getP_name());
                                queryData.add(list.get(i));
                        }
                        for (IsearchCallBack isearchCallBack :mCallBack){
                            isearchCallBack.onResultsearchLoad(queryData);
                        }

                    }else{
                        Log.d("search", "done: album is null");
                    }

                }else{
                    Log.d("search", "done: error"+e);
                }
            }
        });



    }

    @Override
    public void research() {

    }

    @Override
    public void loadmore() {

    }

    @Override
    public void getHotMore() {

    }

    @Override
    public void getRecommendMore(String keyword) {

    }

    @Override
    public void registerViewCallback(IsearchCallBack isearchCallBack) {
       if(! mCallBack.contains(isearchCallBack)){
           mCallBack.add(isearchCallBack);
       }
    }

    @Override
    public void unregisterViewCallback(IsearchCallBack isearchCallBack) {
        mCallBack.remove(isearchCallBack);
    }
}
