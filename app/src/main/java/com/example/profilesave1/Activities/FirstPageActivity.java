package com.example.profilesave1.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.profilesave1.Models.model;
import com.example.profilesave1.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstPageActivity extends AppCompatActivity {
    /*
    This is the main activity. All the fragments will be placed here.
    The menu is declare here.
     */

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout); // The user can view the navigation drawer when they swipe a finger from the left edge of the activity.


        // the access to menu button
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // gives us the option to see the menu on left
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this,R.id.wrapper); // fragment
        NavigationUI.setupWithNavController(navigationView,navController); // navigationView --> the menu on left

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        if (user.getUid() != null)
            userID = user.getUid();


        reference.child(userID).addValueEventListener(new ValueEventListener() { // connection to db
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model modelProfile = snapshot.getValue(model.class);

                // User details from database
                if(modelProfile != null){
                    String fullName = modelProfile.getName();
                    String url = modelProfile.getUrl();

                    // The view of user details (name & pic)
                    final TextView currName= findViewById(R.id.headername);
                    final ImageView currImg= findViewById(R.id.imageProfile);

                    // The photo and the name are going to be shown when u press the menu button
                    currName.setText(fullName);
                    Glide.with(FirstPageActivity.this).load(url).into(currImg);
                }
            }
            // Checks if there is no data
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FirstPageActivity.this,"wrong,wrong,wrong",Toast.LENGTH_SHORT);
            }
        });
    }

}