package com.example.studybridge.Study.StudyMento;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;
import com.example.studybridge.databinding.StudyMentoItemBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;
import com.example.studybridge.http.dto.userMentor.ProfileRes;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudyMentoHolder extends RecyclerView.ViewHolder {

    private ProfileRes profile;

    private StudyMentoItemBinding binding;

    private Activity activity;

    private DataService dataService = new DataService();

    public StudyMentoHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        binding = StudyMentoItemBinding.bind(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudyMentoDetail.class);
                intent.putExtra("profile", profile);
                view.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

    }


    public void onBind(ProfileRes data,Activity activity) {
        binding.mentorSubject.setText(data.getSubject());
        binding.mentorPlace.setText(data.getLocation());
        binding.mentorName.setText(data.getNickName());
        binding.mentorIntro.setText(data.getInfo());
        binding.mentorSchool.setText(data.getSchool());
        profile = data;
        setProfile(data.getUserName());
        this.activity = activity;
    }

    private void setProfile(String userId){
        dataService.userAuth.getProfile(userId).enqueue(new Callback<UserProfileRes>() {
            @Override
            public void onResponse(Call<UserProfileRes> call, Response<UserProfileRes> response) {
                if(response.isSuccessful()){
                    final String path = response.body().getProfileImg();
                    Glide.with(itemView).load(path).into(binding.mentorImg);
                }
            }

            @Override
            public void onFailure(Call<UserProfileRes> call, Throwable t) {

            }
        });
    }
}
