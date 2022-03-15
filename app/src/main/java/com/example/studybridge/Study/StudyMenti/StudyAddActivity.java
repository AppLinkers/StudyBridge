package com.example.studybridge.Study.StudyMenti;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.StudyMakeReq;
import com.example.studybridge.http.dto.StudyMakeRes;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyAddActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText titleEt,introEt,maxNumEt,explainEt;
    private ChipGroup subjectGroup,placeGroup;

    String subject;
    String title;
    String studyPlace;
    int maxNum;
    String studyIntro;
//    String studyExplain;
    StudyMenti study;

    private DataService dataService = new DataService();

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    SharedPreferences sharedPreferences;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_menti_add);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        toolbar = findViewById(R.id.study_toolbar);

        //툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        titleEt = findViewById(R.id.study_add_title);
        introEt = findViewById(R.id.study_add_intro);
        maxNumEt = findViewById(R.id.study_add_max);
        explainEt = findViewById(R.id.study_add_intro);

        //지역 선택 chip
        subjectGroup = (ChipGroup) findViewById(R.id.study_add_subjectGroup);
        placeGroup = (ChipGroup) findViewById(R.id.study_add_placeGroup);





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

                title = titleEt.getText().toString()+"";
                maxNum = Integer.parseInt(maxNumEt.getText().toString()+"");
                studyIntro = introEt.getText().toString()+"";
//                studyExplain = explainEt.getText().toString()+"";
                for(int i=0; i<subjectGroup.getChildCount();i++){
                    Chip chip = (Chip) subjectGroup.getChildAt(i);
                    if(chip.isChecked()){
                        subject = chip.getText().toString();
                    }
                }
                for(int i=0; i<placeGroup.getChildCount();i++){
                    Chip chip = (Chip) placeGroup.getChildAt(i);
                    if(chip.isChecked()){
                        studyPlace = chip.getText().toString();
                    }
                }


                study = new StudyMenti(0L, 0, subject, studyPlace, title,studyIntro,maxNum);

                StudyMakeReq studyMakeReq = new StudyMakeReq(userId, title, subject, studyIntro, studyPlace, maxNum);

                dataService.study.make(studyMakeReq).enqueue(new Callback<StudyMakeRes>() {
                    @Override
                    public void onResponse(Call<StudyMakeRes> call, Response<StudyMakeRes> response) {
                        if (response.isSuccessful()) {
                            Log.d("test", String.valueOf(response.raw()));
                            Toast.makeText(StudyAddActivity.this, "추가가 완료되었습니다. ", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<StudyMakeRes> call, Throwable t) {

                    }
                });

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
