package com.example.studybridge.Mypage.MentoProfile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.ProfileRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageMentoProfileShowActivity extends AppCompatActivity {

    private FloatingActionButton editBtn;
    private TextView nickName, intro, place, subject, school, curi, experience, appeal;

    DataService dataService = new DataService();

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    SharedPreferences sharedPreferences;
    private String userId;
    private String userName;

    public static final String VALUE_NULL_STR = "입력해 주세요!";

    private MyPageMentoProfile mentoProfile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_mentoprofile_show_activity);

        editBtn = (FloatingActionButton) findViewById(R.id.mypage_mentoShow_editBtn);

        nickName = (TextView) findViewById(R.id.mypage_mentoShow_nickName);
        intro = (TextView) findViewById(R.id.mypage_mentoShow_intro);
        place = (TextView) findViewById(R.id.mypage_mentoShow_place);
        subject = (TextView) findViewById(R.id.mypage_mentoShow_subject);
        school = (TextView) findViewById(R.id.mypage_mentoShow_school);
        curi = (TextView) findViewById(R.id.mypage_mentoShow_curi);
        experience = (TextView) findViewById(R.id.mypage_mentoShow_experience);
        appeal = (TextView) findViewById(R.id.mypage_mentoShow_appeal);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        userName = sharedPreferences.getString(USER_NAME, "사용자");


        dataService.userMentor.getProfile(userId).enqueue(new Callback<ProfileRes>() {
            @Override
            public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                if(response.isSuccessful())
                {
                    nickName.setText(checkNull(response.body().getNickName()));
                    intro.setText(checkNull(response.body().getInfo()));
                    place.setText(checkNull(response.body().getLocation()));
                    subject.setText(checkNull(response.body().getSubject()));
                    school.setText(checkNull(response.body().getSchool()));
                    curi.setText(checkNull(response.body().getCurriculum()));
                    experience.setText(checkNull(response.body().getExperience()));
                    appeal.setText(checkNull(response.body().getAppeal()));

                    mentoProfile = new MyPageMentoProfile(
                            userName,
                            response.body().getLocation(),
                            response.body().getSubject(),
                            response.body().getSchool(),
                            response.body().getInfo(),
                            response.body().getNickName(),
                            response.body().getCurriculum(),
                            response.body().getExperience(),
                            response.body().getAppeal(),null,null,null);

                }
            }

            @Override
            public void onFailure(Call<ProfileRes> call, Throwable t) {

            }
        });





        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyPageMentoProfileEditActivity.class);

                intent.putExtra("profile",mentoProfile);

                startActivity(intent);
            }
        });
    }

    public String checkNull(String str){
        if(str.equals("") || str == null){
            return VALUE_NULL_STR;
        }
        else
            return str;
    }
}
