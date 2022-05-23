package com.example.studybridge.ToDo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.studybridge.Chat.ChatActivity;
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
import com.example.studybridge.http.dto.message.FindRoomRes;
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
    private Long roomId;
    private StudyFindRes studyRes;



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
        goChat();

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
                    public void choose(StudyFindRes study, String studyName) {
                        menteebinding.title.setText(studyName);

                        if(study!=null){
                            studyLong = study.getId();
                            studyRes = study;
                            getRoom(studyLong);
                        }
                        else studyLong = null;


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

    private void goChat(){

        menteebinding.goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(roomId!=null){
                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                    chatIntent.putExtra("roomId", roomId);
                    chatIntent.putExtra("study",studyRes);
                    view.getContext().startActivity(chatIntent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
/*                    Toast.makeText(getContext(), roomId.toString()+" "+studyLong.toString(), Toast.LENGTH_SHORT).show();*/
                }
                else {
                    Toast.makeText(getContext(), "스터디를 선택해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getRoom(Long studyId){
        dataService.chat.getRoom(studyId).enqueue(new Callback<FindRoomRes>() {
            @Override
            public void onResponse(Call<FindRoomRes> call, Response<FindRoomRes> response) {
                if(response.isSuccessful()){
                    roomId = response.body().getRoomId();
                }
            }

            @Override
            public void onFailure(Call<FindRoomRes> call, Throwable t) {

            }
        });
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

}

