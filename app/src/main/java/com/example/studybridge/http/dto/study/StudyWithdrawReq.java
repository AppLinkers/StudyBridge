package com.example.studybridge.http.dto.study;

public class StudyWithdrawReq {

    private Long studyId;

    private Long userId;

    public StudyWithdrawReq(Long studyId, Long userId) {
        this.studyId = studyId;
        this.userId = userId;
    }
}
