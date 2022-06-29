package com.example.androidstudioproject.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Comment {
    private static final AtomicInteger intIdHelper = new AtomicInteger(0);
    @PrimaryKey(autoGenerate = true)
    private final long commentID;
    @NonNull
    private String commenterUserName; //(user) one to many (comment)
//    @ForeignKey(Post)
    @NonNull
    private final String postID; //(post) one to many (comment)
    private String content;
    private Boolean isDeleted;

    public Comment(@NonNull String commenterUserName, @NonNull String postID, String content)
    {
        this.commentID = intIdHelper.incrementAndGet();
        this.commenterUserName = commenterUserName;
        this.postID = postID;
        this.content = content;
        this.isDeleted = false;
    }

    //getters
    public long getCommentID() {
        return commentID;
    }

    @NonNull
    public String getCommenterUserName() {
        return commenterUserName;
    }

    @NonNull
    public String getPostID() {
        return postID;
    }

    public String getContent() {
        return content;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    //setters
    public void setCommenterUserName(@NonNull String commenterUserName) {
        this.commenterUserName = commenterUserName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}

