package com.example.studybridge.ToDo.feedback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMenti;
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
    private ImageView chatUserPic;
    private LinearLayout chatItem;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    SharedPreferences sharedPreferences;
    Long userPkId;
    ToDoDetailActivity toDoDetailActivity;

    DataService dataService;
    boolean isSameUser = false;
    Long commentId;

    public CommentHolder(@NonNull View view) {
        super(view);
        chatIDTv = (TextView) view.findViewById(R.id.todo_comment_id);
        chatTv = (TextView) view.findViewById(R.id.todo_comment_text);
/*        chatItem = view.findViewById(R.id.chatItem);
        chatUserPic = view.findViewById(R.id.chat_user_pic);

        dataService = new DataService();
        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
<<<<<<< HEAD
        userPkId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);*/
=======
        userPkId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        chatItem.setOnLongClickListener(new View.OnLongClickListener() {
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


>>>>>>> 86546d789006230d268f7e13f713c9c34e50e12c
    }

    public void onBind(FindFeedBackRes data) {

        chatIDTv.setText(data.getWriterName());
        chatTv.setText(data.getComment());
        commentId = data.getId();

/*        if(data.getWriterId()==userPkId) {
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            isSameUser = true;

        }else{
            chatItem.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }*/
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
                            chatItem.setVisibility(View.GONE);
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
