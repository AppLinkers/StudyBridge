package com.example.studybridge.http;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface S3 {

    @Multipart
    @POST("/s3/chat/one")
    public Call<String> chatImg(@Part MultipartBody.Part image);

}
