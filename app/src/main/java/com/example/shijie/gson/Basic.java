package com.example.shijie.gson;

import com.google.gson.annotations.SerializedName;

//所在城市的天气ID和更新时间
public class Basic {

    @SerializedName("location")        //注解,JSON字段和JAVA字段建立映射
    public String cityName;

    @SerializedName("cid")
    public String weatherId;

//    public Update update;

//    public class Update {
//
//        @SerializedName("loc")
//        public String updateTime;
//
//    }

}
