package com.example.studybridge.Util;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.studybridge.databinding.DialogBackBinding;
import com.example.studybridge.databinding.DialogMentoselectBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.ChangeStatusReq;
import com.example.studybridge.http.dto.study.ChooseMentorRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MentoSelectDialog extends DialogFragment {

    private DialogMentoselectBinding binding;
    private MentoSelectDialog.SelectInterface selectInterface;
    private DialogInterface.OnDismissListener onDismissListener;

    private DataService dataService = new DataService();

    //interface
    public interface SelectInterface{
        void okBtnClick();
    }
    public static MentoSelectDialog getInstance() {
        MentoSelectDialog dialog = new MentoSelectDialog();
        return dialog;
    }

    public void setSelectInterface(SelectInterface selectInterface) {
        this.selectInterface = selectInterface;
    }



    private Long studyId;
    private String mentorId;
    public static final String CONFIRM_APPLY = "MATCHED";

    //onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogMentoselectBinding.inflate(inflater,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = binding.getRoot();

        Bundle bundle = getArguments();
        assert bundle != null;
        mentorId = bundle.getString("mentorId");
        studyId = bundle.getLong("studyId");

        setBtns();

        return view;
    }

    private void setBtns(){
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataService.study.chooseMentor(studyId,mentorId).enqueue(new Callback<ChooseMentorRes>() {
                    @Override
                    public void onResponse(Call<ChooseMentorRes> call, Response<ChooseMentorRes> response) {
                        if(response.isSuccessful())
                        {
                            confirm();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChooseMentorRes> call, Throwable t) {

                    }
                });
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void confirm(){
        ChangeStatusReq csReq = new ChangeStatusReq(studyId,CONFIRM_APPLY);
        dataService.study.status(csReq).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    dismiss();
                    selectInterface.okBtnClick();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
