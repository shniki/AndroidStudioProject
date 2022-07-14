package com.example.androidstudioproject.activities.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.activities.main.intro.WelcomeFragment;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.authentication.AuthenticationViewModel;
import com.example.androidstudioproject.repositories.connection.ConnectionsViewModel;
import com.example.androidstudioproject.repositories.storage.StorageModelFirebase;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    UsersViewModel usersViewModel;
    PostsViewModel postViewModel;
    ConnectionsViewModel connectionsViewModel;
    AuthenticationViewModel authenticationViewModel;
    StorageModelFirebase storageModelFirebase;

    BottomNavigationView bottomNavigationView;
    public static final int CAMERA_PIC_REQUEST = 1337;
    public static final int PICK_PHOTO = 1338;
    int PLACE_PICKER_REQUEST=1;
    public Fragment currentFragment;

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
        storageModelFirebase = new StorageModelFirebase();
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
            this.replaceFragments(WelcomeFragment.class);
            currUser.setLogIn();
            usersViewModel.update(currUser);
        }
        else replaceFragments(FeedFragment.class);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST || requestCode == PICK_PHOTO) {
                Bitmap image = data.getExtras().getParcelable(getString(R.string.data));
                ImageView imageview = (ImageView) findViewById(R.id.frag_addP_iv_p); //sets imageview as the bitmap
                imageview.setImageBitmap(image);
                if( data != null) {
                    Uri selectedUri = data.getData();
                    String[] columns = { MediaStore.Images.Media.DATA,
                            MediaStore.Images.Media.MIME_TYPE };

                    Cursor cursor = getContentResolver().query(selectedUri, columns, null, null, null);
                    cursor.moveToFirst();

                    int pathColumnIndex     = cursor.getColumnIndex( columns[0] );
                    int mimeTypeColumnIndex = cursor.getColumnIndex( columns[1] );

                    String contentPath = cursor.getString(pathColumnIndex);
                    String mimeType    = cursor.getString(mimeTypeColumnIndex);
                    cursor.close();

                    if(mimeType.startsWith("image")) {
                        Bitmap bitmap = data.getExtras().getParcelable("data");
                        //It's an image
                        if(EditDetailsFragment.class.getName().equals(currentFragment.getClass().getName()))
                        {
                            ((EditDetailsFragment)currentFragment).setImage(bitmap);
                        }
                        else if(CreatePostFragment.class.getName().equals(currentFragment.getClass().getName())){
                            ((CreatePostFragment)currentFragment).setImage(bitmap);
                        }
                        //setImage(image);
                    }
                    else if(mimeType.startsWith("video")) {
                        //TODO It's a video
                        if(CreatePostFragment.class.getName().equals(currentFragment.getClass().getName())){
                           // ((CreatePostFragment)currentFragment).setVideo(bitmap);
                        }
                    }
                }
                else {
                    // show error or do nothing
                }
                //convert to URL and save in firebase after save
            }
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

    public StorageModelFirebase getStorageModelFirebase() {
        return storageModelFirebase;
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
}