package com.example.studybridge;

import com.example.studybridge.domain.AccessToken;
import com.example.studybridge.domain.AuthData;
import com.example.studybridge.domain.Certification;

import java.security.cert.Certificate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.*;

public interface IamportClient {

    @POST("/users/getToken")//인증
    Call<AccessToken>token(@Body AuthData auth);

    @GET("/certifications/{imp_uid}")//내 인증번호 가져올때
    Call<Certification> certification_by_imp_uid(
            @Header("Authorization") String token,
            @Path("imp_uid") String imp_uid
    );
}
