package com.example.androidstudioproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.androidstudioproject.daos.CommentDao;
import com.example.androidstudioproject.daos.ConnectionDao;
import com.example.androidstudioproject.daos.MessageDao;
import com.example.androidstudioproject.daos.PostDao;
import com.example.androidstudioproject.daos.UserDao;
import com.example.androidstudioproject.entities.Comment;
import com.example.androidstudioproject.entities.Message;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.entities.UserConnections;

@Database(entities = {User.class, Post.class, Message.class, Comment.class, UserConnections.class}, version = 1)
public abstract class AppDB extends RoomDatabase{
    public abstract UserDao userDao();
    public abstract PostDao postDao();
    public abstract MessageDao messageDao();
    public abstract CommentDao commentDao();
    public abstract ConnectionDao connectionDao();
}
