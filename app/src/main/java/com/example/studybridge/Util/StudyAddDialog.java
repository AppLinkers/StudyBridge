package com.example.studybridge.Util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studybridge.databinding.DialogStudyBinding;

public class StudyAddDialog extends DialogFragment {

    private DialogStudyBinding binding;
    private StudyAddDialog.AddInterface addInterface;
    private int type;

    public interface AddInterface{
        void selectOne(String s);
    }
    private StudyAddDialog(int type){
        this.type = type;
    }
    public static StudyAddDialog getInstance(int type){
        StudyAddDialog dialog = new StudyAddDialog(type);
        return dialog;
    }

    public void setAddInterface(AddInterface addInterface) {
        this.addInterface = addInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogStudyBinding.inflate(inflater,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = binding.getRoot();

        setUI();

        return view;
    }
    private void setUI(){
        if(type==0){
            binding.title.setText("과목을 선택하세요");
            binding.item1.setText("영어");
            binding.item2.setText("수학");
            binding.item3.setText("개발");
            binding.item4.setText("기타");
        }
        else {
            binding.title.setText("지역을 선택하세요");
            binding.item1.setText("서울");
            binding.item2.setText("경기");
            binding.item3.setText("인천");
            binding.item4.setText("기타");
        }

        clickBtn(binding.item1);
        clickBtn(binding.item2);
        clickBtn(binding.item3);
        clickBtn(binding.item4);
    }

    private void clickBtn(TextView textView){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInterface.selectOne(textView.getText().toString());
                dismiss();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
