package com.example.androidstudioproject.daos;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject.entities.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Query("select * from Post") //get all posts
    List<Post> index();

    @Query("select * from Post where postID= :postID") //get a specific post
    Post get(long postID);

    @Query("select * from Post where userEmail= :userEmail") //get a specific post
    List<Post> getByUser(String userEmail);

    //@Insert(onConflict = OnConflictStrategy.REPLACE) void insertAll(post... posts); //add post
    @Insert
    void insert(Post... post); //add post

    @Update
    void update(Post... post); //update post

    @Delete
    void delete(Post post); //delete posts2

    @Query("DELETE FROM Post")
    void clear();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Post> posts);
}
