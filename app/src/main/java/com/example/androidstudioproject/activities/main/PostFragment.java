package com.example.androidstudioproject.activities.main;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
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
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.connection.ConnectionsViewModel;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.google.android.material.snackbar.Snackbar;

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

    //visible only when follows you
    EditText edtDescMsg;//textMoveEdit_post
    Button btnSend;//sendMessage
    //send btn fuctionality

    //visible only
    Button btnEdit;//editPost_post
    Button btnDone; //buttonDone
    Button btnCancel;//buttonCancel
    EditText edtDesc;//textDescriptionEdit_post
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
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragment = this;

        //update info
        Post post = postsViewModel.getPostById(postID);
        if(post.getContent().equals(""))
            txtDesc.setVisibility(View.GONE);
        else
            txtDesc.setVisibility(View.VISIBLE);
        txtDesc.setText(post.getContent());

        User user = usersViewModel.getUserByEmail(post.getUserEmail());

        //TODO imgProfile.setImageResource(user.getProfilePicture());

        txtUserName.setText(user.getFirstName()+" "+user.getLastName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //get post + user
        Post post = postsViewModel.getPostById(postID);
        User user = usersViewModel.getUserByEmail(post.getUserEmail());
        String curremail = ((MainActivity)getActivity()).getCurrEmail();


        imgProfile = view.findViewById(R.id.userProfilePost_post);//userProfilePost_post
        imgProfile.setImageURI(Uri.parse(user.getProfilePicture()));

        txtUserName = view.findViewById(R.id.userName_post);//userName_post
        txtUserName.setText(user.getFirstName()+" "+user.getLastName());

        txtLocation = view.findViewById(R.id.location_post);//location_post
        //TODO txtLocation.setText(post.getLocation());

        image = view.findViewById(R.id.postImage_post);//postImage_post
        if (post.getDataType()==1)//picture
        {
            image.setVisibility(View.VISIBLE);
            image.setImageURI(Uri.parse(post.getDataURL()));
        }
        else
        {
            image.setVisibility(View.GONE);
        }

        video = view.findViewById(R.id.postImage_post);//postImage_post
        if (post.getDataType()==2)//picture
        {
            video.setVisibility(View.VISIBLE);
            video.setVideoURI(Uri.parse(post.getDataURL()));
        }
        else
        {
            video.setVisibility(View.GONE);
        }

        txtDesc = view.findViewById(R.id.description_post);//description_post
        if(post.getContent().equals(""))
            txtDesc.setVisibility(View.GONE);
        else
            txtDesc.setVisibility(View.VISIBLE);
        txtDesc.setText(post.getContent());

        txtDate = view.findViewById(R.id.date_post);//date_post
        txtDate.setText(post.getPostDate());


        //visible only when follows you
        edtDescMsg = view.findViewById(R.id.textMoveEdit_post);//textMoveEdit_post
         btnSend = view.findViewById(R.id.sendMessage);//sendMessage
        //send btn fuctionality
        btnSend.setOnClickListener(v->{
            //TODO chuck do send message
        });

        //visible only
         btnEdit = view.findViewById(R.id.editPost_post);//editPost_post
         btnDone = view.findViewById(R.id.buttonDone); //buttonDone
         btnCancel = view.findViewById(R.id.buttonCancel); //buttonCancel
         edtDesc = view.findViewById(R.id.textDescriptionEdit_post);//textDescriptionEdit_post

        btnDelete = view.findViewById(R.id.deletePost_post);//deletePost_post

        // DELETE & EDIT glide in edit.
        btnEdit.setOnClickListener(v -> {
            //make textbox appear
            edtDesc.setVisibility(View.VISIBLE);
            edtDesc.setText(post.getContent());
            btnDone.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        });
        btnDone.setOnClickListener(v->{
            if(edtDesc== null) //validate input
            {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }

            String strNewDesc = edtDesc.getText().toString();
            post.setContent(strNewDesc);
            //postsViewModel.update(post); //TODO yaniv's update+delete in firebase viewmodel

            edtDesc.setVisibility(View.GONE);
            btnDone.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        });
        btnCancel.setOnClickListener(v->{
            edtDesc.setVisibility(View.GONE);
            btnDone.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        });
        btnDelete.setOnClickListener(v->{
            postsViewModel.delete(post);
            ((MainActivity)this.getActivity()).onBackPressed();
        });

        View layout = view.findViewById(R.id.linearLayout2);
        if(post.getUserEmail().equals(curremail)) {
            //layout appear
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            //layout gone
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        if(connectionsViewModel.getConnectionIfExists(post.getUserEmail(), curremail) != null){
            //make send message visible
            edtDescMsg.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.VISIBLE);
        }
        else
        {
            edtDescMsg.setVisibility(View.GONE);
            btnSend.setVisibility(View.GONE);
        }


    }
}