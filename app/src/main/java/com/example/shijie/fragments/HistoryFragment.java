package com.example.shijie.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.shijie.activities.DetailActivity;
import com.example.shijie.R;
import com.example.shijie.adapter.HistoryAdapter;
import com.example.shijie.base.BaseFragment;
import com.example.shijie.beans.Poetry;
import com.example.shijie.beans.PoetryHistory;
import com.example.shijie.interfaces.HistoryViewCallback;
import com.example.shijie.presenters.AlbumDetailPresenter;
import com.example.shijie.presenters.IHistoryPresenter;
import com.example.shijie.views.UIloader;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HistoryFragment extends BaseFragment implements HistoryViewCallback, UIloader.OnRetyrclickListenr, HistoryAdapter.onRecommendItemClicklistener {

    private String TAG = "tui";
    private RecyclerView mrecyclerView;
    private HistoryAdapter recommenListAdapter;
    private IHistoryPresenter mRecommendPresenter ;
    private Poetry malbum;

    private  UIloader uIloader;
    private View rootView;
    private boolean misloadmore = false;


    @Override
    protected View onSubViewLoader(final LayoutInflater layoutInflater, final ViewGroup container) {

        uIloader = new UIloader(getContext()) {
            @Override
            public View getSuccesView() {
                return createsuccessView(layoutInflater,container);
            }
        };

        //RecyclerVIew 使用

        // 数据
        //getRecommandData();

        /**
         * 获取到逻辑数据的对象
         */

        mRecommendPresenter = IHistoryPresenter.getInstance();
        mRecommendPresenter.registerViewCallback(this);

        /**
         * 获取推荐列表
         */
        mRecommendPresenter.getRecommendList();

        if (uIloader.getParent() instanceof ViewGroup){
            ((ViewGroup) uIloader.getParent()).removeView(uIloader);
        }

        uIloader.setOnRetyrclickListenr(this);

        //返回View 给界面显示
        return uIloader;
    }


    private View createsuccessView(LayoutInflater layoutInflater, ViewGroup container) {
        //View加载完成
        rootView = layoutInflater.inflate(R.layout.activity_history,container,false);
        //1 找到控件
        mrecyclerView = rootView.findViewById(R.id.history_list);
        //2 设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(linearLayoutManager);
        mrecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),10);
                outRect.bottom= UIUtil.dip2px(view.getContext(),10);
                outRect.left = UIUtil.dip2px(view.getContext(),10);
                outRect.right = UIUtil.dip2px(view.getContext(),10);
            }
        });

        // 3 设置 适配器

        recommenListAdapter = new HistoryAdapter();
        mrecyclerView.setAdapter(recommenListAdapter);
        recommenListAdapter.setonRecommendItemClicklistener(this);

        mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mRecommendPresenter!=null){
                    mRecommendPresenter.loadMore();
                    misloadmore = false;
                }

            }
        });
        return rootView;
    }

//    /**
//     * 获取推荐内容
//     */
//    private void getRecommandData() {
//        Map<String, String > map = new HashMap<>();
//        map.put(DTransferConstants.LIKE_COUNT, Constants.recommend_count+"");
//        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
//            @Override
//            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
//                LogUtil.d(TAG,"thread name --- >" + Thread.currentThread().getName());
//                if(gussLikeAlbumList != null){
//                    List<Album>albumList = gussLikeAlbumList.getAlbumList();
//                    for (int i = 0; i < albumList.size(); i++) {
//                        LogUtil.d(TAG,"album  ---->"+ albumList.get(i));
//                        LogUtil.d(TAG,"album  ---->"+"/n");
//
//                    }
//
//
//                   //更新 ui
//                    //upRecommendUI(albumList);
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//                LogUtil.d(TAG,"error --->"+ i+"---"+s);
//
//            }
//        });
//    }



//    private void upRecommendUI(List<Album> albumList) {
//        // 把数据是适配给适配器，并且gengxui
//        recommenListAdapter.setData(albumList);
//
//    }


    @Override
    public void onRecommendListload(List<PoetryHistory> result) {
        //获取到 推荐 内容 就会 被 调用 成功
        //数据 回来 更新 ui
        recommenListAdapter.setData(result);
        uIloader.updatastatus(UIloader.UIStaus.SUCCESS);

    }

    @Override
    public void onLoaderMore(List<PoetryHistory> result) {
        if (mRecommendPresenter!=null){
            mRecommendPresenter.loadMore();
        }

    }

    @Override
    public void onRefreshMore(List<PoetryHistory> result) {
        if (mRecommendPresenter!=null){
            mRecommendPresenter.loadMore();
        }

    }

    @Override
    public void onNetworkerror() {
        uIloader.updatastatus(UIloader.UIStaus.NETWORK_ERROR);
    }

    @Override
    public void onEmpty() {
        uIloader.updatastatus(UIloader.UIStaus.EMPTY);
    }

    @Override
    public void onLoad() {
        uIloader.updatastatus(UIloader.UIStaus.LOADING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消接口的注册
        if (mRecommendPresenter != null){
            mRecommendPresenter.unRegisterCallback(this);
        }
    }

    @Override
    public void ReTyrClick() {
        if (mRecommendPresenter != null){
            mRecommendPresenter.getRecommendList();
        }

    }

    @Override
    public void onItemClick(int postion, PoetryHistory album) {

        Bundle bundle = new Bundle();
        bundle.putString("p_id",album.getP_id());
        bundle.putInt("from",0);
        Log.d("li", "onItemClick: "+album.getP_title());
        BmobQuery<Poetry> query=new BmobQuery<>();
        query.addWhereEqualTo("p_name",album.getP_title());
        query.findObjects(new FindListener<Poetry>() {
            @Override
            public void done(List<Poetry> list, BmobException e) {
                if(e!=null){
                    Log.d("li", "done: 错误"+e);
                }
                malbum=list.get(0);
                Log.d("li", "onItemClick: title"+malbum.getP_name());
                AlbumDetailPresenter.getInstance().setTargetAlbum(malbum);
            }

        });
        // item  被电 跳转到详情界面
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent,bundle);

    }

}

