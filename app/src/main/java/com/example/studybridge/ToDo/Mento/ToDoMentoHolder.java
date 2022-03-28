package com.example.studybridge.ToDo.Mento;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Mento.Inside.ToDoMentoInsideActivity;

public class ToDoMentoHolder extends RecyclerView.ViewHolder{

    private TextView studyName;

    public ToDoMentoHolder(@NonNull View itemView) {
        super(itemView);

        studyName = (TextView) itemView.findViewById(R.id.todo_mentor_studyName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ToDoMentoInsideActivity.class);
                intent.putExtra("studyName",studyName.getText());
                view.getContext().startActivity(intent);
            }
        });
    }

    public void onBind(String example) {

        studyName.setText(example);

    }
}
