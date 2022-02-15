package com.example.studybridge.ToDo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ToDoDetialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ToDoDetail> listData = new ArrayList<>();
    private ArrayList<ToDoInside> list = new ArrayList<>();
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_detail_item, parent, false);

        return new ToDoDetailHolder(view, list);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ((ToDoDetailHolder) holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public int getWholeCount() {
        return list.size();
    }

    public void addItem(ToDoDetail data) {
        listData.add(data);
    }
    public void addAssign(ToDoInside data){list.add(data); }

    public int getAssignCount(){
        int count= list.size();
        for(ToDoInside data : list){
            if(data.getStatus().equals("Done")){
                count -= 1;
            }
        }
        return count; }
}
