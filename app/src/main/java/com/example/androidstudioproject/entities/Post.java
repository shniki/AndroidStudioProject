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

    private int dataType; //0-text only, 1-video, 2-video
    private String dataURL;//photo will be binary, then convert with function that I guess exist

    private Boolean isDeleted;

    //TODO add location
    // enter location? then with google map
    // add videos

    public Post(){
        this.postID = intIdHelper.incrementAndGet();
        this.isDeleted = false;
        this.postDate = dateParse();
        userEmail = null;
        dataURL = null;
        dataType = 0;
    }

    public Post(@NonNull String userEmail, String content, int dataType, String dataURL) {
        this.postID = intIdHelper.incrementAndGet();
        this.userEmail = userEmail;
        this.content = content;
        this.dataType = dataType;
        this.dataURL = dataURL;
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

    public int getDataType() {
        return dataType;
    }

    public String getDataURL() {
        return dataURL;
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

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void setDataURL(String dataURL) {
        this.dataURL = dataURL;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}

