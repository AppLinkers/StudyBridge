package com.example.studybridge.Study.StudyMento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMentiFilterDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class StudyMentoFragment extends Fragment {
    //리사이클러
    private RecyclerView recyclerView;
    private StudyMentoAdapter adapter;
    ArrayList<StudyMento> arrayList;
    private FloatingActionButton filterFab;
    private TextView subjectFilter, placeFilter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_mento_fragment,container,false);

        //filter
        filterFab = (FloatingActionButton) view.findViewById(R.id.mento_filterBtn);
        subjectFilter = (TextView) view.findViewById(R.id.mento_subjectFilter);
        placeFilter = (TextView) view.findViewById(R.id.mento_placeFilter);
        filterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyMentiFilterDialog bottomSheet = StudyMentiFilterDialog.getInstance();
                bottomSheet.show(getChildFragmentManager(),StudyMentiFilterDialog.getInstance().getTag());
                bottomSheet.setDialogInterfacer(new StudyMentiFilterDialog.DialogInterfacer() {
                    @Override
                    public void onButtonClick(String subject, String place) {
                        subjectFilter.setText(subject);
                        placeFilter.setText(place);
                    }
                });
            }
        });

        //recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.study_mento_RCView);
        arrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new StudyMentoAdapter();
        getData();
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getData() {
        StudyMento m1 = new StudyMento("수학","경기","4대대 베스트드라이버","믿고 맡겨주세요","홍익대학교","1종보통",false);
        StudyMento m2 = new StudyMento("개발","기타","4대대 베스트통신","믿고 맡겨주세요","과학기술대학교","스프링",true);
        StudyMento m3 = new StudyMento("기타","인천","수능수학1등급","무조건 1등급","서울대학교","올림피아드",false);
        StudyMento m4 = new StudyMento("영어","기타","안주전에 한잔","미국사관학교출신","펜실베니아대학교","",true);

        adapter.addItem(m1);
        adapter.addItem(m2);
        adapter.addItem(m3);
        adapter.addItem(m4);
        adapter.addItem(m1);
        adapter.addItem(m2);
        adapter.addItem(m3);
        adapter.addItem(m4);

    }
}
