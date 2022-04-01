package com.example.studybridge.Home.Progress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

public class HomeProgressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_rv_item, parent, false);
        return new HomeProgressHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((HomeProgressHolder)holder).onBind();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
