package com.example.studybridge.http.dto;

import lombok.Data;

@Data
public class UserLoginRes {

    private String loginId;
    private String name;

    UserLoginRes(String loginId, String name){
        this.loginId = loginId;
        this.name = name;

    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

