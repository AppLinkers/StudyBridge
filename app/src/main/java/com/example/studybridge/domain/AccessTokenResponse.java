package com.example.studybridge.domain;

import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse {

    public AccessTokenResponse(){

    }

    @SerializedName(value = "access_token")
    String accessToken;

    @SerializedName(value = "now")
    int now;

    @SerializedName("expired_at")
    int expiredAt;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public int getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(int expiredAt) {
        this.expiredAt = expiredAt;
    }
}
