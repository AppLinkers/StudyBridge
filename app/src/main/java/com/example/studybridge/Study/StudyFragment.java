package com.example.studybridge.Study;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMentiFragment;
import com.example.studybridge.Study.StudyMento.StudyMentoFragment;
import com.example.studybridge.databinding.StudyFragmentBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class StudyFragment extends Fragment {

    private StudyFragmentBinding binding;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_NAME = "user_name_key";

    private String userName;

    private StudyMentiFragment mentiFragment;
    private StudyMentoFragment mentoFragment;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = StudyFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        //sharedpreference
        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userName= sharedPreferences.getString(USER_NAME, "사용자");

        Bundle bundle = getArguments();

        setUI(savedInstanceState,bundle);

        return view;
    }

    private void setUI(Bundle savedInstance,Bundle argument){
        String subject;
        String goMentor;
        if(argument!=null){
            subject = argument.getString("subject");
            goMentor = argument.getString("goMentor");
        }
        else {
            subject = null;
            goMentor = null;
        }


        mentiFragment = new StudyMentiFragment(subject);
        mentoFragment = new StudyMentoFragment();
        if(savedInstance==null && goMentor==null){
            getParentFragmentManager().beginTransaction().add(R.id.study_frame,mentiFragment).commit();
            binding.studyIntro.setText("원하는 스터디를 찾아보세요!");
            binding.studyToStudy.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
            binding.studyToStudy.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
            binding.studyToMentor.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary70));
            binding.studyToMentor.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.palletGrey));
        }
        else{
            getParentFragmentManager().beginTransaction().replace(R.id.study_frame,mentoFragment).commit();
            binding.studyIntro.setText("함께할 멘토를 찾아보세요!");
            binding.studyToMentor.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
            binding.studyToMentor.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
            binding.studyToStudy.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary70));
            binding.studyToStudy.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.palletGrey));
        }

        StringBuilder sb = new StringBuilder();
        sb.append(userName).append("님,");
        binding.studyUserName.setText(sb.toString());

        changeFrame();

    }

    private void changeFrame(){
        binding.studyToStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.study_frame,mentiFragment).commit();
                binding.studyIntro.setText("원하는 스터디를 찾아보세요!");
                binding.studyToStudy.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
                binding.studyToStudy.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
                binding.studyToMentor.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary70));
                binding.studyToMentor.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.palletGrey));
            }
        });
        binding.studyToMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.study_frame,mentoFragment).commit();
                binding.studyIntro.setText("함께할 멘토를 찾아보세요!");
                binding.studyToMentor.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary));
                binding.studyToMentor.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
                binding.studyToStudy.setTextColor(ContextCompat.getColor(getContext(),R.color.textColorPrimary70));
                binding.studyToStudy.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.palletGrey));
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
