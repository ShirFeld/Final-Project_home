package com.example.profilesave1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.profilesave1.Activities.MessageActivity;
import com.example.profilesave1.Models.Message;
import com.example.profilesave1.Models.User;
import com.example.profilesave1.Models.UsersAdapter;
import com.example.profilesave1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class ChatFragment extends Fragment {

    /*
    This fragment displays a list of users and when any user is clicked a chat room will open
     */

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ArrayList<User> users2;
    private ArrayList<Message > messages;


    private ProgressBar progressBar;
    private UsersAdapter usersAdapter;
    private SearchView searchUser;
    UsersAdapter.OnUserClickListener onUserClickListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    String myImageUrl;


    public ChatFragment() {
        // Required empty public constructor
    }
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progreeBar);
        users = new ArrayList<>();
        users2 = new ArrayList<>();
        messages = new ArrayList<>();
        searchUser = view.findViewById(R.id.searchUser);

        recyclerView = view.findViewById(R.id.recycler);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               getUsers("");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                getUsers(newText);
                return false;
            }
        });

        // pass the information to the message
        onUserClickListener = new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(int position) { // user details are pass to message activity
                startActivity(new Intent(getActivity(), MessageActivity.class)
                        .putExtra("username_of_roommate", users.get(position).getName())
                        .putExtra("longitude" ,users.get(position).getLongitude())
                        .putExtra("Latitude" ,users.get(position).getLatitude())
                        .putExtra("email_of_roommate", users.get(position).getEmail())
                        .putExtra("img_of_roommate", users.get(position).getUrl())
                        .putExtra("my_img", myImageUrl));
            }
        };
     getUsers("") ;
   //  getMessages();





     return view;
    }

    // here the user can search for a person
    private void getUsers(String s) {

        // All names are saved in the database with the first letter capitalized.
        // So the search will be done when the first letter is capitalized even if the user enters a lowercase .

        if (!s.equals("")){
            String name1 = s.substring(0, 1).toUpperCase(); // the first letter on uppercase
            s = name1 + s.substring(1);
        }
        users.clear();
        FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("name").startAt(s).endAt(s+"\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    // check to show all the users beside the current user.
                    String uuid = dataSnapshot.getKey();
                    if (!mAuth.getCurrentUser().getUid().equals(uuid))
                        users.add(dataSnapshot.getValue(User.class));  // this list will be shown
                    users2.add(dataSnapshot.getValue(User.class));     // this is not
                }
                usersAdapter = new UsersAdapter(users,getActivity(),onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(usersAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                searchUser.setVisibility(View.VISIBLE);

                // this func will show the photo of the current user in the message room
                for(User user :users2){
                    if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())){
                        myImageUrl = user.getUrl();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMessages() {

        FirebaseDatabase.getInstance().getReference().child("messages").child("Admin AdDana levi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getChildren().toString() + " children");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String name = dataSnapshot.child("receiver").getValue().toString();
//                  messages.add(dataSnapshot.getValue(Message.class));
                    System.out.println(name + " !!!!!!!!!!!!!!!!!!!");

                }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    // take all the users and put them on the recyclerView.
//    private void getUsers2(){
//        users.clear();
//        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    // check to show all the users beside the current user.
//                    String uuid = dataSnapshot.getKey();
//                    if (!mAuth.getCurrentUser().getUid().equals(uuid))
//                        users.add(dataSnapshot.getValue(User.class));  // this list will be shown
//                    users2.add(dataSnapshot.getValue(User.class));     // this is not
//                }
//                usersAdapter = new UsersAdapter(users,getActivity(),onUserClickListener);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                recyclerView.setAdapter(usersAdapter);
//                progressBar.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//                searchUser.setVisibility(View.VISIBLE);
//
//                // this func will show the photo of the current user in the message room
//                for(User user :users2){
//                    if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())){
//                        myImageUrl = user.getUrl();
//                        return;
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
}