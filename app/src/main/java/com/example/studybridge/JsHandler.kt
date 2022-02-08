package com.example.studybridge

import android.content.res.Resources
import com.example.studybridge.domain.AccessToken
import com.example.studybridge.domain.AuthData
import com.example.studybridge.domain.Certification
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JsHandler {



    val API_URL = "https://api.iamport.kr"
    var retrofit: Retrofit
    var iamPortClient: IamportClient

    init {
        retrofit = Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).build()
        iamPortClient = retrofit.create(IamportClient::class.java)
    }

    @android.webkit.JavascriptInterface

    fun getData(impUid:String){
        val apiKey: String = "1689708652604243"
        val apiSecretKey: String = "0ea067b2e7cc706db37e24047f0cfc3f720ba39deed40f1aae54200b07c0b56c0b712ae30a16896d"

        val getData = iamPortClient.token(AuthData(apiKey, apiSecretKey))
        getData.enqueue(object: Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>?, response: Response<AccessToken>?) {
                if(response!!.isSuccessful){
                    println("토큰은?")
                    var token = response.body().response?.accessToken
                    var getAuth = iamPortClient.certification_by_imp_uid(token,impUid)

                    getAuth.enqueue(object : Callback<Certification>{
                        override fun onResponse(call: Call<Certification>?, response: Response<Certification>?) {
                            if(response!!.isSuccessful){
                                println("인증정보 ${response.body().response}")
                            }
                        }

                        override fun onFailure(call: Call<Certification>?, t: Throwable?) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

            override fun onFailure(call: Call<AccessToken>?, t: Throwable?) {
                TODO("Not yet implemented")
            }

        })


    }

}