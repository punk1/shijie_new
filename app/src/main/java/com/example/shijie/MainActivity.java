package com.example.shijie;

import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.shijie.adapter.IndicatorAdapter;
import com.example.shijie.adapter.MainContentAdapter;
import com.example.shijie.base.Constants;
import com.example.shijie.beans.Config;
import com.example.shijie.utils.LogUtil;
import com.iflytek.cloud.SpeechUtility;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private static  final  String TAG = "MainActivity";
    private IndicatorAdapter mIndicatoradapter;
    private ViewPager mcontentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Constants.BMOB_SDK_KEY);

//        Log.d("li", "onCreate:  Config.getInstance().user"+ Config.getInstance().user.getObjectId());


        SpeechUtility.createUtility(this, "appid=" + getString(R.string.xunfei_app_id));
        Log.d("xiang", " main onCreate: 初始化讯飞 ");
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        inf();
        login("zj","00000000");

    }
    private void login(String username, String password) {
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null && bmobUser != null) {
                    Config.getInstance().user = bmobUser;

                } else {

                }
            }
        });
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

    }

    private void initView() {
        MagicIndicator magicIndicator= (MagicIndicator)findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(this.getResources().getColor(R.color.Main_color));

        //创建Indicator的适配器

        mIndicatoradapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mIndicatoradapter);
        // ViewPager
        mcontentPager = findViewById(R.id.content_pager);


        // 创建内容适配器
        FragmentManager fragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(fragmentManager);

        mcontentPager.setAdapter(mainContentAdapter);

        //把ViewPager 于 指示器 绑定到一起
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mcontentPager);

    }

}
