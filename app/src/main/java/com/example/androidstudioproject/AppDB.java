package com.example.androidstudioproject;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.androidstudioproject.daos.ConnectionDao;
import com.example.androidstudioproject.daos.PostDao;
import com.example.androidstudioproject.daos.UserDao;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.entities.UserConnections;

@Database(entities = {User.class, Post.class, UserConnections.class}, version = 2, exportSchema = false)
public abstract class AppDB extends RoomDatabase{

    private static AppDB instance;

    public abstract UserDao userDao();
    public abstract PostDao postDao();
    public abstract ConnectionDao connectionDao();

    public static AppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDB.class,
                    "mydatabase").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
