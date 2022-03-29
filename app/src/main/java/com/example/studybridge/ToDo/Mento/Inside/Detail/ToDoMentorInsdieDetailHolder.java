package com.example.studybridge.ToDo.Mento.Inside.Detail;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDo;
import com.example.studybridge.ToDo.ToDoDetailActivity;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;

public class ToDoMentorInsdieDetailHolder extends RecyclerView.ViewHolder{

    private TextView menteeId,menteeStaus;
    private ToDo todo;

    public ToDoMentorInsdieDetailHolder(@NonNull View itemView) {
        super(itemView);

        menteeId = (TextView) itemView.findViewById(R.id.todo_mentor_inside_detail_menteeId);
        menteeStaus = (TextView) itemView.findViewById(R.id.todo_mentor_inside_detail_menteeStatus);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ToDoDetailActivity.class);
                intent.putExtra("toDo",todo);
                view.getContext().startActivity(intent);
            }
        });
    }

    public void onBind(FindAssignedToDoRes data){

        todo = new ToDo(data.getId(), data.getStudyId(), data.getStatus(), data.getMentorName(), data.getMenteeName(), data.getTask(), data.getExplain(), data.getDueDate(), null);
        menteeId.setText(data.getMenteeName());
        menteeStaus.setText(data.getStatus());

    }
}
