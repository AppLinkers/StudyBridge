package com.example.studybridge.Study.StudyMenti;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.studybridge.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class StudyMentiFilterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Chip english,math,dev,etcSubject,seoul,geongi,incheon,etcPlace;
    private AppCompatButton filterBtn;

    private ArrayList<String> selectedSubject;
    private ArrayList<String> selectedPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_menti_filter_activity);

        toolbar = (Toolbar) findViewById(R.id.menti_filter_bar);

        //툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //chip 설정

        //과목 chip
        english = (Chip) findViewById(R.id.menti_filter_subjectEnglish);
        math = (Chip) findViewById(R.id.menti_filter_subjectMath);
        dev = (Chip) findViewById(R.id.menti_filter_subjectDev);
        etcSubject = (Chip) findViewById(R.id.menti_filter_subjectEtc);

        //지역 chip
        seoul = (Chip) findViewById(R.id.menti_filter_placeSeoul);
        geongi = (Chip) findViewById(R.id.menti_filter_placeGeongi);
        incheon = (Chip) findViewById(R.id.menti_filter_placeIncheon);
        etcPlace = (Chip) findViewById(R.id.menti_filter_placeEtc);

        selectedSubject = new ArrayList<>();
        selectedPlace = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener checkedChangeListenerForSubject = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedSubject.add(buttonView.getText().toString());
                }
                else
                {
                    selectedSubject.remove(buttonView.getText().toString());
                }
            }
        };
        CompoundButton.OnCheckedChangeListener checkedChangeListenerForPlace = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedPlace.add(buttonView.getText().toString());
                }
                else
                {
                    selectedPlace.remove(buttonView.getText().toString());
                }
            }
        };

        english.setOnCheckedChangeListener(checkedChangeListenerForSubject);
        math.setOnCheckedChangeListener(checkedChangeListenerForSubject);
        dev.setOnCheckedChangeListener(checkedChangeListenerForSubject);
        etcSubject.setOnCheckedChangeListener(checkedChangeListenerForSubject);

        seoul.setOnCheckedChangeListener(checkedChangeListenerForPlace);
        geongi.setOnCheckedChangeListener(checkedChangeListenerForPlace);
        incheon.setOnCheckedChangeListener(checkedChangeListenerForPlace);
        etcPlace.setOnCheckedChangeListener(checkedChangeListenerForPlace);



        //버튼설정
        filterBtn = findViewById(R.id.menti_filter_btn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("subject",selectedSubject.toString());
                resultIntent.putExtra("place",selectedPlace.toString());
                setResult(101,resultIntent);
                finish();
            }
        });

    }

    //toolbar 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menti_filter_menu,menu);
        return true;
    }

//    //뒤로가기 설정
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
