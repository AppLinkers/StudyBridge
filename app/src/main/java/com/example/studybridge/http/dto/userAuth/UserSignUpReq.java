package com.example.studybridge.http.dto.userAuth;


public class UserSignUpReq {

    private String name;

    private String loginId;

    private String loginPw;

    private String role;

    private String phone;

    private String gender;

    private String location;

    private Boolean personalInfo;

    public UserSignUpReq(String name, String loginId, String loginPw, String role, String phone, String gender, String location, Boolean personalInfo) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.role = role;
        this.phone = phone;
        this.gender = gender;
        this.location = location;
        this.personalInfo = personalInfo;
    }
}
