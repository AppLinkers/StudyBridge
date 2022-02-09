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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ToDoDetailHolder extends RecyclerView.ViewHolder {

    private TextView todoStatus;
    private LinearLayout testlayout;
    private ImageView arrow;
    private Boolean clicked = true;

    private RecyclerView recyclerView;
    private ToDoInsideAdapter adapter;
    private ArrayList<ToDoInside> list;

    public ToDoDetailHolder(View itemView, ArrayList<ToDoInside> assignList) {
        super(itemView);

        todoStatus = (TextView) itemView.findViewById(R.id.todo_status);
        recyclerView = itemView.findViewById(R.id.todo_inside_RCView);
        arrow = (ImageView) itemView.findViewById(R.id.todo_arrow);

        //리사이클러뷰

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
                        getData(statusStr,assignList);
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

    public int countList(){
      return list.size();
    }

    private void getData(String statusStr, ArrayList<ToDoInside> assignList) {
        for(ToDoInside i : assignList){
            if(i.getStatus().equals(statusStr)){
                adapter.addItem(i);
            }
        }

    }

    public void onBind(ToDoDetail data){

        todoStatus.setText(data.getTodoStatus());

    }
}
