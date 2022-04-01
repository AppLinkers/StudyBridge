package com.example.studybridge.http;

import com.example.studybridge.http.dto.assignedToDo.ChangeToDoStatusReq;
import com.example.studybridge.http.dto.assignedToDo.ChangeToDoStatusRes;
import com.example.studybridge.http.dto.assignedToDo.ConfirmToDoReq;
import com.example.studybridge.http.dto.assignedToDo.ConfirmToDoRes;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.example.studybridge.http.dto.toDo.ToDoStatus;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AssignedToDo {

    @POST("/toDo/assigned/status")
    Call<ChangeToDoStatusRes> changeStatus(@Body ChangeToDoStatusReq changeToDoStatusReq);

    @POST("/toDo/assigned/confirm")
    Call<ConfirmToDoRes> confirm(@Body ConfirmToDoReq confirmToDoReq);

    @GET("/toDo/assigned/mentee/cnt")
    Call<Integer> countByMentee(@Query("menteeId") Long menteeId);

    @GET("/toDo/assigned/mentee/status/cnt")
    Call<Integer> countByMenteeAndStatus(@Query("menteeId") Long menteeId, @Query("status") ToDoStatus status);

    @GET("toDo/assigned/mentee/studyId/cnt")
    Call<Map<String, Integer>> countByMenteeAndStudy(@Query("menteeId") Long menteeId, @Query("studyId") Long studyId);

    @GET("toDo/assigned/mentee/status")
    Call<List<FindAssignedToDoRes>> findByMenteeAndStatus(@Query("menteeId") Long menteeId, @Query("status") ToDoStatus status);

    @GET("/toDo/assigned/{toDoId}")
    Call<List<FindAssignedToDoRes>> findByToDo(@Path("toDoId") Long toDoId);

    @GET("/toDo/assigned/mentee")
    Call<List<FindAssignedToDoRes>> findByMentee(@Query("menteeId") Long menteeId);
}
