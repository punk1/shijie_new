package com.example.shijie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shijie.beans.PoemBean;
import com.example.shijie.beans.Poetry;
import com.example.shijie.fragments.BlankFragement;
import com.example.shijie.widget.adapters.BlankFragmentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class chuanguanActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private ImageView iv_back;
    private TextView tv_title;
    private  ImageView tv_title_right;
    private ViewPager mviewPager;
    private  TextView tvLastPoem;
    private TextView tvNextPoem;

    private List<Fragment> fragmentList = new ArrayList<>();
    private BlankFragmentAdapter fragmentAdapter;
    private int schoolType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tian_kong);
        init();
        iv_back.setVisibility(View.VISIBLE);
        initData();
        tvNextPoem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mviewPager.getCurrentItem();
                if(currentItem == fragmentList.size() -1){
                    return;
                }
                mviewPager.setCurrentItem(currentItem + 1);

            }
        });
        tvLastPoem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentItem = mviewPager.getCurrentItem();
                if(currentItem == 0){
                    return;
                }
                mviewPager.setCurrentItem(currentItem -1);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {

        BmobQuery<Poetry> query=new BmobQuery<Poetry>();


        query.findObjects(new FindListener<Poetry>() {
            @Override
            public void done(List<Poetry> list, BmobException e) {
                if(e ==null){
                    if(list!=null && list.size()>0){
                        Log.d("you", "done: list --> "+list.size());

                        loadSuc(list);
                    }else{
                       Toast.makeText(App.getContext(),"查询成功，无数据返回",Toast.LENGTH_SHORT);
                        Log.d("smile", "查询成功，无数据返回");
                    }
                }else{

                    Log.d("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });

    }

    private void loadSuc(List<Poetry> list) {

            Random random = new Random();
            for (int i = 0;i<5;i++) {
                int i1 = random.nextInt(200) % (list.size());
                Poetry poetry = list.get(i);
                PoemBean poemBean = new PoemBean();
                poemBean.setCorrectPoem(poetry.getP_content());
                poemBean.setPoemAuthor(poetry.getP_author());
                poemBean.setPoemTitle(poetry.getP_name());
                poemBean.setPoemYear(poetry.getP_source());
                poemBean.setPoemId(poetry.getObjectId());
                Fragment fragment = BlankFragement.newInstance(1, poemBean);
                fragmentList.add(fragment);
            }
        fragmentAdapter = new BlankFragmentAdapter(getSupportFragmentManager(), fragmentList);
        mviewPager.setAdapter(fragmentAdapter);
        mviewPager.setOffscreenPageLimit(fragmentList.size());
        mviewPager.addOnPageChangeListener(this);
    }


    public void init(){
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title_right = findViewById(R.id.iv_title_right);
        mviewPager = findViewById(R.id.main_view_pager);
        tvLastPoem = findViewById(R.id.tv_last_poem);
        tvNextPoem = findViewById(R.id.tv_next_poem);
        tv_title.setText("诗词闯关");
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if(0 == i){
            tvLastPoem.setVisibility(View.GONE);
            tvNextPoem.setVisibility(View.VISIBLE);
        } else if(i == fragmentList.size() -1){
            tvNextPoem.setVisibility(View.GONE);
            tvLastPoem.setVisibility(View.VISIBLE);
        } else{
            tvNextPoem.setVisibility(View.VISIBLE);
            tvLastPoem.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
