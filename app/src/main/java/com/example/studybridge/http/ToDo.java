package com.example.studybridge.http;

import com.example.studybridge.http.dto.toDo.AssignToDoReq;
import com.example.studybridge.http.dto.toDo.AssignToDoRes;
import com.example.studybridge.http.dto.toDo.ChangeToDoStatusReq;
import com.example.studybridge.http.dto.toDo.ChangeToDoStatusRes;
import com.example.studybridge.http.dto.toDo.ConfirmToDoReq;
import com.example.studybridge.http.dto.toDo.ConfirmToDoRes;
import com.example.studybridge.http.dto.toDo.FeedBackToDoReq;
import com.example.studybridge.http.dto.toDo.FeedBackToDoRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ToDo {

    @POST("/toDo")
    Call<AssignToDoRes> assign(@Body AssignToDoReq assignToDoReq);

    @POST("/toDo/status")
    Call<ChangeToDoStatusRes> changeStatus(@Body ChangeToDoStatusReq changeToDoStatusReq);

    @POST("/toDo/feedBack")
    Call<FeedBackToDoRes> feedBack(@Body FeedBackToDoReq feedBackToDoReq);

    @POST("/toDo/confirm")
    Call<ConfirmToDoRes> confirm(@Body ConfirmToDoReq confirmToDoReq);

    @GET("/toDo/study")
    Call<List<FindToDoRes>> findOfStudy(@Query("studyId") Long studyId, @Query("mentorId") Long mentorId);

    @GET("/toDo/mentee")
    Call<Integer> countOfMentee(@Query("menteeId") Long menteeId);

    @GET("/toDo/mentee/confirmed")
    Call<List<FindToDoRes>> findConfirmedOfMentee(@Query("menteeId") Long menteeId);
}
