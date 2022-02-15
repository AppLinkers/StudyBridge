package com.example.studybridge.http;

import com.example.studybridge.http.dto.UserLoginReq;
import com.example.studybridge.http.dto.UserLoginRes;
import com.example.studybridge.http.dto.UserSignUpReq;
import com.example.studybridge.http.dto.UserSignUpRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface UserAuth {

    @Multipart
    @POST("user/auth/sign")
    Call<UserSignUpRes> signUp(@Part UserSignUpReq userSignUpReq);

    @POST("user/auth/")
    Call<Object> login(@Body UserLoginReq userLoginReq);

    @GET("user/id")
    Call<String> valid(@Query("userLoginId") String userLoginId);

}
