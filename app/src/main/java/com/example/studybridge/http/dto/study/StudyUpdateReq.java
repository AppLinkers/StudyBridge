package com.example.studybridge.http.dto.study;

public class StudyUpdateReq {

    private Long studyId;

    private Long makerId;

    private String name;

    private String type;

    private String info;

    private String explain;

    private String place;

    private Integer maxNum;

    public StudyUpdateReq(Long studyId, Long makerId, String name, String type, String info, String explain, String place, Integer maxNum) {
        this.studyId = studyId;
        this.makerId = makerId;
        this.name = name;
        this.type = type;
        this.info = info;
        this.explain = explain;
        this.place = place;
        this.maxNum = maxNum;
    }
}
