package com.example.studybridge.Study.StudyMento;

import java.util.List;

public class StudyMento {

    String subject;
    String place;
    String mentoName;
    String metnoIntro;
    String metnoSchool;
    List<String> metnoQualification;
    boolean liked;

    public StudyMento(String subject, String place, String mentoName, String metnoIntro, String metnoSchool, List<String> metnoQualification, boolean liked) {
        this.subject = subject;
        this.place = place;
        this.mentoName = mentoName;
        this.metnoIntro = metnoIntro;
        this.metnoSchool = metnoSchool;
        this.metnoQualification = metnoQualification;
        this.liked = liked;
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

    public List<String> getMetnoQualification() {
        return metnoQualification;
    }

    public void setMetnoQualification(List<String> metnoQualification) {
        this.metnoQualification = metnoQualification;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
