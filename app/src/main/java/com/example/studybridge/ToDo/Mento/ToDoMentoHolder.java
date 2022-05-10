package com.example.studybridge.ToDo.Mento;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Mento.Inside.ToDoMentoInsideActivity;
import com.example.studybridge.databinding.TodoMentorRcItemBinding;
import com.example.studybridge.http.dto.study.StudyFindRes;

public class ToDoMentoHolder extends RecyclerView.ViewHolder{

    private StudyFindRes studyRes;

    private TodoMentorRcItemBinding binding;

    public ToDoMentoHolder(@NonNull View itemView) {
        super(itemView);
        binding = TodoMentorRcItemBinding.bind(itemView);

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
        binding.name.setText(study.getName());

    }
}
