package com.example.androidstudioproject.activities.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.adapters.FeedAdapter;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;

import java.util.List;

public class FeedFragment extends Fragment {

    PostsViewModel postsViewModel;
    UsersViewModel usersViewModel;

    public static FeedFragment newInstance() {
        FeedFragment frag = new FeedFragment();

//        Bundle b = new Bundle();
//        b.putSerializable("data", data);
//        b.putInt("position", position);
//        frag.setArguments(b);

        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postsViewModel = ((MainActivity)getActivity()).getPostViewModel();
        usersViewModel = ((MainActivity)getActivity()).getUsersViewModel();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_feed, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView rvFeed = view.findViewById(R.id.rvFeed); //get recycler-view by id
        final FeedAdapter adapter = new FeedAdapter(this); //create adapter
        rvFeed.setAdapter(adapter); //set adapter
        //choose type of layout: linear, horological or staggered
        rvFeed.setLayoutManager(new LinearLayoutManager(getActivity()));

        postsViewModel.getAllPosts().observe(this.getActivity(), new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable final List<Post> posts) {
                // Update the cached copy of the words in the adapter.
                adapter.setPostsList(posts);
            }
        });
    }

    public UsersViewModel getUsersViewModel() {
        return usersViewModel;
    }
}