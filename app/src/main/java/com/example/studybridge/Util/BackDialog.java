package com.example.studybridge.Util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studybridge.databinding.DialogBackBinding;

public class BackDialog extends DialogFragment {

    private DialogBackBinding binding;
    private BackInterface backInterface;
    private int type;

    /////////////////////////////////////////
    private BackDialog(int type) {
        this.type = type;
    }

    public interface BackInterface{
        void okBtnClick();
    }
    public static BackDialog getInstance(int type) {
        BackDialog dialog = new BackDialog(type);
        return dialog;
    }

    public void setBackInterface(BackInterface backInterface) {
        this.backInterface = backInterface;
    }
////////////////////////////////////////////////////////////////////////
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogBackBinding.inflate(inflater,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = binding.getRoot();

        setUI();

        binding.backDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backInterface.okBtnClick();
                dismiss();
            }
        });

        binding.backDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void setUI(){
        if(type==0){

        }
        else {
            binding.dialogTitle.setText("삭제하기");
            binding.dialogInfo.setText("정말 삭제하시겠습니까?");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
