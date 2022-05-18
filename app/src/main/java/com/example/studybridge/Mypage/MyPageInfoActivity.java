package com.example.studybridge.Mypage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyFilter;
import com.example.studybridge.Study.StudyMenti.StudyMentiAdapter;
import com.example.studybridge.Study.StudyMento.StudyMentoAdapter;
import com.example.studybridge.databinding.MypageInfoActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.userMentee.LikeMentorRes;
import com.example.studybridge.http.dto.userMentor.ProfileRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageInfoActivity extends AppCompatActivity {

    private MypageInfoActivityBinding binding;

    private int path;
    public static final int APPLY_STUDY = 0;
    public static final int LIKE_MENTOR = 1;

    private final DataService dataService= new DataService();

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";


    Long userIdPk;
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MypageInfoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //sharedPref
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        intentData();
        setUI();

    }

    private void intentData(){
        Intent intent = getIntent();
        path = intent.getIntExtra("path",0);

    }

    private void setUI(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcView.setLayoutManager(linearLayoutManager);

        if(path == APPLY_STUDY){
            binding.title.setText("신청한 스터디");
            studyRC();
        }
        else if (path == LIKE_MENTOR){
            binding.title.setText("관심 멘토");
            mentorRC();
        }

        backBtn();


    }

    private void studyRC(){
        StudyMentiAdapter studyAdapter = new StudyMentiAdapter((Activity) this);
        StudyFilter filter = new StudyFilter("상태별","과목별","지역별");
        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {

                if(response.isSuccessful()){
                    for(StudyFindRes findRes: response.body()){
                        studyAdapter.addItem(findRes,filter);
                    }
                    binding.rcView.setAdapter(studyAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });

    }

    private void mentorRC(){
        StudyMentoAdapter mentorAdapter = new StudyMentoAdapter((Activity) this);
        StudyFilter filter = new StudyFilter("과목별","지역별");
        dataService.userMentor.getAllProfile(userId).enqueue(new Callback<List<ProfileRes>>() {
            @Override
            public void onResponse(Call<List<ProfileRes>> call, Response<List<ProfileRes>> response) {
                if(response.isSuccessful()){
                    final List<ProfileRes> profileList= response.body();
                    assert profileList != null;
                    for(ProfileRes res: profileList){
                        if(res.getLiked()){
                            mentorAdapter.addItem(res,filter);
                        }
                    }

                    binding.rcView.setAdapter(mentorAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ProfileRes>> call, Throwable t) {

            }
        });
    }


    private void backBtn(){
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }
}
