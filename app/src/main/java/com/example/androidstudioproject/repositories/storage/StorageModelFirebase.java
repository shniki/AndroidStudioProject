package com.example.androidstudioproject.repositories.storage;

import android.app.Application;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.main.CreatePostFragment;
import com.example.androidstudioproject.activities.main.EditDetailsFragment;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StorageModelFirebase {

    FirebaseStorage storage;
    Application application;

    private final Lock lock = new ReentrantLock(true);


    public StorageModelFirebase(Application application) {
        storage = FirebaseStorage.getInstance();
        this.application =application;
    }

    public void addImageAndUploadPost(CreatePostFragment fragment, Bitmap image, Post p) {
        addImage(image).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri == null) {
                        Snackbar.make(fragment.getView(), R.string.media_upload_failed, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        p.setDataURL(downloadUri.toString());
                        ((MainActivity)fragment.getActivity()).getPostViewModel().add(p);
                        Snackbar.make(fragment.getView(), R.string.post_finish_upload, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                else{
                    Snackbar.make(fragment.getView(), R.string.media_upload_failed, Snackbar.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    public void addImageAndUpdateUser(EditDetailsFragment fragment, Bitmap image, User user) {
        addImage(image).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri == null) {
                        Snackbar.make(fragment.getView(), R.string.media_upload_failed, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        user.setProfilePicture(downloadUri.toString());
                        ((MainActivity)fragment.getActivity()).getUsersViewModel().update(user);
                        Snackbar.make(fragment.getView(), R.string.updated_successfully, Snackbar.LENGTH_LONG).show();
                    }
                }
                else{
                    Snackbar.make(fragment.getView(), R.string.media_upload_failed, Snackbar.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    public void addVideoAndUploadPost(CreatePostFragment fragment, Uri video, Post p) {
        addVideo(video).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri == null) {
                        Snackbar.make(fragment.getView(), R.string.media_upload_failed, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        p.setDataURL(downloadUri.toString());
                        ((MainActivity)fragment.getActivity()).getPostViewModel().add(p);
                        Snackbar.make(fragment.getView(), R.string.post_finish_upload, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                else{
                    Snackbar.make(fragment.getView(), R.string.media_upload_failed, Snackbar.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }


    public Task<Uri> addImage(Bitmap image){
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

        //uri = downloadTaskUri.getResult();


        return downloadTaskUri;
    }

    private String getExt(Uri uri){
        ContentResolver contentResolver = application.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public Task<Uri> addVideo(Uri video){
        //gets a uri

        //uploads to firebase
        String path = "tindergarm/" + UUID.randomUUID() + "." + getExt(video);
        StorageReference storageRef = storage.getReference(path);

        UploadTask uploadTask = storageRef.putFile(video);

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

        //uri = downloadTaskUri.getResult();


        return downloadTaskUri;
    }


    public boolean isVaild(String uri){
        return (uri!=null);
    }

}
