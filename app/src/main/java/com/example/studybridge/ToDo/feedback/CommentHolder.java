package com.example.studybridge.ToDo.feedback;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMenti;
import com.example.studybridge.http.dto.feedBack.FindFeedBackRes;

public class CommentHolder extends RecyclerView.ViewHolder {

    private TextView chatIDTv;
    private TextView chatTv;
    private ImageView chatUserPic;
    private LinearLayout chatItem;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    SharedPreferences sharedPreferences;
    Long userPkId;

    public CommentHolder(@NonNull View view) {
        super(view);
        chatIDTv = (TextView) view.findViewById(R.id.todo_comment_id);
        chatTv = (TextView) view.findViewById(R.id.todo_comment_text);
/*        chatItem = view.findViewById(R.id.chatItem);
        chatUserPic = view.findViewById(R.id.chat_user_pic);

        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userPkId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);*/
    }

    public void onBind(FindFeedBackRes data) {

        chatIDTv.setText(data.getWriterName());
        chatTv.setText(data.getComment());

/*        if(data.getWriterId()==userPkId) {
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        }else{
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }*/
    }
}
