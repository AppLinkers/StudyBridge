package com.example.studybridge.Study.StudyMenti.Detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.studybridge.Chat.ChatActivity;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyAddActivity;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;
import com.example.studybridge.databinding.StudyDetailActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.message.FindRoomRes;
import com.example.studybridge.http.dto.study.ChangeStatusReq;
import com.example.studybridge.http.dto.study.StudyApplyReq;
import com.example.studybridge.http.dto.study.StudyApplyRes;
import com.example.studybridge.http.dto.study.StudyDeleteReq;
import com.example.studybridge.http.dto.study.StudyDeleteRes;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.study.StudyWithdrawReq;
import com.example.studybridge.http.dto.study.StudyWithdrawRes;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentiDetail extends AppCompatActivity {

    private StudyDetailActivityBinding binding;
    private StudyFindRes study;

    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ISMENTEE = "user_mentee_key";

    //역할 체크
    private String userId; // 사용자 아이디
    private Long userPkId; // 사용자 long아이디
    private Long studyId;  // 스터디 아이디
    private String makerId;  //방장 아이디
    private Boolean isMentee; //멘티인지?
    private Boolean isApplied; //지원했는지
    private String chosenMentorId; //최종 멘토
    private String imgPath;


    //데이터 서비스
    DataService dataService = new DataService();

    //상태 변경 키워드
    public static final String MENTEE_APPLY = "APPLY";
    public static final String MENTO_APPLY = "WAIT";
    public static final String CONFIRM_APPLY = "MATCHED";


    FindRoomRes findRoomRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StudyDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //sharedPreference, 현재 이용자 아이디 불러옴
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        userPkId = sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        isMentee = sharedPreferences.getBoolean(USER_ISMENTEE,true);

        intentData();
        setUI();
        checkStudyStatus();
        setBtns();
        getChat();

    }

    private void intentData(){
        Intent intent = getIntent();
        study = intent.getExtras().getParcelable("study");
        studyId = study.getId();
        makerId = intent.getStringExtra("managerId");
        isApplied = intent.getBooleanExtra("isApplied",false);
        chosenMentorId = intent.getStringExtra("chosenMentor");

        //toolbar
        setSupportActionBar(binding.appBar);
        binding.appBar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if(userId.equals(makerId)){
            menuInflater.inflate(R.menu.study_detail_toolbar, menu);
        }
        return true;
    }
    //상단 메뉴 선택
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;

            case R.id.detail_del:
                delStudy();
                break;

            case R.id.detail_update:
                updateStudy();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUI(){
        study.statusSet(binding.status,getApplicationContext());
        binding.studyName.setText(study.getName());
        binding.studyIntro.setText(study.getInfo());
        binding.studySubject.setText(study.getType());
        binding.studyPlace.setText(study.getPlace());
        StringBuilder sb = new StringBuilder();
        sb.append("인원수 : ").append(study.getMenteeCnt()).append(" / ").append(study.getMaxNum());
        binding.studyNum.setText(sb.toString());
        binding.makerId.setText(makerId);
        binding.explain.setText(study.getExplain());
        setProfile(makerId,binding.makerImg);

        isMentorMake(makerId,binding.isMentor);

        if(chosenMentorId!=null){
            binding.chosenId.setText(chosenMentorId);
            setProfile(chosenMentorId,binding.chosenImg);
        }

    }

    private void setBtns(){
        binding.menteeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StudyMembers.class);
                intent.putExtra("studyId",studyId);
                intent.putExtra("path",0);
                intent.putExtra("makerId",makerId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        binding.mentorBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StudyMembers.class);
                intent.putExtra("studyId",studyId);
                intent.putExtra("path",1);
                intent.putExtra("makerId",makerId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private void getChat(){
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

    @Override
    protected void onResume() {
        super.onResume();
        checkStudyStatus();
    }



    //(1) 역할을 정해준다 (방장, 멘티, 멘토)
    public void checkRole(){

        if(userId.equals(makerId)){
            binding.btnForMaker.setVisibility(View.VISIBLE);
            binding.btnForMentor.setVisibility(View.GONE);
            binding.btnForMentee.setVisibility(View.GONE);

        } else {
            if (isMentee) {//멘티인 경우
                binding.btnForMaker.setVisibility(View.GONE);
                binding.btnForMentor.setVisibility(View.GONE);
                binding.btnForMentee.setVisibility(View.VISIBLE);
            } else { //멘토인 경우
                binding.btnForMaker.setVisibility(View.GONE);
                binding.btnForMentor.setVisibility(View.VISIBLE);
                binding.btnForMentee.setVisibility(View.GONE);
            }
        }
    }

    //(2) 상태마다 역할마다 버튼의 기능, 텍스트를 달리한다
    public void checkStudyStatus(){

        checkRole();
        checkMaxNum();

        dataService.study.studyStatus(studyId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals(MENTEE_APPLY)){
                    //멘티 모집중

                    binding.status.setText("멘티 모집중");
                    binding.status.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.palletRed));
                    binding.btnForMaker.setText("모집 종료하기");
                    binding.btnForMaker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //모집 종료 함수 작성
                            toWait();
                        }
                    });
                    applyStudyForMentee(binding.btnForMentee);
                } else if (response.body().equals(MENTO_APPLY)){
                    //멘토 모집중

                    applyStudyForMento(binding.btnForMentor);

                } else {
                    //모집 완료

                    matchedBtn(binding.btnForMaker);
                    matchedBtn(binding.btnForMentee);
                    matchedBtn(binding.btnForMentor);
                    afterChooseMento();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //모집완료 버튼
    public void matchedBtn(TextView button){

        binding.status.setText("모집 종료");
        binding.status.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.disableBtn));

        if(isApplied||userId.equals(makerId)||userId.equals(chosenMentorId)){
            button.setText("스터디 입장하기");
            button.setEnabled(true);
            button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.palletRed));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //스터디 시작하기 함수 작성
                    goToChat();
                }
            });
        } else {
            button.setText("마감된 스터디입니다");
            button.setEnabled(false);
            button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.disableBtn));
        }
    }


    //스터디 신청 메서드
    public void applyStudyForMentee(TextView button){

        binding.btnForMentor.setText("멘티 모집중입니다");
        binding.btnForMentor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.disableBtn));
        binding.btnForMentor.setEnabled(false);

        if(isApplied){
            button.setText("신청 취소");
        }
        else {
            if(study.getMenteeCnt() <study.getMaxNum()) { // 최대 인원 전이면
                button.setText("신청 하기");
            }
            else {
                button.setEnabled(false);
                button.setText("최대 인원입니다");
                button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.disableBtn));
            }
        }
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isApplied) {
                    StudyWithdrawReq studyWithdrawReq = new StudyWithdrawReq(studyId,userPkId);
                    dataService.study.withdraw(studyWithdrawReq).enqueue(new Callback<StudyWithdrawRes>() {
                        @Override
                        public void onResponse(Call<StudyWithdrawRes> call, Response<StudyWithdrawRes> response) {
                            if(response.isSuccessful()){
                                checkMaxNum();
                                button.setText("신청 하기");
                                isApplied = false;
                            }
                        }

                        @Override
                        public void onFailure(Call<StudyWithdrawRes> call, Throwable t) {

                        }
                    });
                }
                else {
                    StudyApplyReq studyApplyReq = new StudyApplyReq(userId,studyId);
                    dataService.study.apply(studyApplyReq).enqueue(new Callback<StudyApplyRes>() {
                        @Override
                        public void onResponse(Call<StudyApplyRes> call, Response<StudyApplyRes> response) {
                            if(response.isSuccessful()){
                                checkMaxNum();
                                button.setText("신청 취소");
                                isApplied = true;
                            }
                        }

                        @Override
                        public void onFailure(Call<StudyApplyRes> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }

    //스터디 신청 메서드
    public void applyStudyForMento(TextView button){

        binding.status.setText("멘토 모집중");
        binding.status.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.palletBlue));

        binding.btnForMaker.setText("멘토를 선정해주세요");
        binding.btnForMaker.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.palletBlue));
        binding.btnForMaker.setEnabled(false);

        binding.btnForMentee.setText("멘토 모집중입니다");
        binding.btnForMentee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.palletBlue));
        binding.btnForMentee.setEnabled(false);

        if(isApplied){
            button.setText("신청 취소");
        } else {
            button.setText("신청 하기");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isApplied){
                    StudyWithdrawReq studyWithdrawReq = new StudyWithdrawReq(studyId,userPkId);
                    dataService.study.withdraw(studyWithdrawReq).enqueue(new Callback<StudyWithdrawRes>() {
                        @Override
                        public void onResponse(Call<StudyWithdrawRes> call, Response<StudyWithdrawRes> response) {
                            if(response.isSuccessful()){
                                button.setText("신청 하기");
                                isApplied = false;
                            }
                        }

                        @Override
                        public void onFailure(Call<StudyWithdrawRes> call, Throwable t) {

                        }
                    });

                }
                else {
                    StudyApplyReq studyApplyReq = new StudyApplyReq(userId,studyId);
                    dataService.study.apply(studyApplyReq).enqueue(new Callback<StudyApplyRes>() {
                        @Override
                        public void onResponse(Call<StudyApplyRes> call, Response<StudyApplyRes> response) {
                            if(response.isSuccessful()){
                                button.setText("신청 취소");
                                isApplied = true;
                            }
                        }
                        @Override
                        public void onFailure(Call<StudyApplyRes> call, Throwable t) {

                        }
                    });

                }
            }
        });


    }

    //멘토 모집으로 넘어가는 메서드
    public void toWait(){

        if(isMentee){ //mentee가 방을 만든거라면

            ChangeStatusReq csReq = new ChangeStatusReq(studyId,MENTO_APPLY);
            dataService.study.status(csReq).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if(response.isSuccessful()){

                        applyStudyForMento(binding.btnForMentor);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
        else { //mentor가 방을 만든거라면

            ChangeStatusReq csReq = new ChangeStatusReq(studyId,CONFIRM_APPLY);
            dataService.study.status(csReq).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        matchedBtn(binding.btnForMaker);
                        afterChooseMento();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) { }
            });

        }

    }


    //선택 멘토 찾기
    public void afterChooseMento(){

        binding.mentorBoxArrow.setVisibility(View.GONE);
        binding.mentorBox.setText("선정 멘토");
        binding.chosenId.setVisibility(View.VISIBLE);
        binding.chosenImgCV.setVisibility(View.VISIBLE);

        /*dataService.study.chosenMentor(studyId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                binding.chosenId.setText(response.body());
                setProfile(response.body(),binding.chosenImg);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });*/

        binding.mentorBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudyMentoDetail.class);
                intent.putExtra("mentoId",binding.chosenId.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }


    //인원수 체크
    public void checkMaxNum(){
        dataService.study.menteeList(studyId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    StringBuilder sb = new StringBuilder();
                    int num = response.body().size();
                    sb.append("인원수 : ").append(num).append(" / ").append(study.getMaxNum());
                    binding.studyNum.setText(sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    public void delStudy() {

        if(study.getStatus().equals("MATCHED")){
            Toast.makeText(this, "매칭 후 삭제가 불가합니다", Toast.LENGTH_SHORT).show();
        }
        else{
            StudyDeleteReq delReq = new StudyDeleteReq(studyId,userPkId);
            dataService.study.delete(delReq).enqueue(new Callback<StudyDeleteRes>() {
                @Override
                public void onResponse(Call<StudyDeleteRes> call, Response<StudyDeleteRes> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(StudyMentiDetail.this, "삭제가 완료되었습니다. ", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<StudyDeleteRes> call, Throwable t) {

                }
            });
        }


    }
    public void updateStudy(){
        if(study.getStatus().equals("MATCHED")){
            Toast.makeText(this, "매칭 후 업데이트가 불가합니다", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(),StudyAddActivity.class);
            intent.putExtra("study",study);
            intent.putExtra("managerId",makerId);
            startActivity(intent);
        }
    }

    private void goToChat(){
        Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
        chatIntent.putExtra("roomId", findRoomRes.getRoomId());
        chatIntent.putExtra("study",study);
        startActivity(chatIntent);
    }

    private void setProfile(String userId, ImageView imageView){
        dataService.userAuth.getProfile(userId).enqueue(new Callback<UserProfileRes>() {
            @Override
            public void onResponse(Call<UserProfileRes> call, Response<UserProfileRes> response) {
                if(response.isSuccessful()){
                    final String path = response.body().getProfileImg();
                    Glide.with(getApplicationContext()).load(path).into(imageView);
                }
            }

            @Override
            public void onFailure(Call<UserProfileRes> call, Throwable t) {

            }
        });
    }
    private void isMentorMake(String makerId, MaterialCardView cardView){
        dataService.userAuth.isMentee(makerId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(!response.body()){
                    cardView.setVisibility(View.VISIBLE);
                }
                else {
                    cardView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}