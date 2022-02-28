package com.example.studybridge.http.dto;

import java.util.List;

public class ProfileRes {

    private String userName;

    private String location;

    private String info;

    private String nickName;

    private String school;

    private String schoolImg;

    private String subject;

    private List<String> certificatesImg;

    private String experience;

    private String curriculum;

    private String appeal;

    @Override
    public String toString() {
        return "ProfileRes{" +
                "userName='" + userName + '\'' +
                ", location='" + location + '\'' +
                ", info='" + info + '\'' +
                ", nickName='" + nickName + '\'' +
                ", school='" + school + '\'' +
                ", schoolImg='" + schoolImg + '\'' +
                ", subject='" + subject + '\'' +
                ", certificatesImg=" + certificatesImg +
                ", experience='" + experience + '\'' +
                ", curriculum='" + curriculum + '\'' +
                ", appeal='" + appeal + '\'' +
                '}';
    }

    public ProfileRes(String userName, String location, String info, String nickName, String school, String schoolImg, String subject, List<String> certificatesImg, String experience, String curriculum, String appeal) {
        this.userName = userName;
        this.location = location;
        this.info = info;
        this.nickName = nickName;
        this.school = school;
        this.schoolImg = schoolImg;
        this.subject = subject;
        this.certificatesImg = certificatesImg;
        this.experience = experience;
        this.curriculum = curriculum;
        this.appeal = appeal;
    }

    public String getExperience() {
        return experience;
    }

    public String getUserName() {
        return userName;
    }

    public String getLocation() {
        return location;
    }

    public String getInfo() {
        return info;
    }

    public String getNickName() {
        return nickName;
    }

    public String getSchool() {
        return school;
    }

    public String getSchoolImg() {
        return schoolImg;
    }

    public String getSubject() {
        return subject;
    }

    public List<String> getCertificatesImg() {
        return certificatesImg;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public String getAppeal() {
        return appeal;
    }
}
