package com.example.eventapp;

import static android.content.ContentValues.TAG;

import static com.example.eventapp.utils.FirebaseUtil.getUserType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.eventapp.Models.Meals;
import com.example.eventapp.Models.UserModel;
import com.example.eventapp.adapter.DateAdapter;
import com.example.eventapp.adapter.ShowMealAdapter;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class showBookingAds extends AppCompatActivity {
    private String Price, VenueTitle,description, Short_Description, langitute, latitude, Key,profile,phone, userName,location_, current_userId, this_userId;
    String purpose, ProType, bedroomno,bathroomno,areaunitselector,area_size ,timeStamp;
    ArrayList<String> servicesArray;
    ArrayList<String> imageUrls;
    LinearLayout showStatus;
    Timestamp timestamp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    ArrayList<String> datesarray = new ArrayList<>();
    TextView fav_price,fav_short_description,fav_location,user_name_of_booking,user_phone_of_booking,
            user_address_of_booking,Ordernumber,times_stamp,total_number_of_persons,bookingdays,vendorName,
            parsedPrice;
    RelativeLayout fav_relative_layout;
    Object mealsObj;
    public Query baseQuery;
    List<Meals> meals = new ArrayList<>();
    String priceOfbooking,parsedPriceOfbooking,
            bookingId,BookUsername,address,noOfPersons,ordernumber,phoneNo,BookingtimeStamp;
    RecyclerView recycler_view_for_meals,recycler_view_for_dates;
String UserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_booking_ads);

        Price = getIntent().getStringExtra("price");
        langitute = getIntent().getStringExtra("langitute");
        latitude = getIntent().getStringExtra("latitude");
        VenueTitle = getIntent().getStringExtra("VenueTitle");
        Log.e(TAG, "langitude: " + langitute);
        Log.e(TAG, "latitude: " + latitude);

        Key = getIntent().getStringExtra("id");
        Log.e(TAG, "product id: "+Key);
        this_userId = getIntent().getStringExtra("userId");
        Log.d(TAG, "id of this property user id " + this_userId);
        description = getIntent().getStringExtra("description");
        Short_Description = getIntent().getStringExtra("shortDescription");

        phone=getIntent().getStringExtra("contactNo");

        // Image=getIntent().getStringExtra("image");
        location_ = getIntent().getStringExtra("location");
        ProType = getIntent().getStringExtra("category");
        timeStamp=getIntent().getStringExtra("timeStamp");
        Log.e(TAG, "timeStamp: "+timeStamp );
        System.out.println("Relative time: " + timeStamp);
        imageUrls = getIntent().getStringArrayListExtra("images");
        servicesArray = getIntent().getStringArrayListExtra("serviceArrayList");


        fav_location=findViewById(R.id.fav_location);
        showStatus=findViewById(R.id.show_status);
        fav_relative_layout=findViewById(R.id.fav_relative_layout);
        fav_price=findViewById(R.id.fav_price);
        parsedPrice=findViewById(R.id.parsedPrice);
        fav_short_description=findViewById(R.id.fav_short_description);
        user_name_of_booking=findViewById(R.id.user_name_of_booking);
        user_phone_of_booking=findViewById(R.id.user_phone_of_booking);
        user_address_of_booking=findViewById(R.id.user_address_of_booking);
        Ordernumber=findViewById(R.id.ordernumber);
        times_stamp=findViewById(R.id.times_stamp);
        total_number_of_persons=findViewById(R.id.total_number_of_persons);
        recycler_view_for_meals=findViewById(R.id.recycler_view_for_meals);
        recycler_view_for_dates=findViewById(R.id.recycler_view_for_dates);
        vendorName=findViewById(R.id.vendorName);
        bookingdays=findViewById(R.id.bookingdays);
        showStatus.setVisibility(View.INVISIBLE);
        String firstImg= imageUrls.get(0);
        Glide.with(this)
                .load(firstImg)
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
        fav_price.setText("per head "+Price);
        fav_short_description.setText(VenueTitle);
        fav_location.setText(location_);

        getUserType().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String typeValue) {
                if (typeValue != null && typeValue.equals("Customer")) {
                    getdatafromFromFirestore(Key,"Customer");

                } else {
                    // Type value is not available
                    getdatafromFromFirestore(Key,"Vendor");
                }
            }
        });





    }

    private void getdatafromFromFirestore(String key, String customer) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Booking");
        baseQuery = productsRef;
        baseQuery = baseQuery.whereEqualTo("ProductId", key);
        if(customer.equals("Customer"))
        {
            baseQuery = baseQuery.whereEqualTo("CurrentUserId", FirebaseUtil.currentuserId());
        }else
        {
            baseQuery = baseQuery.whereEqualTo("ProductUserId", FirebaseUtil.currentuserId());
        }

   // baseQuery = baseQuery.whereEqualTo("CurrentUserId", FirebaseUtil.currentuserId());
        baseQuery
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<DocumentSnapshot> productlist = task.getResult().getDocuments();

                            for (DocumentSnapshot productDoc : productlist) {
                                Map<String, Object> data = productDoc.getData();
                                priceOfbooking = (String) data.get("totalPrice");
                                parsedPriceOfbooking = (String) data.get("price");
                                BookUsername = (String) data.get("FullName");
                                address = (String) data.get("address");
                                noOfPersons = (String) data.get("noOfPersons");
                                ordernumber = (String) data.get("orderNumber");
                                phoneNo = (String) data.get("phone");
                                bookingId = (String) data.get("documentId");
                                Log.e(TAG, "document ID: " + bookingId);
                                datesarray = (ArrayList<String>) data.get("BookingDates");
                                Log.e(TAG, "dates: " + datesarray);
                                timestamp = (Timestamp) data.get("timeStamp");
                                 mealsObj = data.get("meals");
                            }
                                BookingtimeStamp = FirebaseUtil.formatTimestamp(timestamp);
                                Log.e(TAG, "BookUsername: "+BookUsername);
                                user_name_of_booking.setText(BookUsername);
                                user_phone_of_booking.setText(phoneNo);
                                user_address_of_booking.setText(address);
                                parsedPrice.setText(parsedPriceOfbooking);
                                Ordernumber.setText("Order #"+ordernumber);
                                times_stamp.setText("Placed on "+BookingtimeStamp);
                                total_number_of_persons.setText("Booked for "+noOfPersons+" Persons");
                                List<Meals> selectedMeals = new ArrayList<>();

                                if (mealsObj instanceof List<?>) {
                                    List<HashMap<String, Object>> mealsList = (List<HashMap<String, Object>>) mealsObj;
                                    for (HashMap<String, Object> mealMap : mealsList) {
                                        // Convert each HashMap to a Meals object
                                        Meals meal = new Meals();
                                        // Assuming your Meals class has setters for its properties, set them here
                                        meal.setMealName((String) mealMap.get("mealName"));
                                        meal.setMealPrice((String) mealMap.get("mealPrice"));
                                        meal.setDescription((String) mealMap.get("description"));
                                        meal.setImageUrl((String) mealMap.get("imageUrl"));
                                        // Add the Meals object to the selectedMeals list
                                        selectedMeals.add(meal);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(showBookingAds.this, LinearLayoutManager.HORIZONTAL, false);
                                        recycler_view_for_meals.setLayoutManager(layoutManager);
                                        ShowMealAdapter adapter = new ShowMealAdapter(selectedMeals);
                                        recycler_view_for_meals.setAdapter(adapter);
                                    }
                                } else {
                                    // Handle case where "meals" is not a list
                                    Log.e(TAG, "Data for 'meals' is not a list.");
                                }
                                 LinearLayoutManager layout_Manager = new LinearLayoutManager(showBookingAds.this, LinearLayoutManager.HORIZONTAL, false);
                                recycler_view_for_dates.setLayoutManager(layout_Manager);
                                DateAdapter adapter = new DateAdapter (datesarray);
                                recycler_view_for_dates.setAdapter(adapter);
                                bookingdays.setText("booked for "+datesarray.size()+" day");


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        final UserModel[] otherUser = {new UserModel()};
        FirebaseUtil.otherUserDetails(this_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                otherUser[0] =task.getResult().toObject(UserModel.class);
                Log.i(TAG, "onComplete: "+otherUser[0].getName());
                vendorName.setText("Rent by "+otherUser[0].getName());
            }
        });



    }
}