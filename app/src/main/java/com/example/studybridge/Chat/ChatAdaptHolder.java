package com.example.studybridge.Chat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

public class ChatAdaptHolder extends RecyclerView.ViewHolder {

    private TextView chatIDTv;
    private TextView chatTv;
    private ImageView chatUserPic;
    private LinearLayout chatItem;

    public ChatAdaptHolder(View view){
        super(view);
        chatIDTv = (TextView) view.findViewById(R.id.chatID);
        chatTv = (TextView) view.findViewById(R.id.chat);
        chatItem = view.findViewById(R.id.chatItem);
        chatUserPic = view.findViewById(R.id.chat_user_pic);
    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onBind(Chat chat, String user){

        chatIDTv.setText(chat.getChatID());
        chatTv.setText(chat.getChat());

        if(chat.getChatID().equals(user)) {
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        }else{
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }
}
