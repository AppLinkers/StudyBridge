package com.example.studybridge.Study.StudyMenti;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Chat.ChatActivity;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.Detail.StudyMentiDetail;
import com.example.studybridge.http.DataService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentiHolder extends RecyclerView.ViewHolder {

    public TextView status;
    public TextView subject;
    public TextView place;
    public TextView studyName;
    public TextView studyIntro;
    public TextView studyMaxNum,studyNowNum;
    public CardView statusColor;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";

    SharedPreferences sharedPreferences;
    String userId;

    Long study_id;
    String managerId;

    DataService dataService = new DataService();


    public StudyMentiHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        status = (TextView) itemView.findViewById(R.id.menti_status);
        subject = (TextView) itemView.findViewById(R.id.menti_subject);
        place = (TextView) itemView.findViewById(R.id.menti_place);
        studyName = (TextView) itemView.findViewById(R.id.menti_study_name);
        studyIntro = (TextView) itemView.findViewById(R.id.menti_study_intro);
        studyNowNum = (TextView) itemView.findViewById(R.id.menti_study_nowNum);
        studyMaxNum = (TextView) itemView.findViewById(R.id.menti_study_maxNum);

        statusColor = (CardView) itemView.findViewById(R.id.menti_status_Card);

        sharedPreferences = itemView.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToDetail = new Intent(view.getContext(), StudyMentiDetail.class);
//                Intent intentToChat = new Intent(view.getContext(), ChatActivity.class);

                String passStatus = status.getText() +"";
                String passSubject = subject.getText() +"";
                String passPlace = place.getText() +"";
                String passName = studyName.getText() +"";
                String passIntro = studyIntro.getText() +"";
                int maxPeople = Integer.parseInt(studyMaxNum.getText().toString());


                StudyMenti studyMenti = new StudyMenti(study_id , statusDef(passStatus),passSubject,passPlace,passName,passIntro,maxPeople);


                intentToDetail.putExtra("study",studyMenti);
                intentToDetail.putExtra("enrollMentiNum",Integer.parseInt(studyNowNum.getText().toString()));
                intentToDetail.putExtra("managerId",managerId);
                view.getContext().startActivity(intentToDetail);

                // 현재 스터디 id 와 사용자 로그인 아이디 필요
                /*dataService.study.isApplied(study_id, userId).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            if (response.body()) {
                                // if user does have the study auth
                                if(studyMenti.getStatus()>=2){
                                    intentToChat.putExtra("study", studyMenti);
                                    view.getContext().startActivity(intentToChat);
                                }else{
                                    intent.putExtra("study", studyMenti);
                                    intent.putExtra("hasAuth", true);
                                    view.getContext().startActivity(intent);
                                }

                            } else {
                                // else
                                intent.putExtra("study", studyMenti);
                                intent.putExtra("hasAuth", false);
                                view.getContext().startActivity(intent);
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });*/

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


    public void onBind(StudyMenti data, int count) {

        study_id = data.getId();
        status.setText(data.statusStr());
        subject.setText(data.getSubject());
        place.setText(data.getPlace());
        studyName.setText(data.getStudyName());
        studyIntro.setText(data.getStudyIntro());
        studyNowNum.setText(String.valueOf(count));
        studyMaxNum.setText(String.valueOf(data.getMaxNum()));

        getManagerId(study_id);

        if(data.status == 0){
            statusColor.setCardBackgroundColor(Color.parseColor("#FF03DAC5"));
        } else if(data.status == 1){
            statusColor.setCardBackgroundColor(Color.parseColor("#FBB8AC"));
        } else{
            statusColor.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
        }

    }


    //스터디 방장 찾기 위함
    public void getManagerId(Long studyId){
        dataService.study.maker(studyId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                managerId = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
