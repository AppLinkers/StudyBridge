package com.example.studybridge.http.dto.feedBack;

public class FindFeedBackRes {

    private Long id;

    private Long assignedToDoId;

    private Long writerId;

    private String writerName;

    private String writerProfileImg;

    private String comment;

    public Long getId() {
        return id;
    }

    public Long getAssignedToDoId() {
        return assignedToDoId;
    }

    public Long getWriterId() {
        return writerId;
    }

    public String getWriterName() {
        return writerName;
    }

    public String getWriterProfileImg() {
        return writerProfileImg;
    }

    public String getComment() {
        return comment;
    }
}
