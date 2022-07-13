package com.example.androidstudioproject.activities.main;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.androidstudioproject.AppDB;
import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.activities.main.intro.IntroFragment;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.authentication.AuthenticationViewModel;
import com.example.androidstudioproject.repositories.connection.ConnectionsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    UsersViewModel usersViewModel;
    PostsViewModel postViewModel;
    ConnectionsViewModel connectionsViewModel;
    AuthenticationViewModel authenticationViewModel;
    BottomNavigationView bottomNavigationView;
    public static final int CAMERA_PIC_REQUEST = 1337;
    public static final int PICK_PHOTO = 1338;
    public String currentFragmentName;

    public String currEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        postViewModel = new PostsViewModel(this.getApplication());
        usersViewModel = new UsersViewModel(this.getApplication());
        connectionsViewModel = new ConnectionsViewModel(this.getApplication());
        authenticationViewModel = new AuthenticationViewModel(this.getApplication());


 //TODO use navigation to get to :
        //user
        //feed -
                //single post
                    //edit
                    //delete
                    //send message
                //glide video + photo
        //create
                //goto pictures
                //goto camera
                //goto location
                //intent back
        //search - NOY
                //on change mvvm
        //settings DONE
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(!authenticationViewModel.isLoggedIn()){ //not logged in
            gotoLoginActivity();
        }

        currEmail = authenticationViewModel.getCurrentEmail();
        User currUser = usersViewModel.getUserByEmail(currEmail);
        if(currUser != null && !currUser.getHasLoggedIn()){
            this.replaceFragments(IntroFragment.class);
            currUser.setLogIn();
            usersViewModel.update(currUser);
        }
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

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_search:
                            if(!SearchFragment.class.getName().equals(currentFragmentName))
                                replaceFragments(SearchFragment.class);
                            break;
                        case R.id.nav_createP:
                            if(!CreatePostFragment.class.getName().equals(currentFragmentName))
                                replaceFragments(CreatePostFragment.class);
                            break;
                        case R.id.nav_home:
                            if(!FeedFragment.class.getName().equals(currentFragmentName))
                                replaceFragments(FeedFragment.class);
                            break;
                        case R.id.nav_profile:
                            if(!UserFragment.class.getName().equals(currentFragmentName))
                                replaceFragments(UserFragment.class);
                            break;
                    }
                    return true;
                }
            };

    public void openCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    public void pickImageFromGallery(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST || requestCode == PICK_PHOTO) {
                Bitmap image = data.getExtras().getParcelable("data");
                ImageView imageview = (ImageView) findViewById(R.id.frag_addP_iv_p); //sets imageview as the bitmap
                imageview.setImageBitmap(image);
            }
        }
        //todo: convert to URL and save in fireabse
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
        if(postViewModel.getPostById(postID)==null)
        {
            Snackbar.make(getWindow().getDecorView().getRootView(), R.string.deleted_post, Snackbar.LENGTH_LONG).show();
            //REFRESH
            return;
        }
        else {
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