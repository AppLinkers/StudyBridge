package com.example.studybridge.http.dto.userMentor;

import java.io.Serializable;
import java.util.List;

import retrofit2.http.Body;

public class ProfileRes implements Serializable {

    private Long userId;

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

    private Boolean liked;

    public ProfileRes(Long userId,
                      String userName,
                      String location,
                      String info,
                      String nickName,
                      String school,
                      String schoolImg,
                      String subject,
                      List<Certificate> certificates,
                      String experience,
                      String curriculum,
                      String appeal,
                      Boolean liked) {
        this.userId = userId;
        this.userName = userName;
        this.location = location;
        this.info = info;
        this.nickName = nickName;
        this.school = school;
        this.schoolImg = schoolImg;
        this.subject = subject;
        this.certificates = certificates;
        this.experience = experience;
        this.curriculum = curriculum;
        this.appeal = appeal;
        this.liked = liked;
    }

    @Override
    public String toString() {
        return "ProfileRes{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
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
                ", liked=" + liked +
                '}';
    }

    public Long getUserId() {
        return userId;
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

    public Boolean getLiked() {
        return liked;
    }
}
