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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studybridge.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


public class StudyFilterDialog extends DialogFragment {

    private ChipGroup statusGroup,subjectGroup,placeGroup;
    private Chip filterStatus,filterSubject, filterPlace;
    private String passStatus,passSubject,passPlace;
    private TextView statusTv,filterTitle;
    private LinearLayout applyBtn;

    private DialogInterface.OnDismissListener onDismissListener;

    private DialogInterfacer dialogInterfacer;
    private int type;

    private StudyFilterDialog(int type){
        this.type = type;
    }

    ////dialog --> fragment 로 값 전달
    public static StudyFilterDialog getInstance(int type){
        StudyFilterDialog dialog = new StudyFilterDialog(type);
        return dialog;
    }

    public interface DialogInterfacer{
        void onFilterBtnClick(String status,String subject, String place);
    }


    public void setDialogInterfacer(DialogInterfacer dialogInterfacer) {
        this.dialogInterfacer = dialogInterfacer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_filter,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height;

        if(type==0){
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,440,getResources().getDisplayMetrics());
        } else {
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,340,getResources().getDisplayMetrics());
        }


        getDialog().getWindow().setLayout(width,height);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //필터 선택
        statusGroup = (ChipGroup) view.findViewById(R.id.study_filter_statusGroup);
        subjectGroup = (ChipGroup) view.findViewById(R.id.study_filter_subjectGroup);
        placeGroup = (ChipGroup) view.findViewById(R.id.study_filter_placeGroup);

        statusTv = (TextView) view.findViewById(R.id.study_filter_statusTv);
        filterTitle = (TextView) view.findViewById(R.id.study_filter_title);

        if(type==1){
            statusGroup.setVisibility(View.GONE);
            statusTv.setVisibility(View.GONE);
            filterTitle.setText("멘토 검색하기");
        }



        //적용하기 클릭 이벤트
        applyBtn = (LinearLayout) view.findViewById(R.id.study_filter_btn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filterStatus = (Chip) view.findViewById(statusGroup.getCheckedChipId());
                filterSubject = (Chip) view.findViewById(subjectGroup.getCheckedChipId());
                filterPlace = (Chip) view.findViewById(placeGroup.getCheckedChipId());

                passStatus = filterStatus.getText().toString()+"";
                passSubject = filterSubject.getText().toString()+"";
                passPlace = filterPlace.getText().toString()+"";

                dialogInterfacer.onFilterBtnClick(passStatus,passSubject,passPlace);

                dismiss();

            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}






