package com.example.studybridge.ToDo.Menti;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Menti.Inside.ToDoMentiInsideAdapter;
import com.example.studybridge.ToDo.ToDo;

import java.util.ArrayList;

public class ToDoMentiHolder extends RecyclerView.ViewHolder{

    private TextView status;
    //리시이클러뷰
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ToDoMentiInsideAdapter toDoAdapter;
    private ArrayList<ToDo> datas = new ArrayList<>();


    public ToDoMentiHolder(@NonNull View itemView) {
        super(itemView);

        status = (TextView) itemView.findViewById(R.id.todo_menti_RV_status);

        recyclerView = (RecyclerView) itemView.findViewById(R.id.todo_menti_inside_RV);



    }

    public void onBind(String statusName){

        status.setText(statusName);
        setData(statusName);
        setRecyclerView();

    }



    private void setData(String statusName){

        int statusNum;

        if(statusName.equals("Ready")){
            statusNum = 0;
        }else if(statusName.equals("Progress")){
            statusNum = 1;
        }else{
            statusNum = 2;
        }

        toDoAdapter = new ToDoMentiInsideAdapter();

        ToDo toDo1 = new ToDo(null,0,"mentor","mentee","문제집 5페이지 풀기",null,"2022/03/28",null);
        ToDo toDo2 = new ToDo(null,1,null,null,"알고리즘 문제 복습",null,"2022/03/26",null);
        ToDo toDo3 = new ToDo(null,0,"mentor",null,"턱걸이 15회.",null,"2022/04/28",null);
        ToDo toDo4 = new ToDo(null,2,"mentor",null,"벤치프레스 10회",null,"2022/03/23",null);

        datas.add(toDo1);
        datas.add(toDo2);
        datas.add(toDo3);
        datas.add(toDo4);

        for(ToDo data : datas){
            if(statusNum == data.getStatus()){
                toDoAdapter.addItem(data);
            }
        }


    }
    private void setRecyclerView(){

        linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(toDoAdapter);
    }

}
