package com.example.studybridge.http.dto;

public class StudyDeleteReq {

    private Long studyId;

    private Long makerId;

    public StudyDeleteReq(Long studyId, Long makerId) {
        this.studyId = studyId;
        this.makerId = makerId;
    }

    public Long getStudyId() {
        return studyId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }

    public Long getMakerId() {
        return makerId;
    }

    public void setMakerId(Long makerId) {
        this.makerId = makerId;
    }
}
