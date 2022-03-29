package com.example.studybridge.ToDo.Mento.Inside;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Mento.Inside.Detail.ToDoMentoInsideDetailAdapter;

public class ToDoMentorInsdieHolder extends RecyclerView.ViewHolder{

    private TextView taskName;
    private RecyclerView recyclerView;
    private ImageView arrow;
    private LinearLayoutManager linearLayoutManager;

    public ToDoMentorInsdieHolder(@NonNull View itemView) {
        super(itemView);

        taskName = (TextView) itemView.findViewById(R.id.todo_mentor_inside_taskName);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.todo_mentor_inside_detailRV);
        arrow = (ImageView) itemView.findViewById(R.id.todo_mentor_arrowIc);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerView.getVisibility()==View.GONE){
                    //안보일 때
                    setRecyclerView();
                } else if(recyclerView.getVisibility()==View.VISIBLE){
                    //보일 때
                    recyclerView.setVisibility(View.GONE);
                    arrow.setImageResource(R.drawable.ic_arrow_down);

                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                setAlertDialog(view);

                return true;
            }
        });
    }

    public void onBind(String example){
        taskName.setText(example);
    }
    private void setRecyclerView(){

        recyclerView.setVisibility(View.VISIBLE);
        arrow.setImageResource(R.drawable.ic_arrow_up);

        linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ToDoMentoInsideDetailAdapter());

    }
    private void setAlertDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Todo 삭제").setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //삭제 메서드 작성
                Toast.makeText(view.getContext(), taskName.getText()+" 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //취소
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
