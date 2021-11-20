package com.example.studybridge.Study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import java.util.ArrayList;


public class StudyMentoFragment extends Fragment {
    //리사이클러
    private RecyclerView recyclerView;
    private StudyMentoAdapter adapter;
    ArrayList<StudyMento> arrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_mento_fragment,container,false);

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
        StudyMento m1 = new StudyMento("운전","하남","4대대 베스트드라이버","믿고 맡겨주세요","홍익대학교","1종보통");
        StudyMento m2 = new StudyMento("코딩","해남","4대대 베스트통신","믿고 맡겨주세요","과학기술대학교","스프링");
        StudyMento m3 = new StudyMento("수학","서울","수능수학1등급","무조건 1등급","서울대학교","올림피아드");
        StudyMento m4 = new StudyMento("영어","미국","안주전에 한잔","미국사관학교출신","펜실베니아대학교","");

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
