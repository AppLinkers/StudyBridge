package com.example.studybridge.http.dto.message;

public class MessageReq {

    private String messageType;

    private Long roomId;

    private Long userId; // 채팅 전송자 식별자

    private String message; // 채팅 본문

    public MessageReq(String messageType, Long roomId, Long userId, String message) {
        this.messageType = messageType;
        this.roomId = roomId;
        this.userId = userId;
        this.message = message;
    }
}
