package com.example.profilesave1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.profilesave1.R;

public class SplashActivity extends AppCompatActivity {

    /*
    This activity is the first one to show - the user will see 2 hands creating a heart
     */
    ImageView handleft,handright,heart;
    TextView text1;
    Animation left,right,text;
    public static int SPLASH_SCREEN = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash);

        handleft = findViewById(R.id.lefthandid);
        handright = findViewById(R.id.righthandid);
        heart = findViewById(R.id.heart);
        text1=findViewById(R.id.textViewRegister);

        text = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.text);
        left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left);
        right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right);

        handleft.startAnimation(left);
        handright.startAnimation(right);
        text1.startAnimation(text);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}