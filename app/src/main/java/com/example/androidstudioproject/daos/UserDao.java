package com.example.androidstudioproject.daos;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject.entities.User;

@Dao
public interface UserDao {
    @Query("select * from User") //get all users
    User[] index();

    @Query("select * from User where email= :email") //get a specific user
    User get(String email);

    //@Insert(onConflict = OnConflictStrategy.REPLACE) void insertAll(user... users); //add user
    @Insert
    void insert(User... user); //add user

    @Update
    void update(User... user); //update user

    @Delete
    void delete(User user); //delete users
}
