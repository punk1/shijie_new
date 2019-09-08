package com.example.shijie.base;

import android.app.Application;
import android.os.Handler;

import com.example.shijie.utils.LogUtil;


public class BaseApplication extends Application {

    private static android.os.Handler shandler = null;
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化LogUtil
        LogUtil.init(this.getPackageName(), false);
        shandler = new Handler();
    }
    public static Handler gethandler(){
        return shandler ;
    }
}
