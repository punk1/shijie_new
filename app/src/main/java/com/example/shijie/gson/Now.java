package com.example.shijie.gson;

import com.google.gson.annotations.SerializedName;

//当前天气信息

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond_code")
    public String condcode;

    @SerializedName("cond_txt")
    public String condtxt;


}
