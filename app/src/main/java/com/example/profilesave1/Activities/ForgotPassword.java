package com.example.profilesave1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.profilesave1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    /*
    This activity is for those who forgot their password and want to set a new one.
     */
    Button reset , back;
    ProgressBar progress;
    EditText enter_email;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
        progress.setVisibility(View.INVISIBLE);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButton();
            }
        });
    }

    public void init(){
        enter_email = findViewById(R.id.enter_email);
        reset = findViewById(R.id.reset_btn);
        back = findViewById(R.id.back);
        progress = findViewById(R.id.progress);
        auth = FirebaseAuth.getInstance();
    }

    private void resetButton() {
        String email = enter_email.getText().toString().trim();  // remove spaces if we have

        // the user didnt write an email address
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ForgotPassword.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
            enter_email.setError("Email is required!");
            enter_email.requestFocus();
        }

        // check if the mail is valid
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(ForgotPassword.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            enter_email.setError("valid Email is required");
            enter_email.requestFocus();
            return;
        }
        // all good
        else {
            progress.setVisibility(View.VISIBLE);
            resetPassword(email);
        }
    }
    private void resetPassword(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Please check your inbox for password reset", Toast.LENGTH_LONG).show();
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else
                    Toast.makeText(ForgotPassword.this, "Something is wrong", Toast.LENGTH_SHORT).show();

                progress.setVisibility(View.GONE);
            }
        });
    }

}

