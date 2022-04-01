package com.example.studybridge.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Home.Progress.HomeProgressAdapter;
import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.ToDoStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    TextView userNameTv;
    String userName;
    String userId;
    Long userIdPk;

    //todo progress bar
    LinearLayout readyBar,progressBar,doneBar,confirmBar;
    TextView readyNum, progressNum, doneNum, confirmNum;
    int totalTask;
    int todoCount;
    float todoPerc;

    //progress RecyclerView
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HomeProgressAdapter adapter;

    //Dataservice
    DataService dataService;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        //화면 위 데이터
        userNameTv = view.findViewById(R.id.home_name);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_RV);

        //ToDoItem
        readyBar = (LinearLayout) view.findViewById(R.id.home_readyBar);
        progressBar = (LinearLayout) view.findViewById(R.id.home_progressBar);
        doneBar = (LinearLayout) view.findViewById(R.id.home_doneBar);
        confirmBar = (LinearLayout) view.findViewById(R.id.home_confirmBar);

        readyNum = (TextView) view.findViewById(R.id.home_readyNum);
        progressNum = (TextView) view.findViewById(R.id.home_progressNum);
        doneNum = (TextView) view.findViewById(R.id.home_doneNum);
        confirmNum = (TextView) view.findViewById(R.id.home_confirmNum);

        //sharedpreference & 넘어온 데이터
        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY,  0);

        Bundle bundle = getArguments();
        userId = bundle.getString("id");
        userName = bundle.getString("name");
        userNameTv.setText(userName);

        //데이터서비스
        dataService = new DataService();


        setToDoBar();

        setRecyclerView();


        return view;
    }


    private void setToDoBar(){
        dataService.assignedToDo.countByMentee(userIdPk).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                totalTask = response.body();
                insertBar(totalTask, ToDoStatus.READY,readyBar,readyNum);
                insertBar(totalTask, ToDoStatus.PROGRESS,progressBar,progressNum);
                insertBar(totalTask, ToDoStatus.DONE, doneBar,doneNum);
                insertBar(totalTask, ToDoStatus.CONFIRMED, confirmBar,confirmNum);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    private void insertBar(int totalTask, ToDoStatus status, LinearLayout bar, TextView num){
        dataService.assignedToDo.countByMenteeAndStatus(userIdPk, status).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    todoCount = response.body();
                    num.setText(todoCount+"");
                    todoPerc = (float) todoCount / (float) totalTask;
                    bar.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,todoPerc));

                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void setRecyclerView(){
        adapter = new HomeProgressAdapter();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    for(StudyFindRes study : response.body()){
                        adapter.addItem(study);
                    }
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });

    }


}
