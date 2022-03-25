package com.example.studybridge.ToDo.Menti.Inside;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDo;
import com.example.studybridge.ToDo.ToDoDetailActivity;

import java.io.Serializable;

public class ToDoMentiInsideHolder extends RecyclerView.ViewHolder{

    private TextView taskName,studyName;
    private ToDo toDo;

    public ToDoMentiInsideHolder(@NonNull View itemView) {
        super(itemView);

        //화면 위 데이터
        taskName = (TextView) itemView.findViewById(R.id.todo_menti_inside_taskName);
        studyName = (TextView) itemView.findViewById(R.id.todo_menti_inside_studyName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ToDoDetailActivity.class);
                intent.putExtra("toDo", toDo);
                Toast.makeText(itemView.getContext(), toDo.getMentiId()+"", Toast.LENGTH_SHORT).show();
                view.getContext().startActivity(intent);
            }
        });


    }

    public void onBind(ToDo toDo){
        taskName.setText(toDo.getTaskName());
        this.toDo = toDo;
    }
}
