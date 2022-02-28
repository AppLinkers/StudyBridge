package com.example.studybridge.Study.StudyMento;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudyMentoDetail.class);

                String passName = mentoName.getText() +"";
                String passSubject = subject.getText() +"";
                String passPlace = place.getText() +"";
                String passIntro = mentoIntro.getText() +"";
                String passSchool = mentoSchool.getText() +"";
                String passQualifi = mentoQualification.getText() +"";

                intent.putExtra("name",passName);
                intent.putExtra("subject",passSubject);
                intent.putExtra("place",passPlace);
                intent.putExtra("school",passSchool);
                intent.putExtra("qualify",passQualifi);
                intent.putExtra("intro",passIntro);

                view.getContext().startActivity(intent);
            }
        });

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
