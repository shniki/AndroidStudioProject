package com.example.androidstudioproject.daos;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject.entities.Comment;

@Dao
public interface CommentDao {
    @Query("select * from Comment") //get all comments
    Comment[] index();

    @Query("select * from Comment where commentID= :commentID") //get a specific comment
    Comment get(String commentID);

    //@Insert(onConflict = OnConflictStrategy.REPLACE) void insertAll(comment... comments); //add comment
    @Insert
    void insert(Comment... comment); //add comment

    @Update
    void update(Comment... comment); //update comment

    @Delete
    void delete(Comment comment); //delete comments
}
