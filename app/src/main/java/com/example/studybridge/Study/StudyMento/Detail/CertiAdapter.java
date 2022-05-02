package com.example.studybridge.Study.StudyMento.Detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.userMentor.Certificate;

import java.util.ArrayList;
import java.util.List;

public class CertiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Certificate> listData;

    public CertiAdapter(List<Certificate> listData){
        this.listData = listData;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mentor_certi_item, parent, false);

        return new CertiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CertiHolder) holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(Certificate data) {
        listData.add(data);
    }
}
