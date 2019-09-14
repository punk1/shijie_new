package com.example.shijie.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shijie.FlyingOrderKeyWordActivity;
import com.example.shijie.R;
import com.example.shijie.TianKongActivity;
import com.example.shijie.base.BaseFragment;
import com.example.shijie.chuanguanActivity;


public class SubscriptionFragment extends BaseFragment {
    private ImageView tian_kong;
    private ImageView bei_song;
    private ImageView fei_hua;
    @Override
    protected View onSubViewLoader(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView = layoutInflater.inflate(R.layout.fragmnet_subscription,container,false);
        init(rootView);
        tian_kong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),TianKongActivity.class);
                startActivity(intent);
            }
        });
        bei_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), chuanguanActivity.class);
                startActivity(intent);
            }
        });
        fei_hua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FlyingOrderKeyWordActivity.class);
                startActivity(intent);
            }
        });
        return rootView;

    }
    public void init(View itemView){
        tian_kong =itemView.findViewById(R.id.button1);
        bei_song =itemView.findViewById(R.id.button2);
        fei_hua =itemView.findViewById(R.id.button3);
    }


}
