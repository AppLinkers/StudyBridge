package com.example.studybridge.ToDo.Menti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDo;

import java.util.ArrayList;

public class ToDoMentiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<String> statusList;
    private ArrayList<ToDo> data;
    private ToDo todo;
    int status;

    public ToDoMentiAdapter() {
        this.data = data;
        statusList = new ArrayList<>();
        statusList.add("Ready");
        statusList.add("Progress");
        statusList.add("Done");
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_menti_rc_item, parent, false);

        return new ToDoMentiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ToDoMentiHolder) holder).onBind(statusList.get(position));

    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }
}
