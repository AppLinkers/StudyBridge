package com.example.studybridge.http.dto.userMentee;

public class LikeMentorRes {

    private Long menteeId;

    private Long mentorId;

    @Override
    public String toString() {
        return "LikeMentorRes{" +
                "menteeId=" + menteeId +
                ", mentorId=" + mentorId +
                '}';
    }

    public Long getMenteeId() {
        return menteeId;
    }

    public Long getMentorId() {
        return mentorId;
    }
}
