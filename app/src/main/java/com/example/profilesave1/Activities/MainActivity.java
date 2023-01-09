package com.example.profilesave1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profilesave1.Models.User;
import com.example.profilesave1.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /*
    This activity is the second page (after the splash).
    Here we have 4 ways to connect our app (signIn , register , phone , google).
     */

    Button btnSignIn,btnRegister ;
    TextView forgot_btn;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference users;
    RelativeLayout root;
    ImageView googleBtn;

    String uid ="";
    String google_email ="";
    String name2 ="";

    //Sign in with google
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN= 22;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhoneActivity.class);
                startActivity(intent);
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRequest();
                signIn();
            }
        });

        // button of signIn
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInWindow();
            }
        });
        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init(){
        forgot_btn = findViewById(R.id.forgot_btn);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnRegister = findViewById(R.id.btn_registry);
        googleBtn = findViewById(R.id.google_btn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        root = findViewById(R.id.root_element);

    }


    //google - 3 methods
    private void createRequest() {
        // Configure Google Sing in , we create the request here (to GoogleSignInClient)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso --> here the request will sent to google
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    // this func will happened only if the user is click on the button --> in the intent the user will select the user he wants to enter with
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        //Result returned from launching the intent from GoogleSignInAccountFromIntent(data)
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google sign in was successful
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google sign in failed
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null); // תעודה = credential

        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = mAuth.getCurrentUser();
                uid = user.getUid();
                google_email = user.getEmail();

                // check if user is new or existing
                if (authResult.getAdditionalUserInfo().isNewUser()){
                    // user is new
                    Snackbar.make(findViewById(R.id.google_btn),"user is new",Snackbar.LENGTH_SHORT).show();
                    addDetails();
                }
                else
                    Snackbar.make(findViewById(R.id.google_btn),"This user is already sign in.Press on Sign in button",Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    // create new user using google
    private void addDetails() {
        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Connects to Google...");
        progressDialog.show();

        // gives the details of google account
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);


        if (signInAccount != null) {
           // System.out.println(signInAccount.getEmail() + "Email - &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            final String phone = "";
            final String city ="";
            final String email = google_email;
            final String name = signInAccount.getDisplayName();
            final String password = signInAccount.getId();
            final String sex = "";
            final String age = "";
            final String haveAnimals = "";
            final String haveChildren = "";
            final String maritalStatus = "";
            final String favoriteMoviesCategory = "";
            final String favoriteHobby = "";
            final String latitude = "";
            final String longitude = "";
            final String UDurl = "";
            final String aboutMe = "";



            //register user if pass authentication
            mAuth.createUserWithEmailAndPassword(email,password).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (name != null){
                                String name1 = name.substring(0, 1).toUpperCase(); // the first letter on uppercase
                                name2 = name1 + name.substring(1);
                            }

                            User user = new User(name2, email,city, phone, sex, age, haveAnimals, haveChildren, maritalStatus, favoriteMoviesCategory, favoriteHobby,latitude, longitude, UDurl,aboutMe);
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, "Login Successful , change your password on the settings page", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, FirstPageActivity.class));
                                }
                            });
                        }
                    });


        }

    }


    //if the user is already connect he will pass to FirstPageActivity
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();

        // if the user is already exist he will pass to the app and he will not see this page
        if (cUser != null) {
            Toast.makeText(this,"User exist!",Toast.LENGTH_SHORT);
            startActivity(new Intent(MainActivity.this,FirstPageActivity.class));
            finish();
        }
        else{
            Toast.makeText(this,"User not exist",Toast.LENGTH_SHORT).show();
        }
    }

    // Button of signIn
    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this,R.style.MyDialogStyle);
        dialog.setTitle("Enter your account");
        dialog.setMessage("Enter your email and password");
        LayoutInflater inflater = LayoutInflater.from(this);
        View signinWindow = inflater.inflate(R.layout.sign_in_window,null);
        dialog.setView(signinWindow);
        onStart();


        final MaterialEditText email = signinWindow.findViewById(R.id.emailField);
        final MaterialEditText password = signinWindow.findViewById(R.id.passwordField);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root,"Enter your email",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(password.getText().toString().length() <=5){
                    Snackbar.make(root,"Your password must be more then 5 chars",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(MainActivity.this,FirstPageActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root,"Authorization failed. " + e.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();

    }

}