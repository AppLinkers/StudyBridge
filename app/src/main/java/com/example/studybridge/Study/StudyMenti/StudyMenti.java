package com.example.studybridge.Study.StudyMenti;

public class StudyMenti {

    int status;
    String subject;
    String place;
    String studyName;
    String studyIntro;
    int studyPeopleNum;



    public StudyMenti(int status, String subject, String place, String studyName, String studyIntro, int studyPeopleNum) {
        this.status = status;
        this.subject = subject;
        this.place = place;
        this.studyName = studyName;
        this.studyIntro = studyIntro;
        this.studyPeopleNum = studyPeopleNum;
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

    public int getStudyPeopleNum() {
        return studyPeopleNum;
    }

    public void setStudyPeopleNum(int studyPeopleNum) {
        this.studyPeopleNum = studyPeopleNum;
    }
}
