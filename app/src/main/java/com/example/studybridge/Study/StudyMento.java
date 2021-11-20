package com.example.studybridge.Study;

public class StudyMento {

    String subject;
    String place;
    String mentoName;
    String metnoIntro;
    String metnoSchool;
    String metnoQualification;

    public StudyMento(String subject, String place, String mentoName, String metnoIntro, String metnoSchool, String metnoQualification) {
        this.subject = subject;
        this.place = place;
        this.mentoName = mentoName;
        this.metnoIntro = metnoIntro;
        this.metnoSchool = metnoSchool;
        this.metnoQualification = metnoQualification;
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

    public String getMentoName() {
        return mentoName;
    }

    public void setMentoName(String mentoName) {
        this.mentoName = mentoName;
    }

    public String getMetnoIntro() {
        return metnoIntro;
    }

    public void setMetnoIntro(String metnoIntro) {
        this.metnoIntro = metnoIntro;
    }

    public String getMetnoSchool() {
        return metnoSchool;
    }

    public void setMetnoSchool(String metnoSchool) {
        this.metnoSchool = metnoSchool;
    }

    public String getMetnoQualification() {
        return metnoQualification;
    }

    public void setMetnoQualification(String metnoQualification) {
        this.metnoQualification = metnoQualification;
    }
}
