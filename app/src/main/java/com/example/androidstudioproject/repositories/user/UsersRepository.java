package com.example.androidstudioproject.repositories.user;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidstudioproject.AppDB;
import com.example.androidstudioproject.daos.UserDao;
import com.example.androidstudioproject.entities.User;

import java.util.LinkedList;
import java.util.List;

public class UsersRepository {
    private static final String TAG = "MyTag";
    private UserDao dao;
    private UserListData userListData;

    public UsersRepository(Application application) {
        AppDB db = AppDB.getInstance(application);
        dao = db.userDao();
        userListData = new UserListData();
    }

    public User getUserByEmail(String email){
        List<User> users = dao.index();
        for(User user : users) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    public void cancellGetAllUsers() {
        UserModelFirebase.instance.cancellGetAllUsers();
    }

    public void update(User user) {
        dao.update(user);
    }

    class UserListData extends MutableLiveData<List<User>> {

        @Override
        protected void onActive() {
            super.onActive();

            Log.d("MyTag", "OnActive");

            new UsersAsyncTask().execute(new Runnable() {
                @Override
                public void run() {

                    List<User> s = dao.index();
                    Log.d(TAG, s.size() + " users in the database");
                    postValue(s);

                    UserModelFirebase.instance.getAllUsers(new UserModelFirebase.GetAllUsersListener() {
                        @Override
                        public void onSuccess(final List<User> usersList) {

                            Log.d(TAG, usersList.size() + " users from firebase");

                            setValue(usersList);

                            new UsersAsyncTask().execute(new Runnable() {
                                @Override
                                public void run() {
                                    dao.clear();
                                    dao.insertAll(usersList);
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();

            Log.d("MyTag", "OnInActive");

            cancellGetAllUsers();
        }

        public UserListData() {
            super();
            setValue(new LinkedList<User>());
            this.onActive();
        }
    }

    public LiveData<List<User>> getAllUsers() {
        return userListData;
    }

    public void add (final User user) {
        new UsersAsyncTask().execute(new Runnable() {
            @Override
            public void run() {
                UserModelFirebase.instance.addUser(user);
            }
        });
    }

    private static class UsersAsyncTask extends AsyncTask<Runnable, Void, Void> {

        @Override
        protected Void doInBackground(final Runnable... params) {
            params[0].run();
            return null;
        }
    }
}

