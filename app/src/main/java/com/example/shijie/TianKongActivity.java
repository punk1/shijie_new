package com.example.shijie;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shijie.beans.Config;
import com.example.shijie.beans.Poetry;
import com.example.shijie.beans.PoetryHistory;
import com.example.shijie.utils.LogUtil;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class TianKongActivity extends AppCompatActivity {
    private ImageView iv_back ;
    private TextView tv_title;
    private ImageView iv_title_right;
    private TextView Poemname;
    private TextView Poemauthor;
    private LinearLayout llContainer;
    private TextView tv_status;
    private Button Poem_ok;
    private Button Poem_next;

    private int poemType;
    private int schoolType;
    private List<Poetry> listData = new ArrayList<>();
    private Poetry current;
    private String[] content;
    private EditText[] editTexts;
    private String[] rights;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiankong);
        init();
        initData();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Poem_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasError = false;
                for (int i = 0, length = editTexts.length; i < length; i++) {
                    if (TextUtils.equals(rights[i], editTexts[i].getText().toString())) {
                        editTexts[i].setTextColor(Color.GREEN);
                    } else {
                        hasError = true;
                        editTexts[i].setTextColor(Color.RED);
                    }
                }
                if (!hasError) {
                    tv_status.setVisibility(View.VISIBLE);
                }

            }
        });

        Poem_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });



    }
    public void init(){
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        iv_title_right = findViewById(R.id.iv_title_right);
        Poemname = findViewById(R.id.name);
        Poemauthor = findViewById(R.id.zuozhe);
        llContainer = findViewById(R.id.ccc);
        tv_status = findViewById(R.id.status);
        Poem_ok = findViewById(R.id.ok);
        Poem_next = findViewById(R.id.next);
        tv_title.setText("诗词填空");
    }

    public void  initData(){
        BmobQuery<Poetry> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(100);
        bmobQuery.findObjects(new FindListener<Poetry>() {
            @Override
            public void done(List<Poetry> list, BmobException e) {
                if(e==null){
                    Log.d("you","查询成功：共"+list.size()+"条数据。");
                    queryDataSuc(list);
                }else{

                    queryDataFailed();
                }
            }
        });
    }

    private void queryDataSuc(List<Poetry> list) {
        if (listData != null) {
            listData.clear();
            listData.addAll(list);
            next();
        }

    }

    private void next() {
        tv_status.setVisibility(View.GONE);

        Random r = new Random();
        int next = r.nextInt(10000) % listData.size();
        Poetry shici = listData.get(next);
        current = shici;

        saveHistory(shici);

        Poemname.setText(shici.getP_name());
        Poemauthor.setText(shici.getP_author());
        String replace = shici.getP_content().replaceAll("[，。、？！!,.?]", "-");//将标点替换成-
        String[] split = replace.split("-");

        if(split.length > 0 && split[split.length-1].contains("：")){
            content = new String[split.length-1];
            for (int i = 0; i < split.length -1; i++) {
                content[i] = split[i];
            }
        }else{
            content = split;
        }

        int hang = content.length / 2;
        editTexts = new EditText[hang];
        rights = new String[hang];

        llContainer.removeAllViews();
        for (int i = 0; i < hang; i++) {
            int kong = r.nextInt(100) % 2;
            int layout = -1;
            if (kong == 0) {
                layout = R.layout.tinakong_item1;
            } else {
                layout = R.layout.tinakong_item;
            }
            View v = getLayoutInflater().inflate(layout, null);
            editTexts[i] = (EditText) v.findViewById(R.id.edit);
            if (kong == 0) {
                ((TextView) v.findViewById(R.id.txt)).setText(content[i * 2 + 1]);
                rights[i] = content[i * 2];
            } else {
                ((TextView) v.findViewById(R.id.txt)).setText(content[i * 2]);
                rights[i] = content[i * 2 + 1];
            }
            llContainer.addView(v);
        }
    }
    public void queryDataFailed(){
        Toast.makeText(this,"加载失败",Toast.LENGTH_SHORT);

    }
    private void saveHistory(Poetry mPoemBean) {
        final PoetryHistory history = new PoetryHistory();
        history.setP_id(mPoemBean.getObjectId());
        history.setP_author(mPoemBean.getP_author());
        history.setP_title(mPoemBean.getP_name());
        history.setU_id(Config.getInstance().user.getObjectId());
        history.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.d("you", "done:更新历史成功 ");
                }else{
                    Log.d("you","更新历史失败");
                    history.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                               Log.d("you","保存历史成功");
                            }else{
                               Log.d("you","保存历史失败");
                            }
                        }
                    });
                }
            }
        });
    }

}
