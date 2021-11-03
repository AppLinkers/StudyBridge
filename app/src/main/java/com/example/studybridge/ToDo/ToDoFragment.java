package com.example.studybridge.ToDo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import java.util.ArrayList;

public class ToDoFragment extends Fragment {

    //리사이클러
    private RecyclerView recyclerView;
    private ToDoAdapter adapter;
    ArrayList<ToDo> arrayList;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_fragment, container, false);

        //recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.toDo_RCView);
        arrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ToDoAdapter();
        getData();
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getData() {
        ToDo t1 = new ToDo("공무원 스터디","2021-11-03",40);
        ToDo t2 = new ToDo("코딩 스터디","2021-11-08",60);
        ToDo t3 = new ToDo("영어 스터디","2021-11-07",80);

        adapter.addItem(t1);
        adapter.addItem(t2);
        adapter.addItem(t3);

    }
}
