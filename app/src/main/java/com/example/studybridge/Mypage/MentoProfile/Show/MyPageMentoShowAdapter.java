package com.example.studybridge.Mypage.MentoProfile.Show;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.userMentor.Certificate;

import java.util.ArrayList;

public class MyPageMentoShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Certificate> listData;

    public MyPageMentoShowAdapter(ArrayList<Certificate> listData){
        this.listData = listData;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_mento_detail_profile_certi_item, parent, false);

        return new MyPageMentoShowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyPageMentoShowHolder) holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(Certificate data) {
        listData.add(data);
    }
}
