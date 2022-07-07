package com.example.androidstudioproject.activities.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;

public class PostFragment extends Fragment {

    PostsViewModel postsViewModel;
    UsersViewModel usersViewModel;
    long postID;


    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(long postID) {
        PostFragment fragment = new PostFragment();

        fragment.postID=postID;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        postsViewModel = ((MainActivity)getActivity()).getPostViewModel();
        usersViewModel = ((MainActivity)getActivity()).getUsersViewModel();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_add_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {}
}