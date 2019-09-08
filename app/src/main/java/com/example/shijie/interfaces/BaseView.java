package com.example.shijie.interfaces;

import android.os.Bundle;

/**
 * @author L
 * @date 2018/4/14 21:19

 * @desc
 */
public interface BaseView {
    void showProgress();
    void showProgress(String message);
    void showProgress(String message, boolean cancelable);
    void hideProgress();
    void toast(String message);
    void toastLong(String message);
    void toastWithTime(int time, String message);
    void startActivity(Class<?> clz);
    void startActivity(Class<?> clz, Bundle bundle);
    void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode);
}

