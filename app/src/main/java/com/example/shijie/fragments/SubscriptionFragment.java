package com.example.shijie.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shijie.R;
import com.example.shijie.base.BaseFragment;


public class SubscriptionFragment extends BaseFragment {
    @Override
    protected View onSubViewLoader(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView = layoutInflater.inflate(R.layout.fragmnet_subscription,container,false);
        return rootView;
    }
}
