package com.example.studybridge.Mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.studybridge.Mypage.Edit.MyPageEditActivity;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MyPageFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MyPageFragmentPagerAdapter adapter;

    private TextView editBtn;

    private TextView userIdTv;
    private TextView userNameTv;

    private String userName;
    private String userId;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_fragment, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.mypage_tab);
        viewPager = (ViewPager2) view.findViewById(R.id.mypage_pager);


        //옆으로 스와이프
        FragmentManager fm = getChildFragmentManager();
        adapter = new MyPageFragmentPagerAdapter(fm,getLifecycle());

        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        viewPager.setSaveEnabled(false);



        userIdTv = view.findViewById(R.id.mypage_id);
        userNameTv = view.findViewById(R.id.mypage_name);

        Bundle bundle =getArguments();
        userName = bundle.getString("name");
        userId = bundle.getString("id");

        userIdTv.setText(userId);
        userNameTv.setText(userName);

        //정보 수정하기로 이동
        editBtn = (TextView) view.findViewById(R.id.mypage_editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyPageEditActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
