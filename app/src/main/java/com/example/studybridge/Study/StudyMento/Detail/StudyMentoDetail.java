package com.example.studybridge.Study.StudyMento.Detail;

import android.app.Activity;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.Detail.DialogInterfaces;
import com.example.studybridge.Study.StudyMenti.Detail.StudyMentiSelectMentoDialog;
import com.example.studybridge.Study.StudyMento.StudyMento;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.ChangeStatusReq;
import com.example.studybridge.http.dto.ProfileRes;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;

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

    private ImageButton heart;


    DataService dataService = new DataService();

    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";

    private String mentoId, managerId,userId;
    private Long studyId;

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

        //멘토 찾기에서 불러온 것
        Intent intent = getIntent();

        MyPageMentoProfile profile = (MyPageMentoProfile) intent.getSerializableExtra("profile");


        //신청한 멘티에서 불러온 것
        mentoId = intent.getExtras().getString("mentoId");
        studyId = intent.getExtras().getLong("studyId");
        managerId = intent.getExtras().getString("managerId");


        //툴바 설정
        if(mentoId == null || mentoId.equals("")){
            toolbar.setTitle(profile.getNickName());
        } else {
            dataService.userMentor.getProfile(mentoId).enqueue(new Callback<ProfileRes>() {
                @Override
                public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                    if (response.isSuccessful())
                    {
                        toolbar.setTitle(response.body().getNickName());
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






        // viewpager & tablayout
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

        heart.setSelected(intent.getExtras().getBoolean("heart"));

        //좋아요 클릭
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heart.isSelected() == true){
                    heart.setSelected(false);
                } else {
                    Toast.makeText(getApplicationContext(),"Liked!",Toast.LENGTH_SHORT).show();
                    heart.setSelected(true);
                }
            }
        });

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

        dialog.show(fm,"selectMetno");
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
/*                    Intent resultIntent = new Intent();*/
/*                    setResult(Activity.RESULT_OK,resultIntent);*/
                    toMatched();
                    finish();
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

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

}
