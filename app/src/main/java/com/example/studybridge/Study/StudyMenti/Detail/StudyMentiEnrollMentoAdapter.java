package com.example.studybridge.Study.StudyMenti.Detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.UserSignUpReq;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StudyMentiEnrollMentoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<String> listData = new ArrayList<>();


    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_menti_detail_enrollmento_item, parent, false);
        return new StudyMentiEnrollMentoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ((StudyMentiEnrollMentoHolder) holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(String data) {
        listData.add(data);
    }
}
