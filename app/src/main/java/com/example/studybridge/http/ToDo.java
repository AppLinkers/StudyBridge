package com.example.studybridge.http;

import com.example.studybridge.http.dto.toDo.AssignToDoReq;
import com.example.studybridge.http.dto.toDo.AssignToDoRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ToDo {

    @POST("/toDo/")
    Call<AssignToDoRes> assign(@Body AssignToDoReq assignToDoReq);

    @GET("/toDo/mentor")
    Call<List<FindToDoRes>> findOfMentor(@Query("mentorId") Long mentorId);

    @GET("/toDo/study")
    Call<List<FindToDoRes>> findOfStudy(@Query("studyId") Long studyId, @Query("mentorId") Long mentorId);

    @POST("/toDo/delete")
    Call<Integer> delete(@Query("toDoId") Long toDoId);
}
