package com.example.studybridge.ToDo;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

public class ToDoInsideHolder extends RecyclerView.ViewHolder {

    private TextView insideText;
    private String status;

    public ToDoInsideHolder(View itemView) {
        super(itemView);

        insideText= (TextView) itemView.findViewById(R.id.todo_inside_text);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), status, Toast.LENGTH_SHORT).show();
                ToDoManageDialog dialog = new ToDoManageDialog(view.getContext());
                dialog.callFunction();
            }
        });
    }

    public void onBind(ToDoInside data) {
        insideText.setText(data.getTitle());
        status = data.getStatus();
    }
}
