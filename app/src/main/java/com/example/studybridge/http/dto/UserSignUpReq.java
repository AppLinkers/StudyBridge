package com.example.studybridge.http.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Multipart;

@Data
@NoArgsConstructor
@Builder
public class UserSignUpReq {

    private String name;

    private String loginId;

    private String loginPw;

    private String role;

    private String phone;

    private String gender;

    private String location;

    public UserSignUpReq(String name, String loginId, String loginPw, String role, String phone, String gender, String location) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.role = role;
        this.phone = phone;
        this.gender = gender;
        this.location = location;
    }
}
