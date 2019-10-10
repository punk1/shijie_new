package com.example.shijie.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shijie.R;

public  class TocangActivity extends AppCompatActivity {
    private EditText cang_tou_id;
    private TextView cang_tou_text;
    private TextView cang_tou_qi;
    private TextView cang_tou_wu;
    private TextView sheng_cheng;
    private ImageView back_mine;
    private Integer type;

    private Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cang_tou_title);
        initVIew();
        cang_tou_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cang_tou_qi.setTextColor(context.getResources().getColor(R.color.Main_color));
                cang_tou_qi.setText("七言诗");
                type  = 2;
            }
        });
        sheng_cheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(type ==  1){
                  String text = "七千万里共鹏抟。十五年前意已阑。华月满天秋色好。诞辰嘉节颂椒盘";
                  text = text.replaceAll("。","\n");
                  cang_tou_text.setText(text);
              }else {
                  String text = "七旬如昨夜。十里走尘埃。华发今何在。诞辰浩浩来";
                  text = text.replaceAll("。","\n");
                  cang_tou_text.setText(text);
              }
            }
        });
        back_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cang_tou_wu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cang_tou_qi.setTextColor(context.getResources().getColor(R.color.Main_color));
                cang_tou_qi.setText("五言诗");
                type = 1;
            }
        });

    }
    public void  initVIew (){
        cang_tou_id = findViewById(R.id.cang_tou_id);
        cang_tou_text = findViewById(R.id.cang_tou_text);
        cang_tou_qi =findViewById(R.id.cang_tou_qi);
        sheng_cheng = findViewById(R.id.sheng_cheng);
        back_mine = findViewById(R.id.back_mine);
        cang_tou_wu = findViewById(R.id.cang_tou_wu);
    }
}
