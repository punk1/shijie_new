package com.example.shijie.fragments;


import com.example.shijie.R;

public class TabDb {
    /***
     * 获得底部所有项
     */
    public static String[] getTabsTxt() {
        String[] tabs = {"首页","交易","地点","我的"};
        return tabs;
    }
    /***
     * 获得所有碎片
     */
    public static Class[] getFramgent(){
        Class[] cls = {OneFm.class,SubscriptionFragment.class,quanFragment
                .class,FourFm.class};
        return cls ;
    }
    /***
     * 获得所有点击前的图片
     */
    public static int[] getTabsImg(){
        int[] img = {R.drawable.ic_home1,R.drawable.ic_home1,R.drawable.ic_home1,R.drawable.ic_home1};
        return img ;
    }
    /***
     * 获得所有点击后的图片
     */
    public static int[] getTabsImgLight(){
        int[] img = {R.drawable.ic_home2,R.drawable.ic_home2,R.drawable.ic_home2,R.drawable.ic_home2};
        return img ;
    }

}
