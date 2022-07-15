package com.example.androidstudioproject.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.main.FeedFragment;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    UsersViewModel usersViewModel;
    private List<Post> postsList;
    MainActivity context;

    private ItemClickListener listener;
    public interface ItemClickListener{
        void onClick(int i);//create function in the feed activity
    }
    //@NonNull
    //?
    public FeedAdapter(MainActivity context)
    {
        usersViewModel=context.getUsersViewModel();
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (postsList != null) {
            Post post = postsList.get(position);
            User user=usersViewModel.getUserByEmail(post.getUserEmail());
        String text = user.getFirstName() + context.getString(R.string.spaceChar) + user.getLastName();
        holder.userName.setText(text);

        holder.date.setText(post.getPostDate());
        holder.description.setText(post.getContent());
        if(post.getDataType()==1)
        {
            Glide.with(context).load(post.getDataURL()).into(holder.image);

            holder.image.setVisibility(View.VISIBLE);
        }
        else
            holder.image.setVisibility(View.GONE);
        //TODO holder.location.setText(post.getLocation());
        if(post.getDataType()==2)
        {
            holder.video.setVideoURI(Uri.parse(post.getDataURL()));
            holder.video.setVisibility(View.VISIBLE);
        }
        else {
            //holder.video.setVisibility(View.GONE);
        }

        if(!user.getProfilePicture().equals(""))
            Glide.with(context).load(user.getProfilePicture()).into(holder.userProfile);
        else
            holder.userProfile.setImageResource(R.drawable.ic_profile);


        holder.location.setText(post.getLocation());


        //click to fragment
        holder.image.setOnClickListener(v->{
            gotoPostFragment(post.getPostID());
        });

//            holder.video.setOnClickListener(v->{
//                gotoPostFragment(post.getPostID());
//            });

        holder.description.setOnClickListener(v->{
            gotoPostFragment(post.getPostID());
        });

        holder.userProfile.setOnClickListener(v->{
            gotoUserFragment(post.getUserEmail());
        });

        holder.userName.setOnClickListener(v->{
            gotoUserFragment(post.getUserEmail());
        });
        }
    }

    private void gotoPostFragment(long postid){
        context.gotoPostFragment(postid);
    }

    private void gotoUserFragment(String useremail){
        context.gotoUserFragment(useremail);
    }

    @Override
    public int getItemCount() {
        if(postsList!=null)
            return postsList.size();
        return 0;
    }

    public void setPostsList(List<Post> postsList) {
        this.postsList = postsList;
        Collections.reverse(this.postsList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        TextView location;
        TextView description;
        TextView date;
        ImageView userProfile;
        ImageView image;
        VideoView video;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.userName);
            location=itemView.findViewById((R.id.location));
            date=itemView.findViewById((R.id.date));
            description=itemView.findViewById((R.id.description));
            userProfile=itemView.findViewById((R.id.userProfilePost));
            image=itemView.findViewById((R.id.postImage));

            //TODO video=itemView.findViewById((R.id.^^^));

        }
    }
}
