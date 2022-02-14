package com.example.studybridge.Study;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.studybridge.Home.HomeFragment;
import com.example.studybridge.R;
import com.google.android.material.tabs.TabLayout;

public class StudyMentoDetail extends AppCompatActivity {

    private StudyMentoProfileFragment profileFragment;
    private StudyMentoExperienceFragment experienceFragment;
    private StudyMentoCommentFragment commentFragment;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private String[] intentArr;
    private ImageButton heart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_mento_detail_activity);

        tabLayout = (TabLayout) findViewById(R.id.mento_detail_tab);
        toolbar = (Toolbar) findViewById(R.id.mento_detial_bar);
        heart = (ImageButton) findViewById(R.id.mento_detial_heart);

        Intent intent = getIntent();
        //툴바 설정
        toolbar.setTitle(intent.getExtras().getString("name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Tab layout
        profileFragment = new StudyMentoProfileFragment();
        experienceFragment = new StudyMentoExperienceFragment();
        commentFragment = new StudyMentoCommentFragment();

        //fragment에 들어갈 intent 받기
        intentArr = new String[5];
        intentArr[0] = intent.getExtras().getString("subject");
        intentArr[1] = intent.getExtras().getString("place");
        intentArr[2] = intent.getExtras().getString("school");
        intentArr[3] = intent.getExtras().getString("qualify");
        intentArr[4] = intent.getExtras().getString("intro");

        //여기서 fragment로 데이터 주기
        Bundle bundle = new Bundle();
        bundle.putString("subject",intentArr[0]);
        bundle.putString("place",intentArr[1]);
        bundle.putString("school",intentArr[2]);
        bundle.putString("qualify",intentArr[3]);
        bundle.putString("intro",intentArr[4]);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mento_detial_frame, profileFragment)
                    .commit();
            profileFragment.setArguments(bundle);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0){
                    selected = profileFragment;
                } else if(position == 1) {
                    selected = experienceFragment;
                } else if(position == 2){
                    selected = commentFragment;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.mento_detial_frame,selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });


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
