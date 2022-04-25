package com.example.studybridge.Util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.studybridge.http.dto.userMentor.ProfileRes;

public class MentoDiffUtil extends DiffUtil.ItemCallback<ProfileRes> {

    @Override
    public boolean areItemsTheSame(@NonNull ProfileRes oldItem, @NonNull ProfileRes newItem) {
        return oldItem.getUserId().equals(newItem.getUserId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ProfileRes oldItem, @NonNull ProfileRes newItem) {
        boolean b = oldItem == newItem;
        return b;
    }
}
