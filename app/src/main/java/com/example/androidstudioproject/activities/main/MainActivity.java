package com.example.androidstudioproject.activities.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.activities.main.intro.WelcomeFragment;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.authentication.AuthenticationViewModel;
import com.example.androidstudioproject.repositories.connection.ConnectionsViewModel;
import com.example.androidstudioproject.repositories.storage.StorageViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    UsersViewModel usersViewModel;
    PostsViewModel postViewModel;
    ConnectionsViewModel connectionsViewModel;
    AuthenticationViewModel authenticationViewModel;
    StorageViewModel storageViewModel;

    BottomNavigationView bottomNavigationView;
    public static final int CAMERA_PIC_REQUEST = 1337;
    public static final int PICK_PHOTO = 1338;
    int PLACE_PICKER_REQUEST=1;
    private String imgPath;
    public Fragment currentFragment;

    public static SharedPreferences settings;

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
        storageViewModel = new StorageViewModel(this.getApplication());
        settings = getSharedPreferences("UserInfo", 0);
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
        if(currUser != null && !currUser.getHasLoggedIn() && currentFragment==null){
            this.replaceFragments(WelcomeFragment.class);
            currUser.setLogIn();
            usersViewModel.update(currUser);
        }
        else if(currentFragment==null)
            replaceFragments(FeedFragment.class);
    }

    public void gotoLoginActivity(){
        try {
            Intent switchActivityIntent = new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(switchActivityIntent);
        }
        catch(Exception e) {
            Log.d(getString(R.string.errorType), getString(R.string.gotoLoginErrorMsg) + e.getMessage());
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_search:
                            if(!SearchFragment.class.getName().equals(currentFragment.getClass().getName()))
                                replaceFragments(SearchFragment.class);
                            break;
                        case R.id.nav_createP:
                            if(!CreatePostFragment.class.getName().equals(currentFragment.getClass().getName()))
                                replaceFragments(CreatePostFragment.class);
                            break;
                        case R.id.nav_home:
                            if(!FeedFragment.class.getName().equals(currentFragment.getClass().getName()))
                                replaceFragments(FeedFragment.class);
                            break;
                        case R.id.nav_setting:
                            if(!SettingsFragment.class.getName().equals(currentFragment.getClass().getName()))
                                replaceFragments(SettingsFragment.class);
                            break;
                        case R.id.nav_profile:
                            if(!UserFragment.class.getName().equals(currentFragment.getClass().getName()))
                                gotoUserFragment(currEmail);
                            else if(UserFragment.class.getName().equals(currentFragment.getClass().getName())
                                    &&!((UserFragment)currentFragment).userEmail.equals(currEmail))
                                gotoUserFragment(currEmail);
                            break;
                    }
                    return true;
                }
            };

    public void openCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    public Boolean isNightModeOn() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

    public String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }

    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

//        if(isNightModeOn())
//            replaceFragments(SettingsFragment.class);

    }

    private Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + UUID.randomUUID() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public void pickImageFromGallery(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType(getString(R.string.imageType));

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(getString(R.string.imageType));

        Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.selectImage));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_PHOTO);
    }

    public Boolean isRelevantPost(Post post, int sexualPreference){
        int gender = usersViewModel.getUserByEmail(post.getUserEmail()).getGender();
        if((sexualPreference == 1 && gender == 0) || (sexualPreference == 0 && gender == 1))
            return false;
        return true;
    }

    public List<Post> getAllRelevantPosts(int sexualPreference){
        List<Post> updatedPosts = postViewModel.getAllPosts().getValue();
        updatedPosts.removeIf(p -> !isRelevantPost(p, sexualPreference));
        return updatedPosts;
    }

    public void pickMediaFromGallery(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/* video/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select media");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_PHOTO);
    }


    private void setImageToActivity(Bitmap bitmap){
        if(EditDetailsFragment.class.getName().equals(currentFragment.getClass().getName()))
        {
            ((EditDetailsFragment)currentFragment).setImage(bitmap);
        }
        else if(CreatePostFragment.class.getName().equals(currentFragment.getClass().getName())){
            ((CreatePostFragment)currentFragment).setImage(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == CAMERA_PIC_REQUEST){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                setImageToActivity(image);
                return;
            }
            else if(requestCode == PICK_PHOTO){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap image = BitmapFactory.decodeStream(imageStream);

                    setImageToActivity(image);
                    return;
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }//todo video
            else if(requestCode==PLACE_PICKER_REQUEST)
            {
//                Place place= PlacePicker.getPlace(data,this);
//                if(CreatePostFragment.class.getName().equals(currentFragment.getClass().getName()))
//                {
//                    ((CreatePostFragment)currentFragment).setLocation(place.getAddress().toString());
//                }
            }
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
                .addToBackStack(null)
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
        onConfigurationChanged(config);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.settings != null && MainActivity.settings.getBoolean("isDarkMode", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        if(MainActivity.settings != null && MainActivity.settings.getBoolean("isRomanian", false)){
            setLanguage(getString(R.string.romanian));
        }
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

    public StorageViewModel getStorageViewModel() {
        return storageViewModel;
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
                    .addToBackStack(null)
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
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }
}