package com.example.androidstudioproject.activities.main;

import static java.lang.Thread.sleep;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidstudioproject.R;
import com.example.androidstudioproject.adapters.FeedAdapter;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.entities.UserConnections;
import com.example.androidstudioproject.repositories.connection.ConnectionsViewModel;
import com.example.androidstudioproject.repositories.post.PostsViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.google.firebase.database.annotations.Nullable;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

    PostsViewModel postsViewModel;
    UsersViewModel usersViewModel;
    ConnectionsViewModel connectionsViewModel;
    String userEmail;
    String loggedInUser;
    FeedAdapter adapter;

    ImageView profilePic;
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

        //profilePic.setImageURI(Uri.parse(user.getProfilePicture()));
//        ((MainActivity)getActivity()).getStorageViewModel().
//                getImageAndSetInView(user.getProfilePicture(),profilePic);
        if(!user.getProfilePicture().equals(""))
            Glide.with(getContext()).load(user.getProfilePicture()).into(profilePic);
        else
            profilePic.setImageResource(R.drawable.ic_profile);


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

        loggedInUser = ((MainActivity)this.getActivity()).currEmail;

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



        connectionsViewModel.getAllConnections().observe(this.getActivity(), new Observer<List<UserConnections>>() {
            @Override
            public void onChanged(@Nullable final List<UserConnections> connections) {
                // Update the cached copy of the words in the adapter.
                if(!loggedInUser.equals(userEmail)) {
                    setButton();
                }
            }
        });
    }

    private void setFollowBtn(){
        if(userEmail.equals(loggedInUser)){
            followBtn.setText(R.string.edit_profile);
            followBtn.setOnClickListener(v -> {
                ((MainActivity) this.getActivity()).replaceFragments(EditDetailsFragment.class);
            });
        }
        else {
            //setButton();

            followBtn.setOnClickListener(v -> {
                UserConnections isFollowing = connectionsViewModel.getConnectionIfExists(loggedInUser, userEmail);

                if(isFollowing == null)
                    connectionsViewModel.add(new UserConnections(loggedInUser, userEmail));
                else //!=null
                    connectionsViewModel.delete(new UserConnections(loggedInUser, userEmail));

            });

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

    private void setButton(){

        UserConnections isFollowing = connectionsViewModel.getConnectionIfExists(loggedInUser, userEmail);
        UserConnections isFollowed = connectionsViewModel.getConnectionIfExists(userEmail, loggedInUser);

        if (isFollowing != null) {
            if (isFollowed != null) {
                followBtn.setText(R.string.match);
            } else {
                followBtn.setText(R.string.following);
            }
        }
        else {
            if (isFollowed != null) {
                followBtn.setText(R.string.follow_back);
            } else {
                followBtn.setText(R.string.follow_txt);
            }


        }
    }
}