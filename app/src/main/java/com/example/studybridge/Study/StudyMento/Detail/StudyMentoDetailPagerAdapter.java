package com.example.studybridge.Study.StudyMento.Detail;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;
import com.example.studybridge.Study.StudyMento.Detail.Comment.StudyMentoCommentFragment;
import com.example.studybridge.Study.StudyMento.Detail.Experience.StudyMentoExperienceFragment;
import com.example.studybridge.Study.StudyMento.Detail.Profile.StudyMentoProfileFragment;

import java.util.ArrayList;


public class StudyMentoDetailPagerAdapter extends FragmentStateAdapter {

    private static int NUM_TABS = 2;
    private MyPageMentoProfile profile;
    private String id;

    public StudyMentoDetailPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setProfile(MyPageMentoProfile profile) {
        this.profile = profile;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                StudyMentoProfileFragment profileFragment = new StudyMentoProfileFragment();
                Bundle bundle = new Bundle();

                bundle.putSerializable("profile", profile);
                bundle.putString("mentoId",id);

                profileFragment.setArguments(bundle);
                return profileFragment;
            case 1:
                StudyMentoExperienceFragment experienceFragment = new StudyMentoExperienceFragment();

                Bundle bundleForExp = new Bundle();

                bundleForExp.putSerializable("profile", profile);
                bundleForExp.putString("mentoId",id);

                experienceFragment.setArguments(bundleForExp);

                return experienceFragment;
/*            case 2:
                StudyMentoCommentFragment commentFragment = new StudyMentoCommentFragment();
                return commentFragment;*/

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
