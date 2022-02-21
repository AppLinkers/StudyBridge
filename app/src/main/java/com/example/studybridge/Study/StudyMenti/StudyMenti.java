package com.example.studybridge.Study.StudyMenti;

import androidx.cardview.widget.CardView;

import java.io.Serializable;

public class StudyMenti implements Serializable {

    Long id;
    int status;
    String subject;
    String place;
    String studyName;
    String studyIntro;
    int maxNum;

    @Override
    public String toString() {
        return "StudyMenti{" +
                "status=" + status +
                ", subject='" + subject + '\'' +
                ", place='" + place + '\'' +
                ", studyName='" + studyName + '\'' +
                ", studyIntro='" + studyIntro + '\'' +
                ", maxNum=" + maxNum +
                '}';
    }

    public StudyMenti(Long id, int status, String subject, String place, String studyName, String studyIntro, int maxNum) {
        this.id = id;
        this.status = status;
        this.subject = subject;
        this.place = place;
        this.studyName = studyName;
        this.studyIntro = studyIntro;
        this.maxNum = maxNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getStudyIntro() {
        return studyIntro;
    }

    public void setStudyIntro(String studyIntro) {
        this.studyIntro = studyIntro;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String statusStr(){
        int status = getStatus();
        if(status == 0){
            return "멘티 모집중";
        }
        else if(status == 1){
            return "멘토 모집중";
        }
        else if(status ==2){
            return "매칭완료";
        }else{
            return "스터디 종료";

        }


    }
}
