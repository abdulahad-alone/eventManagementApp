package com.example.eventapp.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.eventapp.utils.FirebaseUtil.getUserType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.Models.UserModel;
import com.example.eventapp.R;
import com.example.eventapp.utils.AndroidUtil;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Daigbooked_details extends AppCompatActivity {
    TextView fav_price,fav_short_description,fav_location,user_name_of_booking,user_phone_of_booking,
            user_address_of_booking,Ordernumber,times_stamp,total_number_of_persons,bookingdays,vendorName,
            parsedPrice,per_head;
    public String daigName,totalNoOfDaigs;
    public String id;
    public String price;
    public String currentUserId;
    public String bookingDate;
    public String productId;
    public String thisProductUserId;
    public String bookingTime;
    public String description;
    public String daigRef;
    public String phone;
    public String fullName;
    public String address;
    public String timeStamp;
    public String typeOfAd;
    public String imageUrl;
    public String orderNumber;
    public String totalKgs;
    RelativeLayout fav_relative_layout;
    public Query baseQuery;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daigbooked);




        fav_location=findViewById(R.id.fav_location);
        fav_price=findViewById(R.id.fav_price);
        parsedPrice=findViewById(R.id.parsedPrice);
        fav_short_description=findViewById(R.id.fav_short_description);
        user_name_of_booking=findViewById(R.id.user_name_of_booking);
        user_phone_of_booking=findViewById(R.id.user_phone_of_booking);
        user_address_of_booking=findViewById(R.id.user_address_of_booking);
        Ordernumber=findViewById(R.id.ordernumber);
        times_stamp=findViewById(R.id.times_stamp);
        per_head=findViewById(R.id.per_head);
        total_number_of_persons=findViewById(R.id.total_number_of_persons);
        vendorName=findViewById(R.id.vendorName);
        fav_relative_layout=findViewById(R.id.fav_relative_layout);
        bookingdays=findViewById(R.id.bookingdays);
        per_head.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null) {
            daigName = intent.getStringExtra("DaigName");
            id = intent.getStringExtra("id");
            price = intent.getStringExtra("price");
            currentUserId = intent.getStringExtra("CurrentUserId");
            bookingDate = intent.getStringExtra("BookingDate");
            productId = intent.getStringExtra("ProductId");
            thisProductUserId = intent.getStringExtra("this_product_userId");
            bookingTime = intent.getStringExtra("BookingTime");
            description = intent.getStringExtra("description");
            daigRef = intent.getStringExtra("DaigRef");
            phone = intent.getStringExtra("phone");
            fullName = intent.getStringExtra("FullName");
            address = intent.getStringExtra("address");
            timeStamp = intent.getStringExtra("timeStamp");
            typeOfAd = intent.getStringExtra("Type_of_ad");
            imageUrl = intent.getStringExtra("imageUrl");
            orderNumber = intent.getStringExtra("orderNumber");
            totalKgs = intent.getStringExtra("TotalKgs");
            totalNoOfDaigs = intent.getStringExtra("totalNoOfDaigs");
        }
        bookingdays.setText("Booked for "+AndroidUtil.formatedDate(bookingDate)+" at "+AndroidUtil.formatTime(bookingTime));
        total_number_of_persons.setText(totalNoOfDaigs+ " "+typeOfAd+" of "+totalKgs +" kg each");
        Ordernumber.setText("Order #"+orderNumber);
        times_stamp.setText("Placed on "+timeStamp);
        parsedPrice.setText(formatPrice(price));



        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        fav_relative_layout.setBackground( resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {


                    }
                });

        gettingDataFromFireStore(productId,thisProductUserId);


    }

    private void gettingDataFromFireStore(String productId, String thisProductUserId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Products");
        productsRef.document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData(); // Get the data from the document
                        if (data != null) {
                            // Extract data from the document and create a ShowingVenueDetails object
                            ShowingVenueDetails venueDetails = new ShowingVenueDetails(
                                    document.getId(),
                                    (String) data.get("category"),
                                    (String) data.get("userId"),
                                    (String) data.get("contactNo"),
                                    (String) data.get("city"),
                                    (String) data.get("latitude"),
                                    (String) data.get("longitude"),
                                    (String) data.get("price"),
                                    (String) data.get("venueTitle"),
                                    (String) data.get("description"),
                                    (List<String>) data.get("images"),
                                    (Timestamp) data.get("timestamp"),
                                    (ArrayList<String>) data.get("features")
                            );
                             // Add the ShowingVenueDetails object to your list
                            fav_location.setText(venueDetails.getCity());
                            user_phone_of_booking.setText(phone);
                            user_address_of_booking.setText(address);
                            user_name_of_booking.setText(fullName);
                            fav_price.setText(daigName);
                            fav_short_description.setText(venueDetails.getVenueTitle());




                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        final UserModel[] otherUser = {new UserModel()};
        FirebaseUtil.otherUserDetails(thisProductUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                otherUser[0] =task.getResult().toObject(UserModel.class);
                Log.i(ContentValues.TAG, "onComplete: "+otherUser[0].getName());
                vendorName.setText("Rent by "+otherUser[0].getName());
            }
        });

    }
    private String formatPrice(String price) {
        if (price.isEmpty()) {
            return ""; // Return an empty string if the price is empty
        }

        double priceValue = Double.parseDouble(price);
        String formattedPrice;
        if (priceValue >= 10000000) {
            formattedPrice = String.format("%.2f", priceValue / 10000000) + " Crore"; // Convert to Crore
        }
        else if (priceValue >= 100000) {
            formattedPrice = String.format("%.2f", priceValue / 100000) + " Lakh"; // Convert to Lakhs
        } else if (priceValue >= 1000) {
            formattedPrice = String.format("%.1f", priceValue / 1000) + " Thousand"; // Convert to Thousands
        }
        else {
            formattedPrice = price; // No conversion needed
        }

        return formattedPrice;
    }
}