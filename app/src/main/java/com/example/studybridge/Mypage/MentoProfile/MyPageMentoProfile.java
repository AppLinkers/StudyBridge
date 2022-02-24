package com.example.studybridge.Mypage.MentoProfile;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyPageMentoProfile {

    private String name;
    private String place;
    private String subject;
    private String school;
    private String intro;
    private String nickName;
    private String curi;
    private String expeience;
    private String appeal;
    private File schoolImg;
    private List<File> certificateImg;
    private Bitmap quliImg; //자격증 이미지

    public MyPageMentoProfile(){

    }

    public MyPageMentoProfile(String name, String place, String subject, String school, String intro, String nickName, String curi, String expeience, String appeal, File schoolImg, List<File> certificateImg) {
        this.name = name;
        this.place = place;
        this.subject = subject;
        this.school = school;
        this.intro = intro;
        this.nickName = nickName;
        this.curi = curi;
        this.expeience = expeience;
        this.appeal = appeal;
        this.schoolImg = schoolImg;
        this.certificateImg = certificateImg;
    }

    //getter & setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCuri() {
        return curi;
    }

    public void setCuri(String curi) {
        this.curi = curi;
    }

    public String getAppeal() {
        return appeal;
    }

    public void setAppeal(String appeal) {
        this.appeal = appeal;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Bitmap getQuliImg() {
        return quliImg;
    }

    public void setQuliImg(Bitmap quliImg) {
        this.quliImg = quliImg;
    }

    public String getExpeience() {
        return expeience;
    }

    public void setExpeience(String expeience) {
        this.expeience = expeience;
    }
}
