package com.example.studybridge.ToDo.Menti.Inside;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDo;
import com.example.studybridge.ToDo.ToDoDetailActivity;
import com.google.android.material.card.MaterialCardView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.StringTokenizer;

public class ToDoMentiInsideHolder extends RecyclerView.ViewHolder{

    private TextView taskName,d_Day;
    private LinearLayout color;
    private ImageView img;
    private ToDo toDo;

    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    private long dayResult;

    public ToDoMentiInsideHolder(@NonNull View itemView) {
        super(itemView);

        //화면 위 데이터
        taskName = (TextView) itemView.findViewById(R.id.todo_menti_inside_taskName);
        d_Day = (TextView) itemView.findViewById(R.id.todo_menti_inside_CVText);
        color = (LinearLayout)itemView.findViewById(R.id.todo_menti_inside_CVColor);
        img = (ImageView) itemView.findViewById(R.id.todo_menti_inside_CVImg);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ToDoDetailActivity.class);
                intent.putExtra("toDo", toDo);
                intent.putExtra("dayResult",dayResult);
                view.getContext().startActivity(intent);
            }
        });


    }

    @SuppressLint("ResourceAsColor")
    public void onBind(ToDo toDo){
        taskName.setText(toDo.getTaskName());
        this.toDo = toDo;
        d_Day.setText(getDday(toDo.getDueDate()));

        if (toDo.getStatus().equals("CONFIRMED")){
            d_Day.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            color.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.green));
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
            d_Day.setTextColor(Color.WHITE);
            color.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.redDark));
        }

        final String strCount = (String.format(strFormat,dayResult));

        return strCount;
    }
}
