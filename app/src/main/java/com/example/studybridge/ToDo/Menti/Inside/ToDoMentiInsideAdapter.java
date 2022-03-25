package com.example.studybridge.ToDo.Menti.Inside;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDo;

import java.util.ArrayList;

public class ToDoMentiInsideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ToDo> data = new ArrayList<ToDo>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_menti_inside_item,parent,false);

        return new ToDoMentiInsideHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ToDoMentiInsideHolder)holder).onBind(data.get(position));
    }

    public void addItem(ToDo todo){
        data.add(todo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
