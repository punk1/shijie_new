package com.example.shijie.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//总的实体类,用以引用各个天气实体类
public class Weather {


    public String status;


    public Basic basic;


    public Now now;

    public Update update;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;



//    @SerializedName("daily_forecast")
//    public List<Forecast> forecastList;     //用以表示未来几天天气的数组

}
