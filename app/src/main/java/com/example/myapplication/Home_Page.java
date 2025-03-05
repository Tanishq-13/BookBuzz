package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Home_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ImageView gifImageView = findViewById(R.id.gifImageView);
        gifImageView.setImageResource(R.drawable.iiituanim);
        long delayDuration = 5000;

        // Create a handler to post a delayed action
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the new activity
                startActivity(new Intent(Home_Page.this, authentication.class));
                // Finish the current activity to prevent the user from returning to it using the back button
                finish();
            }
        }, delayDuration);
    }
}