package com.example.studybridge.ToDo;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<ToDoInside> list;

    public ToDoDetailHolder(View itemView) {
        super(itemView);

        todoStatus = (TextView) itemView.findViewById(R.id.todo_status);
        recyclerView = itemView.findViewById(R.id.todo_inside_RCView);
        arrow = (ImageView) itemView.findViewById(R.id.todo_arrow);

        //리사이클러뷰
        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ToDoInsideAdapter();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clicked == true){
                        recyclerView.setVisibility(View.VISIBLE);
                        arrow.setImageResource(R.drawable.ic_arrow_up);
                        String statusStr = todoStatus.getText().toString();
                        getData(statusStr);
                        Toast.makeText(view.getContext(), statusStr,Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(adapter);
                        clicked = false;
                    } else {
                       adapter.clearItem();
                       recyclerView.setAdapter(adapter);
                       recyclerView.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_arrow_down);
                        clicked = true;
                    }

                }
            });


    }

    private void getData(String statusStr) {
        list.clear();
        ToDoInside l = new ToDoInside("운전과제","3.1","Done","운");
        ToDoInside l1 = new ToDoInside("운전과제","3.1","Progress","운");
        ToDoInside l2 = new ToDoInside("물리과제 ","5.1","Ready","운");
        ToDoInside l3 = new ToDoInside("화학과제 ","7.1","Ready","운");

        list.add(l);
        list.add(l1);
        list.add(l2);
        list.add(l3);

        for(ToDoInside i : list){
            if(i.getStatus().equals(statusStr)){
                adapter.addItem(i);
            }
        }

    }

    public void onBind(ToDoDetail data){

        todoStatus.setText(data.getTodoStatus());

    }
}
