package com.example.androidstudioproject.activities.main;

import static com.example.androidstudioproject.MyApplication.context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.SelectListener;
import com.example.androidstudioproject.adapters.UserAdapter;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment implements SelectListener {
    UsersViewModel usersViewModel;
    List<User> usersFilteredList;
    RecyclerView listUsers;
    SearchView searchView;
    UserAdapter adapter;
    String searchString;

    public SearchFragment() {
        // Required empty public constructor
        usersFilteredList=new ArrayList<>();
    }
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         adapter = new UserAdapter(usersFilteredList,this,(MainActivity) getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        usersViewModel=((MainActivity)getActivity()).getUsersViewModel();
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       searchView = view.findViewById(R.id.searchView);
       RecyclerView.LayoutManager layoutManager=new LinearLayoutManager((MainActivity) getActivity());
       listUsers=view.findViewById(R.id.rvSearch);
       listUsers.setLayoutManager(layoutManager);
       listUsers.setAdapter(adapter);

     /* usersViewModel.getAllUsers().observe(this.getActivity(), new Observer<List<User>>() {
          @Override
            public void onChanged(@Nullable final List<User> users) {
                // Update the cached copy of the words in the adapter.
                adapter.setUsersList(users);
              // filter(searchString);
            }
        });*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s != null) {
                    filter(s);
                    setStringSearch(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                if (text != null) {
                   filter(text);
                  // setStringSearch(text);
                }
                else
                    adapter.setUsersList(null);
                return false;
            }
        });
    }
    private void setStringSearch(String searchString) {this.searchString=searchString;}

    public void filter(String text) {
        List<User> usersList=usersViewModel.getAllUsers().getValue();
        usersFilteredList.clear();
        for (int i = 0; i < usersList.size(); i++) {
            String userName_ =usersList.get(i).getFirstName() + getString(R.string.spaceChar) + usersList.get(i).getLastName();
            if (userName_.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                usersFilteredList.add(usersList.get(i));
            }
        }
        if(usersFilteredList.isEmpty())
            Toast.makeText(this.getActivity(),"NO USERS FOUND",Toast.LENGTH_SHORT).show();

            adapter.setUsersList(usersFilteredList);
    }

    @Override
    public void onItemClicked(User userSelected) {
        ((MainActivity)getActivity()).gotoUserFragment(userSelected.getEmail());

    }
}