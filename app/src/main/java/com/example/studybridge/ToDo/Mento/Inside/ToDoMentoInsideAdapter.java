package com.example.studybridge.ToDo.Mento.Inside;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.toDo.FindToDoRes;

import java.util.ArrayList;

public class ToDoMentoInsideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<FindToDoRes> todoAssigned = new ArrayList<FindToDoRes>();


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_mentor_inside_item, parent, false);
        return new ToDoMentorInsdieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ToDoMentorInsdieHolder) holder).onBind(todoAssigned.get(position));

    }
    public void deleteItem(int position){
        todoAssigned.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,todoAssigned.size());
    }

    public void addItem(FindToDoRes data){
        todoAssigned.add(data);
    }

    @Override
    public int getItemCount() {
        return todoAssigned.size();
    }
}
