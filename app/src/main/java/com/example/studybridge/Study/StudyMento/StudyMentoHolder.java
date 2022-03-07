package com.example.studybridge.Study.StudyMento;


import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class StudyMentoHolder extends RecyclerView.ViewHolder {

    private TextView subject,place,mentoName,mentoIntro,mentoSchool,mentoQualification;
    private ImageButton heart;
//    StudyMento mentoProfile;
    private MyPageMentoProfile profile;

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



                boolean passHeart = heart.isSelected();

                intent.putExtra("heart",passHeart);
                intent.putExtra("profile", profile);


                view.getContext().startActivity(intent);
            }
        });

    }


    public void onBind(MyPageMentoProfile data) {
        subject.setText(data.getSubject());
        place.setText(data.getPlace());
        mentoName.setText(data.getNickName());
        mentoIntro.setText(data.getIntro());
        mentoSchool.setText(data.getSchool());
        mentoQualification.setText("data.getMetnoQualification()");
//        mentoProfile = data;
        profile = data;

    }
}
