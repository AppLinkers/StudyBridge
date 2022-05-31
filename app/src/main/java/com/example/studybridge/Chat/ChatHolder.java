package com.example.studybridge.Chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.Util.ImgView;
import com.example.studybridge.Util.SharedPrefKey;
import com.example.studybridge.databinding.ChatItemBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.message.Message;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatHolder extends RecyclerView.ViewHolder {

    private ChatItemBinding binding;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    Long userPkId;
    String imgPath;
    DataService dataService = new DataService();

    private String uri;

    public ChatHolder(View itemview){
        super(itemview);
        binding = ChatItemBinding.bind(itemview);

        sharedPreferences = itemview.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userPkId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        imgPath = sharedPreferences.getString(SharedPrefKey.USER_PROFILE,"img");

        binding.sendImgCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ImgView.class);
                intent.putExtra("img",uri);
                view.getContext().startActivity(intent);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onBind(Message message){


        if(message.getMessageType().equals("TALK")){
            binding.chatCon.setVisibility(View.VISIBLE);
            binding.sendImgCon.setVisibility(View.GONE);
            binding.chat.setText(message.getMessage());
        }
        else if(message.getMessageType().equals("PHOTO")){
            binding.chatCon.setVisibility(View.GONE);
            binding.sendImgCon.setVisibility(View.VISIBLE);
            Glide.with(itemView.getContext()).load(message.getMessage()).into(binding.sendImg);
            uri = message.getMessage();
        }

        if(message.getSenderId().equals(userPkId)) {
            binding.senderName.setVisibility(View.INVISIBLE);
            binding.getRoot().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            binding.chat.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.palletRed));
            binding.chat.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            Glide.with(itemView).load(imgPath).into(binding.userImg);

        }else{
            binding.senderName.setVisibility(View.VISIBLE);
            binding.senderName.setText(message.getSenderName());
            binding.getRoot().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            binding.chat.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.palletGrey));
            binding.chat.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.textColorPrimary));
            setProfile(message.getSenderName());
        }

    }

    private void setProfile(String userId){
        dataService.userAuth.getProfile(userId).enqueue(new Callback<UserProfileRes>() {
            @Override
            public void onResponse(Call<UserProfileRes> call, Response<UserProfileRes> response) {
                if(response.isSuccessful()){
                    final String path = response.body().getProfileImg();
                    Glide.with(itemView).load(path).into(binding.userImg);
                }
            }

            @Override
            public void onFailure(Call<UserProfileRes> call, Throwable t) {

            }
        });
    }
}
