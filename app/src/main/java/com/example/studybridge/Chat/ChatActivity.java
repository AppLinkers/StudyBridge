package com.example.studybridge.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMenti;
import com.example.studybridge.http.DataService;
import com.google.gson.Gson;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    SharedPreferences sharedPreferences;


    StudyMenti study;
    String userName;
    String userId;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        study = (StudyMenti) intent.getSerializableExtra("study");

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userName= sharedPreferences.getString(USER_NAME, "사용자");
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(study.getStudyName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}