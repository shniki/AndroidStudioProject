package com.example.androidstudioproject.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"userEmail", "secondUserEmail"})
public class UserConnections {
    @NonNull
    private String userEmail;
    @NonNull
    private String secondUserEmail;

    public UserConnections(@NonNull String userEmail, @NonNull String secondUserEmail) {
        this.userEmail = userEmail;
        this.secondUserEmail = secondUserEmail;
    }

    //getters
    @NonNull
    public String getUserEmail() {
        return userEmail;
    }

    @NonNull
    public String getSecondUserEmail() {
        return secondUserEmail;
    }

    //setters
    public void setUserEmail(@NonNull String userEmail) {
        this.userEmail = userEmail;
    }

    public void setSecondUserEmail(@NonNull String secondUserEmail) {
        this.secondUserEmail = secondUserEmail;
    }
}
