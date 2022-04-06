package com.example.studybridge.Study.StudyMento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyFilter;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StudyMentoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ProfileRes> listData = new ArrayList<>();


    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_mento_item, parent, false);

        return new StudyMentoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ((StudyMentoHolder) holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
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
        else if(s2.equals("전체")){
            return true;
        }
        else return false;
    }

}
