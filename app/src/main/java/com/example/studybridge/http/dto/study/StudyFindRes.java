package com.example.studybridge.http.dto.study;

import java.io.Serializable;

public class StudyFindRes implements Serializable {

    private Long id;

    private String name;

    private String info;

    private String explain;

    private Integer maxNum;

    private String status;

    private String place;

    private String type;

    private int menteeCnt;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getStatus() {
        return status;
    }

    public String getPlace() {
        return place;
    }

    public String getType() {
        return type;
    }

    public int getMenteeCnt() {
        return menteeCnt;
    }
}
