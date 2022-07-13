package com.example.androidstudioproject.repositories.storage;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class StorageViewModel extends AndroidViewModel {

    StorageModelFirebase mRepository;

    public StorageViewModel(@NonNull Application application) {
        super(application);
        mRepository = new StorageModelFirebase();
    }

    public String addImage(Bitmap image) {
        return mRepository.addImage(image);
    }

    public boolean isVaild(String uri) {
        return isVaild(uri);
    }
}
