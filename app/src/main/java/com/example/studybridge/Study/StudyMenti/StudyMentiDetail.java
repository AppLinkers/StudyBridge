package com.example.studybridge.Study.StudyMenti;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studybridge.Chat.ChatActivity;
import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.StudyApplyReq;
import com.example.studybridge.http.dto.StudyApplyRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentiDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView subject,place,peopleNum,status,intro;
    private StudyMenti study;

    DataService dataService = new DataService();

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";

    SharedPreferences sharedPreferences;
    String userId;
    Long studyId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        setContentView(R.layout.study_menti_detail_activity);

        toolbar = (Toolbar) findViewById(R.id.menti_detial_bar);
        subject = (TextView) findViewById(R.id.menti_detail_subject);
        place = (TextView) findViewById(R.id.menti_detail_place);
        peopleNum = (TextView) findViewById(R.id.menti_detail_peopleNum);
        status = (TextView) findViewById(R.id.menti_detail_status);
        intro = (TextView) findViewById(R.id.menti_detail_intro);



        Intent intent = getIntent();
        StudyMenti study = (StudyMenti)intent.getSerializableExtra("study");


        studyId = study.getId();
        subject.setText(study.getSubject());
        place.setText(study.getPlace());
        status.setText(study.statusStr());
        intro.setText(study.getStudyIntro());
        peopleNum.setText(study.getMaxNum()+"");

        Toast.makeText(getApplicationContext(), study.toString(), Toast.LENGTH_SHORT).show();

        StringBuilder sb = new StringBuilder();
        sb.append("1 / ").append(study.getMaxNum()+"").append("명");
        String peopleStr = sb.toString();



        //툴바 설정
        toolbar.setTitle(study.getStudyName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 신청된 userLoginIdList
        dataService.study.userList(studyId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    Log.d("test", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });


    }

    public void applyStudy(View view) {

        //give auth to mentee for the study.

        StudyApplyReq studyApplyReq = new StudyApplyReq(userId, studyId);

        dataService.study.apply(studyApplyReq).enqueue(new Callback<StudyApplyRes>() {
            @Override
            public void onResponse(Call<StudyApplyRes> call, Response<StudyApplyRes> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("study", study);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<StudyApplyRes> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
