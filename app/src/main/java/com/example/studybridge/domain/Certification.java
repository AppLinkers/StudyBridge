package com.example.studybridge.domain;

import com.google.gson.annotations.SerializedName;

public class Certification {

    public Certification(){

    }

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("response")
    CertificationResponse response;

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

    public CertificationResponse getResponse() {
        return response;
    }

    public void setResponse(CertificationResponse response) {
        this.response = response;
    }
}
