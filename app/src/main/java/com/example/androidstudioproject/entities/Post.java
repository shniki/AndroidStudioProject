package com.example.androidstudioproject.entities;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {
    private static final AtomicInteger intIdHelper = new AtomicInteger(0);
//todo: intidhelper or use auto generate?
    @PrimaryKey(autoGenerate = true)
    private final long postID; //make this incremental
    //foreign key
    @NonNull
    private String userEmail; //(user) one to many (post)
    private String content;

    //post date
    @NonNull
    private final String postDate;

    private Bitmap picture; //photo will be binary, then convert with function that I guess exists
    //TODO add video
    private Boolean isDeleted;

    //TODO add location
    // enter location? then with google maps
    // amount of likes, comments...?
    // add videos

    public Post(){
        this.postID = intIdHelper.incrementAndGet();
        this.isDeleted = false;
        this.postDate = dateParse();
        userEmail = null;
    }

    public Post(@NonNull String userEmail, String content, Bitmap picture) {
        this.postID = intIdHelper.incrementAndGet();
        this.userEmail = userEmail;
        this.content = content;
        this.picture = picture;
        this.isDeleted = false;
        this.postDate = dateParse();
    }

    private String dateParse(){
        String[] fullDate= new Date().toString().split(" ");
        return fullDate[1]+" "+fullDate[2]+" "+fullDate[5];
    }

    //getters
    public long getPostID() {
        return postID;
    }

    @NonNull
    public String getUserEmail() {
        return userEmail;
    }

    public String getContent() {
        return content;
    }

    public String getPostDate() {
        return postDate;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }


    //setters
    public void setUserEmail(@NonNull String userEmail) {
        this.userEmail = userEmail;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}

