package com.example.studybridge.ToDo.Menti;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Menti.Inside.ToDoMentiInsideAdapter;
import com.example.studybridge.ToDo.ToDo;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentiHolder extends RecyclerView.ViewHolder{

    private TextView status,taskInfo;
    //리시이클러뷰
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    ToDoMentiInsideAdapter toDoAdapter;
    DataService dataService;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    Long userIdPk;
    String userId;
    private ArrayList<ToDo> datas = new ArrayList<>();


    public ToDoMentiHolder(@NonNull View itemView) {
        super(itemView);

        sharedPreferences = itemView.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        userId = sharedPreferences.getString(USER_ID_KEY,"user");

        status = (TextView) itemView.findViewById(R.id.todo_menti_RV_status);

        recyclerView = (RecyclerView) itemView.findViewById(R.id.todo_menti_inside_RV);



    }

    public void onBind(String statusName){
        status.setText(statusName);
        setRecyclerView();
        setData(statusName);
    }


    public void setRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setData(String statusName){
        dataService = new DataService();
        toDoAdapter = new ToDoMentiInsideAdapter();

        dataService.assignedToDo.findByMentee(userIdPk).enqueue(new Callback<List<FindAssignedToDoRes>>() {
            @Override
            public void onResponse(Call<List<FindAssignedToDoRes>> call, Response<List<FindAssignedToDoRes>> response) {
                System.out.println(response.raw());
                if(response.isSuccessful()){

                    for(FindAssignedToDoRes data : response.body()){
                        ToDo todo = new ToDo(
                                data.getId(),
                                data.getStudyId(),
                                data.getStatus(),
                                data.getMentorName(),
                                data.getMenteeName(),
                                data.getTask(),
                                data.getExplain(),
                                data.getDueDate()+"",
                                null);
                        datas.add(todo);
                    }

                    System.out.println(datas.size());

                    for(ToDo data : datas){
                        if(data.getStatus().equals(statusName)){
                            toDoAdapter.addItem(data);
                        }
                    }
                    recyclerView.setAdapter(toDoAdapter);

                }
            }
            @Override
            public void onFailure(Call<List<FindAssignedToDoRes>> call, Throwable t) {

            }
        });

    }

}
