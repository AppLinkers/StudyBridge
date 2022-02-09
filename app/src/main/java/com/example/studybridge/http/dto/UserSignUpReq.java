package com.example.studybridge.http.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Multipart;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpReq {

    private String name;

    private String loginId;

    private String loginPw;

    private String role;

    private String phone;

    private String gender;

    private String location;

    private Multipart profileImg;

}
