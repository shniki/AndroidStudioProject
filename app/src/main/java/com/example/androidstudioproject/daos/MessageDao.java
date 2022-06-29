package com.example.androidstudioproject.daos;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject.entities.Message;

@Dao
public interface MessageDao {
    @Query("select * from Message") //get all messages
    Message[] index();

    @Query("select * from Message where messageId= :messageId") //get a specific message
    Message get(String messageId);

    //@Insert(onConflict = OnConflictStrategy.REPLACE) void insertAll(message... messages); //add message
    @Insert
    void insert(Message... message); //add message

    @Update
    void update(Message... message); //update message

    @Delete
    void delete(Message message); //delete messages
}
