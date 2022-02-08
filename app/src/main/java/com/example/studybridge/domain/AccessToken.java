package com.example.studybridge.domain;

import com.google.gson.annotations.SerializedName;

public class AccessToken {

    @SerializedName(value = "code")
    int code;

    @SerializedName(value = "message")
    String message;

    @SerializedName(value = "response")
    AccessTokenResponse response;

    public AccessToken(){

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AccessTokenResponse getResponse() {
        return response;
    }

    public void setResponse(AccessTokenResponse response) {
        this.response = response;
    }
}
