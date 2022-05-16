package com.example.studybridge.ToDo.Mento;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.Study;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<StudyFindRes> todoStudy = new ArrayList<>();
    private Activity activity;

    public ToDoMentoAdapter(List<StudyFindRes> todoStudy,Activity activity) {
        this.activity = activity;
        for(StudyFindRes s: todoStudy){
            if(s.getStatus().equals("MATCHED")){
                this.todoStudy.add(s);
            }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_mentor_rc_item, parent, false);
        return new ToDoMentoHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ToDoMentoHolder) holder).onBind(todoStudy.get(position),activity);
    }

    @Override
    public int getItemCount() {
        return todoStudy.size();
    }
}
