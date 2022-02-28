package com.example.studybridge.Study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMentiFragment;
import com.example.studybridge.Study.StudyMento.StudyMentoFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class StudyFragment extends Fragment {

    private StudyMentiFragment mentiFragment;
    private StudyMentoFragment mentoFragment;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private StudyFragmentPagerAdapter adapter;



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_fragment, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.study_tab);
        viewPager = (ViewPager2) view.findViewById(R.id.study_pager);

        FragmentManager fm = getChildFragmentManager();
        adapter = new StudyFragmentPagerAdapter(fm,getLifecycle());

        viewPager.setAdapter(adapter);

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





        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        final List<String> tabElement = Arrays.asList("스터디 찾기","멘토 찾기");


    }
}
