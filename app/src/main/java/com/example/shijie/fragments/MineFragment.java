package com.example.shijie.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.shijie.R;
import com.example.shijie.activities.ToWeatherActivity;
import com.example.shijie.views.RoundRectImageView;


public class MineFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private Context context;
    private boolean isPrepared;
    private boolean mHasLoadedOnce;
    private RoundRectImageView roundRectImageView;
    private RelativeLayout pLayout;
    private RelativeLayout dLayout;
    private RelativeLayout sLayout;
    private RelativeLayout aLayout;
    private RelativeLayout aboutLayout;
    private ImageView divideBar;


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {

            context = getActivity();
            mView = inflater.inflate(R.layout.fragmet_mine, null, true);
            findViews();
            isPrepared = true;
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {

            parent.removeView(mView);
        }
        return mView;
    }

    public void findViews() {

//        roundRectImageView = (RoundRectImageView) roundRectImageView.findViewById(R.id.circle_ImageView);
        pLayout = (RelativeLayout) mView
                .findViewById(R.id.layout_mine_personal);
        pLayout.setOnClickListener(this);
        dLayout = (RelativeLayout) mView
                .findViewById(R.id.layout_mine_download);
        dLayout.setOnClickListener(this);
        sLayout = (RelativeLayout) mView.findViewById(R.id.layout_mine_setting);
        sLayout.setOnClickListener(this);
        aLayout = (RelativeLayout) mView
                .findViewById(R.id.layout_mine_weather_Poetry);
        aLayout.setOnClickListener(this);
        aboutLayout = (RelativeLayout) mView.findViewById(R.id.layout_mine_Appabout);
        aboutLayout.setOnClickListener(this);
        divideBar = (ImageView) mView.findViewById(R.id.divideBar_image);
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

//            case R.id.layout_mine_personal:
//
//                Intent pIntent = new Intent(context, PersonalMessageActivity.class);
//                startActivity(pIntent);
//                break;
//            case R.id.layout_mine_download:
//
//                Intent dIntent = new Intent(context, MyDownLoadActivity.class);
//                startActivity(dIntent);
//                break;
//            case R.id.layout_mine_setting:
//
//                Intent sIntent = new Intent(context, EditInformationActivity.class);
//                startActivity(sIntent);
//                break;
            case R.id.layout_mine_weather_Poetry:

                Intent aIntent = new Intent(context, ToWeatherActivity.class);
                startActivity(aIntent);
                break;
            case R.id.layout_mine_Appabout:

                Intent aboutIntent = new Intent(context, ToWeatherActivity.class);
                startActivity(aboutIntent);
            default:
                break;
        }

    }
}
