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

    private List<Certificate> certificates;

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
                ", certificates=" + certificates +
                ", experience='" + experience + '\'' +
                ", curriculum='" + curriculum + '\'' +
                ", appeal='" + appeal + '\'' +
                '}';
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

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public String getExperience() {
        return experience;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public String getAppeal() {
        return appeal;
    }
}
