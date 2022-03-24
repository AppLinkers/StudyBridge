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

    ///
    private ArrayList<ToDo> data = new ArrayList<>();


    public ToDoMentiHolder(@NonNull View itemView) {
        super(itemView);

        status = (TextView) itemView.findViewById(R.id.todo_menti_RV_status);

        recyclerView = (RecyclerView) itemView.findViewById(R.id.todo_menti_inside_RV);

        setRecyclerView();
    }

    public void onBind(String statusName,ToDo data){

        status.setText(statusName);

        this.data.add(data);

    }

    private void setRecyclerView(){

        linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ToDoMentiInsideAdapter(data));
    }

}
