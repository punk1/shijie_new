package com.example.shijie.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.example.shijie.R;

public class ToWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_weather);     //传入MainActivity的布局并显示
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);      //从缓存中读取数据,如果有则直接显示,
        if (prefs.getString("weather", null) != null) {                              //避免每次开启都要从服务器读取数据
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
