package com.example.studybridge.http.dto;

public class ChangeStatusReq {

    private Long studyId;

    private String status;

    public ChangeStatusReq(Long studyId, String status) {
        this.studyId = studyId;
        this.status = status;
    }
}
