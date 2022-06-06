package com.example.studybridge.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SMS {

    @GET("sendSMS")
    Call<String> sendSMS(@Query("to") String to);
}
