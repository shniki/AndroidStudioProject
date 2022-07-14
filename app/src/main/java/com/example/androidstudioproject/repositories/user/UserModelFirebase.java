package com.example.androidstudioproject.repositories.user;

import android.util.Log;

import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class UserModelFirebase {

    private static final String TAG = "MyTag";
    public static UserModelFirebase instance = new UserModelFirebase();

    private UserModelFirebase(){}

    public void addUser(User user){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("Users");
        ref.child(String.valueOf(user.getEmail().hashCode())).setValue(user);
    }

    public void updateUser(User user){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("Users");

//        ref
//                .orderByChild("email")
//                .equalTo(user.getEmail())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                            String clubkey = childSnapshot.getKey();
        String key = String.valueOf(user.getEmail().hashCode());
        ref.child(key).setValue(user);
    }

    public void cancellGetAllUsers() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Users");
        stRef.removeEventListener(eventListener);
    }

    interface GetAllUsersListener{
        public void onSuccess(List<User> userslist);
    }

    ValueEventListener eventListener;

    public void getAllUsers(final GetAllUsersListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Log.d(TAG, "Getting from firebase");

        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> stList = new LinkedList<>();

                Log.d(TAG, "Back from firebase");

                for (DataSnapshot stSnapshot: dataSnapshot.getChildren()) {
                    User st = stSnapshot.getValue(User.class);
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
