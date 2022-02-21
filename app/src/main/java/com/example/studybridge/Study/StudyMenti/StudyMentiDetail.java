package com.example.studybridge.Study.StudyMenti;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studybridge.Chat.ChatActivity;
import com.example.studybridge.R;

public class StudyMentiDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView subject,place,peopleNum,status,intro;
    private StudyMenti study;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_menti_detail_activity);

        toolbar = (Toolbar) findViewById(R.id.menti_detial_bar);
        subject = (TextView) findViewById(R.id.menti_detail_subject);
        place = (TextView) findViewById(R.id.menti_detail_place);
        peopleNum = (TextView) findViewById(R.id.menti_detail_peopleNum);
        status = (TextView) findViewById(R.id.menti_detail_status);
        intro = (TextView) findViewById(R.id.menti_detail_intro);



        Intent intent = getIntent();
        StudyMenti study = (StudyMenti)intent.getSerializableExtra("study");


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


    }

    public void applyStudy(View view) {

        //give auth to mentee for the study.

        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("study", study);
        startActivity(intent);
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
