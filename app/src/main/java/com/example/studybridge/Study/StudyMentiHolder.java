package com.example.studybridge.Study;


import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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

    }


    public void onBind(StudyMenti data) {
//        status.setText(String.valueOf(data.getStatus()));
        subject.setText(data.getSubject());
        place.setText(data.getPlace());
        studyName.setText(data.getStudyName());
        studyIntro.setText(data.getStudyIntro());
        studyPeopleNum.setText(String.valueOf(data.getStudyPeopleNum()));

        if(data.status == 0){
            status.setText("대기중");
            statusColor.setCardBackgroundColor(Color.parseColor("#FF03DAC5"));
        } else if(data.status == 1){
            status.setText("모집중");
            statusColor.setCardBackgroundColor(Color.parseColor("#FBB8AC"));
        } else{
            status.setText("모집완료");
            statusColor.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
        }

    }
}
