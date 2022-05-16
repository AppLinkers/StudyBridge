package com.example.studybridge.ToDo.Mento;

import android.app.Activity;
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
    private Activity activity;

    public ToDoMentoHolder(@NonNull View itemView) {
        super(itemView);
        binding = TodoMentorRcItemBinding.bind(itemView);

        binding.con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ToDoMentoInsideActivity.class);
                intent.putExtra("study", studyRes);
                view.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    public void onBind(StudyFindRes study,Activity activity) {
        studyRes = study;
        this.activity = activity;
        binding.name.setText(study.getName());
        binding.intro.setText(study.getInfo());
        binding.subject.setText(study.getType());
        binding.place.setText(study.getPlace());
        StringBuilder sb = new StringBuilder();
        sb.append(study.getMenteeCnt()).append("명 참여중..");
        binding.num.setText(sb.toString());

    }
}
