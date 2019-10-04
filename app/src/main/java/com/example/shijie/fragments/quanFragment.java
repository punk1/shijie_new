package com.example.shijie.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shijie.Detail_write_poetry;
import com.example.shijie.R;
import com.example.shijie.adapter.QuanlistAdapter;
import com.example.shijie.base.BaseFragment;
import com.example.shijie.beans.DynamicItem;
import com.example.shijie.interfaces.IquanPresenter;
import com.example.shijie.interfaces.IquanViewCallback;
import com.example.shijie.views.UIloader;
import com.example.shijie.writepoetry;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.List;

public class quanFragment extends BaseFragment implements IquanViewCallback,QuanlistAdapter.onquanItemClicklistener {
    private List<DynamicItem> mData = new ArrayList<>();

    private View rootView;
    private RecyclerView mrecyclerview;
    private QuanlistAdapter quanlistAdapter;
    private IquanPresenter iquanPresenter;
    private ImageView quan_back;
    private TextView write_btn;
    private Boolean misloadMore;
    @Override
    protected View onSubViewLoader(LayoutInflater layoutInflater, ViewGroup container) {
       rootView = layoutInflater.inflate(R.layout.quan,container,false);
        mrecyclerview = rootView.findViewById(R.id.quan_list);
        //2 设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),5);
                outRect.bottom= UIUtil.dip2px(view.getContext(),5);
                outRect.left = UIUtil.dip2px(view.getContext(),5);
                outRect.right = UIUtil.dip2px(view.getContext(),5);
            }
        });


        quanlistAdapter = new QuanlistAdapter();

        quanlistAdapter.setonquanItemClicklistener(this);
        mrecyclerview.setAdapter(quanlistAdapter);

        mrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (iquanPresenter != null){

                    iquanPresenter.loadMore();
                    misloadMore = false;


                }
            }
        });

        iquanPresenter = com.example.shijie.presenters.IquanPresenter.getInstance();
        iquanPresenter.registerViewCallback(this);

        iquanPresenter.getRecommendList();

       return  rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void  initView(){

        quan_back  = (rootView).findViewById(R.id.quan_back);
        write_btn = (rootView).findViewById(R.id.write_btn);
        quan_back.setVisibility(View.INVISIBLE);
        write_btn.setVisibility(View.VISIBLE);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), writepoetry.class);
                startActivity(intent);

            }
        });

    }


    @Override
    public void onItemClick(int postion, DynamicItem poe) {
        Bundle bundle = new Bundle();
        bundle.putString("objectid",poe.getObjectId());

        Intent intent= new Intent(getContext(), Detail_write_poetry.class);
        intent.putExtra("detail",bundle);
        startActivity(intent);

    }

    @Override
    public void onRecommendListload(List<DynamicItem> result) {
        Log.d("quan", "onRecommendListload: 设置数据");
        quanlistAdapter.setData(result);

    }

    @Override
    public void onLoaderMore(List<DynamicItem> result) {
        if (iquanPresenter != null){
            iquanPresenter.loadMore();
        }

    }

    @Override
    public void onRefreshMore(List<DynamicItem> result) {

    }

    @Override
    public void onNetworkerror() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (iquanPresenter != null){
            iquanPresenter.unRegisterCallback(this);
        }

    }
}
