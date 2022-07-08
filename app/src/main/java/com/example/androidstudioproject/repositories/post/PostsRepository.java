package com.example.androidstudioproject.repositories.post;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidstudioproject.AppDB;
import com.example.androidstudioproject.daos.PostDao;
import com.example.androidstudioproject.entities.Post;

import java.util.LinkedList;
import java.util.List;

public class PostsRepository {
    private static final String TAG = "MyTag";
    private PostDao dao;
    private PostListData postListData;

    public PostsRepository(Application application) {
        AppDB db = AppDB.getInstance(application);
        dao = db.postDao();
        postListData = new PostListData();
    }

    public Post getPostById(long postID){
        return dao.get(postID);
    }

    public List<Post> getUserPosts(String userEmail){
        return dao.getByUser(userEmail);
    }

    public void cancellGetAllPosts() {
        PostModelFirebase.instance.cancellGetAllPosts();
    }

    class PostListData extends MutableLiveData<List<Post>> {

        @Override
        protected void onActive() {
            super.onActive();

            Log.d("MyTag", "OnActive");

            new PostsRepository.PostsAsyncTask().execute(new Runnable() {
                @Override
                public void run() {

                    List<Post> s = dao.index();
                    Log.d(TAG, s.size() + " posts in the database");
                    postValue(s);

                    PostModelFirebase.instance.getAllPosts(new PostModelFirebase.GetAllPostsListener() {
                        @Override
                        public void onSuccess(final List<Post> postsList) {

                            Log.d(TAG, postsList.size() + " posts from firebase");

                            setValue(postsList);

                            new PostsRepository.PostsAsyncTask().execute(new Runnable() {
                                @Override
                                public void run() {
                                    dao.clear();
                                    dao.insertAll(postsList);
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();

            Log.d("MyTag", "OnInActive");

            cancellGetAllPosts();
        }

        public PostListData() {
            super();
            setValue(new LinkedList<Post>());
        }
    }

    public LiveData<List<Post>> getAllPosts() {
        return postListData;
    }

    public void add (final Post post) {
        new PostsAsyncTask().execute(new Runnable() {
            @Override
            public void run() {
                PostModelFirebase.instance.addPost(post);
            }
        });
    }

    private static class PostsAsyncTask extends AsyncTask<Runnable, Void, Void> {

        @Override
        protected Void doInBackground(final Runnable... params) {
            params[0].run();
            return null;
        }
    }
}

