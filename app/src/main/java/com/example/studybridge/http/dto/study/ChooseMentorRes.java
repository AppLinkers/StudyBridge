package com.example.studybridge.http.dto.study;

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

    public Long getMentorId() {
        return mentorId;
    }

    public String getStudyName() {
        return studyName;
    }
}
