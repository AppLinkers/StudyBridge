package com.example.studybridge.http.dto.message;

public class MessageRes {

    private String messageType; // 생성 및 입장 / 퇴장 / 일반 채팅 / 이미지 채팅 구분

    private Long roomId; // 전송 채팅방

    private Long userId;

    private String userName; // 전송자 이름

    private String userProfileImg; // 전송자 프로필 이미지 링크

    private String message; // 채팅 본문

    public String getMessageType() {
        return messageType;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfileImg() {
        return userProfileImg;
    }

    public String getMessage() {
        return message;
    }
}
