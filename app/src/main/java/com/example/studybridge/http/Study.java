package com.example.studybridge.http;

import com.example.studybridge.http.dto.study.ChangeStatusReq;
import com.example.studybridge.http.dto.study.ChooseMentorRes;
import com.example.studybridge.http.dto.study.StudyApplyReq;
import com.example.studybridge.http.dto.study.StudyApplyRes;
import com.example.studybridge.http.dto.study.StudyDeleteReq;
import com.example.studybridge.http.dto.study.StudyDeleteRes;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.study.StudyMakeReq;
import com.example.studybridge.http.dto.study.StudyMakeRes;
import com.example.studybridge.http.dto.study.StudyOutReq;
import com.example.studybridge.http.dto.study.StudyOutRes;
import com.example.studybridge.http.dto.study.StudyUpdateReq;
import com.example.studybridge.http.dto.study.StudyUpdateRes;
import com.example.studybridge.http.dto.study.StudyWithdrawReq;
import com.example.studybridge.http.dto.study.StudyWithdrawRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Study {

    @POST("study/make")
    Call<StudyMakeRes> make(@Body StudyMakeReq studyMakeReq);

    @GET("study")
    Call<List<StudyFindRes>> find();

    @GET("study/user")
    Call<List<StudyFindRes>> findByUserId(@Query("userId") Long userId);

    @GET("study/isApplied")
    Call<Boolean> isApplied(@Query("studyId") Long studyId, @Query("userLoginId") String userLoginId);

    @POST("study/apply")
    Call<StudyApplyRes> apply(@Body StudyApplyReq studyApplyReq);

    @POST("study/withdraw")
    Call<StudyWithdrawRes> withdraw(@Body StudyWithdrawReq studyWithdrawReq);

    @GET("study/mentor/candidate")
    Call<List<String>> mentorList(@Query("studyId") Long studyId);

    @GET("study/mentee")
    Call<List<String>> menteeList(@Query("studyId") Long studyId);

    @GET("study/maker")
    Call<String> maker(@Query("studyId") Long studyId);

    @POST("study/status")
    Call<String> status(@Body ChangeStatusReq changeStatusReq);

    @GET("study/status")
    Call<String> studyStatus(@Query("studyId") Long studyId);

    @DELETE("study/mentor")
    Call<Integer> deleteMentor(@Query("studyId") Long studyId);

    @POST("study/mentor")
    Call<ChooseMentorRes> chooseMentor(@Query("studyId") Long studyId, @Query("mentorLoginId") String mentorLoginId);

    @GET("study/mentor")
    Call<String> chosenMentor(@Query("studyId") Long studyId);

    @POST("study/delete")
    Call<StudyDeleteRes> delete(@Body StudyDeleteReq studyDeleteReq);

    @POST("study/update")
    Call<StudyUpdateRes> update(@Body StudyUpdateReq studyUpdateReq);

    @POST("study/out")
    Call<StudyOutRes> studyOut(@Body StudyOutReq studyOutReq);
}
