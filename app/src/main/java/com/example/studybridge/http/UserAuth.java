package com.example.studybridge.http;

import com.example.studybridge.http.dto.userAuth.UserLoginReq;
import com.example.studybridge.http.dto.userAuth.UserSignUpReq;
import com.example.studybridge.http.dto.userAuth.UserSignUpRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UserAuth {

    @POST("user/auth/sign")
    Call<UserSignUpRes> signUp(@Body UserSignUpReq userSignUpReq);

    @POST("user/auth/")
    Call<Object> login(@Body UserLoginReq userLoginReq);

    @GET("user/auth/id")
    Call<String> valid(@Query("userLoginId") String userLoginId);

    @GET("user/auth/role")
    Call<Boolean> isMentee(@Query("userLoginId") String userLoginId);

}
