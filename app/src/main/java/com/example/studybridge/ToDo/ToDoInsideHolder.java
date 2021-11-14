package com.example.studybridge.ToDo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

public class ToDoInsideHolder extends RecyclerView.ViewHolder {

    private TextView insideText;

    public ToDoInsideHolder(View itemView) {
        super(itemView);

        insideText= (TextView) itemView.findViewById(R.id.todo_inside_text);
    }

    public void onBind(ToDoInside data) {
        insideText.setText(data.getInsideText());
    }
}
