package com.example.studybridge.Study;

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

    private TextView school,intro,place,subject;



    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.study_mento_detail_profile_fragment,container,false);

        intro = (TextView) view.findViewById(R.id.mento_profile_intro);
        school = (TextView) view.findViewById(R.id.mento_profile_school);
        place = (TextView) view.findViewById(R.id.mento_profile_place);
        subject = (TextView) view.findViewById(R.id.mento_profile_subject);


        StringBuilder sb = new StringBuilder();
        Bundle extras = getArguments();

        String introStr = sb.append("\"").append(extras.getString("intro")).append("\"").toString();

        if(extras != null){
            intro.setText(introStr);
            school.setText(extras.getString("school"));
            place.setText(extras.getString("place"));
            subject.setText(extras.getString("subject"));
        }

        return view;
    }


}
