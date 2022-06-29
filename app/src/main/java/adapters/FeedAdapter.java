package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.entities.Post;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

List<Post> postsList;

    //@NonNull
    //?
    public FeedAdapter(List<Post> posts)
    {
        postsList=posts;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=postsList.get(position);

        holder.userName.setText(post.getUserName());
        holder.date.setText(post.getPostDate());
        holder.description.setText(post.getContent());
        //holder.location.setText(post.getLocation());
        holder.image.setImageBitmap(post.getPicture());
      //  holder.userProfile.setImageBitmap(post.getUserName());

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        TextView location;
        TextView description;
        TextView date;
        ImageView userProfile;
        ImageView image;
        ImageButton sendMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.userName);
            location=itemView.findViewById((R.id.location));
            date=itemView.findViewById((R.id.date));
            description=itemView.findViewById((R.id.description));
            userProfile=itemView.findViewById((R.id.userProfilePost));
            sendMessage=itemView.findViewById((R.id.sendMessage));
            image=itemView.findViewById((R.id.postImage));




        }
    }
}
