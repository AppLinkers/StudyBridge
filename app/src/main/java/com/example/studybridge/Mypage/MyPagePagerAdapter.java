package com.example.studybridge.Mypage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.studybridge.Mypage.Chat.MyPageChatFragment;

public class MyPagePagerAdapter extends FragmentStateAdapter {

    private static int NUM_TABS = 2;

    public MyPagePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0){
            return new MyPageMyFragment();
        }
        return new MyPageChatFragment();
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
