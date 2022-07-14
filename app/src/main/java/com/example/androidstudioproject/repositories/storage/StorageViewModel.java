package com.example.androidstudioproject.repositories.storage;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidstudioproject.activities.main.CreatePostFragment;
import com.example.androidstudioproject.activities.main.EditDetailsFragment;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;

public class StorageViewModel extends AndroidViewModel {

    StorageModelFirebase mRepository;

    public StorageViewModel(@NonNull Application application) {
        super(application);
        mRepository = new StorageModelFirebase();
    }

    public void addImageAndUploadPost(CreatePostFragment fragment, Bitmap image, Post p) {
        mRepository.addImageAndUploadPost(fragment,image,p);
    }

    public  void addImageAndUpdateUser(EditDetailsFragment fragment, Bitmap image, User user){
        mRepository.addImageAndUpdateUser( fragment, image, user);
    }

    public boolean isVaild(String uri) {
        return isVaild(uri);
    }
}
