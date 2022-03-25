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
    public Study study = retrofitClient.create(Study.class);
    public UserMentor userMentor = retrofitClient.create(UserMentor.class);
    public UserMentee userMentee = retrofitClient.create(UserMentee.class);
    public Chat chat = retrofitClient.create(Chat.class);
    public ToDo toDo = retrofitClient.create(ToDo.class);
    public S3 s3 = retrofitClient.create(S3.class);
    public AssignedToDo assignedToDo = retrofitClient.create(AssignedToDo.class);

}
