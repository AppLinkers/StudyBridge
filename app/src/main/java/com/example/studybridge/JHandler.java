package com.example.studybridge;


import android.content.res.Resources;
import com.example.studybridge.domain.AccessToken;
import com.example.studybridge.domain.AuthData;
import com.example.studybridge.domain.Certification;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JHandler {



        String API_URL = "https://api.iamport.kr";
        Retrofit retrofit= new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        IamportClient iamPortClient = retrofit.create(IamportClient.class);

        @android.webkit.JavascriptInterface

        public void getData(String impUid){
            String apiKey = "1689708652604243";
            String apiSecretKey = "0ea067b2e7cc706db37e24047f0cfc3f720ba39deed40f1aae54200b07c0b56c0b712ae30a16896d";

            Call getData = iamPortClient.token(new AuthData(apiKey, apiSecretKey));

            getData.enqueue(new Callback<AccessToken>() {

                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    if(response.isSuccessful()){
                        System.out.println();
                        String token = response.body().getResponse().getAccessToken();
                        Call<Certification> getAuth = iamPortClient.certification_by_imp_uid(token,impUid);

                        getAuth.enqueue(new Callback<Certification>() {
                            @Override
                            public void onResponse(Call<Certification> call, Response<Certification> response) {
                                if(response.isSuccessful()){
                                    System.out.println("인증정보 ${response.body().response}");
                                }
                            }

                            @Override
                            public void onFailure(Call<Certification> call, Throwable t) {

                            }

                        });
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {

                }

            });

    }
}
