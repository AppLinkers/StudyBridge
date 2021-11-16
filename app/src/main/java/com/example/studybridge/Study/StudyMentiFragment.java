package com.example.studybridge.Study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class StudyMentiFragment extends Fragment {
    //리사이클러
    private RecyclerView recyclerView;
    private StudyMentiAdapter adapter;
    ArrayList<StudyMenti> arrayList;
    private FloatingActionButton mentiFab;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_menti_fragment,container,false);

        //recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.study_menti_RCView);
        arrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new StudyMentiAdapter();
        getData();
        recyclerView.setAdapter(adapter);

        //study add btn
        mentiFab = (FloatingActionButton) view.findViewById(R.id.menti_addBtn);
        mentiFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void getData() {
        StudyMenti m1 = new StudyMenti(0,"운전면허","하남","1종 대형 스터디","대형따고 운전병ㅠㅠ",5);
        StudyMenti m2 = new StudyMenti(1,"영어","강남","프리토킹 스터디","프리토킹",2);
        StudyMenti m3 = new StudyMenti(2,"코딩","동역사","코딩 스터디","안드로이드 마스터",3);
        StudyMenti m4 = new StudyMenti(1,"운동","하남","헬스","3대 500가드아",4);

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
