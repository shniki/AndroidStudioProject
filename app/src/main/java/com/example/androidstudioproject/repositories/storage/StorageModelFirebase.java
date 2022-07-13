package com.example.androidstudioproject.repositories.storage;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.androidstudioproject.activities.main.MainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class StorageModelFirebase {

    FirebaseStorage storage;


    public StorageModelFirebase() {
        storage = FirebaseStorage.getInstance();
    }

    public String addImage(Bitmap image){
        //gets a bitmap
        //compress it
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        //uploads to firebase
        String path = "tindergarm/" + UUID.randomUUID() + ".png";
        StorageReference storageRef = storage.getReference(path);

        UploadTask uploadTask = storageRef.putBytes(data);

        //returns its URI (as string)
        Uri uri;
        Task<Uri> downloadTaskUri = uploadTask.continueWithTask(
                new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                            return null;
                        return storageRef.getDownloadUrl();
                    }
                }
        );

        uri = downloadTaskUri.getResult();

        return uri.toString();
    }

    public boolean isVaild(String uri){
        return (uri!=null);
    }

    //TODO: video
    public String addVideo(/**/){
        return null;
    }
}
