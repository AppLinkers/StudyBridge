package com.example.studybridge.http.dto;

public class StudyApplyReq {

    private String userId;

    private Long studyId;


    public StudyApplyReq(String userId, Long studyId) {
        this.userId = userId;
        this.studyId = studyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getStudyId() {
        return studyId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }
}
