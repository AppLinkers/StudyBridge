package com.example.studybridge.Study.StudyMento.Detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studybridge.R;
import com.example.studybridge.databinding.MentorDetailActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentee.LikeMentorRes;
import com.example.studybridge.http.dto.userMentor.ProfileRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentoDetail extends AppCompatActivity {

    private ProfileRes profile;
    private MentorDetailActivityBinding binding;

    DataService dataService = new DataService();

    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ISMENTEE = "user_mentee_key";

    //넘어온 데이터들
    private String mentoId,userId;
    private Long studyId,userLong,mentoLong;
    private Boolean isMentee;

    private CertiAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MentorDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //sharedPreference, 현재 이용자 아이디 불러옴
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        userLong = sharedPreferences.getLong(USER_PK_ID_KEY,0);
        isMentee = sharedPreferences.getBoolean(USER_ISMENTEE,true);

        intentData();
        setUI();
        setCerti();


    }

    private void intentData(){
        //멘토 찾기에서 불러온 것
        Intent intent = getIntent();

        profile = intent.getExtras().getParcelable("profile");

        //신청한 멘티에서 불러온 것
        mentoId = intent.getExtras().getString("mentoId");
        studyId = intent.getExtras().getLong("studyId");

        //toolbar
        setSupportActionBar(binding.appBar);
        binding.appBar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //툴바 뒤로가기 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUI(){

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.certiRV.setLayoutManager(linearLayoutManager);

        if(mentoId == null || mentoId.equals("")){ //멘토 찾기에서 들어온 경우

            mentoLong = profile.getUserId();
            isMentee(profile.getLiked());
            binding.mentorName.setText(profile.getNickName());
            binding.mentorIntro.setText(profile.getInfo());
            binding.mentoSubject.setText(profile.getSubject());
            binding.mentorPlace.setText(profile.getLocation());
            binding.mentorSchool.setText(profile.getSchool());
            binding.appeal.setText(profile.getAppeal());
            binding.curi.setText(profile.getCurriculum());
            binding.exp.setText(profile.getExperience());


            adapter = new CertiAdapter(profile.getCertificates());
            binding.certiRV.setAdapter(adapter);

        } else { //신청한 멘토에서 불러온 경우
            dataService.userMentor.getProfile(mentoId, userId).enqueue(new Callback<ProfileRes>() {
                @Override
                public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                    if (response.isSuccessful())
                    {
                        mentoLong = response.body().getUserId();
                        isMentee(response.body().getLiked());
                        binding.mentorName.setText(response.body().getNickName());
                        binding.mentorIntro.setText(response.body().getInfo());
                        binding.mentoSubject.setText(response.body().getSubject());
                        binding.mentorPlace.setText(response.body().getLocation());
                        binding.mentorSchool.setText(response.body().getSchool());
                        binding.appeal.setText(response.body().getAppeal());
                        binding.curi.setText(response.body().getCurriculum());
                        binding.exp.setText(response.body().getExperience());

                        adapter = new CertiAdapter(response.body().getCertificates());
                        binding.certiRV.setAdapter(adapter);
                    }
                }
                @Override
                public void onFailure(Call<ProfileRes> call, Throwable t) {

                }
            });
        }


    }


    private void isMentee(Boolean liked){
        if(isMentee){
            if(liked){
                binding.heart.setSelected(true);
            }
            else {
                binding.heart.setSelected(false);
            }
            setHeart();
        }
    }
    //좋아요 설정
    private void setHeart(){

        binding.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.heart.isSelected()){

                    dataService.userMentee.unLikeMentor(userLong,mentoLong).enqueue(new Callback<LikeMentorRes>() {
                        @Override
                        public void onResponse(Call<LikeMentorRes> call, Response<LikeMentorRes> response) {
                            if(response.isSuccessful()){
                                binding.heart.setSelected(false);
                                Toast.makeText(StudyMentoDetail.this, "관심멘토 등록 해제", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<LikeMentorRes> call, Throwable t) {

                        }
                    });
                }
                else if(!binding.heart.isSelected()){
                    dataService.userMentee.likeMentor(userLong,mentoLong).enqueue(new Callback<LikeMentorRes>() {
                        @Override
                        public void onResponse(Call<LikeMentorRes> call, Response<LikeMentorRes> response) {
                            if(response.isSuccessful()) {
                                binding.heart.setSelected(true);
                                Toast.makeText(StudyMentoDetail.this, "관심멘토 등록 성공", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LikeMentorRes> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
    private void setCerti(){
        binding.certiCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.certiRV.getVisibility()==View.GONE){
                    binding.certiRV.setVisibility(View.VISIBLE);
                    binding.certiArrow.setImageResource(R.drawable.ic_arrow_up);
                }
                else {
                    binding.certiRV.setVisibility(View.GONE);
                    binding.certiArrow.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}
