package com.example.shijie.beans;

import android.util.Log;

import cn.bmob.v3.BmobObject;

/**
 * @author L
 * @date 2018/4/18 19:32

 * @desc 对应bmob后台的poetry的数据表
 */
public class Poetry extends BmobObject {
    private String p_type;
    private String p_source;
    private String p_name;
    private String p_content;
    private String p_author;
    private Integer id;

    private String Author_jianjie;

    public String getAuthor_jianjie() {
        return Author_jianjie;
    }

    public void setAuthor_jianjie(String author_jianjie) {
        Author_jianjie = author_jianjie;
    }

    private String Fanyi;

    public String getFanyi() {
        return Fanyi;
    }

    public void setFanyi(String fanyi) {
        Fanyi = fanyi;
    }

    private String Shangxi;


    public void setShangxi(String shangxi) {
        Shangxi = shangxi;
    }

    public String getShangxi() {
        return Shangxi;
    }

    public String getTableName() {
        return "Poetry";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getP_type() {
        Log.d("tui",p_type.trim());
        return p_type.trim();
    }

    public void setP_type(String p_type) {
        this.p_type = p_type.trim();
    }

    public String getP_source() {
        return p_source.trim();
    }

    public void setP_source(String p_source) {
        this.p_source = p_source.trim();
    }

    public String getP_name() {
        return p_name.trim();
    }

    public void setP_name(String p_name) {
        this.p_name = p_name.trim();
    }

    public String getP_content() {
        return p_content.trim();
    }

    public void setP_content(String p_content) {
        this.p_content = p_content.trim();
    }

    public String getP_author() {
        return p_author.trim();
    }

    public void setP_author(String p_author) {
        this.p_author = p_author.trim();
    }
}

