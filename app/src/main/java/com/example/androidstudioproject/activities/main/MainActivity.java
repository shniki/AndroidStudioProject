package com.example.androidstudioproject.activities.main;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.androidstudioproject.AppDB;
import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.repositories.authentication.AuthenticationViewModel;
import com.example.androidstudioproject.repositories.connection.ConnectionsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.example.androidstudioproject.repositories.post.PostsViewModel;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    UsersViewModel usersViewModel;
    PostsViewModel postViewModel;
    ConnectionsViewModel connectionsViewModel;
    AuthenticationViewModel authenticationViewModel;

    String currEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postViewModel = new PostsViewModel(this.getApplication());
        usersViewModel = new UsersViewModel(this.getApplication());
        connectionsViewModel = new ConnectionsViewModel(this.getApplication());
        authenticationViewModel = new AuthenticationViewModel(this.getApplication());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(!authenticationViewModel.isLoggedIn()){ //not logged in
            gotoLoginActivity();
        }

        currEmail = authenticationViewModel.getCurrentEmail();
    }

    public void gotoLoginActivity(){
        try {
            Intent switchActivityIntent = new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(switchActivityIntent);
        }
        catch(Exception e) {
            Log.d("ERROR", "Error going to login activity with message" + e.getMessage());
        }
    }

    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFragment, fragment)
                .commit();
    }

    public void setLanguage(String language)
    {
        Locale locale = new Locale(language);
        locale.setDefault(locale);
        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    //getters

    public UsersViewModel getUsersViewModel() {
        return usersViewModel;
    }

    public PostsViewModel getPostViewModel() {
        return postViewModel;
    }

    public ConnectionsViewModel getConnectionsViewModel() {
        return connectionsViewModel;
    }

    public AuthenticationViewModel getAuthenticationViewModel() {
        return authenticationViewModel;
    }

    public String getCurrEmail() {
        return currEmail;
    }

    public void gotoPostFragment(long postID) {
        PostFragment fragment = null;
        try {
            fragment = (PostFragment) PostFragment.newInstance(postID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFragment, fragment)
                .commit();
    }

    public void gotoUserFragment(String useremail) {
        UserFragment fragment = null;
        try {
            fragment = (UserFragment) UserFragment.newInstance(useremail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFragment, fragment)
                .commit();
    }
}