package com.example.studybridge.Mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.Util.SharedPrefKey;
import com.example.studybridge.databinding.MypageProfileimgBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;
import com.example.studybridge.http.dto.userAuth.UserProfileUpdateReq;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MypageProfileImg extends AppCompatActivity {

    private MypageProfileimgBinding binding;

    //SharedPreference
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";

    private String dir;
    private String userLoginId;
    String imgPath;

    private DataService dataService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MypageProfileimgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //sharedPrefer
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userLoginId = sharedPreferences.getString(USER_ID_KEY, "userLoginId");
        imgPath = sharedPreferences.getString(SharedPrefKey.USER_PROFILE,"img");

        setUI();
    }

    private void setUI(){
        dir = imgPath;
        Glide.with(this).load(dir).into(binding.img);
        changeImg();
        setBtn();
    }

    private void changeImg(){

        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                goToGetImg.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> goToGetImg = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){

                        assert result.getData() != null;

                        dir = uriPath(result.getData().getData());

                        Glide.with(MypageProfileImg.this).load(dir).into(binding.img);


                    }
                }
            }
    );

    private void setBtn(){
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        //기본 이미지로
        binding.basicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dir = null; //기본이미지 경로
                Glide.with(MypageProfileImg.this).load(dir).into(binding.img);
            }
        });
        //완료 버튼
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setConfirm();
            }
        });
    }

    private void setConfirm(){
        if(!dir.equals(imgPath)){
            dataService = new DataService();
            
            File file = new File(dir);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            
            MultipartBody.Part body = MultipartBody.Part.createFormData("imgFile",file.getName(),requestFile);
            
            dataService.userAuth.updateProfileImg(userLoginId,body).enqueue(new Callback<UserProfileRes>() {
                @Override
                public void onResponse(Call<UserProfileRes> call, Response<UserProfileRes> response) {
                    if(response.isSuccessful()){

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SharedPrefKey.USER_PROFILE,dir);
                        editor.apply();

                        finish();
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    }
                }

                @Override
                public void onFailure(Call<UserProfileRes> call, Throwable t) {
                    finish();
                    Toast.makeText(MypageProfileImg.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            finish();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }

    }

    private String uriPath(Uri uri){
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };

            assert uri != null;
            cursor = getContentResolver().query(uri, proj, null, null, null);

            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private File getTempFile(Context context,String uri,String fileName){
        File file = null;
        try {
            file =File.createTempFile(fileName,null,context.getCacheDir());
        } catch (IOException e){
           e.printStackTrace();
        }
        return file;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
