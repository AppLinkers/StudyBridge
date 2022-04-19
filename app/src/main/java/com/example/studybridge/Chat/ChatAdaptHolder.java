package com.example.studybridge.Chat;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.Detail.Profile.StudyMentoProfileCertiImg;
import com.example.studybridge.http.dto.message.Message;
import com.google.android.material.card.MaterialCardView;

public class ChatAdaptHolder extends RecyclerView.ViewHolder {

    private TextView chatIDTv,chatTv;
    private ImageView chatUserPic,chatImg;
    private LinearLayout chatCon;
    private MaterialCardView imgCon;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    Long userPkId;

    private String uri;


    public ChatAdaptHolder(View itemview){
        super(itemview);

        sharedPreferences = itemview.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userPkId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        //화면 위 데이터
        chatUserPic = itemview.findViewById(R.id.chat_user_pic); // 프로필 사진
        chatIDTv = (TextView) itemview.findViewById(R.id.chatID); // 보낸사람 Id(String)

        chatCon = (LinearLayout) itemview.findViewById(R.id.chatCon); // 메세지 컨테이너
        chatTv = (TextView) itemview.findViewById(R.id.chat); // 보낸사람 메세지

        imgCon = (MaterialCardView) itemview.findViewById(R.id.chat_img_con); //이미지 컨테이너
        chatImg = (ImageView) itemview.findViewById(R.id.chat_img); // 이미지
        imgCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudyMentoProfileCertiImg.class);
                intent.putExtra("certiImg",uri);
                view.getContext().startActivity(intent);
            }
        });
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onBind(Message message){

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
            uri = message.getMessage();
        }


        if(message.getSenderId().equals(userPkId)) {
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            chatCon.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));
            chatTv.setTextColor(Color.WHITE);

        }else{
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            chatCon.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
            chatTv.setTextColor(itemView.getContext().getResources().getColor(R.color.textColorPrimary));
        }

    }
}
