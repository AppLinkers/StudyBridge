package com.example.studybridge.Study.StudyMento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

public class StudyMentoExperienceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.study_mento_detail_experience_fragment,container,false);


        return view;
    }


}

