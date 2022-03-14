package com.example.studybridge.http;

import com.example.studybridge.http.dto.LikeMentorRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserMentee {

    @POST("user/mentee/like")
    Call<LikeMentorRes> likeMentor(@Query("menteeId") Long menteeId, @Query("mentorId") Long mentorId);

    @POST("user/mentee/unlike")
    Call<LikeMentorRes> unLikeMentor(@Query("menteeId") Long menteeId, @Query("mentorId") Long mentorId);

    @GET("user/mentee/like")
    Call<List<LikeMentorRes>> findLikedMentors(@Query("menteeId") Long menteeId);
}
