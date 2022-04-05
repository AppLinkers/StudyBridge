package com.example.studybridge.ToDo.feedback;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDoDetailActivity;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.feedBack.FindFeedBackRes;

import java.util.List;

import lombok.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentHolder extends RecyclerView.ViewHolder {

    private TextView chatIDTv;
    private TextView chatTv;
    private ImageView chatUserImg;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    SharedPreferences sharedPreferences;
    Long userPkId;
    ToDoDetailActivity toDoDetailActivity;

    DataService dataService;
    boolean isSameUser = false;
    Long commentId;


    public CommentHolder(@NonNull View itemview) {
        super(itemview);

        chatIDTv = (TextView) itemview.findViewById(R.id.chatID);
        chatTv = (TextView) itemview.findViewById(R.id.chat);
        chatUserImg = (ImageView) itemview.findViewById(R.id.chat_user_pic);

        dataService = new DataService();
        sharedPreferences = itemview.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userPkId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        itemview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(isSameUser){
                    setAlertDialog(view);
                    return true;
                }else{
                    return false;
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onBind(FindFeedBackRes data) {

        chatIDTv.setText(data.getWriterName());
        chatTv.setText(data.getComment());
        commentId = data.getId();


        if(data.getWriterId().equals(userPkId)) {
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            isSameUser = true;

        }else{
            itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }



    private void setAlertDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        toDoDetailActivity = new ToDoDetailActivity();
        builder.setTitle("Comment 삭제").setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //삭제 메서드 작성
                dataService.feedBack.delete(commentId).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(view.getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            itemView.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //취소
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
