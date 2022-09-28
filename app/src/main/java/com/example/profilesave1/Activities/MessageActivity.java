package com.example.profilesave1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.profilesave1.Fragments.ChatFragment;
import com.example.profilesave1.Models.Message;
import com.example.profilesave1.Models.User;
import com.example.profilesave1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {  // this class is the room message

    /*
    This activity represents the message.
    How the chatRoom will call.
     */

    private RecyclerView recyclerView;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;
    private ImageView imgToolBar,imgSend, back;

    private MessageAdapter messageAdapter;
    private ArrayList<Message> messages;

    String usernameOfTheRoommate,emailOfRoommate,chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        usernameOfTheRoommate = getIntent().getStringExtra("username_of_roommate");
        emailOfRoommate = getIntent().getStringExtra("email_of_roommate");

        recyclerView = findViewById(R.id.recyclerMessages);
        edtMessageInput = findViewById(R.id.edtText);
        txtChattingWith = findViewById(R.id.txtChattingWith); // the name of the other person
        progressBar = findViewById(R.id.progreeBar);
        imgToolBar = findViewById(R.id.img_toolbar);
        imgSend = findViewById(R.id.imgSend);
        back = findViewById(R.id.back);

        txtChattingWith.setText(usernameOfTheRoommate);

        messages = new ArrayList<>();
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("messages/"+ chatRoomId ).push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail()
                        ,emailOfRoommate,edtMessageInput.getText().toString()));
                edtMessageInput.setText("");
            }
        });
        messageAdapter = new MessageAdapter(messages,getIntent().getStringExtra("my_img"),getIntent().getStringExtra("img_of_roommate"),MessageActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("img_of_roommate")).placeholder(R.drawable.no_avatar).error(R.drawable.no_avatar).into(imgToolBar);

        setUpChatRoom();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
    }


    // this method creates room name of 2 users in the firebase
    private void setUpChatRoom(){
        FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername ="";
                if (snapshot.getValue(User.class) != null)
                     myUsername = snapshot.getValue(User.class).getName();

                if (usernameOfTheRoommate.compareTo(myUsername)>0)
                    chatRoomId = myUsername +usernameOfTheRoommate;

                else if(usernameOfTheRoommate.compareTo(myUsername) == 0)
                    chatRoomId = myUsername +usernameOfTheRoommate;

                else chatRoomId = usernameOfTheRoommate + myUsername;

                attachMessageListener(chatRoomId);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // this method shows all the messages between 2 users
    private void attachMessageListener(String chatRoomId){
        FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue(Message.class));     // shows us all the messages
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}