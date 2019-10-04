package com.example.shijie.beans;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class DynamicItem extends BmobObject {
    private String title;
    private String q_content;
    private String zhushi;
    private String author_id;
    private String author_name;
    private Integer zan_id;
    public Integer getZan_id() {
        return zan_id;
    }

    public void setZan_id(Integer zan_id) {
        this.zan_id = zan_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }



    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }



    public String getTitle() {
        return title;
    }



    public String getZhushi() {
        return zhushi;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setZhushi(String zhushi) {
        this.zhushi = zhushi;
    }

    public String getQ_content() {
        return q_content;
    }

    public void setQ_content(String q_content) {
        this.q_content = q_content;
    }
}
