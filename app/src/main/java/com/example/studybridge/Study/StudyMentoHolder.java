package com.example.studybridge.Study;


import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

public class StudyMentoHolder extends RecyclerView.ViewHolder {

    public TextView subject,place,mentoName,mentoIntro,mentoSchool,mentoQualification;

    public StudyMentoHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        subject = (TextView) itemView.findViewById(R.id.mento_subject);
        place = (TextView) itemView.findViewById(R.id.mento_place);
        mentoName = (TextView) itemView.findViewById(R.id.mento_study_id);
        mentoIntro = (TextView) itemView.findViewById(R.id.mento_study_intro);
        mentoSchool = (TextView) itemView.findViewById(R.id.mento_study_school);
        mentoQualification = (TextView) itemView.findViewById(R.id.mento_study_qualification);

    }


    public void onBind(StudyMento data) {
        subject.setText(data.getSubject());
        place.setText(data.getPlace());
        mentoName.setText(data.getMentoName());
        mentoIntro.setText(data.getMetnoIntro());
        mentoSchool.setText(data.getMetnoSchool());
        mentoQualification.setText(data.getMetnoQualification());

    }
}
