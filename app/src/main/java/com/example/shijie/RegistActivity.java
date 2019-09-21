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

public class RegistActivity extends AppCompatActivity {
    private ImageView ivBack;
    private EditText etUserName;
    private  EditText etUserPwd;
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
//                checkregist();
            }
        });

    }

//    private void checkregist() {
//        if(TextUtils.isEmpty(etUserName.getText())){
//            Toast.makeText(this,"用户名为空",Toast.LENGTH_SHORT);
//            return;
//        }
//        if(etUserName.getText().toString().length() > 15){
//            toast(getResources().getString(R.string.user_name_long));
//            return;
//        }
//        if(TextUtils.isEmpty(etUserPwd.getText())){
//            toast(getResources().getString(R.string.password_null));
//            return;
//        }
//        if(etUserPwd.getText().toString().length() > 15){
//            toast(getResources().getString(R.string.password_long));
//            return;
//        }
//        if(TextUtils.isEmpty(etUserPwdSecond.getText())){
//            toast(getResources().getString(R.string.password_second_null));
//            return;
//        }
//        if(!etUserPwd.getText().toString().equals(etUserPwdSecond.getText().toString())){
//            toast(getResources().getString(R.string.password_unequals));
//            return;
//        }

//    }

    private void init() {
        ivBack= findViewById(R.id.iv_back);
        etUserName = findViewById(R.id.et_user_name);
        etUserPwd = findViewById(R.id.et_user_pwd);
        etUserPwdSecond = findViewById(R.id.et_user_pwd_second);
        btnRegist = findViewById(R.id.btn_regist);
        tvTitle = findViewById(R.id.tv_title);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("注册");
    }
}
