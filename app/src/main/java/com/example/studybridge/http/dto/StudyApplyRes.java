package com.example.studybridge.http.dto;

public class StudyApplyRes {

    private String studyName;

    private String userName;

    private String userType;

    public StudyApplyRes(String studyName, String userName, String userType) {
        this.studyName = studyName;
        this.userName = userName;
        this.userType = userType;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
