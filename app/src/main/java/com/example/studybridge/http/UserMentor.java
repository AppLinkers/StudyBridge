package com.example.studybridge.http;

import com.example.studybridge.http.dto.userMentor.ProfileRes;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface UserMentor {

    @Multipart
    @POST("user/mentor/profile")
    Call<ProfileRes> profile(@Part MultipartBody.Part schoolImg, @Part List<MultipartBody.Part> certificatesImg, @PartMap Map<String, RequestBody> profileTextReq);

    @GET("user/mentor/profile")
    Call<ProfileRes> getProfile(@Query("mentorLoginId") String mentorLoginId, @Query("userLoginId") String userLoginId);


    @GET("user/mentor/allProfile")
    Call<List<ProfileRes>> getAllProfile();
}
