package com.example.studybridge.ToDo.Menti;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDoDetailActivity;
import com.example.studybridge.databinding.TodoMenteeRvitemBinding;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;

import java.util.Calendar;
import java.util.StringTokenizer;

public class ToDoMentiHolder extends RecyclerView.ViewHolder{

    //리시이클러뷰

    private TodoMenteeRvitemBinding binding;

    private FindAssignedToDoRes toDoRes;
    public final int ONE_DAY = 24 * 60 * 60 * 1000;
    private long dayResult;


    public ToDoMentiHolder(@NonNull View itemView) {
        super(itemView);
        binding = TodoMenteeRvitemBinding.bind(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ToDoDetailActivity.class);
                intent.putExtra("toDo", toDoRes);
                intent.putExtra("dayResult",dayResult);
                view.getContext().startActivity(intent);
            }
        });

    }

    public void onBind(FindAssignedToDoRes data){

        binding.name.setText(data.getTask());
        binding.dueDate.setText(data.getDueDate());
        setBackGround(binding.itemCon,data.getStatus());
        binding.dueDate.setText(getDday(data.getDueDate()));

        if (data.getStatus().equals("CONFIRMED")){
            binding.dueDate.setVisibility(View.INVISIBLE);
            binding.icTime.setVisibility(View.INVISIBLE);
            binding.confirmText.setVisibility(View.VISIBLE);
        }


        toDoRes = data;

    }

    private void setBackGround(RelativeLayout layout,String status){
        switch (status){
            case "READY":
                layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.palletRed));
                break;
            case "PROGRESS":
                layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.palletBlue));
                break;
            case "DONE":
                layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.palletYellow));
                break;
            case "CONFIRMED":
                layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.green));
                break;
        }
    }

    private String getDday(String duedate){

        final int year,month,day;
        StringTokenizer st = new StringTokenizer(duedate,"-");
        year = Integer.parseInt(st.nextToken());
        month = Integer.parseInt(st.nextToken());
        day = Integer.parseInt(st.nextToken().substring(0,2));

        final Calendar ddayCal = Calendar.getInstance();
        ddayCal.set(year,month-1,day);

        final long dday = ddayCal.getTimeInMillis()/ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis()/ONE_DAY;
        dayResult = dday - today;

        // 출력 시 d-day 에 맞게 표시
        final String strFormat;
        if (dayResult > 0) {
            strFormat = "D-%d";
        } else if (dayResult == 0) {
            strFormat = "D-Day";
        } else {
            strFormat = "마감";
            binding.dueDate.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.redDark));
        }

        final String strCount = (String.format(strFormat,dayResult));

        return strCount;
    }

}
