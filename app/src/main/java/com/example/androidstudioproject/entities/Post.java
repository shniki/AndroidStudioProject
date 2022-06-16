package com.example.androidstudioproject.entities;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Post {
    private static final AtomicInteger intIdHelper = new AtomicInteger(0);

    private final long postID; //make this incremental
    private String userName; //(user) one to many (post)
    private String content;

    //post date
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
    }

    public Post(String userName, String content, Bitmap picture) {
        this.postID = intIdHelper.incrementAndGet();
        this.userName = userName;
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

    public String getUserName() {
        return userName;
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
    public void setUserName(String userName) {
        this.userName = userName;
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

