package com.example.studybridge.http;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DataService {

    private String LOCAL_URL = "http://10.0.2.2:8080/";
    private String AWS_URL = "http://3.141.122.128:8080/";


    Retrofit retrofitClient = new Retrofit
            .Builder()
            .baseUrl(AWS_URL)
            .client(new OkHttpClient().newBuilder().build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public UserAuth userAuth = retrofitClient.create(UserAuth.class);


}
