package com.example.studybridge.ToDo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybridge.R;

public class ToDoManageDialog {


    private Context context;
    private RadioGroup radioGroup;


    public ToDoManageDialog(Context context){
        this.context =context;
    }

    public void callFunction(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.todo_manage_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final TextView cancel = (TextView) dialog.findViewById(R.id.todo_add_cancel);
        final TextView confirm = (TextView) dialog.findViewById(R.id.todo_add_confirm);
        radioGroup = (RadioGroup) dialog.findViewById(R.id.todo_radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.todo_add_ready:
                        break;
                    case R.id.todo_add_progress:
                        break;
                    case R.id.todo_add_done:
                        break;
                }
            }
        });



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