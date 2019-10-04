package com.example.shijie.beans;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {
    String Name;
    String Content;
    String biao_id;

    public String getBiao_id() {
        return biao_id;
    }

    public void setBiao_id(String biao_id) {
        this.biao_id = biao_id;
    }

    public Comment() {

    }

    public String getName() {
        return Name;
    }

    public String getContent() {
        return Content;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setContent(String content) {
        Content = content;
    }
}
