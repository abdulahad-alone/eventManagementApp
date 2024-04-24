package com.example.eventapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class addServices extends AppCompatActivity {
EditText addingService;
String servicename;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_services_activity);
        addingService=findViewById(R.id.adding_service);

        AppCompatButton done=findViewById(R.id.done_);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicename=addingService.getText().toString();
                returnToMainActivity();
            }
        });

    }
    public void returnToMainActivity() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("services", servicename);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}