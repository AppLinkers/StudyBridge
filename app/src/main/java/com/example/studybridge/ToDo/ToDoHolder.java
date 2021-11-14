package com.example.studybridge.ToDo;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.jetbrains.annotations.NotNull;

public class ToDoHolder extends RecyclerView.ViewHolder {

    public TextView studyName;
    public TextView startDate;
    public TextView percentage;
    public ProgressBar progressBar;

    public ToDoHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        studyName = (TextView) itemView.findViewById(R.id.study_name);
        startDate = (TextView) itemView.findViewById(R.id.study_start_date);
        percentage = (TextView) itemView.findViewById(R.id.study_percentage);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ToDoDetailActivity.class);

                String passName = studyName.getText() +"";
                intent.putExtra("name",passName);

                view.getContext().startActivity(intent);
            }
        });


    }

    public void onBind(ToDo data){
        studyName.setText(data.getStudyName());
        startDate.setText(data.getStartDate());
        progressBar.setProgress(data.getProgress());
        percentage.setText(String.valueOf(data.getProgress()));

    }
}
