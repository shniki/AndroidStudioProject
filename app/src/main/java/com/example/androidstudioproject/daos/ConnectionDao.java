package com.example.androidstudioproject.daos;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.entities.UserConnections;

import java.util.List;

@Dao
public interface ConnectionDao {
    @Query("select * from UserConnections") //get all posts
    List<UserConnections> index();

    @Query("select * from UserConnections where userId = :email or secondUserId = :email") //get a specific user's connections
    UserConnections get(String email);

    //@Insert(onConflict = OnConflictStrategy.REPLACE) void insertAll(user... users); //add user
    @Insert
    void insert(UserConnections... connection); //add connection

    @Delete
    void delete(UserConnections connection); //delete connection

    @Query("DELETE FROM UserConnections")
    void clear();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserConnections> connections);
}
