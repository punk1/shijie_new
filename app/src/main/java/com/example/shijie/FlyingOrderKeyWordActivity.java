package com.example.shijie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shijie.R;
import com.example.shijie.widget.adapters.KeywordAdapter;

public class FlyingOrderKeyWordActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, TextWatcher {

    private ImageView ivBack;
    private   TextView tvTitle;
    private EditText etInputKeyword;
    private GridView gvKeyword;
    private Button btnSure;

    private String[] keywordArray;
    private KeywordAdapter keywordAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);
        init();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("you", "onClick: 点击.....");
                if(TextUtils.isEmpty(etInputKeyword.getText())){
                    Toast.makeText(getApplicationContext(),"未输入",Toast.LENGTH_SHORT);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("keyword",etInputKeyword.getText().toString());
                Log.d("tui", "onClick:keyword "+etInputKeyword.getText().toString());
                Intent intent = new Intent(FlyingOrderKeyWordActivity.this,FlyingOrderActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

    }

    public void init(){
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        etInputKeyword = findViewById(R.id.et_input_keyword);
        gvKeyword = findViewById(R.id.gv_keyword);
        btnSure = findViewById(R.id.btn_sure);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.set_keyword));
        initGridView();
    }
    private void initGridView() {
        keywordArray = getResources().getStringArray(R.array.keywords);
        keywordAdapter = new KeywordAdapter(this, keywordArray);
        gvKeyword.setAdapter(keywordAdapter);
        gvKeyword.setOnItemClickListener(this);
        etInputKeyword.addTextChangedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        etInputKeyword.setText(keywordArray[position]);
        //当内容过多时，通过设置 光标来 让该位置的 内容 显示
        etInputKeyword.setSelection(keywordArray[position].length());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //先重置所有文字为未选中状态
        gvKeyword.setItemChecked(-1,true);
        gvKeyword.clearChoices();
        //再遍历，如果列表里有和输入的文字一样的，则设置为选中状态
        for (int j = 0; j < keywordArray.length; j++) {
            if(s.toString().equals(keywordArray[j])){
                gvKeyword.setItemChecked(j,true);
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
