package com.example.androidstudioproject.entities;

import java.util.concurrent.atomic.AtomicInteger;

public class Comment {
    private static final AtomicInteger intIdHelper = new AtomicInteger(0);

    private final long commentID;
    private String commenterUserName; //(user) one to many (comment)
    private String postID; //(post) one to many (comment)
    private String content;
    private Boolean isDeleted;

    public Comment(){
        this.commentID = intIdHelper.incrementAndGet();
    }

    public Comment(String commenterUserName, String postID, String content)
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

    public String getCommenterUserName() {
        return commenterUserName;
    }

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
    public void setCommenterUserName(String commenterUserName) {
        this.commenterUserName = commenterUserName;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}

