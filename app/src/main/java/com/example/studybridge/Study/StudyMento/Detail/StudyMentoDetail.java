package com.example.studybridge.Study.StudyMento.Detail;

import android.content.Intent;
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
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.ProfileRes;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

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
//    private String[] intentArr;
    private ImageButton heart;
    private ArrayList intentArray;

    DataService dataService = new DataService();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_mento_detail_activity);

        tabLayout = (TabLayout) findViewById(R.id.mento_detail_tab);
        toolbar = (Toolbar) findViewById(R.id.mento_detail_bar);
        heart = (ImageButton) findViewById(R.id.mento_detail_heart);
        viewPager = (ViewPager2) findViewById(R.id.mento_detail_pager);
        button = (MaterialButton) findViewById(R.id.mento_detail_button);
        buttonLayout = (LinearLayout) findViewById(R.id.mento_detail_layout_button);

        Intent intent = getIntent();
        //툴바 설정
        toolbar.setTitle(intent.getExtras().getString("name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //신청한 멘티에서 불러온 것
        String mentoId = intent.getExtras().getString("mentoId");

        intentArray = new ArrayList();
        intentArray.add(intent.getExtras().getString("subject"));
        intentArray.add(intent.getExtras().getString("place"));
        intentArray.add(intent.getExtras().getString("school"));
        intentArray.add(intent.getExtras().getString("qualify"));
        intentArray.add(intent.getExtras().getString("intro"));
        intentArray.add(mentoId);

        dataService.userMentor.getProfile(mentoId).enqueue(new Callback<ProfileRes>() {
            @Override
            public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                if (response.isSuccessful())
                {
                    toolbar.setTitle(response.body().getNickName());
                    buttonLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ProfileRes> call, Throwable t) {

            }
        });




        // viewpager & tablayout
        FragmentManager fm = getSupportFragmentManager();

        adapter = new StudyMentoDetailPagerAdapter(fm,getLifecycle());

        viewPager.setAdapter(adapter);

        adapter.setArr(intentArray);


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
}
