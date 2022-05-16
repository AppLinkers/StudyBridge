package com.example.studybridge.Study.StudyMenti;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.Detail.StudyMentiDetail;
import com.example.studybridge.databinding.StudyMentiItemBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;

import org.jetbrains.annotations.NotNull;

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

    //sharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";

    String userId;
    Long study_id;
    String managerId;
    Boolean isApplied;

    private StudyFindRes studyFindRes;
    private StudyMentiItemBinding binding;
    private Activity activity;

    DataService dataService = new DataService();


    public StudyMentiHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        binding = StudyMentiItemBinding.bind(itemView);

        sharedPreferences = itemView.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToDetail = new Intent(view.getContext(), StudyMentiDetail.class);
                intentToDetail.putExtra("study",studyFindRes);
                intentToDetail.putExtra("managerId",managerId);
                intentToDetail.putExtra("isApplied",isApplied);

                view.getContext().startActivity(intentToDetail);
                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

    }



    public void onBind(StudyFindRes data, Activity activity) {

        study_id = data.getId();

        binding.studySubject.setText(data.getType());
        binding.studyPlace.setText(data.getPlace());
        binding.studyName.setText(data.getName());
        binding.studyIntro.setText(data.getInfo());
        StringBuilder sb = new StringBuilder();
        sb.append("인원수 : ").append(data.getMenteeCnt()).append(" / ").append(data.getMaxNum());
        binding.studyNum.setText(sb.toString());
        data.statusSet(binding.studyStatus, itemView.getContext());

        getManagerId(study_id);
        findApplied(study_id);

        studyFindRes = data;
        this.activity = activity;

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
    private void findApplied(Long study_id){
        dataService.study.isApplied(study_id,userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                isApplied = response.body();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

}
