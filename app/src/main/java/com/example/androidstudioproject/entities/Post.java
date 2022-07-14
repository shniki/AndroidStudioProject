package com.example.androidstudioproject.entities;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {
    private static final Random random = new Random();
//todo: intidhelper or use auto generate?
    @PrimaryKey(autoGenerate = true)
    private long postID; //make this random + FINAL
    //foreign key
    @NonNull
    private String userEmail; //(user) one to many (post)
    private String content;

    //post date
    @NonNull
    private String postDate; //FINAL

    private int dataType; //0-text only, 1-video, 2-video
    private String dataURL;//photo will be binary, then convert with function that I guess exist

    private String location;
//
//    public Post(){
//        this.postID = intIdHelper.incrementAndGet();
//        this.isDeleted = false;
//        this.postDate = dateParse();
//        userEmail = null;
//        dataURL = null;
//        dataType = 0;
//    }

    public Post(@NonNull String userEmail, String content, int dataType, String dataURL,String location) {
        this.postID = Math.abs(random.nextInt());
        this.userEmail = userEmail;
        this.content = content;
        this.dataType = dataType;
        this.dataURL = dataURL;
        this.postDate = dateParse();
        this.location=location;
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

    public String getLocation() {
        return location;
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

    public void setPostID(long postID) {
        //this.postID = postID;
    }

    public void setPostDate(@NonNull String postDate) {
        //this.postDate = postDate;
    }
}

