package com.example.studybridge.domain;

import com.google.gson.annotations.SerializedName;

public class AuthData {

    public AuthData(String api_key, String api_secret){

    }

    @SerializedName(value = "imp_key")
    private String api_key;

    @SerializedName(value="imp_secret")
    private String api_secret;
}
