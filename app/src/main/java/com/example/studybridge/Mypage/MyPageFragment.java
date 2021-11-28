package com.example.studybridge.Mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.studybridge.R;
import com.google.android.material.tabs.TabLayout;

public class MyPageFragment extends Fragment {
    private TabLayout tabLayout;
    private MyPageMyFragment myFragment;
    private MyPageChatFragment chatFragment;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_fragment, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.mypage_tab);

        myFragment = new MyPageMyFragment();
        chatFragment = new MyPageChatFragment();

        getParentFragmentManager().beginTransaction().add(R.id.mypage_frame,myFragment).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0){
                    selected = myFragment;
                } else if(position == 1) {
                    selected = chatFragment;
                }
                getParentFragmentManager().beginTransaction().replace(R.id.mypage_frame,selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });

        return view;
    }
}
