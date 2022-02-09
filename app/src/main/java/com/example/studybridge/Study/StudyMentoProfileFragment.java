package com.example.studybridge.Study;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybridge.R;

import org.jetbrains.annotations.NotNull;

public class StudyMentoProfileFragment extends Fragment {

    private TextView school,intro;



    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.study_mento_detail_profile_fragment,container,false);

        intro = (TextView) view.findViewById(R.id.mento_profile_intro);
        school = (TextView) view.findViewById(R.id.mento_profile_school);

        Bundle extras = getArguments();
        if(extras != null){
            intro.setText(extras.getString("intro"));
            Toast.makeText(view.getContext(),extras.getString("intro"),Toast.LENGTH_SHORT).show();
        }

        return view;
    }


}
