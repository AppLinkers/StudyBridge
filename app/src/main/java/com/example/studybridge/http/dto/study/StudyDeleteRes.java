package com.example.studybridge.http.dto.study;

import lombok.Builder;
import lombok.Data;

public class StudyDeleteRes {

    private Long studyId;

    private Long makerId;

    public Long getStudyId() {
        return studyId;
    }

    public Long getMakerId() {
        return makerId;
    }
}
