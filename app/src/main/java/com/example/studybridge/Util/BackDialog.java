package com.example.studybridge.Util;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studybridge.Study.StudyMenti.Detail.DialogInterfaces;
import com.example.studybridge.databinding.BackDialogBinding;

public class BackDialog extends DialogFragment {

    private BackDialogBinding binding;
    private BackInterface backInterface;

    public interface BackInterface{
        void okBtnClick();
    }
    public static BackDialog getInstance() {
        BackDialog dialog = new BackDialog();
        return dialog;
    }

    public void setBackInterface(BackInterface backInterface) {
        this.backInterface = backInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BackDialogBinding.inflate(inflater,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = binding.getRoot();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
