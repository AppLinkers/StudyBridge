package com.example.studybridge.http.dto.study;

public class StudyMakeReq {

    private String makerId;

    private String name;

    private String type;

    private String info;

    private String explain;

    private String place;

    private Integer maxNum;


    public StudyMakeReq(String makerId, String name, String type, String info, String explain, String place, Integer maxNum) {
        this.makerId = makerId;
        this.name = name;
        this.type = type;
        this.info = info;
        this.explain = explain;
        this.place = place;
        this.maxNum = maxNum;
    }
}
