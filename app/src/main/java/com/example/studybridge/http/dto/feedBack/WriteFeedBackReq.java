package com.example.studybridge.http.dto.feedBack;

public class WriteFeedBackReq {

    private Long assignedToDoId;

    private Long writerId;

    private String comment;

    public WriteFeedBackReq(Long assignedToDoId, Long writerId, String comment) {
        this.assignedToDoId = assignedToDoId;
        this.writerId = writerId;
        this.comment = comment;
    }
}
