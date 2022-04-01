package com.example.studybridge.Home.Progress;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDoFragment;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;
import com.example.studybridge.http.dto.toDo.ToDoStatus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeProgressHolder extends RecyclerView.ViewHolder{

    private TextView studyName,percentage;
    private LinearLayout todoBar,confirmBar;

    //퍼센트 계산 데이터
    int totalTask;
    float todoPerc;

    private Long userIdPk;
    private Long studyId;

    private ArrayList<FindAssignedToDoRes> toDoRes = new ArrayList<>();
    private ToDoFragment toDoFragment;
    FragmentTransaction transaction;

    private DataService dataService;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ISMENTEE = "user_mentee_key";

    boolean isMentee;
    SharedPreferences sharedPreferences;

    public HomeProgressHolder(@NonNull View itemView) {
        super(itemView);

        studyName = (TextView) itemView.findViewById(R.id.home_progress_studyName);
        percentage = (TextView) itemView.findViewById(R.id.home_progress_percent);
        todoBar = (LinearLayout) itemView.findViewById(R.id.home_progress_todoBar);
        confirmBar = (LinearLayout) itemView.findViewById(R.id.home_progress_confirmBar);

        //sharedpreference & 넘어온 데이터
        sharedPreferences = itemView.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY,  0);
        isMentee = sharedPreferences.getBoolean(USER_ISMENTEE, false);

        dataService =new DataService();

        findStudy();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("studyId", studyId);
                bundle.putBoolean("isMentee", isMentee);
                ToDoFragment toDoFragment = new ToDoFragment();//프래그먼트2 선언
                toDoFragment.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
                transaction.replace(R.id.container, toDoFragment);
                transaction.commit();

            }
        });



    }

    public void onBind(StudyFindRes data, FragmentTransaction ft){
        studyName.setText(data.getName());
        studyId = data.getId();
        transaction = ft;
    }

    private void findStudy(){
        dataService.assignedToDo.findByMentee(userIdPk).enqueue(new Callback<List<FindAssignedToDoRes>>() {
            @Override
            public void onResponse(Call<List<FindAssignedToDoRes>> call, Response<List<FindAssignedToDoRes>> response) {
                if(response.isSuccessful()){
                    for(FindAssignedToDoRes f: response.body()){
                        if(f.getStudyId().equals(studyId)){
                            toDoRes.add(f);
                        }
                    }
                }
                setPercentage();
            }

            @Override
            public void onFailure(Call<List<FindAssignedToDoRes>> call, Throwable t) {

            }
        });
    }

    private void setPercentage(){
        totalTask = toDoRes.size();
        int confirmCount=0;
        for(int i=0; i<totalTask ; i++){
            if(toDoRes.get(i).getStatus().equals("CONFIRMED")) confirmCount++;
        }

        if(totalTask==0){
            todoPerc = 0;
        } else todoPerc = (float) confirmCount / (float) totalTask;


        todoBar.setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1-todoPerc));
        confirmBar.setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,todoPerc));

        percentage.setText(String.format("%.1f %% 완료",todoPerc*100));
    }


}
