package com.example.androidstudioproject.entities;

//@Entity(primaryKeys = {"userId", "secondUserId"})
public class UserConnections {
    private long userId;
    private long secondUserId;

    public UserConnections(){}

    public UserConnections(long userId, long secondUserId) {
        this.userId = userId;
        this.secondUserId = secondUserId;
    }

    //getters
    public long getUserId() {
        return userId;
    }

    public long getSecondUserId() {
        return secondUserId;
    }

    //setters
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setSecondUserId(long secondUserId) {
        this.secondUserId = secondUserId;
    }
}
