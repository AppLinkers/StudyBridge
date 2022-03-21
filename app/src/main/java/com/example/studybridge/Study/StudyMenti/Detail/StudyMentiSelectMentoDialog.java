package com.example.studybridge.Study.StudyMenti.Detail;


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

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.ChooseMentorRes;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentiSelectMentoDialog extends DialogFragment {

    private MaterialCardView selectBtn, cancelBtn;

    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterfaces dialogInterfacer;

    public static final int SELECT_CODE = 1;
    private String mentoNowId;
    private Long studyNowId;

    DataService dataService = new DataService();



    public void setDialogInterfacer(DialogInterfaces dialogInterfacer) {
        this.dialogInterfacer = dialogInterfacer;
    }



    public static StudyMentiSelectMentoDialog newInstance(){
        StudyMentiSelectMentoDialog dialog = new StudyMentiSelectMentoDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_menti_mentoselect_dialog,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectBtn = (MaterialCardView) view.findViewById(R.id.mentoSelect_selectBtn);
        cancelBtn = (MaterialCardView) view.findViewById(R.id.mentoSelect_cancelBtn);


        Bundle bundle = getArguments();
        mentoNowId = bundle.getString("mentoId");
        studyNowId = bundle.getLong("studyId");

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterfacer.onButtonClick(SELECT_CODE);


                dataService.study.chooseMentor(studyNowId,mentoNowId).enqueue(new Callback<ChooseMentorRes>() {
                    @Override
                    public void onResponse(Call<ChooseMentorRes> call, Response<ChooseMentorRes> response) {
                        if(response.isSuccessful())
                        {

                        }
                    }

                    @Override
                    public void onFailure(Call<ChooseMentorRes> call, Throwable t) {

                    }
                });

                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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