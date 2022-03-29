package com.example.studybridge.ToDo.Mento;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Mento.Inside.ToDoMentoInsideActivity;
import com.example.studybridge.http.dto.study.StudyFindRes;

public class ToDoMentoHolder extends RecyclerView.ViewHolder{

    private TextView studyName;
    private StudyFindRes studyRes;

    public ToDoMentoHolder(@NonNull View itemView) {
        super(itemView);

        studyName = (TextView) itemView.findViewById(R.id.todo_mentor_studyName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ToDoMentoInsideActivity.class);
                intent.putExtra("study", studyRes);
                view.getContext().startActivity(intent);
            }
        });
    }

    public void onBind(StudyFindRes study) {
        studyRes = study;
        studyName.setText(study.getName());

    }
}
