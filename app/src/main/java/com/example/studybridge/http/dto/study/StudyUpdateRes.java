package com.example.studybridge.http.dto.study;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StudyUpdateRes {

    private Long studyId;

    private Long makerId;

    private String name;

    private String type;

    private String info;

    private String explain;

    private Integer maxNum;

    public Long getStudyId() {
        return studyId;
    }

    public Long getMakerId() {
        return makerId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getInfo() {
        return info;
    }

    public String getExplain() {
        return explain;
    }

    public Integer getMaxNum() {
        return maxNum;
    }
}
