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

    public Post getPostById(long postID) { return mRepository.getPostById(postID); }

    public List<Post> getPostsByUser(String userEmail) { return mRepository.getUserPosts(userEmail); }

    public void add(Post post) { mRepository.add(post); }
}
