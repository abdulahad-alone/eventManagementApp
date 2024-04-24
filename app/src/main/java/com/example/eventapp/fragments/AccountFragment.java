package com.example.eventapp.fragments;

import static android.content.ContentValues.TAG;

import static com.example.eventapp.utils.FirebaseUtil.getUserType;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.Favourities;
import com.example.eventapp.MainActivity;
import com.example.eventapp.Models.UserModel;
import com.example.eventapp.MyPropertiesVendor;
import com.example.eventapp.bookedAd;
import com.example.eventapp.profileSetting;
import com.example.eventapp.R;
import com.example.eventapp.utils.AndroidUtil;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private CircleImageView userProfile;
    private TextView UserName,UserEmail;
    private AppCompatButton updateButton;
    RelativeLayout MyFavourites,MyProperties_,profile_setting_;
    CardView show_booking;
    LinearLayout logout_btn;

    private String id;
    ProgressBar loadingProgressBar;
    UserModel currentUserModel;
    private Uri uri,selectedImageUri,UriFromStorage;
    private FirebaseAuth mAuth;
    String Username,Email_add;
    private DatabaseReference reference;

    private  int Pick_Image=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.account_fragment, container, false);
        return  view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        loadingProgressBar =view.findViewById(R.id.loadingProgressBar);
        logout_btn=view.findViewById(R.id.logout_btn);
        show_booking=view.findViewById(R.id.show_booking);
        MyProperties_=view.findViewById(R.id.MyProperties);
        MyFavourites=view.findViewById(R.id.MyFavourites);
        profile_setting_=view.findViewById(R.id.profile_setting);
        userProfile=view.findViewById(R.id.profile_image);
        UserName=view.findViewById(R.id.name_update);
        UserEmail=view.findViewById(R.id.email_update);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "Logout Successful !", Toast.LENGTH_SHORT).show();
            }
        });

        show_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(getContext(),
                        bookedAd.class);
                startActivity(intent);
            }
        });
        profile_setting_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), profileSetting.class);
               intent.putExtra("Username",Username);
                intent.putExtra("Email",Email_add);
                startActivity(intent);
            }
        });
        loadingProgressBar.setVisibility(View.VISIBLE);
        fetchDataFromStorage();
        fetchDataFromFirestore();
      MyFavourites.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(getContext(), Favourities.class);
              startActivity(intent);
          }
      });
      MyProperties_.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(getContext(), MyPropertiesVendor.class);
              startActivity(intent);
          }
      });


    }

    private void checkingUserType() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        getUserType().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String typeValue) {
                if (typeValue != null && typeValue.equals("Customer")) {
                    // User type is "vendor", make the button invisible
                    MyProperties_.setVisibility(View.GONE);
                } else {
                    // Type value is not available
                    MyProperties_.setVisibility(View.VISIBLE);
                }
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // Refresh or reload data here
        checkingUserType();
        fetchDataFromFirestore(); // Replace this with your data retrieval logic
       fetchDataFromStorage();
    }
    private void fetchDataFromStorage() {

        final UserModel[] otherUser = {new UserModel()};

        //setInProgress(true);
        FirebaseUtil.currentUserDetials().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // setInProgress(false);
                otherUser[0] =task.getResult().toObject(UserModel.class);
                Log.i(TAG, "onComplete: "+otherUser[0].getName());
                UserName.setText(otherUser[0].getName());
                Username=otherUser[0].getName();
                Email_add=otherUser[0].getEmail();
                otherUser[0].getImage();
                UserEmail.setText(otherUser[0].getEmail());
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }
    private void fetchDataFromFirestore() {
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            UriFromStorage=task.getResult();
                            Log.e(TAG, "image Uri: "+UriFromStorage );
                            AndroidUtil.setProfilePic(getContext(), UriFromStorage, userProfile);
                        }
                    }
                });
    }

}