package com.example.studybridge.Mypage.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.study.StudyFindRes;

import java.util.ArrayList;
import java.util.List;

public class MyPageChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<StudyFindRes> listData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_chat_item, parent, false);
        return new MypageChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MypageChatHolder)holder).onBind(listData.get(position));
    }
    public void addItem(StudyFindRes data){
        listData.add(data);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
