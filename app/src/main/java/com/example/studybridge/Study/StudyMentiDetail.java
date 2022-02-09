package com.example.studybridge.Study;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDoAddDialog;
import com.example.studybridge.ToDo.ToDoDetailActivity;

public class StudyMentiDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView subject,place,peopleNum,status,intro;

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

        StringBuilder sb = new StringBuilder();
        sb.append("1 / ").append(intent.getExtras().getString("peopleNum")).append("명");
        String peopleStr = sb.toString();



        //툴바 설정
        toolbar.setTitle(intent.getExtras().getString("name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //넘어온 데이터 입력
        subject.setText(intent.getExtras().getString("subject"));
        place.setText(intent.getExtras().getString("place"));
        status.setText(intent.getExtras().getString("status"));
        intro.setText(intent.getExtras().getString("intro"));
        peopleNum.setText(peopleStr);

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