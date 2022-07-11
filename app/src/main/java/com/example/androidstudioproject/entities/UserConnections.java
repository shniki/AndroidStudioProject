package com.example.androidstudioproject.entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"userEmail", "secondUserEmail"})
public class UserConnections {
    private String userEmail;
    private String secondUserEmail;

    public UserConnections(String userEmail, String secondUserEmail) {
        this.userEmail = userEmail;
        this.secondUserEmail = secondUserEmail;
    }

    //getters
    public String getUserEmail() {
        return userEmail;
    }

    public String getSecondUserEmail() {
        return secondUserEmail;
    }

    //setters
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setSecondUserEmail(String secondUserEmail) {
        this.secondUserEmail = secondUserEmail;
    }
}
