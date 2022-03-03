package com.example.studybridge.Study.StudyMento;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;


public class StudyMentoDetailPagerAdapter extends FragmentStateAdapter {

    private static int NUM_TABS = 3;
    private ArrayList arr;

    public StudyMentoDetailPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setArr(ArrayList arr) {
        this.arr = arr;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                StudyMentoProfileFragment profileFragment = new StudyMentoProfileFragment();
                Bundle bundle = new Bundle();

                bundle.putString("subject", (String) arr.get(0));
                bundle.putString("place",(String) arr.get(1));
                bundle.putString("school",(String) arr.get(2));
                bundle.putString("qualify",(String) arr.get(3));
                bundle.putString("intro",(String) arr.get(4));
                bundle.putString("mentoId",(String) arr.get(5));

                profileFragment.setArguments(bundle);
                return profileFragment;
            case 1:
                StudyMentoExperienceFragment experienceFragment = new StudyMentoExperienceFragment();
                return experienceFragment;
            case 2:
                StudyMentoCommentFragment commentFragment = new StudyMentoCommentFragment();
                return commentFragment;

            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
