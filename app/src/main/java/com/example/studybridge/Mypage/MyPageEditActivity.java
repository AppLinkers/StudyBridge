package com.example.studybridge.Mypage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.Detail.CertiAdapter;
import com.example.studybridge.Util.SharedPrefKey;
import com.example.studybridge.databinding.MypageEditinfoBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentor.ProfileRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageEditActivity extends AppCompatActivity {

    private MypageEditinfoBinding binding;

    private DataService dataService = new DataService();

    private CertiAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    public static final String USER_ISMENTEE = "user_mentee_key";

    SharedPreferences sharedPreferences;
    private String userName;
    private String userId;
    private String imgPath;
    private boolean isMentee;

    private ProfileRes res;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MypageEditinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userName= sharedPreferences.getString(USER_NAME, "사용자");
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        isMentee = sharedPreferences.getBoolean(USER_ISMENTEE,false);
        imgPath = sharedPreferences.getString(SharedPrefKey.USER_PROFILE,"img");

        setUI();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        imgPath = sharedPreferences.getString(SharedPrefKey.USER_PROFILE,"img");
        Glide.with(this).load(imgPath).into(binding.img);
    }

    private void setUI(){
        //툴바 설정
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitle(" ");

        binding.id.setText(userId);
        binding.name.setText(userName);
        Glide.with(this).load(imgPath).into(binding.img);

        if(isMentee){
            binding.mentorCon.setVisibility(View.GONE);
        }
        else {

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            binding.certiRV.setLayoutManager(linearLayoutManager);
            getData();

        }

        setBtns();
    }

    //뒤로 가기 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        dataService.userMentor.getProfile(userId,userId).enqueue(new Callback<ProfileRes>() {
            @Override
            public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                if(response.isSuccessful()){
                    res = response.body();
                    binding.nickName.setText(findNull(res.getNickName()));
                    binding.place.setText(findNull(res.getLocation()));
                    binding.subject.setText(findNull(res.getSubject()));
                    binding.school.setText(findNull(res.getSchool()));
                    binding.intro.setText(findNull(res.getInfo()));
                    binding.curi.setText(findNull(res.getCurriculum()));
                    binding.exp.setText(findNull(res.getExperience()));
                    binding.appeal.setText(findNull(res.getAppeal()));

                    if(res.getCertificates().size() == 0){
                        binding.nullCertiText.setVisibility(View.VISIBLE);
                    }
                    else{
                        adapter = new CertiAdapter(res.getCertificates());
                        binding.certiRV.setAdapter(adapter);
                        binding.nullCertiText.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileRes> call, Throwable t) {

            }
        });
    }

    private void setBtns(){

        //계정 관리
        binding.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyPageAccountActivity.class);
                isFinish.launch(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        //사진 변경
        binding.imgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageProfileImg.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        //멘토 정보 변경
        binding.mentorInfoChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageProfileActivity.class);
                intent.putExtra("profile",res);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private String findNull(String str){
        final String result;
        if(str==null){
            result = "-";
        }
        else result = str;

        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    ActivityResultLauncher<Intent> isFinish = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
    );

    /* //갤러리에서 이미지 받아오는 메서드
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());


                    Bitmap img = BitmapFactory.decodeStream(in);

                    Bitmap rImg = rotateImage(data.getData(), img);
                    in.close();
                    imgNow.setImageBitmap(rImg);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                //취소할 경우 case
            }
        }
    }

    //사진 돌아감 방지 메서드
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap rotateImage(Uri uri, Bitmap bitmap) throws IOException {
        InputStream in = getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(in);
        in.close();

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90){
            matrix.postRotate(90);
        }
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180){
            matrix.postRotate(180);
        }
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270){
            matrix.postRotate(270);
        }

        return Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);


    }*/

}
