package com.example.androidstudioproject.repositories.authentication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.user.UsersRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AutheticationViewModel extends AndroidViewModel {
    
    private AutheticationModelFirebase mRepository;
    
    private FirebaseUser currUser;

    public AutheticationViewModel(Application application) {
        super(application);
        mRepository = new AutheticationModelFirebase();
        currUser = mRepository.getCurrentUser();
    }

    public FirebaseUser getCurrentUser() { return currUser; }
    public String getCurrentEmail() { return currUser.getEmail(); }

    public boolean authenticate(String email, String password){
        Boolean res = mRepository.authenticate(email,password);
        currUser = mRepository.getCurrentUser();
        return res;
    }

    public void add(String email, String password) { mRepository.add(email,password); }
    
    
}
