package com.example.eventapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class MainActivity extends AppCompatActivity {
TextView vendortxt, usertxt;
    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private TextView justlogin_,sign_up;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vendortxt=findViewById(R.id.textVendor);
        usertxt=findViewById(R.id.textUser);

       // FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        sign_up =findViewById(R.id.create_acc);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);
        progressbar = findViewById(R.id.progressBar);
        justlogin_=findViewById(R.id.justlogin);

      Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(emailChecker(emailTextView.getText().toString().trim())){
                    emailTextView.setError(null);
                    if(!passwordTextView.getText().toString().isEmpty()){
                        emailTextView.setError(null);
                        if(emailTextView.getText().toString().trim().equals("admin@gmail.com")&& passwordTextView.getText().toString().equals("123456")){
                            Intent intent=new Intent(MainActivity.this,vendor_home_activity.class);
                            startActivity(intent);
                            finish();
                        }else {

                           loginUserAccount();

                        }
                    }else{
                        passwordTextView.setError("invalid Password");
                    }
                }else{
                    emailTextView.setError("invalid email");
                }


            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(MainActivity.this,
                        signup.class);
                startActivity(intent);
            }
        });





        vendortxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendortxt.setTextColor(getResources().getColor(R.color.white));
                vendortxt.setBackgroundResource(R.drawable.button_round_bg);
                usertxt.setTextColor(getResources().getColor(R.color.black));
                usertxt.setBackgroundResource(R.drawable.button_round_bg_grey);
            }
        });
        usertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usertxt.setTextColor(getResources().getColor(R.color.white));
                usertxt.setBackgroundResource(R.drawable.button_round_bg);
                vendortxt.setTextColor(getResources().getColor(R.color.black));
                vendortxt.setBackgroundResource(R.drawable.button_round_bg_grey);
            }
        });
    }

    private void loginUserAccount() {
// show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");
        Query query = usersCollection.whereEqualTo("email", email);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Check if there are any documents that match the query
                if (!task.getResult().isEmpty()) {
                    logIn(email,password);
                    Log.d(TAG, "Email exists in 'users' collection");
                } else {
                    // Email does not exist in the 'users' collection

                    Toast.makeText(getApplicationContext(),
                                    " Email not Exist!!",
                                    Toast.LENGTH_LONG)
                            .show();

                    // hide the progress bar
                    progressbar.setVisibility(View.GONE);;
                }
            } else {
                // Handle errors while retrieving documents
                Log.e(TAG, "Error getting documents: ", task.getException());
            }
        });
        // signin existing user


    }
    private void logIn(String email,String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity

                                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                    if(user!=null){
                                        Intent intent
                                                = new Intent(MainActivity.this,
                                                vendor_home_activity.class);
                                        startActivity(intent);
                                        finish();
                                    }


                                    else{
                                        Intent intent
                                                = new Intent(MainActivity.this,
                                                vendor_home_activity.class);
                                        startActivity(intent);}

                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                        "Login failed!!",
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                });

    }

    boolean emailChecker(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}