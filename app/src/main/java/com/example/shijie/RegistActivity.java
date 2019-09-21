package com.example.shijie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shijie.widget.CustomProgressDialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegistActivity extends AppCompatActivity {
    private ImageView ivBack;
    private EditText et_user_name;
    private  EditText et_user_pwd;
    private  EditText etUserPwdSecond;
    private Button btnRegist;
    private  TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkregist();
            }
        });

    }

    private void checkregist() {
        if(TextUtils.isEmpty(et_user_name.getText())){
            Toast.makeText(this,"用户名为空",Toast.LENGTH_SHORT);
            return;
        }
        if(et_user_name.getText().toString().length() > 15){
            Toast.makeText(this,"用户名太长",Toast.LENGTH_SHORT);
            return;
        }
        if(TextUtils.isEmpty(et_user_pwd.getText())){
            Toast.makeText(this,"密码为空",Toast.LENGTH_SHORT);
            return;
        }
        if(et_user_pwd.getText().toString().length() > 15){
            Toast.makeText(this,"密码太长",Toast.LENGTH_SHORT);
            return;
        }
        if(TextUtils.isEmpty(etUserPwdSecond.getText())){
            Toast.makeText(this,"请确认密码",Toast.LENGTH_SHORT);
            return;
        }
        if(!et_user_pwd.getText().toString().equals(etUserPwdSecond.getText().toString())){
            Toast.makeText(this,"两次密码不匹配",Toast.LENGTH_SHORT);
            return;
        }

        CustomProgressDialog.showLoading(this,getResources().getString(R.string.loading),false);

        regist(et_user_name.getText().toString(),et_user_pwd.getText().toString());

    }

    private void regist(String toString, String toString1) {
        BmobUser user = new BmobUser();
        user.setUsername(toString);
        user.setPassword(toString1);
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    loadsuc();
                    finish();
                } else {
                   loadfail();
                }
            }
        });

    }

    private void loadfail() {
        CustomProgressDialog.stopLoading();
        Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT);
    }

    private void loadsuc() {
        CustomProgressDialog.stopLoading();
        Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT);
    }

    private void init() {
        ivBack= findViewById(R.id.iv_back);
        et_user_name = findViewById(R.id.et_user_name);
        et_user_pwd = findViewById(R.id.et_user_pwd);
        etUserPwdSecond = findViewById(R.id.et_user_pwd_second);
        btnRegist = findViewById(R.id.btn_regist);
        tvTitle = findViewById(R.id.tv_title);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("注册");
    }
}
