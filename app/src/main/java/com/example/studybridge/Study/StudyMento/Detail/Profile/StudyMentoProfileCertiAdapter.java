package com.example.studybridge.Study.StudyMento.Detail.Profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.StudyMento;
import com.example.studybridge.Study.StudyMento.StudyMentoHolder;

import java.util.ArrayList;

public class StudyMentoProfileCertiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<String> listData = new ArrayList<>();

    public StudyMentoProfileCertiAdapter(ArrayList<String> listData){
        this.listData = listData;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_mento_detail_profile_certi_item, parent, false);

        return new StudyMentoProfileCertiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((StudyMentoProfileCertiHolder) holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(String data) {
        listData.add(data);
    }
}
