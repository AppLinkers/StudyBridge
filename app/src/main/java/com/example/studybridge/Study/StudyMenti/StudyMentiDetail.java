package com.example.studybridge.Study.StudyMenti;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentiDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView subject,place,peopleNum,status,intro,enrollList,mentorList;
    private StudyMenti study;
    private Button applyBtn;
    private Button startStudy;

    DataService dataService = new DataService();

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";

    boolean hasAuth = false;

    SharedPreferences sharedPreferences;
    String userId;
    Long studyId;

    int enrollCount;

    boolean isMentee;


    @SuppressLint("ResourceAsColor")
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
        enrollList = findViewById(R.id.enroll_members);
        applyBtn = findViewById(R.id.applyBtn);
        mentorList = (TextView) findViewById(R.id.enroll_mentor);
        startStudy = (Button) findViewById(R.id.startStudy);

        enrollCount = 0;


        Intent intent = getIntent();
        study = (StudyMenti)intent.getSerializableExtra("study");
        hasAuth = intent.getBooleanExtra("hasAuth",false);



        studyId = study.getId();
        subject.setText(study.getSubject());
        place.setText(study.getPlace());
        status.setText(study.statusStr());
        intro.setText(study.getStudyIntro());


        //툴바 설정
        toolbar.setTitle(study.getStudyName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // 신청된 menteeLoginIdList
        dataService.study.menteeList(studyId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    enrollList.setText(response.body().toString());
                    enrollCount = response.body().size();
                    StringBuilder sb = new StringBuilder();
                    sb.append(enrollCount+"").append("/").append(study.getMaxNum()+"").append("명");
                    String peopleStr = sb.toString();
                    peopleNum.setText(peopleStr);
//                    if(response.body().get(0).equals(userId)){
//                        //방장일 경우에
//                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        dataService.study.mentorList(studyId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()) {
                    mentorList.setText(response.body().toString());

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });




        if(hasAuth){
            applyBtn.setEnabled(false);
            applyBtn.setText("이미 신청하셨습니다");
            applyBtn.setBackgroundColor(R.color.disableBtn);
        }else{

            dataService.userAuth.isMentee(userId).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.body()){
                        applyBtn.setText("스터디 신청하기");
                    }else{
                        applyBtn.setText("멘토 신청하기");
                    }
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });

        }

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

    public void applyStudy(View view) {

        //give auth to mentee for the study.
        StudyApplyReq studyApplyReq = new StudyApplyReq(userId, studyId);

        dataService.study.apply(studyApplyReq).enqueue(new Callback<StudyApplyRes>() {
            @Override
            public void onResponse(Call<StudyApplyRes> call, Response<StudyApplyRes> response) {
                if (response.isSuccessful()) {
                    applyBtn.setText("신청완료");
                    applyBtn.setEnabled(false);
//                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
//                    intent.putExtra("study", study);
//                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<StudyApplyRes> call, Throwable t) {  }
        });
    }





    public void isManager(String manager, String userId){
        if(manager.equals(userId)){
            startStudy.setVisibility(View.VISIBLE);
            applyBtn.setVisibility(View.GONE);
        }
    }


    public void startStudy(View view) {
        

    }
}
