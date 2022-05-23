package com.example.studybridge.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studybridge.ToDo.TodoDialogAdapter;
import com.example.studybridge.databinding.DialogTodoBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoDialog extends DialogFragment {

    private DialogTodoBinding binding;
    private TodoDialog.TodoInterface todoInterface;
    private DialogInterface.OnDismissListener onDismissListener;

    private LinearLayoutManager linearLayoutManager;
    private TodoDialogAdapter adapter;
    private DataService dataService = new DataService();

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";

    private Long userIdPk;

    ////////////////////////////////////
    public interface TodoInterface{
        void choose(StudyFindRes study, String studyName);
    }
    public static TodoDialog getInstance(){
        TodoDialog dialog = new TodoDialog();

        return dialog;
    }

    public void setTodoInterface(TodoInterface todoInterface) {
        this.todoInterface = todoInterface;
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

    /////////////////////////////////////////////////
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogTodoBinding.inflate(inflater,container,false);

        View view = binding.getRoot();

        //sharedPref
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        setUI();

        return view;
    }

    private void setUI(){
        setRecycler();
        binding.showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoInterface.choose(null,"전체 보기");
                dismiss();
            }
        });

        adapter.setDialogInterface(new TodoDialogAdapter.dialogInterface() {
            @Override
            public void select(StudyFindRes study, String studyName) {
                todoInterface.choose(study, studyName);
                dismiss();
            }
        });
    }

    private void setRecycler(){
        adapter = new TodoDialogAdapter();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.RCView.setLayoutManager(linearLayoutManager);
        getData();
    }

    private void getData(){
        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    for(StudyFindRes res: response.body()){
                        if(res.getStatus().equals("MATCHED")){
                            adapter.addItem(res);
                        }
                        else continue;
                    }

                }
                binding.RCView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        final int width = ViewGroup.LayoutParams.MATCH_PARENT;
        final int height = ViewGroup.LayoutParams.WRAP_CONTENT;;


        getDialog().getWindow().setLayout(width,height);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
