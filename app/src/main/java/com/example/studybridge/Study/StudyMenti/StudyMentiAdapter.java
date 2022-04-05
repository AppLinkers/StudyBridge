package com.example.studybridge.Study.StudyMenti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyFilter;
import com.example.studybridge.http.dto.study.StudyFindRes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StudyMentiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<StudyFindRes> listData = new ArrayList<>();


    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_menti_item, parent, false);
        return new StudyMentiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ((StudyMentiHolder) holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(StudyFindRes data, StudyFilter filter) {
        if(isEqual(data.getStatus(),filter.getStatus())
                &&isEqual(data.getType(),filter.getType())
                &&isEqual(data.getPlace(),filter.getPlace())){
            listData.add(data);
        }
    }

    public void clearItem(){listData.clear();}

    private boolean isEqual(String s1,String s2){
        if(s1.equals(s2)){
            return true;
        }
        else if(s2.equals("전체")){
            return true;
        }
        else return false;
    }
}
