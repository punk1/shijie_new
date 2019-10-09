package com.example.shijie.activities;

import android.support.annotation.Nullable;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.shijie.R;
import com.example.shijie.adapter.IndicatorAdapter;
import com.example.shijie.base.Constants;
import com.example.shijie.beans.NewPoetry;
import com.example.shijie.beans.Poetry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {

    String resultString;
    String s ;

    private static  final  String TAG = "MainActivity";
    private IndicatorAdapter mIndicatoradapter;
    private ViewPager mcontentPager;
    private ImageView search_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Constants.shi_BMOB_SDK_KEY);
        setContentView(R.layout.shi);
//        for (int i = 1; i<=2;i++){
//            synchronized (MainActivity.class){
//                readTextFromSDcard(i);
//                laod(resultString);
//            }
//        }
        final  NewPoetry newPoetry = new NewPoetry();
        newPoetry.setNd("fsfhs");
        newPoetry.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });




    }
    private void laod(String jsondata){
        try {
            final NewPoetry newpoetry = new NewPoetry();
            JSONObject jsonObject = new JSONObject(jsondata);
            String con=null;
            con= jsonObject.getString("content");
            String fanyi=null;
            String shan =null;
            String author_desc=null;
            if (!jsonObject.isNull("fanyi")){

                fanyi= jsonObject.getString("fanyi");
                Log.d("haha", "laod: "+fanyi);
                newpoetry.setFanyi(fanyi);
            }

            String title =null;
            title= jsonObject.getString("name");
            if (!jsonObject.isNull("shangxi")){
                shan= jsonObject.getString("shangxi");
                Log.d("haha", "laod: "+shan);
                newpoetry.setShangxi(shan);
            }

            JSONObject jsonObject1 = jsonObject.getJSONObject("poet");
            String name =null;
            name=jsonObject1.getString("name");
            if(!jsonObject1.isNull("desc")){

                author_desc=jsonObject1.getString("desc");
                newpoetry.setAuthor_jianjie(author_desc);
                Log.d("cha", "laod: ");
            }


//            String author_desc=null;
//            author_desc=jsonObject1.getString("desc");
//            Log.d("tui", "laod: "+name);
            String dynasty =null;
            dynasty= jsonObject.getString("dynasty");

            newpoetry.setP_author(name);
            newpoetry.setP_name(title);
            newpoetry.setP_content(con);
            newpoetry.setP_source(dynasty);
            if (shan != null && fanyi!= null ){
                newpoetry.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {

                    }
                });
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

        private void readTextFromSDcard(int i) {
        InputStreamReader inputStreamReader;
        try {

            inputStreamReader = new InputStreamReader(getAssets().open("poetry_"+i+".json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
            resultString = stringBuilder.toString();
//            Log.d("haha", "readTextFromSDcard: "+resultString);
//            Log.i("TAG", stringBuilder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Bmob.initialize(this, Constants.shi_BMOB_SDK_KEY);
////        Log.d("li", "onCreate:  Config.getInstance().user"+ Config.getInstance().user.getObjectId());
//        SpeechUtility.createUtility(this, "appid=" + getString(R.string.xunfei_app_id));
//        Log.d("xiang", " main onCreate: 初始化讯飞 ");
//        setContentView(R.layout.activity_main);
//        initView();
//        initEvent();
//        inf();
////        login("zj","00000000");
//    }
////    private void login(String username, String password) {
////        BmobUser user = new BmobUser();
////        user.setUsername(username);
////        user.setPassword(password);
////        user.login(new SaveListener<BmobUser>() {
////            @Override
////            public void done(BmobUser bmobUser, BmobException e) {
////                if (e == null && bmobUser != null) {
////                    Log.d(TAG, "done:登录 ");
////                    Config.getInstance().user = bmobUser;
////                } else {
////
////                }
////            }
////        });
////    }
//
//    private void inf() {
//        LogUtil.d(TAG,"close");
//    }
//
//    private void initEvent() {
//        LogUtil.d(TAG,"creat");
//        mIndicatoradapter.setOnINdicatorTapClickListener(new IndicatorAdapter.OnINdicatorTapClickListener() {
//            @Override
//            public void onTapclick(int index) {
//                LogUtil.d(TAG,"click is --->" + index);
//                if (mcontentPager != null){
//                    mcontentPager.setCurrentItem(index);
//                }
//            }
//        });
//
//        search_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,searchActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//    }
//
//    private void initView() {
//        MagicIndicator magicIndicator= (MagicIndicator)findViewById(R.id.magic_indicator);
//        magicIndicator.setBackgroundColor(this.getResources().getColor(R.color.Main_color));
//
//        //创建Indicator的适配器
//
//        mIndicatoradapter = new IndicatorAdapter(this);
//        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdjustMode(true);
//        commonNavigator.setAdapter(mIndicatoradapter);
//        // ViewPager
//        mcontentPager = findViewById(R.id.content_pager);
//
//
//        // 创建内容适配器
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        MainContentAdapter mainContentAdapter = new MainContentAdapter(fragmentManager);
//
//        mcontentPager.setAdapter(mainContentAdapter);
//
//        //把ViewPager 于 指示器 绑定到一起
//        magicIndicator.setNavigator(commonNavigator);
//        ViewPagerHelper.bind(magicIndicator, mcontentPager);
//
//        search_btn = findViewById(R.id.search);
//
//    }
//

}
