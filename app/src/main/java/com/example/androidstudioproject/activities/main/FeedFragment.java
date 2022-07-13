package com.example.androidstudioproject.activities.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.adapters.FeedAdapter;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;

import java.util.List;

public class FeedFragment extends Fragment {

    PostsViewModel postsViewModel;
    UsersViewModel usersViewModel;

    FeedAdapter adapter;

    public static FeedFragment newInstance() {
        FeedFragment frag = new FeedFragment();

        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postsViewModel = ((MainActivity)getActivity()).getPostViewModel();
        usersViewModel = ((MainActivity)getActivity()).getUsersViewModel();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragment = this;

        //update info

        User currUser = usersViewModel.getUserByEmail(((MainActivity)this.getActivity()).currEmail);
        adapter.setPostsList(((MainActivity)this.getActivity()).getAllRelevantPosts(currUser.getSexualPreferences()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_feed, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView rvFeed = view.findViewById(R.id.rvFeed); //get recycler-view by id
        adapter = new FeedAdapter((MainActivity) this.getActivity()); //create adapter
        rvFeed.setAdapter(adapter); //set adapter
        //choose type of layout: linear, horological or staggered
        rvFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
        SwipeRefreshLayout swipeToRefresh=view.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setPostsList(postsViewModel.getAllPosts().getValue());
            }
        }
  );
        //TODO ON REFRESH
//        postsViewModel.getAllPosts().observe(this.getActivity(), new Observer<List<Post>>() {
//            @Override
//            public void onChanged(@Nullable final List<Post> posts) {
//                // Update the cached copy of the words in the adapter.
//                adapter.setPostsList(posts);
//            }
//        });
    }


}