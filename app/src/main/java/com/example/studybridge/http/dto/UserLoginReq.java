package com.example.studybridge.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class UserLoginReq {

    private String loginId;

    private String loginPw;

    public UserLoginReq(String toString, String toString1) {
    }
}
