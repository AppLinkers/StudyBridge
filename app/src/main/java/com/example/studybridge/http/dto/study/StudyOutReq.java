package com.example.studybridge.http.dto.study;

public class StudyOutReq {

    private Long studyId;

    private Long menteeId;

    public StudyOutReq(Long studyId, Long menteeId) {
        this.studyId = studyId;
        this.menteeId = menteeId;
    }
}
