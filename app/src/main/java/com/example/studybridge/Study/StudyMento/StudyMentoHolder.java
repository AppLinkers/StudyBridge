package com.example.studybridge.Study.StudyMento;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;
import com.example.studybridge.databinding.StudyMentoItemBinding;
import com.example.studybridge.http.dto.userMentor.ProfileRes;

import org.jetbrains.annotations.NotNull;



public class StudyMentoHolder extends RecyclerView.ViewHolder {

    private ProfileRes profile;

    private StudyMentoItemBinding binding;

    public StudyMentoHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        binding = StudyMentoItemBinding.bind(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudyMentoDetail.class);
                intent.putExtra("profile", profile);
                view.getContext().startActivity(intent);
            }
        });

    }


    public void onBind(ProfileRes data) {
        binding.mentorSubject.setText(data.getSubject());
        binding.mentorPlace.setText(data.getLocation());
        binding.mentorName.setText(data.getNickName());
        binding.mentorIntro.setText(data.getInfo());
        binding.mentorSchool.setText(data.getSchool());

        final int certiNum = data.getCertificates().size();
        if(certiNum>1){
            StringBuilder sb = new StringBuilder();
            sb.append(data.getCertificates().get(0).getCertificate()).append(" 외 ").append(data.getCertificates().size()-1).append("개");
            binding.mentorQualification.setText(sb.toString());
        } else if (certiNum==1){
            binding.mentorQualification.setText(data.getCertificates().get(0).getCertificate());
        }
        else{
            binding.mentorQualification.setText("자격증 없음");
        }
        profile = data;
    }
}
