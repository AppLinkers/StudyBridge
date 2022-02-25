package com.example.studybridge.Study.StudyMenti;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.studybridge.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Field;

public class StudyMentiFilterDialog extends BottomSheetDialogFragment {

    private RadioGroup subjectGroup,placeGroup;
    private RadioButton filterSubject, filterPlace;
    private String passSubject,passPlace;
    private LinearLayout applyBtn;
    private Toolbar toolbar;

    ////dialog --> fragment 로 값 전달
    public static StudyMentiFilterDialog getInstance(){
        StudyMentiFilterDialog dialog = new StudyMentiFilterDialog();
        return dialog;
    }
    public interface DialogInterfacer{
        void onButtonClick(String subject,String place);
    }
    private DialogInterfacer dialogInterfacer;

    public void setDialogInterfacer(DialogInterfacer dialogInterfacer) {
        this.dialogInterfacer = dialogInterfacer;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
        bottomSheetDialog.setContentView(R.layout.study_menti_filter);


        //다이얼로그 확장 방지
        try {
            Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
            behaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        } catch (NoSuchFieldException e) { e.printStackTrace(); }
        catch (IllegalAccessException e) { e.printStackTrace(); }

        //상단바
        toolbar = (Toolbar) bottomSheetDialog.findViewById(R.id.menti_filter_bar);


        //필터 선택
        subjectGroup = (RadioGroup) bottomSheetDialog.findViewById(R.id.menti_filter_subjectGroup);
        placeGroup = (RadioGroup) bottomSheetDialog.findViewById(R.id.menti_filter_placeGroup);

        subjectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.menti_filter_allSubject:
                        break;
                    case R.id.menti_filter_english:
                        break;
                    case R.id.menti_filter_math:
                        break;
                    case R.id.menti_filter_dev:
                        break;
                    case R.id.menti_filter_etcSubject:
                        break;
                }
            }
        });

        placeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.menti_filter_allPlace:
                        break;
                    case R.id.menti_filter_seoul:
                        break;
                    case R.id.menti_filter_geongi:
                        break;
                    case R.id.menti_filter_incheon:
                        break;
                    case R.id.menti_filter_etcPlace:
                        break;
                }
            }
        });



        //적용하기 클릭 이벤트
        applyBtn = (LinearLayout) bottomSheetDialog.findViewById(R.id.menti_filter_btn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filterSubject =bottomSheetDialog.findViewById(subjectGroup.getCheckedRadioButtonId());
                filterPlace =bottomSheetDialog.findViewById(placeGroup.getCheckedRadioButtonId());
                passSubject = filterSubject.getText().toString()+"";
                passPlace = filterPlace.getText().toString()+"";

                dialogInterfacer.onButtonClick(passSubject,passPlace);

                dismiss();

            }
        });
    }

}






