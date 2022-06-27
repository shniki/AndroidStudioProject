package com.example.androidstudioproject.daos;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject.entities.Post;

@Dao
public interface PostDao {
    @Query("select * from Post") //get all posts
    Post[] index();

    @Query("select * from Post where postID= :postID") //get a specific post
    Post get(String postID);

    //@Insert(onConflict = OnConflictStrategy.REPLACE) void insertAll(post... posts); //add post
    @Insert
    void insert(Post... post); //add post

    @Update
    void update(Post... post); //update post

    @Delete
    void delete(Post post); //delete posts
}
