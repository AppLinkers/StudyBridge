package com.example.studybridge.Mypage.MentoProfile;

import android.graphics.Bitmap;

public class MyPageMentoCertiInfo {

    private Bitmap qualiImg;
    private String qualiName;

    public MyPageMentoCertiInfo(){}
    public MyPageMentoCertiInfo(Bitmap qualiImg, String qualiName) {
        this.qualiImg = qualiImg;
        this.qualiName = qualiName;
    }

    public Bitmap getQualiImg() {
        return qualiImg;
    }

    public void setQualiImg(Bitmap qualiImg) {
        this.qualiImg = qualiImg;
    }

    public String getQualiName() {
        return qualiName;
    }

    public void setQualiName(String qualiName) {
        this.qualiName = qualiName;
    }
}
