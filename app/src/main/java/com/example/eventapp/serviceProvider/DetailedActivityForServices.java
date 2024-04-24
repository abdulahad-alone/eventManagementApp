package com.example.eventapp.serviceProvider;

import static android.content.ContentValues.TAG;

import static com.example.eventapp.utils.FirebaseUtil.getUserType;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.eventapp.Models.RatingModel;
import com.example.eventapp.R;
import com.example.eventapp.Vender_store_activity;
import com.example.eventapp.chatwin;
import com.example.eventapp.detailed_activities.Detailed_Activity_for;
import com.example.eventapp.detailed_activities.add_packages;
import com.example.eventapp.utils.FirebaseUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DetailedActivityForServices extends AppCompatActivity {
    ArrayList<String> imageUrls;
    ImageSlider imageSlider;
    ImageView imageView;
    String Key,current_userId,ProType,location_,timeStamp,Price,imageUrI,this_userId,phone
           ,profile,userName;
    TextView mentions,time,price,location,lol4,chat_me,store,ratingit,showRating;
    RecyclerView recycler_view_for_packages;
    boolean ProKey;
    String RatingSt="0",numberOfRatingsSt="0";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_for_services_activity);
        chat_me=findViewById(R.id.chat_me);
        imageView = findViewById(R.id.fav_btn);
        mentions = findViewById(R.id.mentions);
        imageSlider =findViewById(R.id.image_slider);
        store =findViewById(R.id.store);
        time =findViewById(R.id.time);
        recycler_view_for_packages =findViewById(R.id.recycler_view_for_packages);
        location =findViewById(R.id.locattion);
        price =findViewById(R.id.price);
        lol4 =findViewById(R.id.lol4);
        showRating=findViewById(R.id.showRating);
        ratingit=findViewById(R.id.ratingit);


        imageUrls = getIntent().getStringArrayListExtra("images");
        Key = getIntent().getStringExtra("id");
        location_ = getIntent().getStringExtra("location");
        ProType = getIntent().getStringExtra("category");
        timeStamp=getIntent().getStringExtra("timeStamp");
        Price = getIntent().getStringExtra("price");
        this_userId = getIntent().getStringExtra("userId");
        phone=getIntent().getStringExtra("contactNo");
        imageUrI=imageUrls.get(0);
        showingImages();
        checkFavBtnStatus();
        //checkVendorStatus();
        Log.e(TAG, "this_userId: "+this_userId );
        String men=ProType+" in "+location_;
        mentions.setText(men);
        time.setText(timeStamp);
        location.setText(location_);
        price.setText("Pkr "+Price);
        numberOfRatingsSt = getIntent().getStringExtra("numberOfRatings");
        RatingSt = getIntent().getStringExtra("rating");
        ratingit.setVisibility(View.GONE);
        if (numberOfRatingsSt != null && RatingSt != null)
        {
            showRating.setText(RatingSt+" /5 "+"("+numberOfRatingsSt+")");

        }else
        {
            showRating.setText("0/5 (0)");
        }


        lol4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailedActivityForServices.this, BookingForService.class);
                intent.putExtra("id", Key);
                intent.putExtra("category", ProType);
                intent.putExtra("City", location_);
                intent.putExtra("this_userId", this_userId);
                intent.putExtra("Price", Price);
                intent.putExtra("imageUri", imageUrI);
                startActivity(intent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String childKey = Key;
                Log.e(TAG, "current user: "+current_userId );
                Log.e(TAG, "current property id: "+Key );
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

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedActivityForServices.this, Vender_store_activity.class);
                intent.putExtra("thisId", this_userId);// id of this product user
                startActivity(intent);
            }
        });
        if(FirebaseUtil.currentuserId().equals(this_userId))
        {
            chat_me.setVisibility(View.GONE);
        }
        chat_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedActivityForServices.this, chatwin.class);
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
        getUserType().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String typeValue) {
                if (typeValue != null && typeValue.equals("Customer"))
                {
                    Log.e(TAG, "Customer: " );
                    checkBooking();
                }
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
                Toast.makeText(DetailedActivityForServices.this, "Thanks for rating", Toast.LENGTH_SHORT).show();

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


    private void checkVendorStatus() {
        if (this_userId.equals(FirebaseUtil.currentuserId())) {
            Log.e(TAG, "onCreate: lol ");
            lol4.setVisibility(View.VISIBLE);
        } else {
            Log.e(TAG, "onCreate: lol not ");
            lol4.setVisibility(View.GONE);
        }
    }
    private void deleteUserfav (String key) {
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
                                Toast.makeText(DetailedActivityForServices.this, "Removed from favourite", Toast.LENGTH_SHORT).show();
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
    private void saveUserData (String userId, String key, boolean fav){
        // Create a new user object with name and email
        FirebaseFirestore db;
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
                    Toast.makeText(DetailedActivityForServices.this, "Added to favourite", Toast.LENGTH_SHORT).show();
                    //Log.d("TAG", "User data added with ID: " + documentReference.getId());
                    // You can perform actions after the data is successfully added
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.w("TAG", "Error adding user data", e);
                });
    }
    private void checkFavBtnStatus() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            current_userId = currentUser.getUid();
            Log.d(TAG, "user id: " + current_userId);
            // You can now use the userId variable to access the current user's ID
        } else {
            // No user is currently logged in
        }

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
    }

    private void showingImages() {
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
        imageSlider.setImageList(imageList, ScaleTypes.FIT);
        //imageSlider.setSlideAnimation(AnimationTypes.ZOOM_OUT);
        imageSlider.stopSliding() ;
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                String imageUrl = imageUrls.get(i);
                new StfalconImageViewer.Builder<>(DetailedActivityForServices.this, Collections.singletonList(imageUrl), (imageView, url) -> {
                    // Load the image from the URL into the provided ImageView using your image loading library
                    Picasso.get().load(url).into(imageView);
                }).show();
            }

            @Override
            public void doubleClick(int i) {

            }
        });
    }


}