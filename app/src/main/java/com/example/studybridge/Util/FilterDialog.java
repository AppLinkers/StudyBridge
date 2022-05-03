package com.example.studybridge.Util;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studybridge.databinding.StudyFiltBinding;
import com.google.android.material.chip.Chip;

public class FilterDialog extends DialogFragment {

    private StudyFiltBinding binding;
    private FilterDialog.FilterInterFace filterInterFace;
    private DialogInterface.OnDismissListener onDismissListener;

    private int type;

    ///////////////////////////////////////////////////////////////
    private FilterDialog(int type){
        this.type = type;
    }

    public interface FilterInterFace{
        void select(String filter);
    }

    public static FilterDialog getInstance(int type){
        FilterDialog dialog = new FilterDialog(type);
        return dialog;
    }

    public void setFilterInterFace(FilterInterFace filterInterFace) {
        this.filterInterFace = filterInterFace;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    ///////////////////////////////////////////////////////////////////
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StudyFiltBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        setUI();

        return view;
    }

    private void setUI(){
        Bundle bundle = getArguments();
        String filter = bundle.getString("filter");
        Chip selectChip = null;

        if(type==0){ //상태
            binding.filtertitle.setText("상태별 찾기");
            binding.chip2.setText("멘티 모집");
            binding.chip3.setText("멘토 모집");
            binding.chip4.setText("모집 종료");
            binding.chip5.setVisibility(View.GONE);

            selectChip = (Chip) binding.chipGroup.getChildAt(checkChipForStatus(filter));
        }
        else if(type ==1){ //과목
            binding.filtertitle.setText("과목별 찾기");
            binding.chip2.setText("영어");
            binding.chip3.setText("수학");
            binding.chip4.setText("개발");
            binding.chip5.setText("기타");

            selectChip = (Chip) binding.chipGroup.getChildAt(checkChipForSubject(filter));
        }
        else { //지역
            binding.filtertitle.setText("지역별 찾기");
            binding.chip2.setText("서울");
            binding.chip3.setText("경기");
            binding.chip4.setText("인천");
            binding.chip5.setText("기타");

            selectChip = (Chip) binding.chipGroup.getChildAt(checkChipForPlace(filter));
        }


        selectChip.setChecked(true);

        selectAll(binding.chip1);
        selectChip(binding.chip2);
        selectChip(binding.chip3);
        selectChip(binding.chip4);
        selectChip(binding.chip5);
    }

    private void selectAll(Chip chip){
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type==0){
                    filterInterFace.select("상태별");
                }
                else if(type==1){
                    filterInterFace.select("과목별");
                }
                else{
                    filterInterFace.select("지역별");
                }
                dismiss();
            }
        });
    }
    private void selectChip(Chip chip){

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterInterFace.select(chip.getText().toString());
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        final int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height;

        if(type==0){
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,340,getResources().getDisplayMetrics());
        } else {
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,400,getResources().getDisplayMetrics());
        }

        getDialog().getWindow().setLayout(width,height);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static int checkChipForStatus(String str){
        if(str!=null){
            switch (str)
            {
                case "멘티 모집" :
                    return 1;
                case "멘토 모집" :
                    return 2;
                case "모집 종료" :
                    return 3;
                default:
                    return 0;

            }
        } else {
            return 0;
        }
    }

    public static int checkChipForSubject(String str){
        if(str!=null){
            switch (str)
            {
                case "영어" :
                    return 1;
                case "수학" :
                    return 2;
                case "개발" :
                    return 3;
                case "기타" :
                    return 4;
                default:
                    return 0;

            }
        } else {
            return 0;
        }
    }
    public static int checkChipForPlace(String str){

        if(str!=null){
            switch (str)
            {
                case "서울" :
                    return 1;
                case "경기" :
                    return 2;
                case "인천" :
                    return 3;
                case "기타" :
                    return 4;
                default:
                    return 0;

            }
        } else {
            return 0;
        }

    }
}
