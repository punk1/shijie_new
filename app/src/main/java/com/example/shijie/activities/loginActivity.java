package com.example.shijie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shijie.R;
import com.example.shijie.base.Constants;
import com.example.shijie.beans.Config;
import com.example.shijie.utils.DESUtil;
import com.example.shijie.utils.SPUtil;
import com.example.shijie.utils.StringUtils;
import com.example.shijie.widget.CustomProgressDialog;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class loginActivity extends AppCompatActivity {
//    private TextView tvTitle;
    private EditText et_user_name;
    private EditText et_user_pwd;
    private Button btn_login;
    private  TextView btn_regist;
    private AppCompatCheckBox cbRemember;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Constants.shi_BMOB_SDK_KEY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
        initViewdata();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklogin();
            }
        });
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });

    }
    private void checklogin(){
        if (TextUtils.isEmpty(et_user_name.getText())) {
            Toast.makeText(this,"用户名为空",Toast.LENGTH_SHORT);
            return;
        }
        if (et_user_name.getText().toString().length() > 15) {
            Toast.makeText(this,"用户名太长",Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(et_user_pwd.getText())) {
            Toast.makeText(this,"密码为空",Toast.LENGTH_SHORT);
            return;
        }
        if (et_user_pwd.getText().toString().length() > 15) {
            Toast.makeText(this,"密码太长",Toast.LENGTH_SHORT);
            return;
        }

        CustomProgressDialog.showLoading(this,getResources().getString(R.string.logining), false);

        login(et_user_name.getText().toString(),et_user_pwd.getText().toString());

    }

    private void login(String toString, String toString1) {
        Log.d("login", "login: "+toString);
        BmobUser bmobUser =new BmobUser();
        bmobUser.setUsername(toString);
        bmobUser.setPassword(toString1);
        bmobUser.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null&&bmobUser != null){
                    Config.getInstance().user = bmobUser;
                    loadSuccess();
                }else{
                    CustomProgressDialog.stopLoading();
                    Toast.makeText(loginActivity.this,"登录失败",Toast.LENGTH_SHORT);
                }
            }
        });


    }

    private void loadSuccess() {
        CustomProgressDialog.stopLoading();
        //登录成功后保存账号
        SPUtil.setParam(this,"username",et_user_name.getText().toString());
        //登录成功后保存是否开启了记住密码
        SPUtil.setParam(this,"checed",cbRemember.isChecked());
        //如果点击了记住密码，则保存密码，但需要加密, 如果未记住密码，则保存空
        if(cbRemember.isChecked()){
            SPUtil.setParam(this,"password", DESUtil.Encryption(et_user_pwd.getText().toString()));
        }else{
            SPUtil.setParam(this,"password", "");
        }
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);
        finish();
    }

    private void initView() {
//        tvTitle = findViewById(R.id.tv_title);
        cbRemember = findViewById(R.id.cb_remember);
        et_user_name = findViewById(R.id.et_user_name);
        et_user_pwd = findViewById(R.id.et_user_pwd);
        btn_login= findViewById(R.id.btn_login);
        btn_regist= findViewById(R.id.btn_regist);
    }

    public void initViewdata(){
//        tvTitle.setText("登录");

        String username = (String) SPUtil.getParam(this, "username", "");
        String password = (String) SPUtil.getParam(this, "password", "");
        boolean isChecked = (boolean) SPUtil.getParam(this,"checed",false);
        cbRemember.setChecked(isChecked);
        if(!StringUtils.isEmpty(username)){//如果密码不为空，且账号不为空，则进行自动登录
            et_user_name.setText(username);
            et_user_name.setSelection(username.length());
            if(!StringUtils.isEmpty(password)){//如果密码不为空，在将密码设置到密码输入框
                et_user_pwd.setText(DESUtil.Decryption(password));
            }
        }
    }
}
