package com.example.studybridge.Study.StudyMenti.Detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMenti;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.ChangeStatusReq;
import com.example.studybridge.http.dto.ProfileRes;
import com.example.studybridge.http.dto.StudyApplyReq;
import com.example.studybridge.http.dto.StudyApplyRes;
import com.example.studybridge.http.dto.StudyFindRes;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentiDetail extends AppCompatActivity {

    // 화면 위 데이터들
    private Toolbar toolbar;
    private TextView subject,place,peopleNum,status,intro,enrollList,mentorList;
    private StudyMenti study;
    private MaterialButton BtnForMaker,BtnForMentee,BtnForMento;


    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";

    //스터디에 신청했는지
//    boolean hasAuth = false;


    //역할 체크
    private String userId; // 사용자 아이디
    private Long studyId;  // 스터디 아이디
    private String managerId;  //방장 아이디

    // 신청한 멘티 수
    private int enrollCount;
    private boolean isApply;

    //멘토 리사이클러뷰
    private StudyMentiEnrollMentoAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    //데이터 서비스
    DataService dataService = new DataService();

    public static final String MENTEE_APPLY = "APPLY";
    public static final String MENTO_APPLY = "WAIT";
    public static final String CONFIRM_APPLY = "MATCHED";

//    public static final int selectMento = 121;
    int roleInt; //임시데이터


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.study_menti_detail_activity);

        //sharedPreference, 현재 이용자 아이디 불러옴
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");


        //holder에서 데이터 불러오기
        Intent intent = getIntent();
        study = (StudyMenti)intent.getSerializableExtra("study");
        studyId = study.getId();
        enrollCount = intent.getIntExtra("enrollMentiNum",1);
        managerId = intent.getStringExtra("managerId");

        //툴바 설정
        toolbar = (Toolbar) findViewById(R.id.menti_detial_bar);
        toolbar.setTitle(study.getStudyName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //화면 위 데이터
        status = (TextView) findViewById(R.id.menti_detail_status);
        subject = (TextView) findViewById(R.id.menti_detail_subject);
        place = (TextView) findViewById(R.id.menti_detail_place);
        intro = (TextView) findViewById(R.id.menti_detail_intro);
        peopleNum = (TextView) findViewById(R.id.menti_detail_peopleNum);

        subject.setText(study.getSubject());
        place.setText(study.getPlace());
        status.setText(study.statusStr());
        intro.setText(study.getStudyIntro());
        peopleNum.setText(enrollCount + " / "+study.getMaxNum()+" 명");

        //버튼 (역할마다 상태 바꿔주기 위함)
        BtnForMaker = (MaterialButton) findViewById(R.id.menti_detail_BtnForMaker); // 방장용
        BtnForMentee = (MaterialButton) findViewById(R.id.menti_detail_BtnForMentee);  // 방장 외 멘티 용
        BtnForMento = (MaterialButton) findViewById(R.id.menti_detail_BtnForMento); // 멘토 용

        //멘토&멘티 리스트
        enrollList = findViewById(R.id.enroll_members);
        mentorList = (TextView) findViewById(R.id.enroll_mentor);


        checkStudyStatus();

//        Toast.makeText(getApplicationContext(),roleInt + managerId,Toast.LENGTH_SHORT).show();



        //멘토 리사이클러뷰
        recyclerView = (RecyclerView) findViewById(R.id.menti_detail_enrollMento_RV);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new StudyMentiEnrollMentoAdapter();



        /*@SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, List<String>> listAPI = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                Call<List<String>> call = dataService.study.mentorList(studyId);
                try {
                    for(String res : call.execute().body()) {
                        adapter.addItem(res);
                    }
                    adapter.setStudyId(studyId);
                    recyclerView.setAdapter(adapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<String> s) {super.onPostExecute(s);}
        }.execute();

        List<String> result = null;

        try {
            result = listAPI.get();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }



    //상단 메뉴 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //(1) 역할을 정해준다 (방장, 멘티, 멘토)
    public void checkRole(){

        if(userId.equals(managerId)){
            BtnForMaker.setVisibility(View.VISIBLE);
            BtnForMento.setVisibility(View.GONE);
            BtnForMentee.setVisibility(View.GONE);
        } else {


            dataService.userAuth.isMentee(userId).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.body()) {//멘티인 경우
                        BtnForMaker.setVisibility(View.GONE);
                        BtnForMento.setVisibility(View.GONE);
                        BtnForMentee.setVisibility(View.VISIBLE);
                    } else { //멘토인 경우
                        BtnForMaker.setVisibility(View.GONE);
                        BtnForMento.setVisibility(View.VISIBLE);
                        BtnForMentee.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });
        }
    }

    //(2) 상태마다 역할마다 버튼의 기능, 텍스트를 달리한다
    public void checkStudyStatus(){

        AsyncTask<Void, Void, String> listAPI = new AsyncTask<Void, Void, String>() {
            @SuppressLint({"ResourceAsColor", "StaticFieldLeak"})
            @Override
            protected String doInBackground(Void... params) {
                Call<String> call = dataService.study.studyStatus(studyId);
                try {
                    String response = call.execute().body();
                    checkRole();

                    if (response.equals(MENTEE_APPLY)){
                        //멘티 모집중
                        BtnForMaker.setText("모집 종료하기");
                        BtnForMaker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //모집 종료 함수 작성
                                toWait();
                            }
                        });

                        applyStudy(BtnForMentee);

                        BtnForMento.setText("멘티 모집중입니다");
                        BtnForMento.setBackgroundColor(R.color.disableBtn);
                        BtnForMento.setEnabled(false);

                   } else if (response.equals(MENTO_APPLY)){
                         //멘토 모집중

                        BtnForMaker.setText("멘토 모집중입니다");
                        BtnForMaker.setBackgroundColor(R.color.disableBtn);
                        BtnForMaker.setEnabled(false);

                        BtnForMentee.setText("멘토 모집중입니다");
                        BtnForMentee.setBackgroundColor(R.color.disableBtn);
                        BtnForMentee.setEnabled(false);

                        applyStudy(BtnForMento);
                    } else {
                        //모집 완료
                        matchedBtn(BtnForMaker);
                        matchedBtn(BtnForMentee);
                        matchedBtn(BtnForMento);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {super.onPostExecute(s);}
        }.execute();

        String result = null;

        try {
            result = listAPI.get();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public void matchedBtn(Button button){
        button.setText("스터디 시작하기");
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //스터디 시작하기 함수 작성
            }
        });
    }


    //스터디 신청 메서드
    public void applyStudy(Button button){

        dataService.study.isApplied(studyId,userId).enqueue(new Callback<Boolean>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    if(response.body()==true){
                        //이미 신청 했으면
                        button.setEnabled(false);
                        button.setBackgroundColor(R.color.disableBtn);
                        button.setText("신청 완료");
                    } else {
                        //신청 전이면
                        button.setEnabled(true);
                        button.setText("신청하기");

                        StudyApplyReq studyApplyReq = new StudyApplyReq(userId,studyId);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dataService.study.apply(studyApplyReq).enqueue(new Callback<StudyApplyRes>() {
                                    @Override
                                    public void onResponse(Call<StudyApplyRes> call, Response<StudyApplyRes> response) {
                                        if(response.isSuccessful()){
                                            button.setEnabled(false);
                                            button.setBackgroundColor(R.color.disableBtn);
                                            button.setText("신청 완료");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<StudyApplyRes> call, Throwable t) {

                                    }
                                });
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

    }

    //멘토 모집으로 넘어가는 메서드
    public void toWait(){
        ChangeStatusReq csReq = new ChangeStatusReq(studyId,MENTO_APPLY);

        dataService.study.status(csReq).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////
    //버튼 변경
/*    public void setButton(int status){
        switch (status){
            case 0: //멘티 모집중
                setRole(roleInt);

                BtnForMaker.setText("모집 종료");
                BtnForMaker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //모집 종료 함수 작성
                        toWait();
                    }
                });

                applyStudy(BtnForMentee);

                BtnForMento.setText("멘티 모집중입니다");
                BtnForMento.setEnabled(false);

            case 1: //멘토 모집중
                setRole(roleInt);

                BtnForMaker.setText("멘토 모집중입니다");
                BtnForMaker.setEnabled(false);

                BtnForMentee.setText("멘토 모집중입니다");
                BtnForMentee.setEnabled(false);

                applyStudy(BtnForMento);


            case 2: //모집 완료
                setRole(roleInt);

                BtnForMaker.setText("스터디 시작하기");
                BtnForMaker.setEnabled(true);
                BtnForMaker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //스터디 시작하기 함수 작성
                    }
                });

                BtnForMento.setText("스터디 시작하기");
                BtnForMento.setEnabled(true);
                BtnForMento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //스터디 시작하기 함수 작성
                    }
                });


                BtnForMentee.setText("스터디 시작하기");
                BtnForMentee.setEnabled(true);
                BtnForMentee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //스터디 시작하기 함수 작성
                    }
                });

        }

    }*/








    //////////////////////////////////////////////////////////////////////////////////////////
    //스터디 상태
/*    public void checkStudyStatus(Long studyId){
        dataService.study.studyStatus(studyId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    if(response.body().equals("APPLY")){
                        dataService.userAuth.isMentee(userId).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.body()){
                                    startStudy.setText("멘토 모집하기");
                                }else{
                                    startStudy.setText("스터디 시작하기");
                                }
                            }
                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                    }else if(response.body().equals("WAIT")){
                        startStudy.setText("스터디 시작하기");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }*/

/*    //스터디 대기
    public void toWait(){
        ChangeStatusReq csReq =  new ChangeStatusReq(studyId, "WAIT");
        dataService.study.status(csReq).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    startStudy.setText("스터디 시작하기");
                    Toast.makeText(StudyMentiDetail.this, "멘토모집이 시작되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }*/

/*    //스터디 신청
    public void applyStudy(View view) {

        //give auth to mentee for the study.
        StudyApplyReq studyApplyReq = new StudyApplyReq(userId, studyId);

        dataService.study.apply(studyApplyReq).enqueue(new Callback<StudyApplyRes>() {
            @Override
            public void onResponse(Call<StudyApplyRes> call, Response<StudyApplyRes> response) {
                if (response.isSuccessful()) {
                    applyBtn.setText("신청완료");
                    applyBtn.setEnabled(false);
                }
            }
            @Override
            public void onFailure(Call<StudyApplyRes> call, Throwable t) {  }
        });
    }*/
    //스터디 상태
/*    public void checkStudyStatus(Long studyId){
        dataService.study.studyStatus(studyId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    if(response.body().equals("APPLY")){
                        dataService.userAuth.isMentee(userId).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.body()){
                                    startStudy.setText("멘토 모집하기");
                                }else{
                                    startStudy.setText("스터디 시작하기");
                                }
                            }
                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                    }else if(response.body().equals("WAIT")){
                        startStudy.setText("스터디 시작하기");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }*/

    /*//버튼 변경
    public void buttonChange(){
        if(hasAuth){
            checkAuth();
        }else{
            checkMentee();
        }

    }




    //스터디 시작하기
    public void startStudy(View view) {
        if(startStudy.getText().equals("스터디 시작하기")){
            toMatched();
        }else if(startStudy.getText().equals("멘토 모집하기")){
            toWait();
        }
    }

    //매치 완료
    public void toMatched(){
        ChangeStatusReq csReq =  new ChangeStatusReq(studyId, "MATCHED");
        dataService.study.status(csReq).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText(StudyMentiDetail.this, "스터디가 시작되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }







    // 최대인원
    public void overMaxNum(){
        dataService.study.studyStatus(studyId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    if(response.body().equals("APPLY")){
                        toWait();
                    }else if(response.body().equals("WAIT")){
                        toMatched();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }*/

    // 멘토로 선정
/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            switch (requestCode)
            {
                case selectMento:
            }
        }
        else if(resultCode==RESULT_CANCELED)
        {

        }
    }*/
}
