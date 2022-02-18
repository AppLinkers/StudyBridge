package com.example.studybridge.http;

import com.example.studybridge.http.dto.StudyFindRes;
import com.example.studybridge.http.dto.StudyMakeReq;
import com.example.studybridge.http.dto.StudyMakeRes;
import com.example.studybridge.http.dto.UserSignUpReq;
import com.example.studybridge.http.dto.UserSignUpRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Study {

    @POST("study/make")
    Call<StudyMakeRes> make(@Body StudyMakeReq studyMakeReq);

    @GET("study")
    Call<List<StudyFindRes>> find();
}
