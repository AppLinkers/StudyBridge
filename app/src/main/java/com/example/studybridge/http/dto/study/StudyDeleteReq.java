package com.example.studybridge.http.dto.study;

public class StudyDeleteReq {

    private Long studyId;

    private Long makerId;

    public StudyDeleteReq(Long studyId, Long makerId) {
        this.studyId = studyId;
        this.makerId = makerId;
    }

}
