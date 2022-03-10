package com.example.studybridge.Study.StudyMenti;


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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.Detail.DialogInterfaces;


public class StudyMentiFilterDialog extends DialogFragment {

    private RadioGroup subjectGroup,placeGroup;
    private RadioButton filterSubject, filterPlace;
    private String passSubject,passPlace;
    private LinearLayout applyBtn;
    private DialogInterface.OnDismissListener onDismissListener;

    private DialogInterfacer dialogInterfacer;

    ////dialog --> fragment 로 값 전달
    public static StudyMentiFilterDialog getInstance(){
        StudyMentiFilterDialog dialog = new StudyMentiFilterDialog();
        return dialog;
    }

    public interface DialogInterfacer{
        void onFilterBtnClick(String subject, String place);
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
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,340,getResources().getDisplayMetrics());

        getDialog().getWindow().setLayout(width,height);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //필터 선택
        subjectGroup = (RadioGroup) view.findViewById(R.id.menti_filter_subjectGroup);
        placeGroup = (RadioGroup) view.findViewById(R.id.menti_filter_placeGroup);


        //적용하기 클릭 이벤트
        applyBtn = (LinearLayout) view.findViewById(R.id.menti_filter_btn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filterSubject =(RadioButton) view.findViewById(subjectGroup.getCheckedRadioButtonId());
                filterPlace =(RadioButton) view.findViewById(placeGroup.getCheckedRadioButtonId());
                passSubject = filterSubject.getText().toString()+"";
                passPlace = filterPlace.getText().toString()+"";

                dialogInterfacer.onFilterBtnClick(passSubject,passPlace);

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






