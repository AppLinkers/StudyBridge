package com.example.studybridge.Mypage.MentoProfile.Edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.Util.Permission;
import com.example.studybridge.databinding.MypageMentoprofileBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentor.Certificate;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageProfileActivity extends AppCompatActivity {


    private MypageMentoprofileBinding binding;
    private ProfileRes res;

    private LinearLayoutManager linearLayoutManager;
    private MyPageProfileAdapter adapter;
    //SharedPreference
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_NAME = "user_name_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";

    private List<Certificate> certificates;

    private String userName;
    private String userLoginId;
    private Long userPkId;

    String schoolDir;

    DataService dataService = new DataService();
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MypageMentoprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //sharedPrefer
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(USER_NAME, "사용자");
        userLoginId = sharedPreferences.getString(USER_ID_KEY, "userLoginId");
        userPkId = sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        Permission.verifyStoragePermissions(this);


        getIntentData();
        setUI();

    }




    private void getIntentData() {
        //기존 입력값
        Intent intent = getIntent();
        res = intent.getExtras().getParcelable("profile");
        if (res != null) {
            binding.nickName.setText(res.getNickName());
            binding.intro.setText(res.getInfo());
            binding.curi.setText(res.getCurriculum());
            binding.exp.setText(res.getExperience());
            binding.appeal.setText(res.getAppeal());
            schoolDir = res.getSchoolImg();
            Glide.with(this).load(schoolDir).into(binding.schoolImg);

            StringTokenizer st = new StringTokenizer(res.getSchool(), "-");
            binding.school.setText(st.nextToken());
            binding.major.setText(st.nextToken());

            Chip chipForSubject = (Chip) binding.subjectSelect.getChildAt(checkChipForSubject(res.getSubject()));
            Chip chipForPlace = (Chip) binding.placeSelect.getChildAt(checkChipForPlace(res.getLocation()));
            chipForSubject.setChecked(true);
            chipForPlace.setChecked(true);

            certificates = new ArrayList<>();
            certificates = res.getCertificates();
        }

    }
    private void setUI(){

        setToolbar();

        editData(binding.nickName,binding.nickNameLine,binding.nickNameCheckCon,binding.nickNameCheck);
        editData(binding.school,binding.schoolLine,null,null);
        editData(binding.major,binding.majorLine,null,null);
        editData(binding.intro,binding.introLine,null,null);
        editData(binding.curi,binding.curiLine,null,null);
        editData(binding.exp,binding.expLine,null,null);
        editData(binding.appeal,binding.appealLine,null,null);

        setRecyclerView();
        setButton();
    }

    private void setRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.certiRV.setLayoutManager(linearLayoutManager);
        adapter = new MyPageProfileAdapter(certificates,(Context)this);
        binding.certiRV.setAdapter(adapter);
    }

    private void editData(EditText editText, View view, MaterialCardView cardView, TextView textView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                    if(cardView !=null && textView != null){
                        textView.setEnabled(false);
                        cardView.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
                        textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
                    }


                }
                else {
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                    if(cardView !=null && textView != null){
                        textView.setEnabled(true);
                        cardView.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.textColorPrimary70));
                        textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.textColorPrimary70));
                    }


                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                    if(cardView !=null && textView != null){
                        textView.setEnabled(false);
                        cardView.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
                        textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
                    }


                }
                else {
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                    if(cardView !=null && textView != null){
                        textView.setEnabled(true);
                        cardView.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.textColorPrimary70));
                        textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.textColorPrimary70));
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setButton(){

        binding.schoolImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                goToGetImg.launch(intent);
            }
        });

        binding.addCerti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                goToAddCerti.launch(intent);
            }
        });

    }

    //툴바 설정
    private void setToolbar(){

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mypage_mentoprofile_menu,menu);
        return true;
    }

    ActivityResultLauncher<Intent> goToGetImg = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){

                        assert result.getData() != null;

                        schoolDir = uriPath(result.getData().getData());
                        Glide.with(MyPageProfileActivity.this).load(schoolDir).into(binding.schoolImg);


                    }
                }
            }
    );
    ActivityResultLauncher<Intent> goToAddCerti = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        assert result.getData() != null;

                        String dir = uriPath(result.getData().getData());
                        Certificate dataForAdd = new Certificate(null,dir);
                        certificates.add(dataForAdd);
                        adapter.notifyItemInserted(adapter.getItemCount());
                    }
                }
            }
    );

    //메뉴 버튼 설정
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
                //상단 완료버튼 눌렀을때
            case R.id.mentoProfile_complete_btn:


                List<Certificate> inputCerti = new ArrayList<>();
                String selectedPlace=null,selectedSubject=null;

                for(int i=0; i<certificates.size(); i++){
                    inputCerti.add(new Certificate(
                                    certificates.get(i).getCertificate(),
                                    certificates.get(i).getImgUrl()));
                }

                for(int i=0; i<binding.placeSelect.getChildCount();i++){
                    Chip chip = (Chip) binding.placeSelect.getChildAt(i);
                    if(chip.isChecked()){
                        selectedPlace = chip.getText().toString();
                    }
                }

                for(int i=0; i<binding.subjectSelect.getChildCount();i++){
                    Chip chip = (Chip) binding.subjectSelect.getChildAt(i);
                    if(chip.isChecked()){
                        selectedSubject = chip.getText().toString();
                    }
                }

                StringBuilder sbForSchool = new StringBuilder();
                sbForSchool.append(binding.school.getText().toString().trim()).append("-").append(binding.major.getText().toString().trim());

                ProfileRes mentoProfile = new ProfileRes(
                        userPkId,
                        userName,
                        selectedPlace,
                        binding.intro.getText().toString().trim()+"",
                        binding.nickName.getText().toString().trim()+"",
                        sbForSchool.toString(),
                        schoolDir,
                        selectedSubject,
                        inputCerti,
                        binding.exp.getText().toString().trim()+"",
                        binding.curi.getText().toString().trim()+"",
                        binding.appeal.getText().toString().trim()+"",
                        false
                );

                Map<String, RequestBody> profileReq = new HashMap<>();

                profileReq.put("profileTextReq.userLoginId", RequestBody.create(MultipartBody.FORM, userLoginId));
                profileReq.put("profileTextReq.location", RequestBody.create(MultipartBody.FORM, mentoProfile.getLocation()));
                profileReq.put("profileTextReq.info", RequestBody.create(MultipartBody.FORM, mentoProfile.getInfo()));
                profileReq.put("profileTextReq.nickName", RequestBody.create(MultipartBody.FORM, mentoProfile.getNickName()));
                profileReq.put("profileTextReq.subject", RequestBody.create(MultipartBody.FORM, mentoProfile.getSubject()));
                profileReq.put("profileTextReq.school", RequestBody.create(MultipartBody.FORM, mentoProfile.getSchool()));
                profileReq.put("profileTextReq.experience", RequestBody.create(MultipartBody.FORM, mentoProfile.getExperience()));
                profileReq.put("profileTextReq.curriculum", RequestBody.create(MultipartBody.FORM, mentoProfile.getCurriculum()));
                profileReq.put("profileTextReq.appeal", RequestBody.create(MultipartBody.FORM, mentoProfile.getAppeal()));

                List<MultipartBody.Part> certificates = new ArrayList<>();
                List<MultipartBody.Part> certificatesImgReq = new ArrayList<>();
                if (mentoProfile.getCertificates().size() > 0) {
                    mentoProfile.getCertificates().forEach(cn -> {
                        certificates.add(MultipartBody.Part.createFormData("certificates", cn.getCertificate()));
                        RequestBody certificateImg = RequestBody.create(MediaType.parse("multipart/form-data"),new File(cn.getImgUrl()));
                        certificatesImgReq.add(MultipartBody.Part.createFormData("certificatesImg", cn.getCertificate(), certificateImg));
                    });
                }

                // school Img
                RequestBody schoolImg = RequestBody.create(MediaType.parse("multipart/form-data"), new File(mentoProfile.getSchoolImg()));
                MultipartBody.Part schoolImgReq = MultipartBody.Part.createFormData("schoolImg", mentoProfile.getSchool(), schoolImg);


                dataService.userMentor.profile(schoolImgReq,certificatesImgReq,certificates, profileReq).enqueue(new Callback<ProfileRes>() {
                    @Override
                    public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                        if (response.isSuccessful()) {
                            Log.d("test", response.body().toString());
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileRes> call, Throwable t) {
                        Log.d("test", t.toString());
                    }
                });


                return true;
        }
        return super.onOptionsItemSelected(item);
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

/*   public String saveUrlToJpg(String uri) {

        File storage = getCacheDir(); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
        String fileName = uri + ".jpg";
        File imgFile = new File(storage, fileName);
        try {
            if(!imgFile.getParentFile().exists()){
                imgFile.getParentFile().mkdirs();
            }
            if(!imgFile.exists()){
                imgFile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(imgFile);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("saveUrlToJpg","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("saveUrlToJpg","IOException : " + e.getMessage());
        }
        Log.d("imgPath" , getCacheDir() + "/" +fileName);

        return getCacheDir() + "/" +fileName;
    }*/

    //기존 과목&&지역 선택 함수
    public static int checkChipForSubject(String str){
        if(str!=null){
            switch (str)
            {
                case "영어" :
                    return 0;
                case "수학" :
                    return 1;
                case "개발" :
                    return 2;
                case "기타" :
                    return 3;
                default:
                    return 0;

            }
        } else {
            return 0;
        }
    }
    public static int checkChipForPlace(String str){

        if(str!=null){
            switch (str)
            {
                case "서울" :
                    return 0;
                case "경기" :
                    return 1;
                case "인천" :
                    return 2;
                case "기타" :
                    return 3;
                default:
                    return 0;

            }
        } else {
            return 0;
        }

    }

}
