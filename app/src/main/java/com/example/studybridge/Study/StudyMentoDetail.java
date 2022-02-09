package com.example.studybridge.Study;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private String[] intentArr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_mento_detail_activity);

        tabLayout = (TabLayout) findViewById(R.id.mento_detail_tab);
        toolbar = (Toolbar) findViewById(R.id.mento_detial_bar);

        Intent intent = getIntent();
        //툴바 설정
        toolbar.setTitle(intent.getExtras().getString("name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Tab layout
        profileFragment = new StudyMentoProfileFragment();

        //fragment에 들어갈 intent 받기
        intentArr = new String[5];
        intentArr[0] = intent.getExtras().getString("subject");
        intentArr[1] = intent.getExtras().getString("place");
        intentArr[2] = intent.getExtras().getString("school");
        intentArr[3] = intent.getExtras().getString("qualify");
        intentArr[4] = intent.getExtras().getString("intro");
        //여기서 fragment로 데이터 주기
        Bundle bundle = new Bundle(5);
        bundle.putString("subject",intentArr[0]);
        bundle.putString("place",intentArr[1]);
        bundle.putString("school",intentArr[2]);
        bundle.putString("qualify",intentArr[3]);
        bundle.putString("intro",intentArr[4]);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mento_detial_frame, new StudyMentoProfileFragment())
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
                } else if(position == 2){
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.mento_detial_frame,selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
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
