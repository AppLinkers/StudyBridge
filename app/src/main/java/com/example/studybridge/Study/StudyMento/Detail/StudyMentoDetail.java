package com.example.studybridge.Study.StudyMento.Detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.Detail.DialogInterfaces;
import com.example.studybridge.Study.StudyMenti.Detail.StudyMentiSelectMentoDialog;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.ChangeStatusReq;
import com.example.studybridge.http.dto.userMentee.LikeMentorRes;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentoDetail extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private StudyMentoDetailPagerAdapter adapter;
    private Toolbar toolbar;
    private MaterialButton button;
    private LinearLayout buttonLayout;

    private ProfileRes profile;

    private ImageButton heart;

    DataService dataService = new DataService();

    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ISMENTEE = "user_mentee_key";

    //넘어온 데이터들
    private String mentoId, managerId,userId;
    private Long studyId,userLong,mentoLong;
    private Boolean isMentee;

    //dialog 통신용 int
    private int selectOK;

    public static final String CONFIRM_APPLY = "MATCHED";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_mento_detail_activity);


        //화면 위 데이터
        tabLayout = (TabLayout) findViewById(R.id.mento_detail_tab);
        toolbar = (Toolbar) findViewById(R.id.mento_detail_bar);
        heart = (ImageButton) findViewById(R.id.mento_detail_heart);
        viewPager = (ViewPager2) findViewById(R.id.mento_detail_pager);
        button = (MaterialButton) findViewById(R.id.mento_detail_button);
        buttonLayout = (LinearLayout) findViewById(R.id.mento_detail_layout_button);

        //sharedPreference, 현재 이용자 아이디 불러옴
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        userLong = sharedPreferences.getLong(USER_PK_ID_KEY,0);
        isMentee = sharedPreferences.getBoolean(USER_ISMENTEE,true);

        //멘토 찾기에서 불러온 것
        Intent intent = getIntent();

        profile = intent.getExtras().getParcelable("profile");

        //신청한 멘티에서 불러온 것
        mentoId = intent.getExtras().getString("mentoId");
        studyId = intent.getExtras().getLong("studyId");
        managerId = intent.getExtras().getString("managerId");

        //들어온 경로에 따라
        setPath();

        // viewpager & tablayout
        setTabLayout();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDialog(mentoId,studyId);
            }
        });


    }

    //툴바 뒤로가기 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSelectDialog(String mentoId,Long studyId){

        FragmentManager fm = getSupportFragmentManager();
        StudyMentiSelectMentoDialog dialog = StudyMentiSelectMentoDialog.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("mentoId",mentoId);
        bundle.putLong("studyId",studyId);
        dialog.setArguments(bundle);

        dialog.show(fm,"selectMentor");
        dialog.setDialogInterfacer(new DialogInterfaces() {
            @Override
            public void onButtonClick(int selectCode) {
                selectOK = selectCode;
            }

        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (selectOK==1){
                    toMatched();
                }
            }
        });
    }

    private void setPath(){

        if(mentoId == null || mentoId.equals("")){ //멘토 찾기에서 들어온 경우
            toolbar.setTitle(profile.getNickName());
            mentoLong = profile.getUserId();
            isMentee(profile.getLiked());

        } else { //신청한 멘토에서 불러온 경우
            dataService.userMentor.getProfile(mentoId, userId).enqueue(new Callback<ProfileRes>() {
                @Override
                public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                    if (response.isSuccessful())
                    {
                        toolbar.setTitle(response.body().getNickName());
                        mentoLong = response.body().getUserId();
                        isMentee(response.body().getLiked());

                        if(userId.equals(managerId)){
                            buttonLayout.setVisibility(View.VISIBLE);
                        } else {
                            buttonLayout.setVisibility(View.GONE);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ProfileRes> call, Throwable t) {

                }
            });
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void isMentee(Boolean liked){
        if(isMentee){
            if(liked){
                heart.setSelected(true);
            }
            else {
                heart.setSelected(false);
            }
            setHeart();
        }
    }
    //좋아요 설정
    private void setHeart(){

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(StudyMentoDetail.this, mentoLong.toString(), Toast.LENGTH_SHORT).show();

                if(heart.isSelected()){

                    dataService.userMentee.unLikeMentor(userLong,mentoLong).enqueue(new Callback<LikeMentorRes>() {
                        @Override
                        public void onResponse(Call<LikeMentorRes> call, Response<LikeMentorRes> response) {
                            if(response.isSuccessful()){
                                heart.setSelected(false);
                                Toast.makeText(StudyMentoDetail.this, "관심멘토 등록 해제", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<LikeMentorRes> call, Throwable t) {

                        }
                    });
                }
                else if(!heart.isSelected()){
                    dataService.userMentee.likeMentor(userLong,mentoLong).enqueue(new Callback<LikeMentorRes>() {
                        @Override
                        public void onResponse(Call<LikeMentorRes> call, Response<LikeMentorRes> response) {
                            if(response.isSuccessful()) {
                                heart.setSelected(true);
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

    //매칭 종료 메서드
    public void toMatched(){
        ChangeStatusReq csReq =  new ChangeStatusReq(studyId, CONFIRM_APPLY);

        dataService.study.status(csReq).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(CONFIRM_APPLY);
                finish();

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }


    //tabLayout, Viewpager 설정
    public void setTabLayout(){
        FragmentManager fm = getSupportFragmentManager();

        adapter = new StudyMentoDetailPagerAdapter(fm,getLifecycle());

        viewPager.setAdapter(adapter);

        adapter.setProfile(profile);
        adapter.setId(mentoId);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        viewPager.setSaveEnabled(false);
    }

}
