package com.example.studybridge.http;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface UserMentor {

    @POST("user/mentor/profile")
    Call<Object> profile(@PartMap Map<String, RequestBody> profileReq);
}
