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
import com.example.studybridge.http.dto.study.StudyFindRes;

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

    private StudyFindRes studyFindRes;

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

                intentToDetail.putExtra("study",studyFindRes);
                intentToDetail.putExtra("managerId",managerId);
                view.getContext().startActivity(intentToDetail);

            }
        });

    }



    public void onBind(StudyFindRes data) {

        study_id = data.getId();
        subject.setText(data.getType());
        place.setText(data.getPlace());
        studyName.setText(data.getName());
        studyIntro.setText(data.getInfo());
        studyNowNum.setText(String.valueOf(data.getMenteeCnt()));
        studyMaxNum.setText(String.valueOf(data.getMaxNum()));
        status.setText(data.statusSet(statusColor));

        getManagerId(study_id);

        studyFindRes = data;

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
