package com.example.androidstudioproject.repositories.post;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidstudioproject.entities.Post;

import java.util.List;

public class PostsViewModel extends AndroidViewModel {

    private PostsRepository mRepository;

    private LiveData<List<Post>> posts;

    public PostsViewModel(Application application) {
        super(application);
        mRepository = new PostsRepository(application);
        posts = mRepository.getAllPosts();
    }

    public LiveData<List<Post>> getAllPosts() { return posts; }

    public void add(Post post) { mRepository.add(post); }
}
