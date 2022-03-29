package com.example.studybridge.ToDo.feedback;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Chat.ChatAdaptHolder;
import com.example.studybridge.R;
import com.example.studybridge.ToDo.Menti.ToDoMentiHolder;
import com.example.studybridge.http.dto.feedBack.FindFeedBackRes;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<FindFeedBackRes> commentList = new ArrayList<FindFeedBackRes>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_comment_item, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CommentHolder) holder).onBind(commentList.get(position));
    }

    public void addItem(FindFeedBackRes data){
        commentList.add(data);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
