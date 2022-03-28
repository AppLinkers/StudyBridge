package com.example.studybridge.ToDo.Mento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import java.util.ArrayList;

public class ToDoMentoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<String> exampleList;

    public ToDoMentoAdapter() {
        exampleList = new ArrayList<>();
        exampleList.add("A스터디");
        exampleList.add("B스터디");
        exampleList.add("C스터디");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_mentor_rc_item, parent, false);
        return new ToDoMentoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ToDoMentoHolder) holder).onBind(exampleList.get(position));
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }
}
