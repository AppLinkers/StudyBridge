package com.example.studybridge.Chat;

public class Chat {

    String chatID;
    String chat;
    String imgUrl;

    public Chat(String chatID, String chat,String imgUrl){
        this.chatID = chatID;
        this.chat = chat;
        this.imgUrl = imgUrl;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
