package com.example.androidstudioproject.repositories.authentication;


import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.example.androidstudioproject.MyApplication;
import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.activities.login.LoginFragment;
import com.example.androidstudioproject.activities.login.SignUpFragment;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.example.androidstudioproject.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

public class AuthenticationModelFirebase {

    private FirebaseAuth mAuth;

    Executor executor;

    private FirebaseUser currUser;

    public AuthenticationModelFirebase(Executor executor) {
        mAuth = FirebaseAuth.getInstance();
        currUser = null;
        this.executor=executor;
    }

    public FirebaseUser getCurrentUser() {
        FirebaseUser curr = mAuth.getCurrentUser();
        updateUI(mAuth.getCurrentUser());
        return currUser;
    }

    public boolean isLoggedIn() {
        return (mAuth.getCurrentUser() != null);
    }

    public void updateUI(FirebaseUser user) {
        currUser = user;
    }

    public void authenticate(LoginFragment fragment, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(executor, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            ((LoginActivity)fragment.getActivity()).gotoMainActivity();
                        } else {
                            // If sign in fails
                            updateUI(null);
                            Snackbar.make(fragment.getView(), R.string.wrong_input, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void add(SignUpFragment fragment, User u, String password) {
        mAuth.createUserWithEmailAndPassword(u.getEmail(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference ref = mDatabase.child("Users");
                        ref.child(String.valueOf(u.getEmail().hashCode())).setValue(u).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        currUser = mAuth.getCurrentUser();
                                        ((LoginActivity)fragment.getActivity()).gotoMainActivity();
                                    } else {
                                        Snackbar.make(fragment.getView(), R.string.failed_signup, Snackbar.LENGTH_LONG).show();
                                        return;
                                    }
                                });
                    } else {
                        Snackbar.make(fragment.getView(), R.string.failed_signup, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                });
    }

    public void googleLogin(Activity activity, GoogleSignInAccount account){
        if (account.getIdToken() !=  null) {
            // Got an ID token from Google. Use it to authenticate
            // with Firebase.
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(executor, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        ((LoginActivity)activity).gotoMainActivity();
                    } else {
                        // If sign in fails
                        updateUI(null);
                    }
                }
            });
        }
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
