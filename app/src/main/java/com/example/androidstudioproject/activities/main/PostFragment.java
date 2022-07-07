package com.example.androidstudioproject.activities.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.repositories.connection.ConnectionsViewModel;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;

public class PostFragment extends Fragment {

    PostsViewModel postsViewModel;
    UsersViewModel usersViewModel;
    ConnectionsViewModel connectionsViewModel;
    long postID;

    ImageView imgProfile;//userProfilePost_post
    TextView txtUserName;//userName_post
    TextView txtLocation;//location_post
    ImageView image;//postImage_post
    VideoView video;//postImage_post
    TextView txtDesc;//description_post
    TextView txtDate;//date_post

    EditText edtDesc;//textMoveEdit_post
    Button btnSend;//sendMessage

    Button btnEdit;//editPost_post
    Button btnDelete;//deletePost_post

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
        connectionsViewModel = ((MainActivity)getActivity()).getConnectionsViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        //TODO DELETE & EDIT glide in edit.

        Post post = postsViewModel.get(postID);
        String curremail = ((MainActivity)getActivity()).getCurrEmail();

        View layout = view.findViewById(R.id.linearLayout2);
        if(post.getUserEmail().equals(curremail)) {
            //TODO layout appear
        }
        else
        {
            //layout gone
        }

        if(connectionsViewModel.contains(post.getUserEmail(),curremail)){
            //TODO make send message visible
        }


    }
}