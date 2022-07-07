package com.example.androidstudioproject.repositories.authentication;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.androidstudioproject.AppDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class AuthenticationModelFirebase {

    private FirebaseAuth mAuth;

    private FirebaseUser currUser;

    public AuthenticationModelFirebase() {
        mAuth = FirebaseAuth.getInstance();
        currUser = null;
    }

    public FirebaseUser getCurrentUser() {
        updateUI(mAuth.getCurrentUser());
        return currUser;
    }

    public boolean isLoggedIn() {
        return (mAuth.getCurrentUser() != null);
    }

    public void updateUI(FirebaseUser user) {
        currUser = user;
    }

    public Boolean authenticate(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails
                            updateUI(null);
                        }
                    }
                });
        return (mAuth.getCurrentUser() != null);
    }

    public void add(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails
                            updateUI(null);
                        }
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }

    public void changePassword(String password) {
        getCurrentUser().updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //successfully
                        }
                    }
                });
    }
}
