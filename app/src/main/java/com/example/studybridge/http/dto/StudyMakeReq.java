package com.example.studybridge.http.dto;

public class StudyMakeReq {

    private String makerId;

    private String name;

    private String type;

    private String info;

    private String place;

    private Integer maxNum;

    public StudyMakeReq(String makerId, String name, String type, String info, String place, Integer maxNum) {
        this.makerId = makerId;
        this.name = name;
        this.type = type;
        this.info = info;
        this.place = place;
        this.maxNum = maxNum;
    }


}
