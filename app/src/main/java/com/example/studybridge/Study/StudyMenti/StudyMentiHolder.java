package com.example.studybridge.Study.StudyMenti;


import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

public class StudyMentiHolder extends RecyclerView.ViewHolder {

    public TextView status;
    public TextView subject;
    public TextView place;
    public TextView studyName;
    public TextView studyIntro;
    public TextView studyPeopleNum;
    public CardView statusColor;


    public StudyMentiHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        status = (TextView) itemView.findViewById(R.id.menti_status);
        subject = (TextView) itemView.findViewById(R.id.menti_subject);
        place = (TextView) itemView.findViewById(R.id.menti_place);
        studyName = (TextView) itemView.findViewById(R.id.menti_study_name);
        studyIntro = (TextView) itemView.findViewById(R.id.menti_study_intro);
        studyPeopleNum = (TextView) itemView.findViewById(R.id.menti_study_peopleNum);

        statusColor = (CardView) itemView.findViewById(R.id.menti_status_Card);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudyMentiDetail.class);

                String passName = studyName.getText() +"";
                String passSubject = subject.getText() +"";
                String passPlace = place.getText() +"";
                String passPeople = studyPeopleNum.getText() +"";
                String passStatus = status.getText() +"";
                String passIntro = studyIntro.getText() +"";


                StudyMenti studyMenti = new StudyMenti(statusDef(passStatus),passSubject,passPlace,passName,passIntro,10);

                intent.putExtra("study", studyMenti);

                view.getContext().startActivity(intent);
            }
        });

    }

    public int statusDef(String status){
        if(status.equals("멘티 모집중")){
            return 0;
        }
        else if(status.equals("멘토 모집중")){
            return 1;
        }
        else if(status.equals("매칭 완료")){
            return 2;
        }else{
            return 3;
        }


    }


    public void onBind(StudyMenti data) {
//        status.setText(String.valueOf(data.getStatus()));
        subject.setText(data.getSubject());
        place.setText(data.getPlace());
        studyName.setText(data.getStudyName());
        studyIntro.setText(data.getStudyIntro());
        studyPeopleNum.setText(String.valueOf(data.getStudyPeopleNum()));
        status.setText(data.statusStr());

        if(data.status == 0){
            statusColor.setCardBackgroundColor(Color.parseColor("#FF03DAC5"));
        } else if(data.status == 1){
            statusColor.setCardBackgroundColor(Color.parseColor("#FBB8AC"));
        } else{
            statusColor.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
        }

    }
}
