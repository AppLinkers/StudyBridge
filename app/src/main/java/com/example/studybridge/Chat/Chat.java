package com.example.studybridge.Chat;

public class Chat {

    String chatID;
    String chat;

    public Chat(String chatID, String chat){
        this.chatID = chatID;
        this.chat = chat;
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
