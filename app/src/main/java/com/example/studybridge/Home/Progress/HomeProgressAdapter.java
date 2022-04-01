package com.example.studybridge.Home.Progress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.study.StudyFindRes;

import java.util.ArrayList;

public class HomeProgressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<StudyFindRes> todoStudy = new ArrayList<>();
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_rv_item, parent, false);
        return new HomeProgressHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((HomeProgressHolder)holder).onBind(todoStudy.get(position));
    }

    @Override
    public int getItemCount() {
        return todoStudy.size();
    }
    public void addItem(StudyFindRes study){
        todoStudy.add(study);
    }
}
