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
import com.example.shijie.beans.People;
import com.example.shijie.beans.Poetry;
import com.example.shijie.beans.PoetryHistory;
import com.example.shijie.utils.LogUtil;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechUtility;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {

    String resultString;
    String s ;

    private static  final  String TAG = "MainActivity";
    private IndicatorAdapter mIndicatoradapter;
    private ViewPager mcontentPager;

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        Bmob.initialize(this, Constants.shi_BMOB_SDK_KEY);
//        setContentView(R.layout.shi);
//        for (int i = 1001; i <=1100; i++) {
//            readTextFromSDcard(i);
//            laod(resultString);
//        }
//
//    }
//    private void laod(String jsondata){
//        try {
//            JSONObject jsonObject = new JSONObject(jsondata);
//            String con = jsonObject.getString("content");
//            String title  = jsonObject.getString("name");
//            JSONObject jsonObject1 = jsonObject.getJSONObject("poet");
//            String name =jsonObject1.getString("name");
//            Log.d("tui", "laod: "+name);
//            String dynasty = jsonObject.getString("dynasty");
//            final Poetry poetry = new Poetry();
//            poetry.setP_author(name);
//            poetry.setP_name(title);
//            poetry.setP_content(con);
//            poetry.setP_source(dynasty);
//            poetry.update(new UpdateListener() {
//                @Override
//                public void done(BmobException e) {
//                    if (e!=null){
//                        poetry.save(new SaveListener<String>() {
//                            @Override
//                            public void done(String s, BmobException e) {
//                                Log.d("tui", "done: 插入成功");
//                            }
//                        });
//                    }
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void readTextFromSDcard(int i) {
//        InputStreamReader inputStreamReader;
//        try {
//
//            inputStreamReader = new InputStreamReader(getAssets().open("poetry_"+i+".json"), "UTF-8");
//            BufferedReader bufferedReader = new BufferedReader(
//                    inputStreamReader);
//            String line;
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            inputStreamReader.close();
//            bufferedReader.close();
//            resultString = stringBuilder.toString();
//            Log.i("TAG", stringBuilder.toString());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }




        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bmob.initialize(this, Constants.shi_BMOB_SDK_KEY);
//        Log.d("li", "onCreate:  Config.getInstance().user"+ Config.getInstance().user.getObjectId());
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.xunfei_app_id));
        Log.d("xiang", " main onCreate: 初始化讯飞 ");
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        inf();
//        login("zj","00000000");
    }
//    private void login(String username, String password) {
//        BmobUser user = new BmobUser();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.login(new SaveListener<BmobUser>() {
//            @Override
//            public void done(BmobUser bmobUser, BmobException e) {
//                if (e == null && bmobUser != null) {
//                    Log.d(TAG, "done:登录 ");
//                    Config.getInstance().user = bmobUser;
//                } else {
//
//                }
//            }
//        });
//    }

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
