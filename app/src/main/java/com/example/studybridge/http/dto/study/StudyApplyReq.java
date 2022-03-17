package com.example.studybridge.http.dto.study;

public class StudyApplyReq {

    private String userId;

    private Long studyId;

    public StudyApplyReq(String userId, Long studyId) {
        this.userId = userId;
        this.studyId = studyId;
    }
}
