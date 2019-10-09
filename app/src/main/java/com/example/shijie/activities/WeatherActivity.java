package com.example.shijie.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shijie.R;
import com.example.shijie.gson.Forecast;
import com.example.shijie.gson.Weather;
import com.example.shijie.service.AutoUpdateService;
import com.example.shijie.util.HttpUtil;
import com.example.shijie.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static org.litepal.LitePalApplication.getContext;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    private TextView titleCity;

//    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherCondCode;

    private TextView weatherCondTxt;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;

    private String mweatherId;

    public DrawerLayout drawerLayout;

    private Button navButton;

    private TextView firstPoetry;

    private TextView secondPoetry;

    private String[] poetry;

    private String weathertype;

    private int refreshPicture = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        // 初始化控件
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        bingPicImg = findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
//        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
//        weatherCondCode = (TextView) findViewById(R.id.weather_info_text);
        weatherCondTxt = (TextView) findViewById(R.id.weather_info_text);
        firstPoetry = (TextView) findViewById(R.id.tv_poetry1);
        secondPoetry = (TextView) findViewById(R.id.tv_poetry2);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
//        aqiText = (TextView) findViewById(R.id.aqi_text);
//        pm25Text = (TextView) findViewById(R.id.pm25_text);
//        comfortText = (TextView) findViewById(R.id.comfort_text);
//        carWashText = (TextView) findViewById(R.id.car_wash_text);
//        sportText = (TextView) findViewById(R.id.sport_text);
        degreeText = (TextView) findViewById(R.id.degree_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);      //读取必应图片缓存
        String weatherString = prefs.getString("weather", null);        //读取天气信息缓存
        String weatherForecastString = prefs.getString("weather_forecast",null);
//        String bingPic = prefs.getString("bing_pic", null);
//        if (bingPic != null) {
//            Glide.with(this).load(bingPic).into(bingPicImg);        //存在缓存,直接加载
//        } else {
//            loadBingPic();      //没有缓存,重新调用方法请求
//        }



        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mweatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            // 无缓存时去服务器查询天气
            mweatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mweatherId);
        }

        if(weatherForecastString != null){
            Weather weather = Utility.handleWeatherResponse1(weatherForecastString);
            showWeatherForecast(weather);
        }else{
            requestWeatherForecast(mweatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mweatherId);
                refreshPicture = (refreshPicture + 1)%5;
                Log.d("numbers", "onRefresh: "+refreshPicture);
                if (refreshPicture == 0){refreshPicture++;}
                bingPicImg.getDrawable().setLevel(refreshPicture);
                bingPicImg.setVisibility(View.VISIBLE);

            }
        });
    }

    //
    //根据天气id请求城市天气信息。
    //
    public void requestWeather(final String weatherId) {
        String weatherUrl = "https://free-api.heweather.net/s6/weather/now?location=" + weatherId + "&key=3666caf5c800424b88a2f3230c45a7fc";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                Log.d("aaaa", "weatherid" + weather.basic.weatherId);//JSON数据转换为Weather实体类
//                Log.d("aaaa", "onResponse: "+weather.status);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {     //切换到主线程,更新UI
                        if (weather != null && "ok".equals(weather.status)) {
                            Log.d("aaaa", "run: status " + weather.status);
                            //向PreferenceManager中缓存数据
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mweatherId = weather.basic.weatherId;
                            Log.d("aaaa", "runmweatherd: " + mweatherId);
                            showWeatherInfo(weather);
                        } else {

                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });



    }

    private void requestWeatherForecast(final String weatherId){
        final String weatherUrl_forecast = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=ed37c3ce0eae4f82ab568842d0268105";
        HttpUtil.sendOkHttpRequest(weatherUrl_forecast, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText_forecast = response.body().string();
                final Weather weather_forecast = Utility.handleWeatherResponse1(responseText_forecast);
                Log.d("aaaa", "weatherid" + weather_forecast.basic.weatherId);//JSON数据转换为Weather实体类
//                Log.d("aaaa", "onResponse: "+weather.status);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {     //切换到主线程,更新UI
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString("weather_forecast", responseText_forecast);
                        editor.apply();
//                        if (weather_forecast != null && "ok".equals(weather_forecast.status)) {
//                            Log.d("aaaa", "run: status " + weather_forecast.status);
//                            mweatherId = weather_forecast.basic.weatherId;
//                            Log.d("aaaa", "runmweatherd: " + mweatherId);
                        showWeatherForecast(weather_forecast);
//                        } else {
//                            Toast.makeText(WeatherActivity.this, "获取天气预报信息失败", Toast.LENGTH_SHORT).show();
//                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气预报信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    //
    //处理并展示Weather实体类中的数据。
    //
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String condcode = weather.now.condcode;
        String condtxt = weather.now.condtxt;
        weathertype = condtxt;


        Log.d("aaa==", degree);
        titleCity.setText(cityName);
//        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
//        weatherCondCode.setText(condcode);
        weatherCondTxt.setText(condtxt);
//        forecastLayout.removeAllViews();
//        for (Forecast forecast : weather.forecastList) {
//            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
//            TextView dateText = (TextView) view.findViewById(R.id.date_text);
//            TextView infoText = (TextView) view.findViewById(R.id.info_text);
//            TextView maxText = (TextView) view.findViewById(R.id.max_text);
//            TextView minText = (TextView) view.findViewById(R.id.min_text);
//            dateText.setText(forecast.date);
//            infoText.setText(forecast.more.info);
//            maxText.setText(forecast.temperature.max);
//            minText.setText(forecast.temperature.min);
//            forecastLayout.addView(view);
//        }
//        if (weather.aqi != null) {
//            aqiText.setText(weather.aqi.city.aqi);
//            pm25Text.setText(weather.aqi.city.pm25);
//        }
//        String comfort = "舒适度：" + weather.suggestion.comfort.info;
//        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
//        String sport = "运行建议：" + weather.suggestion.sport.info;
//        comfortText.setText(comfort);
//        carWashText.setText(carWash);
//        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        getPoetryByWeather();
        Intent intent = new Intent(this, AutoUpdateService.class);
    }

    //
    //加载必应每日一图
    //
    private void loadBingPic() {

//        String fileName = "main.re"
//        Bitmap bm = BitmapFactory.decodeFile(fileName);
//        String requestBingPic = "http://guolin.tech/api/bing_pic";
//        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String bingPic = response.body().string();
//                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
//                editor.putString("bing_pic", bingPic);
//                editor.apply();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

    private void showWeatherForecast(Weather weathers){

//        Calendar calendar2 = Calendar.getInstance();
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
//        calendar2.add(Calendar.DATE, 3);
//        String three_days_after = sdf2.format(calendar2.getTime());
//        System.out.println(three_days_after);

        Log.d("10086", "showWeatherForecast: "+weathers.now.temperature);
        forecastLayout.removeAllViews();
        int i = 0;
        for (Forecast forecast :weathers.forecastList) {
            if(i<3){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
            }
            i++;
        }
    }

    private void getPoetryByWeather() {

        switch (weathertype) {
            case "晴":
            case "晴间多云":
                String address0 = "https://api.gushi.ci/tianqi/taiyang.txt";//写太阳
                okHttpPoetry(address0);
                break;
            case "阴":
            case "多云":
                String address1 = "https://api.gushi.ci/tianqi/xieyun.txt";//写云
                okHttpPoetry(address1);
                break;
            case "有风":
            case "微风":
            case "和风":
            case "清风":
            case "大风":
            case "风暴":
                String address2 = "https://api.gushi.ci/tianqi/xiefeng.txt";//写风
                okHttpPoetry(address2);
                break;
            case "阵雨":
            case "强阵雨":
            case "雷阵雨":
            case "强雷阵雨":
            case "小雨":
            case "中雨":
            case "大雨":
            case "细雨":
            case "暴雨":
            case "大暴雨":
            case "雨":
            case "小到中雨":
            case "中到大雨":
            case "大到暴雨":
            case "暴雨到大暴雨":
                String address3 = "https://api.gushi.ci/tianqi/xieyu.txt";//写雨
                okHttpPoetry(address3);
                break;
            case "小雪":
            case "中雪":
            case "大雪":
            case "暴雪":
            case "雨夹雪":
            case "雨雪天气":
            case "阵雨夹雪":
            case "阵雪":
            case "小到中雪":
            case "中到大雪":
            case "大到暴雪":
            case "雪":
                String address4 = "https://api.gushi.ci/tianqi/xieyu.txt";//写雪
                okHttpPoetry(address4);
                break;
            default:
                String address5 = "https://api.gushi.ci/all.txt";//全部
                okHttpPoetry(address5);
                break;

        }
    }

    private void okHttpPoetry(String address) {
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(), "诗词获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respondsTest = response.body().string();
                respondsTest = respondsTest.substring(0, respondsTest.length() - 1);
                Log.d("天气诗词", "onResponse: 根据天气获取到的诗句" + respondsTest);
                poetry = null;
                poetry = respondsTest.split("，");
                if (poetry.length < 2) {
                    getPoetryByWeather();
                    return;
                }
//                findPoetry = poetry[0];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        firstPoetry.setText(poetry[0]);

                        secondPoetry.setText(poetry[1]);

                        Log.d("天气诗句", "run: "+firstPoetry+secondPoetry);

                    }
                });

            }
        });
    }
}
