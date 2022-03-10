package com.example.studybridge.http.dto;

public class ChooseMentorRes {

    private Long studyId;

    private Long mentorId;

    private String studyName;

    @Override
    public String toString() {
        return "ChooseMentorRes{" +
                "studyId=" + studyId +
                ", mentorId=" + mentorId +
                ", studyName='" + studyName + '\'' +
                '}';
    }

    public Long getStudyId() {
        return studyId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }
}
