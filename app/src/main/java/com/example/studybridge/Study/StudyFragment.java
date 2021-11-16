package com.example.studybridge.Study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDo;
import com.example.studybridge.ToDo.ToDoAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class StudyFragment extends Fragment {

    private StudyMentiFragment mentiFragment;
    private StudyMentoFragment mentoFragment;
    private TabLayout tabLayout;



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_fragment, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.study_tab);

        //Tab layout
        mentiFragment = new StudyMentiFragment();
        mentoFragment = new StudyMentoFragment();

        getParentFragmentManager().beginTransaction().add(R.id.study_frame,mentiFragment).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0){
                    selected = mentiFragment;
                } else if(position == 1) {
                    selected = mentoFragment;
                }
                getParentFragmentManager().beginTransaction().replace(R.id.study_frame,selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });





        return view;
    }

}
