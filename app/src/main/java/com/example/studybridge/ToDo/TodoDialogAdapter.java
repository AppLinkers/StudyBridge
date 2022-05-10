package com.example.studybridge.ToDo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Util.ItemClickListener;
import com.example.studybridge.databinding.DialogTodoItemBinding;
import com.example.studybridge.http.dto.study.StudyFindRes;

import java.util.ArrayList;
import java.util.List;

public class TodoDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<StudyFindRes> listData = new ArrayList<>();
    private TodoDialogAdapter.dialogInterface dialogInterface;

    public void setDialogInterface(TodoDialogAdapter.dialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
    }

    public interface dialogInterface{
        void select(long studyId,String studyName);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_todo_item,parent,false);
        return new ToDoDialogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ToDoDialogHolder) holder).onBind(listData.get(position));
    }
    public void addItem(StudyFindRes res){
        this.listData.add(res);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ToDoDialogHolder extends RecyclerView.ViewHolder{

        private DialogTodoItemBinding binding;
        private long studyId;
        private String studyName;

        public ToDoDialogHolder(@NonNull View itemView) {
            super(itemView);
            binding = DialogTodoItemBinding.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogInterface.select(studyId,studyName);
                }
            });

        }

        public void onBind(StudyFindRes res){

            studyId = res.getId();
            studyName = res.getName();
            binding.name.setText(res.getName());

        }
    }
}
