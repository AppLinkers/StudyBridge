package com.example.studybridge.Study.StudyMenti.Detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Chat.ChatActivity;
import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMenti;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.message.FindRoomRes;
import com.example.studybridge.http.dto.study.ChangeStatusReq;
import com.example.studybridge.http.dto.study.StudyApplyReq;
import com.example.studybridge.http.dto.study.StudyApplyRes;
import com.example.studybridge.http.dto.study.StudyDeleteReq;
import com.example.studybridge.http.dto.study.StudyDeleteRes;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentiDetail extends AppCompatActivity {

    // 화면 위 데이터들
    private Toolbar toolbar;
    private int studyMaxNum;
    private StudyMenti study;

    private TextView subject,place,peopleNum,maxNum,status,intro,mentolistTv,selectMentoTv,delBtn;
    private MaterialButton BtnForMaker,BtnForMentee,BtnForMento;

    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";

    //역할 체크
    private String userId; // 사용자 아이디
    private Long studyId;  // 스터디 아이디
    private String managerId;  //방장 아이디
    private Long userPkId;



    private RelativeLayout chosenMentoRL;
    private ImageView chosenMentoImg;
    private TextView chosenMentoId;

    // 신청한 멘티 수
    private int enrollCount;

    //멘토 리사이클러뷰
    private StudyMentiEnrollMentoAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    //데이터 서비스
    DataService dataService = new DataService();

    public static final String MENTEE_APPLY = "APPLY";
    public static final String MENTO_APPLY = "WAIT";
    public static final String CONFIRM_APPLY = "MATCHED";

    TextView enrollList;

    FindRoomRes findRoomRes;
/*    //resultForActivity 용
    public static final int selectMento = 121;*/



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.study_menti_detail_activity);

        //sharedPreference, 현재 이용자 아이디 불러옴
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        userPkId = sharedPreferences.getLong(USER_PK_ID_KEY, 0);


        //holder에서 데이터 불러오기
        Intent intent = getIntent();
        study = (StudyMenti) intent.getSerializableExtra("study");
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
        maxNum = (TextView) findViewById(R.id.menti_detail_maxNum);

        mentolistTv = (TextView) findViewById(R.id.menti_detail_mentoList_TV);
        selectMentoTv = (TextView) findViewById(R.id.menti_detail_selectMento_TV);

        chosenMentoRL = (RelativeLayout) findViewById(R.id.menti_detail_chosenMento);
        chosenMentoImg = (ImageView) findViewById(R.id.menti_detail_chosenMento_img);
        chosenMentoId = (TextView) findViewById(R.id.menti_detail_chosenMento_id);

        enrollList = findViewById(R.id.enroll_members);

        status.setText(study.statusStr());
        subject.setText(study.getSubject());
        place.setText(study.getPlace());
        intro.setText(study.getStudyIntro());
        maxNum.setText(String.valueOf(study.getMaxNum()));



        //버튼 (역할마다 상태 바꿔주기 위함)
        BtnForMaker = (MaterialButton) findViewById(R.id.menti_detail_BtnForMaker); // 방장용
        BtnForMentee = (MaterialButton) findViewById(R.id.menti_detail_BtnForMentee);  // 방장 외 멘티 용
        BtnForMento = (MaterialButton) findViewById(R.id.menti_detail_BtnForMento); // 멘토 용

        //멘토&멘티 리스트


        checkStudyStatus();


        //멘토 리사이클러뷰
        recyclerView = (RecyclerView) findViewById(R.id.menti_detail_enrollMento_RV);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new StudyMentiEnrollMentoAdapter();

        // 멘티 리스트 찍어보기 (나중에 지울꺼)
        dataService.study.menteeList(studyId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    enrollList.setText(response.body()+"");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });



        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, List<String>> API = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                Call<List<String>> call = dataService.study.mentorList(studyId);
                try {
                    for(String res : call.execute().body()) {
                        adapter.addItem(res);
                    }
                    adapter.setStudyId(studyId);
                    adapter.setManagerId(managerId);
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
            result = API.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //선정 멘토 확인
        chosenMentoProfile(chosenMentoRL);


        // 채팅 방 받아오기
        dataService.chat.getRoom(studyId).enqueue(new Callback<FindRoomRes>() {
            @Override
            public void onResponse(Call<FindRoomRes> call, Response<FindRoomRes> response) {
                findRoomRes = response.body();
            }

            @Override
            public void onFailure(Call<FindRoomRes> call, Throwable t) {

            }
        });

    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        if(userId.equals(managerId)){
            menuInflater.inflate(R.menu.study_detail_toolbar, menu);
        }

        return true;
    }



    //상단 메뉴 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.detail_del:
                delStudy();
                break;

            case android.R.id.home:
                finish();
                break;
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


        @SuppressLint({"ResourceAsColor", "StaticFieldLeak"})
        AsyncTask<Void, Void, String> listAPI = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Call<String> call = dataService.study.studyStatus(studyId);


                try {
                    checkRole();
                    checkMaxNum();
                    String response = call.execute().body();
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

                        applyStudyForMentee(BtnForMentee);
                   } else if (response.equals(MENTO_APPLY)){
                         //멘토 모집중


                        applyStudyForMento(BtnForMento);


                    } else {
                        //모집 완료
                        matchedBtn(BtnForMaker);
                        matchedBtn(BtnForMentee);
                        matchedBtn(BtnForMento);

                        afterChooseMento();


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

    public void matchedBtn(MaterialButton button){
        button.setText("스터디 입장하기");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //스터디 시작하기 함수 작성
                Toast.makeText(getApplicationContext(),"스터디 시작",Toast.LENGTH_SHORT).show();
                Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
                chatIntent.putExtra("roomId", findRoomRes.getRoomId());
                chatIntent.putExtra("studyId",studyId);
                startActivity(chatIntent);
            }
        });
    }


    //스터디 신청 메서드
    public void applyStudyForMentee(Button button){

        dataService.study.isApplied(studyId,userId).enqueue(new Callback<Boolean>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){

                    BtnForMento.setText("멘티 모집중입니다");
                    BtnForMento.setBackgroundColor(R.color.disableBtn);
                    BtnForMento.setEnabled(false);

                    if(response.body()){
                        //이미 신청 했으면
                        button.setEnabled(false);
                        button.setBackgroundColor(R.color.disableBtn);
                        button.setText("신청 완료");
                    } else {//신청 전이면

                        if(Integer.parseInt(String.valueOf(peopleNum.getText()))
                                <Integer.parseInt(String.valueOf(maxNum.getText()))){ // 최대 인원 전이면
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
                                                button.setText("신청완료");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<StudyApplyRes> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                        } else {
                            button.setEnabled(false);
                            button.setText("최대 인원입니다");
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

    }

    //스터디 신청 메서드
    public void applyStudyForMento(Button button){

        dataService.study.isApplied(studyId,userId).enqueue(new Callback<Boolean>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){

                    BtnForMaker.setText("멘토 모집중입니다");
                    BtnForMaker.setBackgroundColor(R.color.disableBtn);
                    BtnForMaker.setEnabled(false);

                    BtnForMentee.setText("멘토 모집중입니다");
                    BtnForMentee.setBackgroundColor(R.color.disableBtn);
                    BtnForMentee.setEnabled(false);

                    if(response.body()){
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
                                            button.setText("신청완료");
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
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()){
                    BtnForMaker.setText("멘토 모집중입니다");
                    BtnForMaker.setBackgroundColor(R.color.disableBtn);
                    BtnForMaker.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    //선택 멘토 찾기
    public void afterChooseMento(){
        dataService.study.chosenMentor(studyId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                chosenMentoId.setText(response.body());

                selectMentoTv.setVisibility(View.VISIBLE);
                mentolistTv.setVisibility(View.GONE);

                chosenMentoRL.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //인원수 체크
    public void checkMaxNum(){

        dataService.study.menteeList(studyId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    peopleNum.setText(String.valueOf(response.body().size()));
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    public void delStudy() {
        StudyDeleteReq delReq = new StudyDeleteReq(studyId,userPkId);
        dataService.study.delete(delReq).enqueue(new Callback<StudyDeleteRes>() {
            @Override
            public void onResponse(Call<StudyDeleteRes> call, Response<StudyDeleteRes> response) {
                if(response.isSuccessful()){
                    Toast.makeText(StudyMentiDetail.this, "삭제가 완료되었습니다. ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StudyDeleteRes> call, Throwable t) {

            }
        });

    }
    //선정 멘토 보기
    public void chosenMentoProfile(RelativeLayout view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), StudyMentoDetail.class);
                intent.putExtra("mentoId",chosenMentoId.getText());
                startActivity(intent);
            }
        });
    }







}
