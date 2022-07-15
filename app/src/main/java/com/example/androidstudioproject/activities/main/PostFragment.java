package com.example.androidstudioproject.activities.main;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.androidstudioproject.R;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.connection.ConnectionsViewModel;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.google.android.material.snackbar.Snackbar;

public class PostFragment extends Fragment {
    private static final int SMS_REQUEST_ID = 123;
    PostsViewModel postsViewModel;
    UsersViewModel usersViewModel;
    ConnectionsViewModel connectionsViewModel;
    long postID;
    String curremail;
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
        if(post.getContent().equals(getString(R.string.emptyString)))
            txtDesc.setVisibility(View.GONE);
        else
            txtDesc.setVisibility(View.VISIBLE);
        txtDesc.setText(post.getContent());

        User user = usersViewModel.getUserByEmail(post.getUserEmail());

        //TODO imgProfile.setImageResource(user.getProfilePicture());
        String text = user.getFirstName()+getString(R.string.spaceChar)+user.getLastName();
        txtUserName.setText(text);

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
         curremail = ((MainActivity)getActivity()).getCurrEmail();


        imgProfile = view.findViewById(R.id.userProfilePost_post);//userProfilePost_post
        if(!user.getProfilePicture().equals(""));
            Glide.with(getContext()).load(user.getProfilePicture()).into(imgProfile);

        txtUserName = view.findViewById(R.id.userName_post);//userName_post
        String text = user.getFirstName()+getString(R.string.spaceChar)+user.getLastName();
        txtUserName.setText(text);

        txtLocation = view.findViewById(R.id.location_post);//location_post
        //TODO txtLocation.setText(post.getLocation());

        image = view.findViewById(R.id.postImage_post);//postImage_post
        if (post.getDataType()==1)//picture
        {
            image.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(user.getProfilePicture()).into(image);
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
        if(post.getContent().equals(getString(R.string.emptyString)))
            txtDesc.setVisibility(View.GONE);
        else
            txtDesc.setVisibility(View.VISIBLE);
        txtDesc.setText(post.getContent());

        txtDate = view.findViewById(R.id.date_post);//date_post
        txtDate.setText(post.getPostDate());


        //visible only when follows you
        edtDescMsg = view.findViewById(R.id.textMoveEdit_post);//textMoveEdit_post
         btnSend = view.findViewById(R.id.sendMessage);//sendMessage
        //send btn functionality
        btnSend.setOnClickListener(v->{
            sendSms(view);
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
            if(post.getDataType() == 0 && TextUtils.isEmpty(strNewDesc))
            {
                Snackbar.make(view, R.string.empty_content, Snackbar.LENGTH_LONG).show();
                return;
            }
            post.setContent(strNewDesc);
            postsViewModel.update(post);

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
    public void sendSms(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(getActivity(),Manifest.permission.SEND_SMS) == PermissionChecker.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_REQUEST_ID);
            }
        } else {
            sendSMS();
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_REQUEST_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void sendSMS() {
        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(usersViewModel.getUserByEmail(curremail).getPhoneNumber(), null,edtDescMsg.getText().toString() , null, null);// pendingIntent, pendingIntent2);
    }
}