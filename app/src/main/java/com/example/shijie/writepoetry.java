package com.example.shijie;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shijie.beans.Config;
import com.example.shijie.beans.DynamicItem;
import com.example.shijie.presenters.IquanPresenter;
import com.example.shijie.widget.CustomProgressDialog;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class writepoetry extends AppCompatActivity {
    private ImageView quan_back;
    private TextView write_btn;
    private EditText write_title;
    private EditText write_content;
    private EditText write_zhushi;
    private boolean iswrite = false;

    private String poetry_title= null;
    private String poetry_content = null;
    private String poetry_zhushi = null;
    private String authoir_name;
    private String u_id;
    private Integer page ;
    private final DynamicItem dynamicItem = new DynamicItem();
    private IquanPresenter iquanPresenter ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_poetry);
        initView();
    }
    public void  initView(){
        quan_back = findViewById(R.id.quan_back);
        write_btn = findViewById(R.id.write_btn);
        write_title = findViewById(R.id.write_title);
        write_content = findViewById(R.id.write_content);
        write_zhushi = findViewById(R.id.write_zhushi);

        quan_back.setVisibility(View.VISIBLE);
        write_btn.setVisibility(View.VISIBLE);
        write_btn.setText("发布");

        quan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();

                poetry_title =write_title.getText().toString().trim();
                poetry_content = write_content.getText().toString().trim();
                poetry_zhushi = write_zhushi.getText().toString().trim();
                authoir_name = Config.getInstance().user.getUsername() ;
                u_id = Config.getInstance().user.getObjectId();

                //隐藏键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(write_zhushi.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


                dynamicItem.setTitle(poetry_title);
                dynamicItem.setQ_content(poetry_content);
                dynamicItem.setZhushi(poetry_zhushi);
                dynamicItem.setAuthor_name(authoir_name);
                dynamicItem.setAuthor_id(u_id);



                dynamicItem.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                       if(e==null){
                           IquanPresenter.getInstance().pullRefreshMore();
                           CustomProgressDialog.stopLoading();
                       }else{

                       }
                    }
                });





            }


        });

    }

    public void login(){
        CustomProgressDialog.showLoading(this,"发布中",false);
    }


}
