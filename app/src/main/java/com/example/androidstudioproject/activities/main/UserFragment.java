package com.example.androidstudioproject.activities.main;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.activities.login.SignUpFragment;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.entities.UserConnections;
import com.example.androidstudioproject.repositories.connection.ConnectionsViewModel;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

    PostsViewModel postsViewModel;
    UsersViewModel usersViewModel;
    ConnectionsViewModel connectionsViewModel;
    String userEmail;


    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String email) {
        UserFragment fragment = new UserFragment();
        fragment.userEmail = email;
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragmentName = this.getClass().getName();
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
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        User user = usersViewModel.getUserByEmail(userEmail);
        CircleImageView profilePic = (CircleImageView) view.findViewById(R.id.profileImg_info);
        profilePic.setImageURI(Uri.parse(user.getProfilePicture()));

        TextView username = (TextView) view.findViewById(R.id.userName_info);
        String text = user.getFirstName() + ' ' + user.getLastName();
        username.setText(text);

        Button followBtn = (Button) view.findViewById(R.id.followBtn_info);
        String loggedInUser = ((MainActivity)this.getActivity()).currEmail;
        if(userEmail.equals(loggedInUser)){
            followBtn.setText(R.string.settings);
            followBtn.setOnClickListener(v -> {
                ((MainActivity) this.getActivity()).replaceFragments(SettingsFragment.class);
            });
        }

        UserConnections isFollowing = connectionsViewModel.getConnectionIfExists(loggedInUser, userEmail);
        UserConnections isFollowed = connectionsViewModel.getConnectionIfExists(userEmail, loggedInUser);
        if(isFollowing != null){
            if(isFollowed != null) {
                followBtn.setText(R.string.match);
            }
            else{
                followBtn.setText(R.string.following);
            }
        }

        else{
            String textAfterClick;
            if(isFollowed != null) {
                followBtn.setText(R.string.follow_back);
                textAfterClick = "Its a Match!";
            }
            else{
                followBtn.setText(R.string.follow_txt);
                textAfterClick = "Following";
            }

            followBtn.setOnClickListener(v -> {
                connectionsViewModel.add(new UserConnections(loggedInUser, userEmail));
            });
            followBtn.setText(textAfterClick);
        }

        TextView moreInfo = (TextView) view.findViewById(R.id.moreInfo_info);
        String gender;
        if(user.getGender() == 0) gender = "Male";
        else gender = "Female";
        String preference;
        if(user.getSexualPreferences() == 0) preference = "Likes men";
        else if(user.getSexualPreferences() == 1) preference = "Likes women";
        else preference = "Likes both";
        String info = user.getAge() + " | " + gender + " | " + preference;
        moreInfo.setText(info);

        TextView bio = (TextView) view.findViewById(R.id.bio_info);
        bio.setText(user.getBio());

        //TODO recycle view
    }
}