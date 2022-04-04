package com.example.studybridge.Study.StudyMento;


import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class StudyMentoHolder extends RecyclerView.ViewHolder {

    private TextView subject,place,mentoName,mentoIntro,mentoSchool,mentoQualification;
    private ImageView heart;
    private ProfileRes profile;

    public StudyMentoHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        subject = (TextView) itemView.findViewById(R.id.mento_subject);
        place = (TextView) itemView.findViewById(R.id.mento_place);
        mentoName = (TextView) itemView.findViewById(R.id.mento_study_id);
        mentoIntro = (TextView) itemView.findViewById(R.id.mento_study_intro);
        mentoSchool = (TextView) itemView.findViewById(R.id.mento_study_school);
        mentoQualification = (TextView) itemView.findViewById(R.id.mento_study_qualification);
        heart = (ImageView) itemView.findViewById(R.id.mento_heart);


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
        subject.setText(data.getSubject());
        place.setText(data.getLocation());
        mentoName.setText(data.getNickName());
        mentoIntro.setText(data.getInfo());
        mentoSchool.setText(data.getSchool());

        if(data.getLiked()){
            heart.setSelected(true);
        } else heart.setSelected(false);

        if(data.getCertificates().size()>0){
            mentoQualification.setText(data.getCertificates().get(0).getCertificate());
        } else {
            mentoQualification.setText("자격증 없음");
        }

        profile = data;

    }
}
