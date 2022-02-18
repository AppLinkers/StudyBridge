package com.example.studybridge.ToDo.ToDoInside;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDoManageActivity;

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
