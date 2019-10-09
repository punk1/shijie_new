package com.example.shijie.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shijie.R;
import com.example.shijie.adapter.IndicatorAdapter;
import com.example.shijie.adapter.MainContentAdapter;
import com.example.shijie.activities.searchActivity;
import com.example.shijie.utils.LogUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class OneFm  extends Fragment  {
    private static  final  String TAG = "MainActivity";
    private IndicatorAdapter mIndicatoradapter;
    private ViewPager mcontentPager;
    private ImageView search_btn;
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragmet1, container, false);
            initView();
            initEvent();
            inf();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }



        return rootView;

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

    }
    private void inf() {
        LogUtil.d(TAG,"close");
    }

    private void initEvent() {
        LogUtil.d(TAG,"creat");
        mIndicatoradapter.setOnINdicatorTapClickListener(new IndicatorAdapter.OnINdicatorTapClickListener() {
            @Override
            public void onTapclick(int index) {
                LogUtil.d(TAG,"click is --->" + index);
                if (mcontentPager != null){
                    mcontentPager.setCurrentItem(index);
                }
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), searchActivity.class);
                startActivity(intent);
            }
        });



    }

    private void initView() {

        MagicIndicator magicIndicator= (MagicIndicator)rootView.findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(this.getResources().getColor(R.color.tuijian));
        //创建Indicator的适配器

        mIndicatoradapter = new IndicatorAdapter(getContext());
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mIndicatoradapter);
        // ViewPager
        mcontentPager = rootView.findViewById(R.id.content_pager);


        // 创建内容适配器
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(fragmentManager);

        mcontentPager.setAdapter(mainContentAdapter);

        //把ViewPager 于 指示器 绑定到一起
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mcontentPager);

        search_btn = rootView.findViewById(R.id.search);

    }

}
