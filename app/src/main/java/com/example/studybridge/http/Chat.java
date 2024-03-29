package com.example.studybridge.http;

import com.example.studybridge.http.dto.message.FindRoomRes;
import com.example.studybridge.http.dto.message.MessageRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Chat {
    @GET("/message/{room_id}")
    Call<List<MessageRes>> messageList(@Path("room_id") Long room_id);

    @GET("/room")
    Call<FindRoomRes> getRoom(@Query("studyId") Long studyId);
}
