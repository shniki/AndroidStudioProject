package com.example.androidstudioproject.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidstudioproject.R;
import com.example.androidstudioproject.SelectListener;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.example.androidstudioproject.entities.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> usersList;
    private SelectListener listener;
    MainActivity context;

    public UserAdapter(List<User> usersList, SelectListener listener, MainActivity context) {
        this.usersList = usersList;
        this.listener=listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }
    public void setUsersList(List<User> users_List) {
        this.usersList = users_List;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final User user=usersList.get(position);
        holder.userName.setText(user.getFirstName()+" "+user.getLastName());
        //holder.userProfile.setImageURI(Uri.parse(user.getProfilePicture()));
        String text = user.getFirstName()+context.getString(R.string.spaceChar)+user.getLastName();
        holder.userName.setText(text);
        if(!user.getProfilePicture().equals(""))
            Glide.with(context).load(user.getProfilePicture()).into(holder.userProfile);
        else
            holder.userProfile.setImageResource(R.drawable.ic_profile);

        //holder.userProfile.setImageBitmap(user.getProfilePicture());
       // Glide.with()- image profile
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(usersList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         TextView userName;
         CircleImageView userProfile;
         CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_name_search_item);
            userProfile=itemView.findViewById(R.id.user_profile_item);
            cardView=itemView.findViewById(R.id.main_container);
        }
    }
}
