package com.example.studybridge.ToDo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.ToDo.Menti.FilterSpinner;
import com.example.studybridge.ToDo.Menti.ToDoMentiAdapter;
import com.example.studybridge.ToDo.Mento.ToDoMentoAdapter;
import com.example.studybridge.ToDo.Mento.ToDoMentoHolder;
import com.example.studybridge.Util.TodoDialog;
import com.example.studybridge.databinding.TodoMenteeFragmentBinding;
import com.example.studybridge.databinding.TodoMentorFragmentBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.ToDoStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoFragment extends Fragment {


    private TodoMenteeFragmentBinding menteebinding;
    private TodoMentorFragmentBinding mentorbinding;

    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ToDoMentiAdapter adapter;
    private ToDoMentoAdapter adapterMentor;

    private DataService dataService = new DataService();


    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_ISMENTEE = "user_mentee_key";

    private Long userIdPk;
    private String userId;
    private boolean isMentee;
    private Long studyLong;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //sharedPref
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        isMentee = sharedPreferences.getBoolean(USER_ISMENTEE,true);

        View view;

        if(isMentee){
            menteebinding = TodoMenteeFragmentBinding.inflate(inflater,container,false);
            view = menteebinding.getRoot();
            menteeUI();
        }
        else {
            mentorbinding = TodoMentorFragmentBinding.inflate(inflater,container,false);
            view = mentorbinding.getRoot();
            mentorUI();
        }


        return view;
    }

    private void menteeUI(){

        setFilter();
        menteeRV();

    }

    private void mentorUI(){
        mentorRV();

    }
    private void menteeRV(){

        gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        menteebinding.RCView.setLayoutManager(gridLayoutManager);
        setData();

    }
    private void mentorRV(){
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mentorbinding.RCView.setLayoutManager(linearLayoutManager);
        studyData();
    }
    private void setFilter(){
        menteebinding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoDialog dialog = TodoDialog.getInstance();
                FragmentManager fm = getParentFragmentManager();
                dialog.show(fm,"filter");

                dialog.setTodoInterface(new TodoDialog.TodoInterface() {
                    @Override
                    public void choose(Long studyId, String studyName) {
                        menteebinding.title.setText(studyName);
                        studyLong = studyId;
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        menteeRV();

                    }
                });
            }
        });
    }

    private void setData(){
        dataService.assignedToDo.findByMentee(userIdPk).enqueue(new Callback<List<FindAssignedToDoRes>>() {
            @Override
            public void onResponse(Call<List<FindAssignedToDoRes>> call, Response<List<FindAssignedToDoRes>> response) {
                if(response.isSuccessful()){
                    Collections.reverse(response.body());
                    adapter = new ToDoMentiAdapter(response.body(),studyLong);

                    List<FindAssignedToDoRes> calcList = new ArrayList<>();
                    if(studyLong == null){
                        calcList.addAll(response.body());
                    }
                    else {
                        for(FindAssignedToDoRes data:response.body()){
                            if(data.getStudyId().equals(studyLong)){
                                calcList.add(data);
                            }
                        }
                    }
                    setNumber(calcList,
                            menteebinding.readyNum,
                            menteebinding.progressNum,
                            menteebinding.doneNum,
                            menteebinding.confirmNum,
                            menteebinding.percent,
                            menteebinding.progressBar);


                }
                menteebinding.RCView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FindAssignedToDoRes>> call, Throwable t) {

            }
        });
    }

    private void setNumber(List<FindAssignedToDoRes> calcData,TextView ready, TextView progress, TextView done, TextView confirm, TextView percent, ProgressBar progressBar){
        final int total = calcData.size();
        int readyNum = 0,progressNum=0,doneNum=0,confirmNum=0;
        float percentNum;

        for(FindAssignedToDoRes data: calcData){
            if(data.getStatus().equals("READY")){
                readyNum++;
            }
            else if(data.getStatus().equals("PROGRESS")){
                progressNum++;
            }
            else if(data.getStatus().equals("DONE")){
                doneNum++;
            }
            else confirmNum++;

        }
        ready.setText(String.valueOf(readyNum));
        progress.setText(String.valueOf(progressNum));
        done.setText(String.valueOf(doneNum));
        confirm.setText(String.valueOf(confirmNum));

        if(total==0){
            percentNum = 0;
        }
        else{
            percentNum = (float) confirmNum/total*100;
        }
        progressBar.setProgress((int)percentNum);
        StringBuilder sb = new StringBuilder();
        sb.append((int)percentNum).append(" %");
        percent.setText(sb.toString());

    }

    private void studyData(){

        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    adapterMentor = new ToDoMentoAdapter(response.body(),getActivity());
                }
                mentorbinding.RCView.setAdapter(adapterMentor);
                StringBuilder sb = new StringBuilder();
                sb.append("(").append(adapterMentor.getItemCount()).append(")");
                mentorbinding.listNum.setText(sb.toString());
            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        menteebinding = null;
        mentorbinding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isMentee){
            setData();
        }

    }

    /*private void setSpinnerData(){
        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    Long a = Long.valueOf(0);
                    ArrayList<FilterSpinner> filtedList = new ArrayList<FilterSpinner>();
                    filtedList.add(new FilterSpinner(a,"전체"));
                    for(StudyFindRes data : response.body()){
                        if(data.getStatus().equals("MATCHED")){
                            filtedList.add(new FilterSpinner(data.getId(),data.getName()));
                        }
                    }
                    setSpinner(filtedList);
                }
            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });
    }

    int pos = 1;
    private void setSpinner(ArrayList<FilterSpinner> filtList){

        ArrayAdapter<FilterSpinner> categoryAdapter = new ArrayAdapter<FilterSpinner>(getContext(), android.R.layout.simple_spinner_item , filtList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(int j=0; j<filtList.size();j++){
            if(filtList.get(j).getKey()==filter){
                pos = j;
            }
        }
        menteebinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(pos !=0){
                    menteebinding.spinner.setSelection(pos);
                }

                FilterSpinner spinnerItem  = (FilterSpinner)adapterView.getItemAtPosition(i);
                key = spinnerItem.getKey();
                if(isMentee==true){
                    //Todolist 갯수확인
                    //setTaskCount();
                    setEachStudyCount(spinnerItem.getKey());
                    //리사이클러뷰 설정
                    menteeRV(spinnerItem.getKey());

                } else {}
                pos=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        menteebinding.spinner.setAdapter(categoryAdapter);

    }

    private void setEachStudyCount(Long filt){
        Long a = Long.valueOf(0);
        if(filt == a){
            setTaskCount();
        }else{
            dataService.assignedToDo.countByMenteeAndStudy(userIdPk,filt).enqueue(new Callback<Map<String, Integer>>() {
                @Override
                public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                    if(response.isSuccessful()){
                        int total = response.body().get("total");
                        int confirmed = response.body().get("confirmed");
                        menteebinding.todoNum.setText(total-confirmed+"");
                        menteebinding.percent.setText(((double)confirmed/(double)total) * 100 +"%");
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Integer>> call, Throwable t) {

                }
            });
        }

    }

    private void setConfirmedCount(int totalTask){

        dataService.assignedToDo.countByMenteeAndStatus(userIdPk, ToDoStatus.CONFIRMED).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    confirmedTask = response.body();
                    confPerc = ((double)confirmedTask / (double)totalTask ) * 100;
                    menteebinding.percent.setText(String.format("%.0f", confPerc)+"%");
                    menteebinding.todoNum.setText(totalTask-confirmedTask+"");
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void setTaskCount(){
        dataService.assignedToDo.countByMentee(userIdPk).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    totalTask = response.body();
                    setConfirmedCount(totalTask);
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }*/

    /*private void setMenteeUI(View view) {
        //멘티 화면 위 데이터
        year = (TextView) view.findViewById(R.id.todo_year_tv);
        month = (TextView) view.findViewById(R.id.todo_month_tv);
        day = (TextView) view.findViewById(R.id.todo_day_tv);
        taskCount = (TextView) view.findViewById(R.id.todo_taskCount);
        taskPerc = (TextView) view.findViewById(R.id.toDo_perc) ;
        filterSpinner = (Spinner) view.findViewById(R.id.todo_filter);
        recyclerView = (RecyclerView) view.findViewById(R.id.todo_menti_RV);
        setTime();
        setSpinnerData();
    }

    private void setMentorUI(View view){
        //멘토 화면 위 데이터
        year = (TextView) view.findViewById(R.id.todo_year_tv);
        month = (TextView) view.findViewById(R.id.todo_month_tv);
        day = (TextView) view.findViewById(R.id.todo_day_tv);
        recyclerView = (RecyclerView) view.findViewById(R.id.todo_mentor_RV);

        setTime();
        setMentorRecyclerView();
    }

    @SuppressLint("SimpleDateFormat")
    private void setTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        year.setText(new SimpleDateFormat("yyyy").format(date));
        month.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(date));
        day.setText(new SimpleDateFormat("dd").format(date));
    }

    private void setMenteeRecyclerView(Long filter) {
        ToDoMentiAdapter adapter = new ToDoMentiAdapter();
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //smooth scroll
        recyclerView.setOnFlingListener(null);
        linearSnapHelper.attachToRecyclerView(recyclerView);

        adapter.setFilter(filter);
        recyclerView.setAdapter(adapter);
    }


    private void setMentorRecyclerView() {
        toDoMentoAdapter = new ToDoMentoAdapter();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    for(StudyFindRes study : response.body()){
                        toDoMentoAdapter.addItem(study);
                    }
                    recyclerView.setAdapter(toDoMentoAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });

    }*/



}

