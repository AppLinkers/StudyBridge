package com.example.studybridge.Mypage.MentoProfile.Show;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Mypage.MentoProfile.Edit.MyPageMentoProfileEditActivity;
import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentor.Certificate;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageMentoProfileShowActivity extends AppCompatActivity {

    //화면 위 데이터
    private FloatingActionButton editBtn;
    private TextView nickName, intro, place, subject, school, curi, experience, appeal, noCertiMsg;

    //자격증 리사이클러
    private RecyclerView recyclerView;
    private MyPageMentoShowAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    DataService dataService = new DataService();

    //shared preference
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";

    private String userId;

    //null값 처리
    public static final String VALUE_NULL_STR = "입력해 주세요";

    //이동 용도
    private ProfileRes mentoProfile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_mentoprofile_show_activity);

        //화면 위 데이터
        editBtn = (FloatingActionButton) findViewById(R.id.mypage_mentoShow_editBtn);

        nickName = (TextView) findViewById(R.id.mypage_mentoShow_nickName);
        intro = (TextView) findViewById(R.id.mypage_mentoShow_intro);
        place = (TextView) findViewById(R.id.mypage_mentoShow_place);
        subject = (TextView) findViewById(R.id.mypage_mentoShow_subject);
        school = (TextView) findViewById(R.id.mypage_mentoShow_school);
        curi = (TextView) findViewById(R.id.mypage_mentoShow_curi);
        experience = (TextView) findViewById(R.id.mypage_mentoShow_experience);
        appeal = (TextView) findViewById(R.id.mypage_mentoShow_appeal);
        noCertiMsg = (TextView) findViewById(R.id.mypage_mentoShow_noCerti_msg);

        //shared preference
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        //자격증 리사이클러뷰
        recyclerView = (RecyclerView) findViewById(R.id.mypage_mentoShow_RV);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyPageMentoShowAdapter();


        getData();

        setEditBtn();


    }

    private void getData(){
        dataService.userMentor.getProfile(userId, userId).enqueue(new Callback<ProfileRes>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                if(response.isSuccessful())
                {
                    assert response.body() != null;
                    nickName.setText(checkNull(response.body().getNickName()));
                    intro.setText(checkNull(response.body().getInfo()));
                    place.setText(checkNull(response.body().getLocation()));
                    subject.setText(checkNull(response.body().getSubject()));
                    school.setText(checkNull(response.body().getSchool()));
                    curi.setText(checkNull(response.body().getCurriculum()));
                    experience.setText(checkNull(response.body().getExperience()));
                    appeal.setText(checkNull(response.body().getAppeal()));

                    List<Certificate> certiList = response.body().getCertificates();

                    if(certiList.size()>0){
                        for(int i=0; i<response.body().getCertificates().size(); i++){
                            adapter.addItem(certiList.get(i));
                        }
                    } else {
                        noCertiMsg.setVisibility(View.VISIBLE);
                    }

                    recyclerView.setAdapter(adapter);

                    mentoProfile = response.body();

                }
            }

            @Override
            public void onFailure(Call<ProfileRes> call, Throwable t) {

            }
        });
    }

    private void setEditBtn(){
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageMentoProfileEditActivity.class);

                intent.putExtra("profile",mentoProfile);

                startActivity(intent);
            }
        });
    }

    public String checkNull(String str){
        if(str==null){
            return VALUE_NULL_STR;
        }
        else
            return str;
    }
}
