package com.example.studybridge.ToDo.Mento.Inside.Detail;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;

import java.util.ArrayList;
import java.util.List;

public class ToDoMentoInsideDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<FindAssignedToDoRes> listItem;
    private Activity activity;

    public ToDoMentoInsideDetailAdapter(List<FindAssignedToDoRes> listItem, Activity activity) {
        this.listItem = listItem;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_mentor_inside_detail_item, parent, false);
        return new ToDoMentorInsdieDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ToDoMentorInsdieDetailHolder) holder).onBind(listItem.get(position),activity);
    }

    public void addItem(FindAssignedToDoRes data){
        listItem.add(data);
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
