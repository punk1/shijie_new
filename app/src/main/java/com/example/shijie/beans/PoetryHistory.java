package com.example.shijie.beans;

import cn.bmob.v3.BmobObject;

public class PoetryHistory extends BmobObject {
    private String p_id;
    private String u_id;
    private String p_title;
    private String p_author;

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getP_title() {
        return p_title;
    }

    public void setP_title(String p_title) {
        this.p_title = p_title;
    }

    public String getP_author() {
        return p_author;
    }

    public void setP_author(String p_author) {
        this.p_author = p_author;
    }

}
