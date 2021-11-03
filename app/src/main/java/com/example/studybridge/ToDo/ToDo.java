package com.example.studybridge.ToDo;

public class ToDo {

    String studyName;
    String startDate;
    int progress;



    public ToDo(String studyName, String startDate, int progress) {
        this.studyName = studyName;
        this.startDate = startDate;
        this.progress = progress;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
