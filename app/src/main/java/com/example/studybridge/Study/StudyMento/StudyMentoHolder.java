package com.example.studybridge.Study.StudyMento;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class StudyMentoHolder extends RecyclerView.ViewHolder {

    private TextView subject,place,mentoName,mentoIntro,mentoSchool,mentoQualification;
    private ImageButton heart;

    public StudyMentoHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        subject = (TextView) itemView.findViewById(R.id.mento_subject);
        place = (TextView) itemView.findViewById(R.id.mento_place);
        mentoName = (TextView) itemView.findViewById(R.id.mento_study_id);
        mentoIntro = (TextView) itemView.findViewById(R.id.mento_study_intro);
        mentoSchool = (TextView) itemView.findViewById(R.id.mento_study_school);
        mentoQualification = (TextView) itemView.findViewById(R.id.mento_study_qualification);
        heart = (ImageButton) itemView.findViewById(R.id.mento_heart);


        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heart.isSelected() == true){
                    heart.setSelected(false);
                } else {
                    Snackbar.make(itemView.getRootView(),"관심멘토로 등록되었습니다",Snackbar.LENGTH_SHORT).show();
                    heart.setSelected(true);
                }
            }
        });

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
                boolean passHeart = heart.isSelected();



                intent.putExtra("name",passName);
                intent.putExtra("intro",passIntro);
                intent.putExtra("subject",passSubject);
                intent.putExtra("place",passPlace);
                intent.putExtra("school",passSchool);
                intent.putExtra("qualify",passQualifi);
                intent.putExtra("heart",passHeart);


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
