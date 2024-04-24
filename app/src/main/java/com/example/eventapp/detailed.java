package com.example.eventapp;

import static android.content.ContentValues.TAG;

import static com.example.eventapp.utils.FirebaseUtil.getUserType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.eventapp.Models.CateringServiceModel;
import com.example.eventapp.Models.DaigModel;
import com.example.eventapp.Models.GetDaigs;
import com.example.eventapp.Models.Meals;
import com.example.eventapp.Models.RatingModel;
import com.example.eventapp.Models.UserModel;
import com.example.eventapp.adapter.CateringServiceAdapter;
import com.example.eventapp.adapter.ServiceAdapter;
import com.example.eventapp.adapter.ShowDaigAdapter;
import com.example.eventapp.adapter.ShowMealAdapter;
import com.example.eventapp.detailed_activities.Detailed_Activity_for;
import com.example.eventapp.fragments.BookBottomFragment;
import com.example.eventapp.utils.FirebaseUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class detailed extends AppCompatActivity {
    private ImageView imageView, favorites_deals_, options_;
    private TextView price, shortDescription, Description, location, show_on_map, store, add_rentout_user,showRating;
    FirebaseFirestore db;
    LinearLayout rentOut_Layout;
    private ProgressDialog progressDialog;
    private DatabaseReference reference, favouritesRef, likeref, buttonstatus;
    private Uri uri;
    String RatingSt="0",numberOfRatingsSt="0";
    List<DaigModel> daigList = new ArrayList<>();
    boolean isFavorite = false;
    List<GetDaigs> getDaigs = new ArrayList<>();
    RelativeLayout event;
    TextView ratingit;

    List<Meals> meals = new ArrayList<>();
    List<CateringServiceModel> cateringServiceModelslist = new ArrayList<>();
    boolean ProKey;
    String autoDesc;

    RecyclerView recycler_view_for_meals, recycler_view_for_services_catering;
    String firstImageUrl;
    private static final int MAP_REQUEST_CODE = 123;
    private String Price, VenueTitle, description, Short_Description, langitute, latitude, Key, profile, phone, userName, location_, current_userId, this_userId;
    String purpose, ProType, bedroomno, bathroomno, areaunitselector, area_size, timeStamp;
    ArrayList<String> servicesArray;
    ArrayList<String> imageUrls;
    TextView mentions, time, autoDes, goToDescrption, add_rentout;
    LinearLayout only_meal;
    CardView book_ad;
    TextView chat_me, Price_per_head, addMeals, add_Daig;
    ImageView daig_mail_img;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);
        ImageSlider imageSlider;

        imageSlider = findViewById(R.id.image_slider);
        rentOut_Layout = findViewById(R.id.rentOut_Layout);

        store = findViewById(R.id.store);
        imageView = findViewById(R.id.fav_btn);
        addMeals = findViewById(R.id.add_meal);
        showRating = findViewById(R.id.showRating);
        add_Daig = findViewById(R.id.add_Daig);
        daig_mail_img = findViewById(R.id.daig_mail_img);
        ratingit = findViewById(R.id.ratingit);
        book_ad = findViewById(R.id.book_ad);
        recycler_view_for_services_catering = findViewById(R.id.recycler_view_for_services_catering);
        chat_me = findViewById(R.id.chat_me);
        add_rentout_user = findViewById(R.id.add_rentout_user);
        event = findViewById(R.id.event);
        price = findViewById(R.id.price);
        recycler_view_for_meals = findViewById(R.id.recycler_view_for_meals);
        mentions = findViewById(R.id.mentions);
        autoDes = findViewById(R.id.autoDes);
        time = findViewById(R.id.time);
        location = findViewById(R.id.locattion);
        options_ = findViewById(R.id.options);
        Price_per_head = findViewById(R.id.Price_per_head);
        add_rentout = findViewById(R.id.add_rentout);

        only_meal = findViewById(R.id.only_meal);
        show_on_map = findViewById(R.id.show_on_map);
        goToDescrption = findViewById(R.id.lol4);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_for_services);


        favorites_deals_ = findViewById(R.id.fav_btn);
        Price = getIntent().getStringExtra("price");
        langitute = getIntent().getStringExtra("langitute");
        latitude = getIntent().getStringExtra("latitude");
        VenueTitle = getIntent().getStringExtra("VenueTitle");
        Log.e(TAG, "langitude: " + langitute);
        Log.e(TAG, "latitude: " + latitude);

        Key = getIntent().getStringExtra("id");
        Log.e(TAG, "product id: " + Key);
        this_userId = getIntent().getStringExtra("userId");
        numberOfRatingsSt = getIntent().getStringExtra("numberOfRatings");
        RatingSt = getIntent().getStringExtra("rating");
        Log.d(TAG, "id of this property user id " + this_userId);
        description = getIntent().getStringExtra("description");
        Short_Description = getIntent().getStringExtra("shortDescription");
        ratingit.setVisibility(View.GONE);
        phone = getIntent().getStringExtra("contactNo");

        // Image=getIntent().getStringExtra("image");
        location_ = getIntent().getStringExtra("location");
        ProType = getIntent().getStringExtra("category");
        timeStamp = getIntent().getStringExtra("timeStamp");
        Log.e(TAG, "timeStamp: " + timeStamp);
        System.out.println("Relative time: " + timeStamp);
        imageUrls = getIntent().getStringArrayListExtra("images");
        servicesArray = getIntent().getStringArrayListExtra("serviceArrayList");
        Log.e(TAG, "numberOfRatingsSt : " + numberOfRatingsSt);
        String men = ProType + " in " + location_;
        time.setText(timeStamp);
        mentions.setText(men);
        if(ProType.equals("Catering"))
        {
            book_ad.setVisibility(View.GONE);
        }
        numberOfRatingsSt = getIntent().getStringExtra("numberOfRatings");
        RatingSt = getIntent().getStringExtra("rating");
        if (numberOfRatingsSt != null && RatingSt != null)
        {
            showRating.setText(RatingSt+" /5 "+"("+numberOfRatingsSt+")");

        }else
        {
            showRating.setText("0/5 (0)");
        }
        getUserType().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String typeValue) {
                if (typeValue != null && typeValue.equals("Customer")) {

                    Log.e(TAG, "Customer: " );
                    checkBooking();
                    // User type is "vendor", make the button invisible
                    if (ProType.equals("Catering")) {
                        only_meal.setVisibility(View.GONE);
                        book_ad.setVisibility(View.VISIBLE);
                        chat_me.setVisibility(View.VISIBLE);
                        addMeals.setVisibility(View.GONE);
                        add_Daig.setVisibility(View.GONE);
                        add_rentout.setVisibility(View.GONE);
                        add_rentout_user.setVisibility(View.VISIBLE);
                        daig_mail_img.setVisibility(View.GONE);
                    } else {
                        book_ad.setVisibility(View.VISIBLE);
                        chat_me.setVisibility(View.VISIBLE);
                        only_meal.setVisibility(View.VISIBLE);
                        addMeals.setVisibility(View.GONE);
                        add_Daig.setVisibility(View.GONE);
                        add_rentout.setVisibility(View.GONE);
                        add_rentout_user.setVisibility(View.VISIBLE);
                        daig_mail_img.setVisibility(View.GONE);
                    }


                } else {


                    Log.e(TAG, "vendor: " );
                    // Type value is Customer
                    book_ad.setVisibility(View.GONE);
                    chat_me.setVisibility(View.GONE);

                    only_meal.setVisibility(View.GONE);
                    addMeals.setVisibility(View.VISIBLE);
                    add_Daig.setVisibility(View.VISIBLE);
                    add_rentout.setVisibility(View.VISIBLE);
                    add_rentout_user.setVisibility(View.GONE);
                    daig_mail_img.setVisibility(View.VISIBLE);


                if (ProType.equals("Catering")) {
                    Price_per_head.setText("starting ");
                    addMeals.setVisibility(View.GONE);

                } else {
                    rentOut_Layout.setVisibility(View.GONE);
                    add_Daig.setVisibility(View.GONE);
                }
                if (this_userId.equals(FirebaseUtil.currentuserId())) {

                    Log.e(TAG, "onCreate:not vendor ");
                    only_meal.setVisibility(View.GONE);
                    addMeals.setVisibility(View.VISIBLE);
                    add_Daig.setVisibility(View.VISIBLE);
                    add_rentout.setVisibility(View.VISIBLE);
                    add_rentout_user.setVisibility(View.GONE);
                    daig_mail_img.setVisibility(View.VISIBLE);

                } else {
                    Log.e(TAG, "onCreate:yes vendor ");
                    only_meal.setVisibility(View.VISIBLE);
                    addMeals.setVisibility(View.GONE);
                    add_Daig.setVisibility(View.GONE);
                    add_rentout.setVisibility(View.GONE);
                    add_rentout_user.setVisibility(View.VISIBLE);
                    daig_mail_img.setVisibility(View.GONE);
                }
            }

            }
        });


        autoDesc = area_size + " in " + location_;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            current_userId = currentUser.getUid();
            Log.d(TAG, "user id: " + current_userId);
            // You can now use the userId variable to access the current user's ID
        } else {
            // No user is currently logged in
        }
        ArrayList<PhotoView> photoViews = new ArrayList<>();
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for (String url : imageUrls) {
            imageList.add(new SlideModel(url, null));
            PhotoView photoView = new PhotoView(this);
            Glide.with(this).load(url).into(photoView);
            photoView.setMaximumScale(10); // Set max zoom level

            // Add the PhotoView to the list
            photoViews.add(photoView);


        }
        add_Daig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detailed.this, Daig.class);
                intent.putExtra("product_id", Key);
                intent.putExtra("Category", ProType);
                startActivity(intent);
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detailed.this, Vender_store_activity.class);
                intent.putExtra("thisId", this_userId);// id of this product user
                startActivity(intent);
            }
        });
        add_rentout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detailed.this, RentoutService.class);
                intent.putExtra("thisId", this_userId);// id of this product user
                intent.putExtra("product_id", Key);
                intent.putExtra("Category", ProType);
                startActivity(intent);
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(detailed.this, chatwin.class);
            }
        });
        chat_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detailed.this, chatwin.class);
                intent.putExtra("thisId", this_userId);
                intent.putExtra("currentId", current_userId);
                intent.putExtra("propertyName", ProType);
                intent.putExtra("Username", userName);
                intent.putExtra("UserImage", profile);
                intent.putExtra("Phone", phone);
                intent.putExtra("yourExtraKey", 0);
                intent.putExtra("price", Price);
                intent.putExtra("imageUri", imageUrls.get(0));
                startActivity(intent);
            }
        });


        FirebaseFirestore dbo = FirebaseFirestore.getInstance();
        CollectionReference usersRef = dbo.collection("favourites");
        // Create a query to find documents where the 'name' field matches the searchName
        Query query = usersRef.whereEqualTo("PropertyId", Key).whereEqualTo("UserId", current_userId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the email field from the document
                    ProKey = (boolean) document.get("favourite");
                    if (ProKey != false) {
                        // Process the email (e.g., display it, use it in your app)
                        Log.d("TAG", "true: " + ProKey);
                        imageView.setImageResource(R.drawable.filled_heart);
                        // You can handle the email here or pass it to another method
                    }
                }
            } else {
                // Handle errors
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });

        if (ProType.equals("Catering")) {
            checkDaig(Key);
            checkService(Key);
        } else {

            checkmeals(Key);
        }

        imageSlider.setImageList(imageList, ScaleTypes.FIT);
        //imageSlider.setSlideAnimation(AnimationTypes.ZOOM_OUT);
        imageSlider.stopSliding();
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                String imageUrl = imageUrls.get(i);
                new StfalconImageViewer.Builder<>(detailed.this, Collections.singletonList(imageUrl), (imageView, url) -> {
                    // Load the image from the URL into the provided ImageView using your image loading library
                    Picasso.get().load(url).into(imageView);
                }).show();
            }

            @Override
            public void doubleClick(int i) {

            }
        });

        if (imageUrls != null && !imageUrls.isEmpty()) {
            firstImageUrl = imageUrls.get(0);

            // Load the image into the ImageView using Picasso or Glide
            // If using Glide:
            // Glide.with(context).load(firstImageUrl).into(imageView);
        }

        fetchDataFromFirestore();
        // fetchDataFromStorage();
        location.setText(location_);
        price.setText(Price);


        FirebaseDatabase.getInstance().getReference().child("images");
        progressDialog = new ProgressDialog(detailed.this);
        progressDialog.setMessage("Deleting please wait...");
        progressDialog.setCancelable(false);
        likeref = FirebaseDatabase.getInstance().getReference("favorites");

        if (this_userId.equals(FirebaseUtil.currentuserId())) {
            Log.e(TAG, "onCreate: lol ");
            options_.setVisibility(View.VISIBLE);
        } else {
            Log.e(TAG, "onCreate: lol not ");
            options_.setVisibility(View.GONE);
        }
        options_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: ");
                PopupMenu popupMenu = new PopupMenu(detailed.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.city_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        switch (itemId) {
                            case R.id.Delete_:

                                confirmDeletion();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String childKey = Key;
                Log.e(TAG, "current user: " + current_userId);
                Log.e(TAG, "current property id: " + Key);
                boolean isfav = ProKey;
                // Check the current state and toggle it
                //isFavorite =true; /* determine if the item is favorited or not */;
                if (isfav) {
                    imageView.setImageResource(R.drawable.ic_empty_favorite); // Set to unfilled heart icon
                    // Unfavorite the item and update your data
                    deleteUserfav(childKey);


                } else {
                    saveUserData(current_userId, childKey, true);
                    imageView.setImageResource(R.drawable.filled_heart);
                }
                // Toggle the state
                isfav = !isfav;
                // isFavorite = !isFavorite;
            }
        });
        show_on_map.setOnClickListener(view -> {
            double latitude_ = Double.parseDouble(latitude); // Replace with your latitude
            double longitude = Double.parseDouble(langitute); // Replace with your longitude
           /*String uri = "geo:" + latitude_ + "," + longitude + "?q=" + latitude + "," + longitude;
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                // Start the activity if there's an app to handle the Intent
                startActivity(mapIntent);
            } else {
                // Handle case where there is no activity to handle the intent (e.g., Google Maps is not installed)
                // You can display a message or prompt the user to install Google Maps
                Log.e(TAG, "onClick: "+ "lol" );
            }*/
            openMapActivity();


        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ServiceAdapter adapter = new ServiceAdapter(servicesArray);
        recyclerView.setAdapter(adapter);

        addMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(detailed.this, meals.class);
                intent.putExtra("product_id", Key);
                startActivity(intent);
            }
        });

        book_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookBottomFragment bottomSheetFragment = new BookBottomFragment();
                Bundle args = new Bundle();
                args.putString("price", Price);
                args.putString("location", location_);
                args.putString("description", description);
                args.putString("VenueTitle", VenueTitle);
                args.putString("id", Key);
                args.putString("langitute", langitute);
                args.putString("latitude", latitude);
                args.putString("userId", this_userId);
                args.putString("contactNo", phone);
                args.putString("category", ProType);
                args.putString("timeStamp", timeStamp);
                args.putStringArrayList("images", imageUrls);
                args.putSerializable("meals", (Serializable) meals);
                bottomSheetFragment.setArguments(args);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
        ratingit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRatingDialog();
            }
        });
    }
    private void openRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);

        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);

        builder.setView(dialogView)
                .setTitle("Rate this")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        float rating = ratingBar.getRating();
                        storeRatingToFirebase(rating);
                        // Use the rating as needed
                        dialog.dismiss(); // Dismiss the dialog here
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void storeRatingToFirebase(float rating) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new Rating object
        RatingModel ratingModel= new RatingModel();
        ratingModel.setUserId(FirebaseUtil.currentuserId());
        ratingModel.setRatingValue(rating);

        FirebaseUtil.getRatingReference(Key,FirebaseUtil.currentuserId()).set(ratingModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FirebaseUtil.getRatingReference(Key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            float totalRating = 0;
                            int numberOfRatings = 0;

                            // Iterate through all ratings
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RatingModel ratingModel1 = document.toObject(RatingModel.class);
                                totalRating += ratingModel1.getRatingValue();
                                numberOfRatings++;
                            }
                            if (numberOfRatings > 0) {
                                float averageRating = totalRating / numberOfRatings;
                                Log.e(TAG, "onComplete: "+ averageRating );
                                updateRatingToProduct(averageRating,numberOfRatings);
                            }
                        }
                        else {
                            Log.e(TAG, "Error getting ratings", task.getException());
                        }
                    }
                });
            }
        });


    }

    private void updateRatingToProduct(float averageRating, int numberOfRatings) {
    String ratingSt= String.valueOf(averageRating);
    String numberOfRatingsSt= String.valueOf(numberOfRatings);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "Product" collection
        CollectionReference productsRef = db.collection("Products");

        // Document reference for the specific product ID
        DocumentReference productDocRef = productsRef.document(Key);
        productDocRef.update("rating", ratingSt,"numberOfRatings",numberOfRatingsSt).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "rating updated successfully");
                Toast.makeText(detailed.this, "Thanks for rating", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error updating rating", e);
                // Error updating city
            }
        });

    }

    private void checkBooking() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to "Booking" collection
        CollectionReference bookingRef = db.collection("Booking");

        // Query to find bookings of the current user
        Query query = bookingRef.whereEqualTo("CurrentUserId", FirebaseUtil.currentuserId()).whereEqualTo("ProductId", Key);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        // Handle case when no bookings are found
                        // For example, display a message or perform another action
                    } else {
                        // Bookings found, make the ratingit view visible
                        ratingit.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Handle errors
                    Log.e(TAG, "Error getting bookings: ", task.getException());
                }
            }
        });
    }


    private void checkService(String key) {
        FirebaseUtil.getServiceReference(key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {

                    } else {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CateringServiceModel cateringServiceModel = document.toObject(CateringServiceModel.class);
                            cateringServiceModelslist.add(cateringServiceModel);
                        }
                        Log.e(TAG, "service:list  " + cateringServiceModelslist.get(0).getServiceName());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(detailed.this, LinearLayoutManager.HORIZONTAL, false);
                        recycler_view_for_services_catering.setLayoutManager(layoutManager);
                        CateringServiceAdapter adapter = new CateringServiceAdapter(detailed.this, cateringServiceModelslist);
                        recycler_view_for_services_catering.setAdapter(adapter);
                    }
                }

            }
        });
    }

    private void checkDaig(String key) {
        List<Map<String, Object>> daigModelsList = new ArrayList<>();
        FirebaseUtil.getDaigReference(key).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Retrieve data from Firestore document
                    String mealName = documentSnapshot.getString("DaigName");
                    String mealPrice = documentSnapshot.getString("DaigPrice");
                    String description = documentSnapshot.getString("description");
                    String imageUrl = documentSnapshot.getString("imageUrl");
                    String id = documentSnapshot.getString("id");
                    String minimumOrder = documentSnapshot.getString("minimumOrder");
                    String DaigRef = documentSnapshot.getString("DaigRef");

                    // Retrieve the list of DaigModels
                    List<Map<String, Object>> daigModels = (List<Map<String, Object>>) documentSnapshot.get("daigModels");

                    // Create a Map to hold the retrieved data
                    Map<String, Object> mealData = new HashMap<>();
                    mealData.put("DaigName", mealName);
                    mealData.put("DaigPrice", mealPrice);
                    mealData.put("description", description);
                    mealData.put("imageUrl", imageUrl);
                    mealData.put("id", id);
                    mealData.put("minimumOrder", minimumOrder);
                    mealData.put("daigModels", daigModels);
                    mealData.put("DaigRef", DaigRef);

                    // Add the meal data to the list
                    daigModelsList.add(mealData);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(detailed.this, LinearLayoutManager.HORIZONTAL, false);
                recycler_view_for_meals.setLayoutManager(layoutManager);
                ShowDaigAdapter adapter = new ShowDaigAdapter(detailed.this, daigModelsList);
                recycler_view_for_meals.setAdapter(adapter);
            }
        });
    }

    private void checkmeals(String key) {
        Log.e(TAG, "checkmeals: " + key);
        FirebaseUtil.getMealReference(key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        // No data present in the collection
                        Log.d(TAG, "No data found in the collection");
                        // Handle accordingly (e.g., display a message to the user)
                    } else {
                        // Data is present in the collection

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Meals meal = document.toObject(Meals.class);
                            meals.add(meal);
                        }
                        // Pass the data to your RecyclerView adapter
                        LinearLayoutManager layoutManager = new LinearLayoutManager(detailed.this, LinearLayoutManager.HORIZONTAL, false);
                        recycler_view_for_meals.setLayoutManager(layoutManager);
                        ShowMealAdapter adapter = new ShowMealAdapter(meals);
                        recycler_view_for_meals.setAdapter(adapter);
                    }
                } else {
                    // Handle errors
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void openMapActivity() {
        double latitude_ = Double.parseDouble(latitude); // Replace with your latitude
        double longitude = Double.parseDouble(langitute);
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("latitude", latitude_);
        intent.putExtra("longitude", longitude);
        intent.putExtra("image", firstImageUrl);
        startActivityForResult(intent, MAP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            double latitude = data.getDoubleExtra("latitude", 0.0);
            double longitude = data.getDoubleExtra("longitude", 0.0);

            String location = "Latitude: " + latitude + ", Longitude: " + longitude;


            // Use the latitude and longitude in your AdPostingActivity
        }
    }

    private void confirmDeletion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(detailed.this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete?");

        // Add buttons for Yes and No
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform deletion here
                deleteItem();
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItem() {
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Define the value to match
        String idToDelete = Key;

// Query to find documents where the field "id" matches the specified value
        Query query = db.collection("Products").whereEqualTo("id", idToDelete);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Delete each document found by the query
                    db.collection("Products").document(document.getId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                deletionfromfav();
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                progressDialog.dismiss();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                // Handle errors
                                Log.w(TAG, "Error deleting document", e);
                            });
                }
            } else {
                // Handle errors while retrieving documents
                Log.w(TAG, "Error getting documents: ", task.getException());
            }
        });

    }

    private void deletionfromfav() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Define the value to match
        String idToDelete = Key;
        Log.e(TAG, "deletionfromfav: " + idToDelete);
// Query to find documents where the field "id" matches the specified value
        Query query = db.collection("favourites").whereEqualTo("PropertyId", idToDelete);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Delete each document found by the query
                    String documentId = document.getId();
                    Log.e(TAG, "deletionfromfav: " + documentId);
                    db.collection("favourites").document(document.getId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                // Document successfully deleted
                                Log.d(TAG, "DocumentSnapshot successfully deleted! from fav");
                                Log.e(TAG, "deletionfromfav: " + document.get("PropertyId"));
                            })
                            .addOnFailureListener(e -> {
                                // Handle errors
                                Log.w(TAG, "Error deleting document", e);
                            });
                }
            } else {
                // Handle errors while retrieving documents
                Log.w(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private void fetchDataFromFirestore() {


        final UserModel[] otherUser = {new UserModel()};
        FirebaseUtil.currentUserDetials().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                otherUser[0] = task.getResult().toObject(UserModel.class);
                Log.i(TAG, "onComplete: " + otherUser[0].getName());
                userName = otherUser[0].getName();
            }
        });
    }

    private void fetchDataFromStorage() {
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();

                            profile = String.valueOf(uri);
                        }
                    }
                });
    }

    private void deleteUserfav(String key) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "users" collection
        CollectionReference usersRef = db.collection("favourites");
        Query query = usersRef.whereEqualTo("PropertyId", key);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID of the matching document
                    String documentId = document.getId();

                    // Delete the document with the obtained document ID
                    db.collection("favourites").document(documentId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                // Document successfully deleted
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                Toast.makeText(detailed.this, "Removed from favourite", Toast.LENGTH_SHORT).show();
                                // You can perform any action after successful deletion
                            })
                            .addOnFailureListener(e -> {
                                // Handle any errors
                                Log.w(TAG, "Error deleting document", e);
                            });
                }
            } else {
                // Handle errors while retrieving documents
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private void saveUserData(String userId, String key, boolean fav) {
        // Create a new user object with name and email
        Map<String, Object> map = new HashMap<>();
        map.put("UserId", userId);
        map.put("PropertyId", key);
        map.put("favourite", fav);
        db = FirebaseFirestore.getInstance();
        // Add the user to a collection called "users" in Firestore
        db.collection("favourites")
                .add(map)
                .addOnSuccessListener(documentReference -> {
                    // Handle successful addition
                    Toast.makeText(detailed.this, "Added to favourite", Toast.LENGTH_SHORT).show();
                    //Log.d("TAG", "User data added with ID: " + documentReference.getId());
                    // You can perform actions after the data is successfully added
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.w("TAG", "Error adding user data", e);
                });
    }
}