package com.example.androidstudioproject.activities.main;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.activities.login.SignUpFragment;
import com.example.androidstudioproject.adapters.FeedAdapter;
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
    FeedAdapter adapter;

    CircleImageView profilePic;
    Button followBtn;
    TextView username;
    TextView moreInfo;
    TextView bio;



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
        ((MainActivity)this.getActivity()).currentFragment = this;


        //reset info
        User user = usersViewModel.getUserByEmail(userEmail);

        profilePic.setImageURI(Uri.parse(user.getProfilePicture()));

        String text = user.getFirstName() + getString(R.string.spaceChar) + user.getLastName();
        username.setText(text);

        setFollowBtn();

        setMoreInfo(user);

        bio.setText(user.getBio());

        adapter.setPostsList(postsViewModel.getPostsByUser(userEmail));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postsViewModel = ((MainActivity)getActivity()).getPostViewModel();
        usersViewModel = ((MainActivity)getActivity()).getUsersViewModel();
        connectionsViewModel = ((MainActivity)getActivity()).getConnectionsViewModel();

        adapter = new FeedAdapter((MainActivity) this.getActivity()); //create adapter
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        profilePic = (CircleImageView) view.findViewById(R.id.profileImg_info);

        username = (TextView) view.findViewById(R.id.userName_info);

        followBtn = (Button) view.findViewById(R.id.followBtn_info);

        moreInfo = (TextView) view.findViewById(R.id.moreInfo_info);

        bio = (TextView) view.findViewById(R.id.bio_info);

        //recycle view
        RecyclerView rvFeed = view.findViewById(R.id.rvUserFeed); //get recycler-view by id
        rvFeed.setAdapter(adapter); //set adapter
        //choose type of layout: linear, horological or staggered
        rvFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setFollowBtn(){
        String loggedInUser = ((MainActivity)this.getActivity()).currEmail;
        if(userEmail.equals(loggedInUser)){
            followBtn.setText(R.string.edit_profile);
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
                textAfterClick = getString(R.string.match);
            }
            else{
                followBtn.setText(R.string.follow_txt);
                textAfterClick = getString(R.string.following);
            }

            followBtn.setOnClickListener(v -> {
                connectionsViewModel.add(new UserConnections(loggedInUser, userEmail));
            });
            followBtn.setText(textAfterClick);
        }

    }

    private void setMoreInfo(User user){

        //TODO non static
        String gender;
        if(user.getGender() == 0) gender = getString(R.string.male);
        else gender = getString(R.string.female);
        String preference;
        if(user.getSexualPreferences() == 0) preference = getString(R.string.malePreference);
        else if(user.getSexualPreferences() == 1) preference = getString(R.string.femalePreference);
        else preference = getString(R.string.bothPreference);
        String info = user.getAge() + getString(R.string.separator) + gender + getString(R.string.separator) + preference;
        moreInfo.setText(info);
    }
}