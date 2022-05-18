package com.example.studybridge.http.dto.userAuth;

import java.util.Optional;

import okhttp3.MultipartBody;

public class UserProfileUpdateReq {

    private String loginId;

    private String name;

    private String phone;

    private Optional<MultipartBody.Part> profileImg;

    private String location;

    private String gender;

}
