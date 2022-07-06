package com.example.androidstudioproject.repositories.post;

import android.util.Log;


import com.example.androidstudioproject.entities.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class PostModelFirebase {

    private static final String TAG = "MyTag";
    public static PostModelFirebase instance = new PostModelFirebase();

    private PostModelFirebase(){}

    public void addPost(Post post){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("Posts");
        ref.child(ref.push().getKey()).setValue(post);
    }

    public void cancellGetAllPosts() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        stRef.removeEventListener(eventListener);
    }

    interface GetAllPostsListener{
        public void onSuccess(List<Post> postslist);
    }

    ValueEventListener eventListener;

    public void getAllPosts(final GetAllPostsListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        Log.d(TAG, "Getting from firebase");

        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> stList = new LinkedList<>();

                Log.d(TAG, "Back from firebase");

                for (DataSnapshot stSnapshot: dataSnapshot.getChildren()) {
                    Post st = stSnapshot.getValue(Post.class);
                    stList.add(st);
                }

                Log.d(TAG, stList.size() + " from firebase");

                listener.onSuccess(stList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
