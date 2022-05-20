package com.example.studybridge.ToDo.feedback;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.Util.BackDialog;
import com.example.studybridge.Util.SharedPrefKey;
import com.example.studybridge.databinding.ChatItemBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.feedBack.FindFeedBackRes;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<FindFeedBackRes> commentList = new ArrayList<FindFeedBackRes>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new CommentHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CommentHolder) holder).onBind(commentList.get(position));
    }

    public void addItem(FindFeedBackRes data){
        commentList.add(data);
    }

    public void deleteItem(int position){
        commentList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,commentList.size());
    }



    @Override
    public int getItemCount() {
        return commentList.size();
    }



    public class CommentHolder extends RecyclerView.ViewHolder {


        private ChatItemBinding binding;

        public static final String SHARED_PREFS = "shared_prefs";
        public static final String USER_PK_ID_KEY = "user_pk_id_key";
        SharedPreferences sharedPreferences;
        Long userPkId;
        String imgPath;

        DataService dataService;
        boolean isSameUser = false;
        Long commentId;


        public CommentHolder(@NonNull View itemview) {
            super(itemview);
            binding = ChatItemBinding.bind(itemview);


            dataService = new DataService();
            sharedPreferences = itemview.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            userPkId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
            imgPath = sharedPreferences.getString(SharedPrefKey.USER_PROFILE,"img");

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


            binding.chat.setText(data.getComment());
            commentId = data.getId();


            if(data.getWriterId().equals(userPkId)) {
                isSameUser = true;
                itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                binding.senderName.setVisibility(View.INVISIBLE);
                binding.chat.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.palletRed));
                binding.chat.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                Glide.with(itemView).load(imgPath).into(binding.userImg);

            }else{
                binding.senderName.setText(data.getWriterName());
                binding.senderName.setVisibility(View.VISIBLE);
                itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                binding.chat.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.palletGrey));
                binding.chat.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.textColorPrimary));
                setProfile(data.getWriterName());
            }
        }



        private void setAlertDialog(View view){

            BackDialog dialog =BackDialog.getInstance(1);
            FragmentManager fm = ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();
            dialog.show(fm,"delete");

            dialog.setBackInterface(new BackDialog.BackInterface() {
                @Override
                public void okBtnClick() {
                    dataService.feedBack.delete(commentId).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.isSuccessful()){
                                deleteItem(getAdapterPosition());
                                Toast.makeText(view.getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
                }
            });


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


}
