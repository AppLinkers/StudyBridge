package com.example.studybridge.Chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.study.StudyFindRes;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<StudyFindRes> listData = new ArrayList<>();
    private Activity activity;

    public ChatRoomAdapter(List<StudyFindRes> listData,Activity activity,String filterText) {
        for(StudyFindRes res: listData){
            if(res.getStatus().equals("MATCHED")){
                if(filterText==null){
                    this.listData.add(res);
                }
                else {
                    if(res.getName().contains(filterText)){
                        this.listData.add(res);
                    }
                }
            }
        }
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_item, parent, false);
        return new ChatRoomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ChatRoomHolder)holder).onBind(listData.get(position),activity);
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }
}
