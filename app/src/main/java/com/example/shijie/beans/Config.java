package com.example.shijie.beans;

import cn.bmob.v3.BmobUser;

public class Config {
    private static Config instance = null;

    private Config(){
    }

    public static Config getInstance() {
        if(instance == null){
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }
    public BmobUser user;
}
