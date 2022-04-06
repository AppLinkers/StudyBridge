package com.example.studybridge.Study;

public class StudyFilter {

    private String status;
    private String type;
    private String place;

    public StudyFilter(String status, String type, String place) {
        this.status = status;
        this.type = type;
        this.place = place;
    }

    public StudyFilter(String type, String place) {
        this.type = type;
        this.place = place;
    }

    public String getStatus() {
        if(this.status.equals("멘티 모집")){
            return "APPLY";
        } else if(this.status.equals("멘토 모집")){
            return "WAIT";
        } else if(this.status.equals("모집 종료")){
            return "MATCHED";
        }
        else return status;
    }

    public String getType() {
        return type;
    }

    public String getPlace() {
        return place;
    }
}
