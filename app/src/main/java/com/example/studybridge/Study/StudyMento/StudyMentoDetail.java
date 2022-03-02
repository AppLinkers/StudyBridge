package com.example.studybridge.Study.StudyMento;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class StudyMentoDetail extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private StudyMentoDetailPagerAdapter adapter;
    private Toolbar toolbar;
//    private String[] intentArr;
    private ImageButton heart;
    private ArrayList intentArray;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_mento_detail_activity);

        tabLayout = (TabLayout) findViewById(R.id.mento_detail_tab);
        toolbar = (Toolbar) findViewById(R.id.mento_detail_bar);
        heart = (ImageButton) findViewById(R.id.mento_detail_heart);
        viewPager = (ViewPager2) findViewById(R.id.mento_detail_pager);

        Intent intent = getIntent();
        //툴바 설정
        toolbar.setTitle(intent.getExtras().getString("name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intentArray = new ArrayList();
        intentArray.add(intent.getExtras().getString("subject"));
        intentArray.add(intent.getExtras().getString("place"));
        intentArray.add(intent.getExtras().getString("school"));
        intentArray.add(intent.getExtras().getString("qualify"));
        intentArray.add(intent.getExtras().getString("intro"));


//        //fragment에 들어갈 intent 받기
//        intentArr = new String[5];
//        intentArr[0] = intent.getExtras().getString("subject");
//        intentArr[1] = intent.getExtras().getString("place");
//        intentArr[2] = intent.getExtras().getString("school");
//        intentArr[3] = intent.getExtras().getString("qualify");
//        intentArr[4] = intent.getExtras().getString("intro");

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
