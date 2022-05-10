package com.example.studybridge.ToDo.Mento.Inside;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Mento.Inside.Detail.ToDoMentoInsideDetailAdapter;
import com.example.studybridge.Util.BackDialog;
import com.example.studybridge.databinding.TodoMentorInsideItemBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentorInsdieHolder extends RecyclerView.ViewHolder{

    private TodoMentorInsideItemBinding binding;

    private LinearLayoutManager linearLayoutManager;
    private ToDoMentoInsideDetailAdapter adapter;
    private DataService dataService = new DataService();
    private FindToDoRes todo;

    public ToDoMentorInsdieHolder(@NonNull View itemView) {
        super(itemView);
        binding = TodoMentorInsideItemBinding.bind(itemView);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.RCView.getVisibility()==View.GONE){
                    //안보일 때
                    setRecyclerView();
                } else if(binding.RCView.getVisibility()==View.VISIBLE){
                    //보일 때
                    binding.RCView.setVisibility(View.GONE);
                    binding.arrow.setImageResource(R.drawable.ic_arrow_down);

                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                setAlertDialog();

                return true;
            }
        });
    }

    public void onBind(FindToDoRes data){
        todo = data;
        binding.taskName.setText(data.getTask());
    }


    private void setRecyclerView(){
        adapter= new ToDoMentoInsideDetailAdapter();
        dataService = new DataService();
        binding.RCView.setVisibility(View.VISIBLE);
        binding.arrow.setImageResource(R.drawable.ic_arrow_up);
        linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.RCView.setLayoutManager(linearLayoutManager);

        getData();

    }

    private void getData(){
        dataService.assignedToDo.findByToDo(todo.getId()).enqueue(new Callback<List<FindAssignedToDoRes>>() {
            @Override
            public void onResponse(Call<List<FindAssignedToDoRes>> call, Response<List<FindAssignedToDoRes>> response) {
                for(FindAssignedToDoRes todo : response.body()){
                    adapter.addItem(todo);
                }
                binding.RCView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FindAssignedToDoRes>> call, Throwable t) {

            }
        });
    }
    private void setAlertDialog(){

        BackDialog dialog =BackDialog.getInstance(1);
        FragmentManager fm = ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();
        dialog.show(fm,"delete");

        dialog.setBackInterface(new BackDialog.BackInterface() {
            @Override
            public void okBtnClick() {
                dataService.toDo.delete(todo.getId()).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(itemView.getContext(), "삭제 완료!",Toast.LENGTH_SHORT).show();
                            getData();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });
            }
        });
    }
}
