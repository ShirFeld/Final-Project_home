package com.example.profilesave1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profilesave1.Models.User;
import com.example.profilesave1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    /*
    This activity is one of a few options to connect our app.
     The user will get a code and if everything will going well he will connects.
     */

    String verificationID;
    TextView textView;
    EditText phone , OTP_input , email , name;
    Button verify_btn , otp_btn;
    FirebaseAuth mAuth;
    String name2 = "";
    FirebaseDatabase db;
    DatabaseReference users;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        init();

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone.getText().toString()) || phone.getText().length() <10)
                    Toast.makeText(PhoneActivity.this, "Pleas enter valid phone number", Toast.LENGTH_SHORT).show();

                else {
                    String number = phone.getText().toString();
                    senderificationacode(number);
                }
            }
        });

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(OTP_input.getText().toString()))
                    Toast.makeText(PhoneActivity.this, "Wrong code entered", Toast.LENGTH_SHORT).show();
                else {
                    verifyCode(OTP_input.getText().toString());
                }
            }
        });
    }
    private void init(){
        textView = findViewById(R.id.enter);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.emailInput);
        name = findViewById(R.id.nameInput);
        OTP_input = findViewById(R.id.OTP_input);
        verify_btn = findViewById(R.id.verify_btn);
        otp_btn = findViewById(R.id.btn2); // get code
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
    }

    private void senderificationacode(String number ) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+972"+number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        Toast.makeText(PhoneActivity.this, "Wait for the code", Toast.LENGTH_LONG).show();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // verification complete
            final String code = credential.getSmsCode();  // firebase send otp -> One-time password
            if (code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId,token);
            verificationID = verificationId;

        }
    };
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code); // getCredential(verificationID,code) --> the user code
        sighInByCredentials(credential);
    }

    private void sighInByCredentials(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            addDetails();
                        }
                    }
                });
    }

    //check if the user is connected or not
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        // if the user is already exist he will pass to the app and he will not see this page
        if (cUser != null) {
            Toast.makeText(this,"User exist!",Toast.LENGTH_SHORT);
            startActivity(new Intent(PhoneActivity.this,FirstPageActivity.class));
            finish();
        }
        else{
            Toast.makeText(this,"waiting",Toast.LENGTH_SHORT).show();
        }
    }

    // create new user
    private void addDetails(){
        final EditText phone2 = phone;
        final EditText email2 = email;
        final EditText name0 = name;
        final String password = phone.getText().toString();
        final String sex = "";
        final String age = "";
        final String city = "";
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
        mAuth.createUserWithEmailAndPassword(email2.getText().toString() , password).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        if (name != null) {
                            String name1 = name0.getText().toString().substring(0, 1).toUpperCase(); // the first letter on uppercase
                            name2 = name1 + name0.getText().toString().substring(1);
                        }

                        User user = new User(name2,email2.getText().toString(),city,phone2.getText().toString(),sex,age,haveAnimals,haveChildren,maritalStatus,favoriteMoviesCategory,
                                favoriteHobby,latitude,longitude,UDurl,aboutMe);
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                //Toast.makeText(PhoneActivity.this, "Login Successful , change your password on the settings page", Toast.LENGTH_SHORT).show();
                                if (mProgressDialog == null) {
                                    mProgressDialog = new ProgressDialog(PhoneActivity.this);
                                    mProgressDialog.setMessage("Login Successful , change your password on the settings page ");
                                    mProgressDialog.setIndeterminate(true);
                                }
                                mProgressDialog.show();
                                startActivity(new Intent(PhoneActivity.this, FirstPageActivity.class));
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PhoneActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();

            }
        });

    }
}