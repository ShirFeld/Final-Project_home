package com.example.profilesave1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.profilesave1.Activities.MessageActivity;
import com.example.profilesave1.Activities.MyAdapter2;
import com.example.profilesave1.Models.User;
import com.example.profilesave1.Models.UsersAdapter;
import com.example.profilesave1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class descfragment extends Fragment {

    /*
    This fragment show user details.
    This fragment will happens after refragment.
    Here the user can start a chat with other users.
    */

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    UsersAdapter.OnUserClickListener onUserClickListener;
    private ArrayList<User> users ;
    private ArrayList<User> users2 ;
    String myImageUrl;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String city, name, Url,age ,relationship, haveAnimals;


    public descfragment() {
    }
    public descfragment(String city, String name, String Url, String age, String relationship, String haveAnimals) {
        this.city=city;
        this.name = name;
        this.Url=Url;
        this.age = age;
        this.relationship = relationship;
        this.haveAnimals=haveAnimals;
    }

    public static descfragment newInstance(String param1, String param2) {
        descfragment fragment = new descfragment();
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

        View view=inflater.inflate(R.layout.fragment_descfragment, container, false);

        users = new ArrayList<>();
        users2 = new ArrayList<>();

        ImageView imageholder=view.findViewById(R.id.imagegholder);
        TextView cityholder=view.findViewById(R.id.cityholder);
        TextView ageholder=view.findViewById(R.id.ageholder);
        TextView nameholder=view.findViewById(R.id.nameholder);
        TextView relationshipHolder=view.findViewById(R.id.typeRelationshipHolder);
        TextView haveAnimalsholder=view.findViewById(R.id.animalsHolder);

        Button btn_back = view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btn_chat = view.findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(), MessageActivity.class)
                      .putExtra("username_of_roommate",users.get(MyAdapter2.indexPosition).getName())
                      .putExtra("email_of_roommate",users.get(MyAdapter2.indexPosition).getEmail())
                      .putExtra("img_of_roommate",users.get(MyAdapter2.indexPosition).getUrl())
                      .putExtra("my_img",myImageUrl));
           }
        });
        onUserClickListener = new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(int position) {
                startActivity(new Intent(getActivity(), MessageActivity.class)
                        .putExtra("username_of_roommate",users.get(position).getName())
                        .putExtra("email_of_roommate",users.get(position).getEmail())
                        .putExtra("img_of_roommate",users.get(position).getUrl())
                        .putExtra("my_img",myImageUrl));
            }

        };
        getUsers();

        // titles in layout
        TextView agePlace=view.findViewById(R.id.agePlace);
        TextView haveAnimalsholder1=view.findViewById(R.id.animalsHolder1);
        TextView cityholder1= view.findViewById(R.id.cityholder1);
        TextView typeRelationship1= view.findViewById(R.id.typeRelationship1);
        TextView Nameholder1= view.findViewById(R.id.Nameholder1);

        // show user's data
        cityholder.setText(city);
        ageholder.setText(age);
        nameholder.setText(name);
        Glide.with(getContext()).load(Url).into(imageholder); // url
        relationshipHolder.setText(relationship);
        haveAnimalsholder.setText(haveAnimals);

        // if there are no data in the fields so we dont want to show the the titles and the context
        if(haveAnimalsholder.getText().toString().equals("")){
            haveAnimalsholder.setVisibility(View.GONE);
            haveAnimalsholder1.setVisibility(View.GONE);
        }
        if(ageholder.getText().toString().equals("")){
            ageholder.setVisibility(View.GONE);
            agePlace.setVisibility(View.GONE);
        }
        if(cityholder.getText().toString().equals("")){
            cityholder.setVisibility(View.GONE);
            cityholder1.setVisibility(View.GONE);
        }
        if(relationshipHolder.getText().toString().equals("")){
            relationshipHolder.setVisibility(View.GONE);
            typeRelationship1.setVisibility(View.GONE);
        }
        if(nameholder.getText().toString().equals("")){
            nameholder.setVisibility(View.GONE);
            Nameholder1.setVisibility(View.GONE);
        }
        return  view;
    }


    public void onBackPressed(){
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new recfragment()).addToBackStack(null).commit();
    }

    private void getUsers(){
        users.clear();
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    // check to show all the users beside the current user.
                    String uuid = dataSnapshot.getKey();
                    if (!mAuth.getCurrentUser().getUid().equals(uuid))
                        users.add(dataSnapshot.getValue(User.class));  // this list will be shown
                    users2.add(dataSnapshot.getValue(User.class));     // this is not
                }
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
}