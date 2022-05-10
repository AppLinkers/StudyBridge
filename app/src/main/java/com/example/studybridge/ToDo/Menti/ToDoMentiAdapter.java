package com.example.studybridge.ToDo.Menti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.AssignToDoReq;

import java.util.ArrayList;
import java.util.List;

public class ToDoMentiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<FindAssignedToDoRes> listData = new ArrayList<>();

    public ToDoMentiAdapter(List<FindAssignedToDoRes> listData,Long studyId) {

        if(studyId==null){
            this.listData = listData;
        }
        else{
            for(FindAssignedToDoRes data:listData){

                if(data.getStudyId().equals(studyId)){
                    this.listData.add(data);
                }
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_mentee_rvitem, parent, false);

        return new ToDoMentiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ToDoMentiHolder) holder).onBind(listData.get(position));
    }



    @Override
    public int getItemCount() {
        return listData.size();
    }

}
