package com.example.studybridge.Chat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.google.android.material.card.MaterialCardView;

public class ChatAdaptHolder extends RecyclerView.ViewHolder {

    private TextView chatIDTv;
    private TextView chatTv;
    private ImageView chatUserPic,chatImg;
    private LinearLayout chatItem;
    private MaterialCardView imgCon;

    public ChatAdaptHolder(View itemview){
        super(itemview);
        chatIDTv = (TextView) itemview.findViewById(R.id.chatID);
        chatTv = (TextView) itemview.findViewById(R.id.chat);
        chatItem = itemview.findViewById(R.id.chatItem);
        chatUserPic = itemview.findViewById(R.id.chat_user_pic);
        imgCon = (MaterialCardView) itemview.findViewById(R.id.chat_img_con);
        chatImg = (ImageView) itemview.findViewById(R.id.chat_img);
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onBind(Chat chat, String user){

        chatIDTv.setText(chat.getChatID());


        if(chat.imgUrl!=null){
            chatTv.setVisibility(View.GONE);
            imgCon.setVisibility(View.VISIBLE);
            Glide.with(itemView.getContext()).load(chat.imgUrl).into(chatImg);
        } else {
            chatTv.setText(chat.getChat());
        }

        if(chat.getChatID().equals(user)) {
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        }else{
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }
}
