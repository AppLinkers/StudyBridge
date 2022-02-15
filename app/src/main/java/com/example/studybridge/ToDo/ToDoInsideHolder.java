package com.example.studybridge.ToDo;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

public class ToDoInsideHolder extends RecyclerView.ViewHolder {

    private TextView insideText;
    ToDoInside datas;

    public ToDoInsideHolder(View itemView) {
        super(itemView);

        insideText= (TextView) itemView.findViewById(R.id.todo_inside_text);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(view.getContext(), ToDoManageActivity.class);
               intent.putExtra("manageData", datas);
               view.getContext().startActivity(intent);
            }
        });
    }

    public void onBind(ToDoInside data) {
        datas = data;
        insideText.setText(datas.getTitle());
    }
}
