package com.example.studybridge.Util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.studybridge.R;

public class AddTextWatcher implements TextWatcher {

    Context context;
    TextView v;
    EditText[] editList;

    public AddTextWatcher(Context context, TextView v, EditText[] editList) {
        this.context = context;
        this.v = v;
        this.editList = editList;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        for(EditText editText : editList){
            if(editText.getText().toString().trim().length()<=0||editText.getText().toString().equals("과목")||editText.getText().toString().equals("지역")){
                v.setEnabled(false);
                v.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                break;
            }

            else {
                v.setEnabled(true);
                v.setTextColor(ContextCompat.getColor(context, R.color.palletRed));
            }
        }
    }
}
