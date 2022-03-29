package com.example.studybridge.http;

import com.example.studybridge.http.dto.feedBack.FindFeedBackRes;
import com.example.studybridge.http.dto.feedBack.WriteFeedBackReq;
import com.example.studybridge.http.dto.feedBack.WriteFeedBackRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FeedBack {

    @POST("feedBack/")
    Call<WriteFeedBackRes> write(@Body WriteFeedBackReq writeFeedBackReq);

    @GET("feedBack/assignedToDo")
    Call<List<FindFeedBackRes>> findByAssignedToDo(@Query("assignedToDoId") Long assignedToDoId);

    @POST("feedBack/delete")
    Call<Integer> delete(@Query("feedBackId") Long feedBackId);
}
