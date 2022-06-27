package com.example.androidstudioproject.daos;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject.entities.UserConnections;

@Dao
public interface ConnectionDao {

    @Query("select * from UserConnections where userId = :email or secondUserId = :email") //get a specific user's connections
    UserConnections get(String email);

    //@Insert(onConflict = OnConflictStrategy.REPLACE) void insertAll(user... users); //add user
    @Insert
    void insert(UserConnections... connection); //add connection

    @Delete
    void delete(UserConnections connection); //delete connection
}
