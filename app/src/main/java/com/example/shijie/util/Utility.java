package com.example.shijie.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.shijie.db.City;
import com.example.shijie.db.County;
import com.example.shijie.db.Province;
import com.example.shijie.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    //处理服务器返回的省级数据

    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {       //遍历allProvinces,把数据保存到Province实体类中
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


     //处理服务器返回的市级数据

    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {      //遍历allCities,把数据保存到City实体类中
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


     //处理服务器返回的县级数据

    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {        //遍历allCounties,把数据保存到County实体类中
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


     //将返回的JSON数据解析成Weather实体类

    public static Weather handleWeatherResponse(String response) {
        Log.d("aaaa", "handleWeatherResponse: "+response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");     //将HeWeather标志的实例转换为JSONArray
            String weatherContent = jsonArray.getJSONObject(0).toString();      //取出JSONArray的第一项
            return new Gson().fromJson(weatherContent, Weather.class);      //将数据解析成Weather对象
        } catch (Exception e) {
            Log.d("aaaa","e"+e);
            e.printStackTrace();
        }
        return null;
    }

    public static Weather handleWeatherResponse1(String response) {
        Log.d("aaaa", "handleWeatherResponse: "+response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");     //将HeWeather标志的实例转换为JSONArray
            String weatherContent = jsonArray.getJSONObject(0).toString();      //取出JSONArray的第一项
            return new Gson().fromJson(weatherContent, Weather.class);      //将数据解析成Weather对象
        } catch (Exception e) {
            Log.d("aaaa","e"+e);
            e.printStackTrace();
        }
        return null;
    }
}
