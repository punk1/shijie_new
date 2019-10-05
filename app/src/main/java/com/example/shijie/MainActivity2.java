package com.example.shijie;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.shijie.base.Constants;
import com.example.shijie.fragments.TabDb;
import com.iflytek.cloud.SpeechUtility;

import cn.bmob.v3.Bmob;

public class MainActivity2 extends AppCompatActivity implements TabHost.OnTabChangeListener {

    private FragmentTabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Constants.shi_BMOB_SDK_KEY);
//        Log.d("li", "onCreate:  Config.getInstance().user"+ Config.getInstance().user.getObjectId());
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.xunfei_app_id));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);

        //初始化FragmentTabHost
        initHost();
        //初始化底部导航栏
        initTab();
        //默认选中
        mTabHost.onTabChanged(TabDb.getTabsTxt()[0]);
    }

    private void initTab() {
        String[] tabs = TabDb.getTabsTxt();
        for (int i = 0; i < tabs.length; i++) {
            //新建TabSpec
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(TabDb.getTabsTxt()[i]);
            //设置view
            View view = LayoutInflater.from(this).inflate(R.layout.tab_foot, null);
            ((TextView) view.findViewById(R.id.foot_tv)).setText(TabDb.getTabsTxt()[i]);
            ((ImageView) view.findViewById(R.id.foot_iv)).setImageResource(TabDb.getTabsImg()[i]);
            tabSpec.setIndicator(view);
            //加入TabSpec
            mTabHost.addTab(tabSpec,TabDb.getFramgent()[i],null);
        }
    }
    /***
     * 初始化Host
     */
    private void initHost() {
        mTabHost = (FragmentTabHost) findViewById(R.id.main_tab);
        //调用setup方法 设置view
        mTabHost.setup(this, getSupportFragmentManager(),R.id.main_view);
        //去除分割线
        mTabHost.getTabWidget().setDividerDrawable(null);
        //监听事件
        mTabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String arg0) {
        //从分割线中获得多少个切换界面
        TabWidget tabw = mTabHost.getTabWidget();
        for (int i = 0; i < tabw.getChildCount(); i++) {
            View v = tabw.getChildAt(i);
            TextView tv = (TextView) v.findViewById(R.id.foot_tv);
            ImageView iv = (ImageView) v.findViewById(R.id.foot_iv);
            //修改当前的界面按钮颜色图片
            if (i == mTabHost.getCurrentTab()) {
                tv.setTextColor(getResources().getColor(R.color.tab_light_color));
                iv.setImageResource(TabDb.getTabsImgLight()[i]);
            }else{
                tv.setTextColor(getResources().getColor(R.color.tab_color));
                iv.setImageResource(TabDb.getTabsImg()[i]);
            }
        }
    }


}
