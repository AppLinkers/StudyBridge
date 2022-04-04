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
import com.example.studybridge.http.dto.message.Message;
import com.google.android.material.card.MaterialCardView;

public class ChatAdaptHolder extends RecyclerView.ViewHolder {

    private TextView chatIDTv,chatTv;
    private ImageView chatUserPic,chatImg;
    private LinearLayout chatItem, chatCon;
    private MaterialCardView imgCon;

    public ChatAdaptHolder(View itemview){
        super(itemview);

        //화면 위 데이터
        chatItem = itemview.findViewById(R.id.chatItem); // 화면 전체
        chatUserPic = itemview.findViewById(R.id.chat_user_pic); // 프로필 사진
        chatIDTv = (TextView) itemview.findViewById(R.id.chatID); // 보낸사람 Id(String)

        chatCon = (LinearLayout) itemview.findViewById(R.id.chatCon); // 메세지 컨테이너
        chatTv = (TextView) itemview.findViewById(R.id.chat); // 보낸사람 메세지

        imgCon = (MaterialCardView) itemview.findViewById(R.id.chat_img_con); //이미지 컨테이너
        chatImg = (ImageView) itemview.findViewById(R.id.chat_img); // 이미지
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onBind(Message message, String user){

        chatIDTv.setText(message.getSenderName());

        if(message.getMessageType().equals("TALK")){
            chatCon.setVisibility(View.VISIBLE);
            imgCon.setVisibility(View.GONE);
            chatTv.setText(message.getMessage());
        }
        else if(message.getMessageType().equals("PHOTO")){
            chatCon.setVisibility(View.GONE);
            imgCon.setVisibility(View.VISIBLE);
            Glide.with(itemView.getContext()).load(message.getMessage()).into(chatImg);
        }


        if(message.getSenderName().equals(user)) {
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        }else{
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

    }
}
