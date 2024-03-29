package com.example.androidstudioproject.repositories.authentication;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.example.androidstudioproject.activities.login.LoginFragment;
import com.example.androidstudioproject.activities.login.SignUpFragment;
import com.example.androidstudioproject.entities.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthenticationViewModel extends AndroidViewModel {
    
    private final AuthenticationModelFirebase mRepository;


    private FirebaseUser currUser;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public AuthenticationViewModel(Application application) {
        super(application);
        mRepository = new AuthenticationModelFirebase(application.getMainExecutor());
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

    public void authenticate(LoginFragment fragment, String email, String password){
        mRepository.authenticate(fragment, email,password);
        currUser = mRepository.getCurrentUser();
    }

    public void googleLogin(Activity activity, GoogleSignInAccount account){
        mRepository.googleLogin(activity,account);
    }

    public void add(SignUpFragment fragment, User u, String password) { mRepository.add(fragment, u,password); }
    
    public void signOut(){
        mRepository.signOut();
    }

    public void changePassword(Fragment fragment, String password){
        mRepository.changePassword(fragment, password);
    }
}
