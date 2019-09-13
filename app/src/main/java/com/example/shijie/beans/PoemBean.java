package com.example.shijie.beans;

import java.io.Serializable;

public class PoemBean implements Serializable {
    private String poemTitle;
    private String poemYear;
    private String poemAuthor;
    private String correctPoem;
    private String poemId;

    public String getPoemId() {
        return poemId;
    }

    public void setPoemId(String poemId) {
        this.poemId = poemId;
    }

    public String getPoemTitle() {
        return poemTitle;
    }

    public void setPoemTitle(String poemTitle) {
        this.poemTitle = poemTitle;
    }

    public String getPoemYear() {
        return poemYear;
    }

    public void setPoemYear(String poemYear) {
        this.poemYear = poemYear;
    }

    public String getPoemAuthor() {
        return poemAuthor;
    }

    public void setPoemAuthor(String poemAuthor) {
        this.poemAuthor = poemAuthor;
    }

    public String getCorrectPoem() {
        return correctPoem;
    }

    public void setCorrectPoem(String correctPoem) {
        this.correctPoem = correctPoem;
    }

//    public String getDisturbPoem() {
//        return disturbPoem;
//    }
//
//    public void setDisturbPoem(String disturbPoem) {
//        this.disturbPoem = disturbPoem;
//    }

}
