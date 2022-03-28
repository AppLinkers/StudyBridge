package com.example.studybridge.ToDo.Mento.Inside;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import java.util.ArrayList;

public class ToDoMentoInsideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<String> exampleList;

    public ToDoMentoInsideAdapter() {
        exampleList = new ArrayList<>();
        exampleList.add("A과제");
        exampleList.add("B과제");
        exampleList.add("C과제");

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_mentor_inside_item, parent, false);
        return new ToDoMentorInsdieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ToDoMentorInsdieHolder) holder).onBind(exampleList.get(position));

    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }
}
