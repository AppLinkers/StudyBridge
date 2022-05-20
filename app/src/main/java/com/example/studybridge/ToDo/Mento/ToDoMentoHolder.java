package com.example.studybridge.ToDo.Mento;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.ToDo.Mento.Inside.ToDoMentoInsideActivity;
import com.example.studybridge.databinding.TodoMentorRcItemBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentoHolder extends RecyclerView.ViewHolder{

    private StudyFindRes studyRes;

    private TodoMentorRcItemBinding binding;
    private Activity activity;
    private ArrayList<String> menteeArr;

    private DataService dataService = new DataService();

    public ToDoMentoHolder(@NonNull View itemView) {
        super(itemView);
        binding = TodoMentorRcItemBinding.bind(itemView);

        binding.con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ToDoMentoInsideActivity.class);
                intent.putExtra("study", studyRes);
                intent.putStringArrayListExtra("menteeArr",menteeArr);
                view.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    public void onBind(StudyFindRes study,Activity activity) {
        studyRes = study;
        this.activity = activity;
        binding.name.setText(study.getName());
        binding.intro.setText(study.getInfo());
        binding.subject.setText(study.getType());
        binding.place.setText(study.getPlace());
        StringBuilder sb = new StringBuilder();
        sb.append(study.getMenteeCnt()).append("명 참여중..");
        setImgCnt(study.getMenteeCnt());
        binding.num.setText(sb.toString());
        getProfile(study.getId());

    }

    private void setImgCnt(int cnt){
        if(cnt<=1){
            binding.imgSecondCon.setVisibility(View.GONE);
            binding.imgThirdCon.setVisibility(View.GONE);

        }
        else if(cnt == 2){
            binding.imgThirdCon.setVisibility(View.GONE);
        }
    }


    private void getProfile(Long studyId){

        dataService.study.menteeList(studyId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){

                    final List<String> arr = response.body();
                    menteeArr = new ArrayList<>(arr);
                    final int size = arr.size();

                    if(size<=1){
                        setImg(menteeArr.get(0),binding.img1);

                    }
                    else if(size == 2){
                        setImg(menteeArr.get(0),binding.img1);
                        setImg(menteeArr.get(1),binding.img2);
                    }
                    else {
                        setImg(menteeArr.get(0),binding.img1);
                        setImg(menteeArr.get(1),binding.img2);
                        setImg(menteeArr.get(2),binding.img3);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    private void setImg(String userId, ImageView imageView){
        dataService.userAuth.getProfile(userId).enqueue(new Callback<UserProfileRes>() {
            @Override
            public void onResponse(Call<UserProfileRes> call, Response<UserProfileRes> response) {
                if(response.isSuccessful()){
                    final String path = response.body().getProfileImg();
                    Glide.with(itemView).load(path).into(imageView);
                }
            }

            @Override
            public void onFailure(Call<UserProfileRes> call, Throwable t) {

            }
        });
    }
}
