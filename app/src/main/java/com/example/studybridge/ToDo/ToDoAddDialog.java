package com.example.studybridge.ToDo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybridge.R;

public class ToDoAddDialog {

    private Context context;

    public ToDoAddDialog(Context context){
        this.context =context;
    }
    public void callFunction(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.todo_add_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final TextView cancel = (TextView) dialog.findViewById(R.id.todo_add_cancel);
        final TextView confirm = (TextView) dialog.findViewById(R.id.todo_add_confirm);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"추가 되었습니다",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
}
