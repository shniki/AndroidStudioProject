package com.example.androidstudioproject.repositories.connection;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.entities.UserConnections;

import java.util.List;

public class ConnectionsViewModel extends AndroidViewModel {

    private ConnectionsRepository mRepository;

    private LiveData<List<UserConnections>> connections;

    public ConnectionsViewModel(Application application) {
        super(application);
        mRepository = new ConnectionsRepository(application);
        connections = mRepository.getAllConnections();
    }

    public LiveData<List<UserConnections>> getAllUsers() { return connections; }

    public void add(UserConnections connection) { mRepository.add(connection); }
}
