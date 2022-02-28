package com.example.studybridge.ToDo.ToDoInside;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ToDoInsideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ToDoInside> listData = new ArrayList<>();
    private ToDoInside toDoInside;
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_detail_inside_item, parent, false);
        return new ToDoInsideHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ((ToDoInsideHolder) holder).onBind(listData.get(position));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    public void addItem(ToDoInside data) {
        listData.add(data);
    }
    public void clearItem(){listData.clear();}

    public void manageData(ToDoInside data){
        data.setStatus("Progress");
        toDoInside = data;
    }


    public ToDoInside getToDoInside() {
        return toDoInside;
    }

    public void setToDoInside(ToDoInside toDoInside) {
        this.toDoInside = toDoInside;
    }
}