package com.example.shijie.utils;

import android.widget.Toast;

import com.example.shijie.App;


/**
 * @author L
 * @date 2018/4/14 20:43

 * @desc toast工具类
 */
public class ToastUtil {

    private static Toast toast=null;

    public static void showSingleToast(String msg){
        showShort(msg);
    }
    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(String message) {
        if (null == toast) {
            toast = Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(String message) {
        if (null == toast) {
            toast = Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void showWithTime(String message, int duration) {
        if (null == toast) {
            toast = Toast.makeText(App.getContext(), message, duration);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        toast.show();
    }}
