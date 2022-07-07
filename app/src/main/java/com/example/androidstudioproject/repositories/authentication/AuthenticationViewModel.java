package com.example.androidstudioproject.repositories.authentication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.auth.FirebaseUser;

public class AuthenticationViewModel extends AndroidViewModel {
    
    private AuthenticationModelFirebase mRepository;
    
    private FirebaseUser currUser;

    public AuthenticationViewModel(Application application) {
        super(application);
        mRepository = new AuthenticationModelFirebase();
        currUser = mRepository.getCurrentUser();
    }

    public FirebaseUser getCurrentUser() {
        currUser = mRepository.getCurrentUser();
        return currUser;
    }

    public boolean isLoggedIn() {return mRepository.isLoggedIn();}

    public String getCurrentEmail() {
        getCurrentUser();
        return currUser.getEmail();
    }

    public boolean authenticate(String email, String password){
        Boolean res = mRepository.authenticate(email,password);
        currUser = mRepository.getCurrentUser();
        return res;
    }

    public void add(String email, String password) { mRepository.add(email,password); }
    
    public void signOut(){
        mRepository.signOut();
    }

    public void changePassword(String password){
        mRepository.changePassword(password);
    }
}
