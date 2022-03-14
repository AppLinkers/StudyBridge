package com.example.studybridge.http.dto;

import lombok.Data;

@Data
public class UserLoginRes {

    private Long id;
    private String loginId;
    private String name;

    public UserLoginRes(Long id, String loginId, String name) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

