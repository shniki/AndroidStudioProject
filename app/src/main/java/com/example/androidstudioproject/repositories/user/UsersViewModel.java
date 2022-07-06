package com.example.androidstudioproject.repositories.user;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidstudioproject.entities.User;

import java.util.List;

public class UsersViewModel extends AndroidViewModel {

    private UsersRepository mRepository;

    private LiveData<List<User>> users;

    public UsersViewModel(Application application) {
        super(application);
        mRepository = new UsersRepository(application);
        users = mRepository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() { return users; }

    public void add(User user) { mRepository.add(user); }
}
