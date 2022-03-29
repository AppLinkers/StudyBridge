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
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentorInsdieHolder extends RecyclerView.ViewHolder{

    private TextView taskName;
    private RecyclerView recyclerView;
    private ImageView arrow;
    private LinearLayoutManager linearLayoutManager;
    private ToDoMentoInsideDetailAdapter adapter;
    private DataService dataService;
    private FindToDoRes todo;

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

    public void onBind(FindToDoRes data){
        todo = data;
        taskName.setText(data.getTask());
    }


    private void setRecyclerView(){
        adapter= new ToDoMentoInsideDetailAdapter();
        dataService = new DataService();
        recyclerView.setVisibility(View.VISIBLE);
        arrow.setImageResource(R.drawable.ic_arrow_up);
        linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        dataService.assignedToDo.findByToDo(todo.getId()).enqueue(new Callback<List<FindAssignedToDoRes>>() {
            @Override
            public void onResponse(Call<List<FindAssignedToDoRes>> call, Response<List<FindAssignedToDoRes>> response) {
                for(FindAssignedToDoRes todo : response.body()){
                    adapter.addItem(todo);
                    adapter.notifyDataSetChanged();/////////////////비동기 처리시 삭제
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FindAssignedToDoRes>> call, Throwable t) {

            }
        });

    }
    private void setAlertDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Todo 삭제").setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //삭제 메서드 작성
                dataService.toDo.delete(todo.getId()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(view.getContext(), taskName.getText()+" 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });
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
