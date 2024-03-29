package com.example.studybridge.Study.StudyMento;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyFilter;
import com.example.studybridge.http.dto.userMentor.ProfileRes;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StudyMentoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ProfileRes> listData = new ArrayList<>();
    private Activity activity;


    public StudyMentoAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_mento_item, parent, false);

        return new StudyMentoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ((StudyMentoHolder) holder).onBind(listData.get(position),activity);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public long getItemId(int position) {
        return listData.get(position).getUserId();
    }

    public void addItem(ProfileRes data, StudyFilter filter) {
        if(isEqual(data.getSubject(),filter.getType())
                &&isEqual(data.getLocation(),filter.getPlace())){
            listData.add(data);
        }
    }
    public void clearItem(){listData.clear();}

    private boolean isEqual(String s1,String s2){
        if(s1.equals(s2)){
            return true;
        }
        else if(s2.equals("과목별")||s2.equals("지역별")){
            return true;
        }
        else return false;
    }

}
