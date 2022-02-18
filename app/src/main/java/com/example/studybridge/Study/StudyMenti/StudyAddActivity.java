package com.example.studybridge.Study.StudyMenti;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.google.android.material.textfield.TextInputEditText;

public class StudyAddActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText subjectEt;
    private TextInputEditText titleEt;
    private TextInputEditText studyPlaceEt;
    private TextInputEditText maxNumEt;
    private TextInputEditText studyIntroEt;

    String subject;
    String title;
    String studyPlace;
    int maxNum;
    String studyIntro;
    StudyMenti study;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_menti_add);

        toolbar = findViewById(R.id.study_toolbar);

        //툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subjectEt = findViewById(R.id.study_add_subject);
        titleEt = findViewById(R.id.study_add_title);
        studyPlaceEt = findViewById(R.id.study_add_place);
        maxNumEt = findViewById(R.id.study_add_max);
        studyIntroEt = findViewById(R.id.study_add_intro);




    }

    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.study_toolbar_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.study_add:
                subject = subjectEt.getText().toString();
                title = titleEt.getText().toString();
                studyPlace = studyPlaceEt.getText().toString();
                maxNum = Integer.parseInt(maxNumEt.getText().toString());
                studyIntro = studyIntroEt.getText().toString();


                study = new StudyMenti(0, subject, studyPlace, title,studyIntro,maxNum);
                Toast.makeText(getApplicationContext(), study.getSubject(), Toast.LENGTH_SHORT).show();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
