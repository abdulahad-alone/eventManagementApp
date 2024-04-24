package com.example.eventapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser!=null){
                    Intent intent
                            = new Intent(splashScreen.this,
                            vendor_home_activity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent
                            = new Intent(splashScreen.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },1500);
    }
}