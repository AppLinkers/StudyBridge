package com.example.studybridge.http;

import com.example.studybridge.http.dto.Message;
import com.example.studybridge.http.dto.FindRoomRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Chat {
    @GET("/message/{room_id}")
    Call<List<Message>> messageList(@Path("room_id") Long room_id);

    @GET("/room")
    Call<FindRoomRes> getRoom(@Query("studyId") Long studyId);
}
