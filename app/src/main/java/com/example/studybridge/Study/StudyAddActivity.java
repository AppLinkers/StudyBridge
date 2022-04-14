package com.example.studybridge.Study;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.study.StudyMakeReq;
import com.example.studybridge.http.dto.study.StudyMakeRes;
import com.example.studybridge.http.dto.study.StudyUpdateReq;
import com.example.studybridge.http.dto.study.StudyUpdateRes;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyAddActivity extends AppCompatActivity {

    private TextInputEditText titleEt,introEt,maxNumEt,explainEt;
    private TextView addTxt;
    private ChipGroup subjectGroup,placeGroup;
    private ImageView backBtn;
    private LinearLayout addBtn;

    String subject;
    String studyPlace;
    String userId;

    private DataService dataService = new DataService();
    private StudyFindRes study;
    private Long userPkId;

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_add_activity);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        userPkId = sharedPreferences.getLong(USER_PK_ID_KEY, 0);


        //화면 위 데이터
        titleEt = findViewById(R.id.study_add_title);
        introEt = findViewById(R.id.study_add_intro);
        maxNumEt = findViewById(R.id.study_add_max);
        explainEt = findViewById(R.id.study_add_explain);
        backBtn = (ImageView) findViewById(R.id.study_add_backBtn);
        addBtn = (LinearLayout) findViewById(R.id.study_add_btn);
        addTxt = (TextView) findViewById(R.id.study_addTxt);

        //지역 선택 chip
        subjectGroup = (ChipGroup) findViewById(R.id.study_add_subjectGroup);
        placeGroup = (ChipGroup) findViewById(R.id.study_add_placeGroup);

        Intent intent = getIntent();
        study = (StudyFindRes) intent.getSerializableExtra("study");

        setPath();

    }

    private void setPath(){
        if(study==null){ //추가하기로 들어왔을 때

        }
        else{ //수정하기로 들어왔을 때
            addTxt.setText("수정하기");
            setUpdateData();
        }
        setAddBtn();
    }

    private void setUpdateData(){
        titleEt.setText(study.getName());
        introEt.setText(study.getInfo());
        maxNumEt.setText(String.valueOf(study.getMaxNum()));
        explainEt.setText(study.getExplain());

        Chip chipForSubject = (Chip) subjectGroup.getChildAt(checkChipForSubject(study.getType()));
        Chip chipForPlace = (Chip) placeGroup.getChildAt(checkChipForPlace(study.getPlace()));
        chipForSubject.setChecked(true);
        chipForPlace.setChecked(true);
    }

    private void setAddBtn(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                if(study==null){
                    StudyMakeReq studyMakeReq = new StudyMakeReq(
                            userId,
                            titleEt.getText().toString()+"",
                            subject,
                            introEt.getText().toString()+"",
                            explainEt.getText().toString()+"",
                            studyPlace,
                            Integer.parseInt(maxNumEt.getText().toString()+""));

                    dataService.study.make(studyMakeReq).enqueue(new Callback<StudyMakeRes>() {
                        @Override
                        public void onResponse(Call<StudyMakeRes> call, Response<StudyMakeRes> response) {
                            if (response.isSuccessful()) {
                                Log.d("test", String.valueOf(response.raw()));
                                Toast.makeText(StudyAddActivity.this, "추가가 완료되었습니다", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<StudyMakeRes> call, Throwable t) {

                        }
                    });
                }
                else{
                    StudyUpdateReq studyUpdateReq = new StudyUpdateReq(
                            study.getId(),
                            userPkId,
                            titleEt.getText().toString()+"",
                            subject,introEt.getText().toString()+"",
                            explainEt.getText().toString(),
                            Integer.parseInt(maxNumEt.getText().toString()+""));
                    dataService.study.update(studyUpdateReq).enqueue(new Callback<StudyUpdateRes>() {
                        @Override
                        public void onResponse(Call<StudyUpdateRes> call, Response<StudyUpdateRes> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(StudyAddActivity.this, "업데이트 완료!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<StudyUpdateRes> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }

    //기존 과목&&지역 선택 함수
    public static int checkChipForSubject(String str){
        if(str!=null){
            switch (str)
            {
                case "영어" :
                    return 0;
                case "수학" :
                    return 1;
                case "개발" :
                    return 2;
                case "기타" :
                    return 3;
                default:
                    return 0;

            }
        } else {
            return 0;
        }
    }
    public static int checkChipForPlace(String str){

        if(str!=null){
            switch (str)
            {
                case "서울" :
                    return 0;
                case "경기" :
                    return 1;
                case "인천" :
                    return 2;
                case "기타" :
                    return 3;
                default:
                    return 0;

            }
        } else {
            return 0;
        }

    }

}
