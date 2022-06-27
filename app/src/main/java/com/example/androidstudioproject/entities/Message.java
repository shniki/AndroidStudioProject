package com.example.androidstudioproject.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Message {
    private static final AtomicInteger intIdHelper = new AtomicInteger(0);
    @PrimaryKey(autoGenerate = true)
    private final long messageId;
    private long senderId;
    private long receiverId;
    private String content;

    public Message(){
        this.messageId = intIdHelper.incrementAndGet();
    }

    public Message(long senderId, long receiverId, String content) {
        this.messageId = intIdHelper.incrementAndGet();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }

    //getters
    public long getMessageId() {
        return messageId;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    //setters
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
