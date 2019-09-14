package com.example.shijie.beans;

import com.example.shijie.beans.Poetry;

import java.io.Serializable;

/**
 * @author L
 * @desc 飞花令游戏中数据bean
 */
public class FlyiingOrderBean implements Serializable{
    private int userType;//用户类型， 0 用户； 1 机器
    private Poetry poetry;
    private String content;//内容
    private int status;//状态 0 表示机器状态(不需要处理)； 1 表示成功匹配; 2 表示匹配到，但只有半句匹配;
    // 3表示没有匹配正确，但是数据库中有该诗句；4 表示没有匹配正确，且数据库中没有该诗句;
    //5 表示以前输入过该诗句
    private int roundCount;//回合数

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public Poetry getPoetry() {
        return poetry;
    }

    public void setPoetry(Poetry poetry) {
        this.poetry = poetry;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
