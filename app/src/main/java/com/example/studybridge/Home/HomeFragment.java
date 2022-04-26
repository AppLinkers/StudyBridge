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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Home.Progress.HomeProgressAdapter;
import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMentiFragment;
import com.example.studybridge.Study.StudyMento.StudyMentoFragment;
import com.example.studybridge.databinding.HomeFragmentBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.ToDoStatus;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    //todo progress bar
    LinearLayout readyBar,progressBar,doneBar,confirmBar;
    TextView readyNum, progressNum, doneNum, confirmNum,totalPercent;
    int totalTask;
    int todoCount;
    float todoPerc;

    //progress RecyclerView
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HomeProgressAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    //Dataservice
    DataService dataService;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_NAME = "user_name_key";

    private String userName,userId;
    private Long userIdPk;

    private HomeFragmentBinding binding;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater,container,false);

        View view = binding.getRoot();

        //sharedpreference
        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY,  0);
        userName= sharedPreferences.getString(USER_NAME, "사용자");

        setUI();
        //화면 위 데이터
//        userNameTv = view.findViewById(R.id.home_name);
/*        recyclerView = (RecyclerView) view.findViewById(R.id.home_RV);

        //ToDoItem
        readyBar = (LinearLayout) view.findViewById(R.id.home_readyBar);
        progressBar = (LinearLayout) view.findViewById(R.id.home_progressBar);
        doneBar = (LinearLayout) view.findViewById(R.id.home_doneBar);
        confirmBar = (LinearLayout) view.findViewById(R.id.home_confirmBar);

        readyNum = (TextView) view.findViewById(R.id.home_readyNum);
        progressNum = (TextView) view.findViewById(R.id.home_progressNum);
        doneNum = (TextView) view.findViewById(R.id.home_doneNum);
        confirmNum = (TextView) view.findViewById(R.id.home_confirmNum);
        totalPercent = (TextView) view.findViewById(R.id.home_totalPercent);*/



        //데이터서비스
        dataService = new DataService();


/*        setToDoBar();

        setRecyclerView();*/


        return view;
    }

    private void setUI(){
        StringBuilder sb = new StringBuilder();
        sb.append(userName).append("님 Let's Study!");
        binding.homeName.setText(sb.toString());
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
                    if(totalTask==0){
                        todoPerc = 0;
                    } else todoPerc = (float) todoCount / (float) totalTask;

                    bar.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,todoPerc));

                    if(status.equals(ToDoStatus.CONFIRMED)){
                        if(totalTask==0){
                            totalPercent.setText("0 %");
                        } else {
                            totalPercent.setText(String.format("%.0f %%",((float)response.body()/(float) totalTask)*100));
                        }
                    }

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
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    for(StudyFindRes study : response.body()){
                        if(study.getStatus().equals("MATCHED")){
                            adapter.addItem(study);
                        }
                    }
                    adapter.setFragmentManager(transaction);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
