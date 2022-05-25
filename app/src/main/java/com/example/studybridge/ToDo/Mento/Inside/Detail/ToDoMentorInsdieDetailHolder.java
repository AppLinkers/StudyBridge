package com.example.studybridge.ToDo.Mento.Inside.Detail;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDo;
import com.example.studybridge.ToDo.ToDoDetailActivity;
import com.example.studybridge.databinding.TodoMentorInsideDetailItemBinding;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.google.android.material.card.MaterialCardView;

public class ToDoMentorInsdieDetailHolder extends RecyclerView.ViewHolder{

    private TodoMentorInsideDetailItemBinding binding;

    private FindAssignedToDoRes res;
    private Long menteePKId;
    private Activity activity;

    public ToDoMentorInsdieDetailHolder(@NonNull View itemView) {
        super(itemView);
        binding = TodoMentorInsideDetailItemBinding.bind(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ToDoDetailActivity.class);
                intent.putExtra("toDo",res);
                intent.putExtra("menteePKId",menteePKId);
                view.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }

    public void onBind(FindAssignedToDoRes data,Activity activity){

        this.activity = activity;
        binding.menteeId.setText(data.getMenteeName());
        binding.status.setText(data.getStatus());
        menteePKId = data.getMenteeId();
        setColor(binding.statusColor,data.getStatus());
        res = data;

    }

    private void setColor(MaterialCardView cardView,String status){
        switch (status){
            case "READY":
                cardView.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.palletRed));
                break;
            case "PROGRESS":
                cardView.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.palletBlue));
                break;
            case "DONE":
                cardView.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.palletYellow));
                break;
            case "CONFIRMED":
                cardView.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.palletGreen));
                break;
        }
    }
}
