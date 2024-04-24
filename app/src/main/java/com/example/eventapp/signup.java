package com.example.eventapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.Models.UserModel;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.auth.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    TextView vendortxt, usertxt,checkingType,passwordSyntax,confirmpasswordSyntax;
    String Type="";
   CheckBox showPasswordCheckBox;
    private EditText emailTextView, passwordTextView,conformPassword,userName;
    private AppCompatButton Btn;
    private TextView login_;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    UserModel userModel;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseApp.initializeApp(this);
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        userName=findViewById(R.id.user_name);
        showPasswordCheckBox=findViewById(R.id.showPasswordCheckBox);
        passwordSyntax=findViewById(R.id.passwordSyntax);

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        login_ =findViewById(R.id.have_acc);
        passwordTextView = findViewById(R.id.password);
        confirmpasswordSyntax = findViewById(R.id.confirmpasswordSyntax);
        checkingType = findViewById(R.id.checkingType);
        conformPassword=findViewById(R.id.confirm_password);
        Btn = findViewById(R.id.btn_register);
        progressbar = findViewById(R.id.progressBar);
        checkingType.setVisibility(View.INVISIBLE);
        passwordSyntax.setVisibility(View.INVISIBLE);
        confirmpasswordSyntax.setVisibility(View.INVISIBLE);
        login_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(signup.this,MainActivity.class);
                startActivity(intent);
            }
        });
        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Show the password
                    passwordTextView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    conformPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // Hide the password
                    passwordTextView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    conformPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // Move the cursor to the end of the text
                passwordTextView.setSelection(passwordTextView.getText().length());
                conformPassword.setSelection(conformPassword.getText().length());
            }

        });






        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Type.isEmpty())
                {
                    checkingType.setVisibility(View.VISIBLE);

                }else
                    {
                        checkingType.setVisibility(View.INVISIBLE);
                        if ( userName.getText().toString().trim().isEmpty())
                        {
                            userName.setError("Please Enter UserName");

                        }
                        else {
                            userName.setError(null);
                            if (emailTextView.getText().toString().trim().isEmpty()) {
                                emailTextView.setError("Please Enter Email");

                            } else {
                                emailTextView.setError(null);
                                if (emailChecker(emailTextView.getText().toString().trim()))
                                {
                                    emailTextView.setError(null);
                                    if (passwordTextView.getText().toString().trim().isEmpty()) {
                                        passwordTextView.setError("Please enter password");
                                    } else {
                                        passwordTextView.setError(null);
                                        String password = passwordTextView.getText().toString().trim();
                                        if (!isValidPassword(password)) {
                                            // Password syntax is invalid
                                            Log.e(TAG, "onClick: "+"invalid" );
                                            passwordTextView.setError("Password must contain at least one uppercase letter\nlowercase letter\ndigit\nspecial character\nand be at least 8 characters long");
                                        }else
                                        {
                                            passwordTextView.setError(null);
                                            if (conformPassword.getText().toString().trim().isEmpty())
                                            {
                                                conformPassword.setError("Enter confirm Password");
                                            }else
                                            {
                                                conformPassword.setError(null);

                                                if (!passwordTextView.getText().toString().trim().equals(conformPassword.getText().toString().trim())) {

                                                    confirmpasswordSyntax.setVisibility(View.VISIBLE);

                                                }else
                                                {
                                                    confirmpasswordSyntax.setVisibility(View.GONE);
                                                    if (emailChecker(emailTextView.getText().toString().trim())) {

                                                        createUser(emailTextView.getText().toString().trim(), passwordTextView.getText().toString().trim(),
                                                                userName.getText().toString().trim(), Type);

                                                    } else {
                                                        Toast.makeText(signup.this, "Enter valid Email", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }else
                                {
                                    emailTextView.setError("Invalid Email");
                                }


                            }
                        }
                    }





                //PerforAuth();
            }


        });

        passwordTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordSyntax.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });







        vendortxt=findViewById(R.id.textVendor);
        usertxt=findViewById(R.id.textUser);
        vendortxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendortxt.setTextColor(getResources().getColor(R.color.white));
                vendortxt.setBackgroundResource(R.drawable.button_round_bg);
                usertxt.setTextColor(getResources().getColor(R.color.black));
                usertxt.setBackgroundResource(R.drawable.button_round_bg_grey);
                Type="Vendor";
            }
        });
        usertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usertxt.setTextColor(getResources().getColor(R.color.white));
                usertxt.setBackgroundResource(R.drawable.button_round_bg);
                vendortxt.setTextColor(getResources().getColor(R.color.black));
                vendortxt.setBackgroundResource(R.drawable.button_round_bg_grey);
                Type="Customer";
            }
        });
    }
    boolean emailChecker(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidPassword(String password) {
        // Define a regular expression for password syntax
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

        // Compile the regular expression into a pattern
        Pattern pattern = Pattern.compile(regex);

        // Create a matcher with the input password
        Matcher matcher = pattern.matcher(password);

        // Check if the password matches the pattern
        return matcher.matches();
    }
    void createUser(String email,String password,String user_Name,String type)
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                                    userModel =new UserModel(user_Name,email, Timestamp.now(), FirebaseUtil.currentuserId(),type);
                            FirebaseUtil.currentUserDetials().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                            Intent intent=new Intent(signup.this,vendor_home_activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(signup.this,"User Created Successfully....",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(signup.this,"failed",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(e -> Toast.makeText(signup.this,"Fail",Toast.LENGTH_SHORT).show());


    }
}