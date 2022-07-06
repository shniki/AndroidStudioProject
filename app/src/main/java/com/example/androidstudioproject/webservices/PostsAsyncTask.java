package com.example.androidstudioproject.webservices;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.androidstudioproject.MyApplication;
import com.example.androidstudioproject.R;
import com.example.androidstudioproject.daos.PostDao;
import com.example.androidstudioproject.entities.Post;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PostsAsyncTask extends AsyncTask<Void, Void, Void>
{
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;

    public PostsAsyncTask(MutableLiveData<List<Post>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(Void... urls)
    {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(MyApplication.context.getString(R.string.PostsUrl));
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                result.append(line);
            }

            dao.clear();

            JSONArray posts = new JSONArray(result.toString());

            for (int i = 0; i < posts.length(); i++) {
                Post post = new Gson().fromJson(String.valueOf(posts.getJSONObject(i)), Post.class);
                post.setPic(R.drawable.pic1);
                dao.insert(post);
            }

            postListData.postValue(dao.get());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        return null;
    }
}