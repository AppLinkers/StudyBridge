package com.example.studybridge.ToDo;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ToDoDetailHolder extends RecyclerView.ViewHolder {

    private TextView todoStatus;
    private LinearLayout testlayout;
    private ImageView arrow;
    private Boolean clicked = true;

    private RecyclerView recyclerView;
    private ToDoInsideAdapter adapter;
    private ArrayList<ToDoInside> arrayList;

    public ToDoDetailHolder(View itemView) {
        super(itemView);

        todoStatus = (TextView) itemView.findViewById(R.id.todo_status);
        recyclerView = itemView.findViewById(R.id.todo_inside_RCView);
        arrow = (ImageView) itemView.findViewById(R.id.todo_arrow);

        //리사이클러뷰
        arrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ToDoInsideAdapter();
        getData();
        recyclerView.setAdapter(adapter);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clicked == true){
                        recyclerView.setVisibility(View.VISIBLE);
                        arrow.setImageResource(R.drawable.ic_arrow_up);
                        clicked = false;
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_arrow_down);
                        clicked = true;
                    }

                }
            });


    }

    private void getData() {
        ToDoInside l1 = new ToDoInside("1주차 과제");
        ToDoInside l2 = new ToDoInside("2주차 과제");
        ToDoInside l3 = new ToDoInside("3주차 과제");

        adapter.addItem(l1);
        adapter.addItem(l2);
        adapter.addItem(l3);
    }

    public void onBind(ToDoDetail data){

        todoStatus.setText(data.getTodoStatus());

    }
}
