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

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getPlace() {
        return place;
    }
}
