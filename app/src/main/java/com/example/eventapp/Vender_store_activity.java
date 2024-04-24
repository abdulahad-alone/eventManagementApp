package com.example.eventapp;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.Models.UserModel;
import com.example.eventapp.Models.favitems;
import com.example.eventapp.adapter.allviewRV;
import com.example.eventapp.detailed_activities.Detailed_Activity_for;
import com.example.eventapp.listeners.ItemListener;
import com.example.eventapp.serviceProvider.DetailedActivityForServices;
import com.example.eventapp.utils.AndroidUtil;
import com.example.eventapp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Vender_store_activity extends AppCompatActivity {
ImageView insert_profileIm,image_slider;
    ActivityResultLauncher<Intent> imagePickLauncher;
    private  int Pick_Image=1;
    RecyclerView favorities_RV;
    CircleImageView profile_image;
    TextView user_name,user_email,user_time_stamp;
    String Username,Email_add;
    Uri selectedImageUri,uri,UriFromStorage;
    String thisStoreUserId;
    private List<ShowingVenueDetails> propertyList;
    private List<favitems> itemsList;
    private allviewRV adapter;
    public String id,userId;
    public Query baseQuery;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_store);
        insert_profileIm=findViewById(R.id.insert_profileIm);
        image_slider=findViewById(R.id.image_slider);
        user_name=findViewById(R.id.user_name);
        user_time_stamp=findViewById(R.id.user_time_stamp);
        user_email=findViewById(R.id.user_email);
        profile_image=findViewById(R.id.profile_image);
        favorities_RV=findViewById(R.id.favorities_RV);
        insert_profileIm.setVisibility(View.GONE);
        thisStoreUserId = getIntent().getStringExtra("thisId");
        Log.e(TAG, "thisStoreUserId: "+thisStoreUserId );
        fetchDataFromFirestore(thisStoreUserId);
        UserDataFromFirestore();
        RecyclerView topDealsRV_ = findViewById(R.id.favorities_RV);
        ProgressBar loadingProgressBar =findViewById(R.id.loadingProgressBar);
        propertyList=new ArrayList<>();
        imagePickLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode()== RESULT_OK){
                        Intent data=result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri=data.getData();
                            Glide.with(Vender_store_activity.this).load(selectedImageUri). centerCrop().into(image_slider);
                            if(selectedImageUri!=null){
                                FirebaseUtil.getCurrentCoverPicStorageRef().putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        AndroidUtil.showToast(Vender_store_activity.this, "Image uploaded Successfully");
                                        fetchDataFromFirestore();
                                        Log.e(TAG, "onComplete: " );
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.e(TAG, "onSuccess: " );
                                        fetchDataFromFirestore();
                                    }
                                });

                            }
                        }
                    }
                }

        );

        insert_profileIm.setOnClickListener(view -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            userId= currentUser.getUid();
            Log.d(TAG, "user id: "+ userId);
            // You can now use the userId variable to access the current user's ID
        } else {
            // No user is currently logged in
        }
         if(thisStoreUserId.equals(userId))
         {
             insert_profileIm.setVisibility(View.VISIBLE);
         }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Products");

        baseQuery = productsRef;
        if (thisStoreUserId != null && !thisStoreUserId.isEmpty()) {
            baseQuery = baseQuery.whereEqualTo("userId", thisStoreUserId);
        }
        baseQuery
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.e(ContentValues.TAG, "Error getting documents: ", task.getException());
                        if(task.isSuccessful())
                        {
                            List<DocumentSnapshot> productlist = task.getResult().getDocuments();
                            for (DocumentSnapshot productDoc : productlist)
                            {
                                Map<String, Object> data = productDoc.getData();
                                Log.d(ContentValues.TAG, "City from Firestore: " + data.get("city"));
                                Log.d(ContentValues.TAG, "Price from Firestore: " + data.get("price"));
                                propertyList.add(new ShowingVenueDetails(
                                        productDoc.getId(),
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
                                        (ArrayList<String>) data.get("features"),
                                        (String) data.get("rating"),
                                        (String) data.get("numberOfRatings")

                                ));

                            }
                            loadingProgressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();


                        }else{
                            Log.e(ContentValues.TAG, "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(ContentValues.TAG, "onFailure: ");
                    }
                });
        adapter=new allviewRV(this, propertyList, new ItemListener() {
            @Override
            public void OnItemPosition(int position) {
                if (propertyList.get(position).getCategory().equals("Photographer")
                        || propertyList.get(position).getCategory().equals("Transportation Service")
                        || propertyList.get(position).getCategory().equals("DJ")
                        || propertyList.get(position).getCategory().equals("Makeup Artist")
                ) {
                    Intent intent = new Intent(Vender_store_activity.this, Detailed_Activity_for.class);
                    intent.putExtra("price", propertyList.get(position).getPrice());
                    intent.putExtra("location", propertyList.get(position).getCity());
                    intent.putExtra("description", propertyList.get(position).getDescription());
                    intent.putExtra("VenueTitle", propertyList.get(position).getVenueTitle());
                    intent.putExtra("id", propertyList.get(position).getId());
                    intent.putExtra("langitute", propertyList.get(position).getLongitude());
                    intent.putExtra("latitude", propertyList.get(position).getLatitude());
                    intent.putExtra("userId", propertyList.get(position).getUserId());
                    intent.putExtra("contactNo", propertyList.get(position).getContactNo());
                    intent.putExtra("category", propertyList.get(position).getCategory());
                    String timeStamp = (FirebaseUtil.formatedTimeStamp(propertyList.get(position).getTimestamp()));
                    intent.putExtra("timeStamp", timeStamp);

                    ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(position).getImages());
                    ArrayList<String> features = new ArrayList<>(propertyList.get(position).getFeatures());
                    intent.putStringArrayListExtra("serviceArrayList", features);
                    intent.putStringArrayListExtra("images", imagesArrayList);
                    intent.putExtra("rating",propertyList.get(position).getRating());
                    intent.putExtra("numberOfRatings",propertyList.get(position).getNumberOfRatings());
                    //intent.putExtra("id",keyitems.get(position).getKey());
                    startActivity(intent);
                }else if(propertyList.get(position).getCategory().equals("Service Only"))
                {
                    Intent intent=new Intent(Vender_store_activity.this, DetailedActivityForServices.class);
                    intent.putExtra("price",propertyList.get(position).getPrice());
                    intent.putExtra("location",propertyList.get(position).getCity());
                    intent.putExtra("description",propertyList.get(position).getDescription());
                    intent.putExtra("VenueTitle",propertyList.get(position).getVenueTitle());
                    intent.putExtra("id",propertyList.get(position).getId());
                    intent.putExtra("langitute",propertyList.get(position).getLongitude());
                    intent.putExtra("latitude",propertyList.get(position).getLatitude());
                    intent.putExtra("userId",propertyList.get(position).getUserId());
                    intent.putExtra("contactNo",propertyList.get(position).getContactNo());
                    intent.putExtra("category",propertyList.get(position).getCategory());
                    String timeStamp=(FirebaseUtil.formatedTimeStamp(propertyList.get(position).getTimestamp()));
                    intent.putExtra("timeStamp",timeStamp);

                    ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(position).getImages());
                    ArrayList<String> features = new ArrayList<>(propertyList.get(position).getFeatures());
                    intent.putStringArrayListExtra("serviceArrayList", features);
                    intent.putStringArrayListExtra("images", imagesArrayList);
                    intent.putExtra("rating",propertyList.get(position).getRating());
                    intent.putExtra("numberOfRatings",propertyList.get(position).getNumberOfRatings());
                    //intent.putExtra("id",keyitems.get(position).getKey());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Vender_store_activity.this, detailed.class);
                    intent.putExtra("price", propertyList.get(position).getPrice());
                    intent.putExtra("location", propertyList.get(position).getCity());
                    intent.putExtra("description", propertyList.get(position).getDescription());
                    intent.putExtra("VenueTitle", propertyList.get(position).getVenueTitle());
                    intent.putExtra("id", propertyList.get(position).getId());
                    intent.putExtra("langitute", propertyList.get(position).getLongitude());
                    intent.putExtra("latitude", propertyList.get(position).getLatitude());
                    intent.putExtra("userId", propertyList.get(position).getUserId());
                    intent.putExtra("contactNo", propertyList.get(position).getContactNo());
                    intent.putExtra("category", propertyList.get(position).getCategory());
                    String timeStamp = (FirebaseUtil.formatedTimeStamp(propertyList.get(position).getTimestamp()));
                    intent.putExtra("timeStamp", timeStamp);
                    intent.putExtra("rating",propertyList.get(position).getRating());
                    intent.putExtra("numberOfRatings",propertyList.get(position).getNumberOfRatings());
                    ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(position).getImages());
                    ArrayList<String> features = new ArrayList<>(propertyList.get(position).getFeatures());
                    intent.putStringArrayListExtra("serviceArrayList", features);
                    intent.putStringArrayListExtra("images", imagesArrayList);

                    //intent.putExtra("id",keyitems.get(position).getKey());
                    startActivity(intent);
                }
            }
        });


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favorities_RV.setLayoutManager(linearLayoutManager);
        favorities_RV.setAdapter(adapter);


    }

    private void fetchDataFromFirestore(String thisStoreUserId) {
        FirebaseUtil.getOtherCoverPicStorageRef(thisStoreUserId).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            UriFromStorage=task.getResult();
                            Log.e(TAG, "image Uri: "+UriFromStorage );
                            AndroidUtil.setCoverPic(Vender_store_activity.this, UriFromStorage, image_slider);
                        }
                    }
                });
    }

    private void UserDataFromFirestore() {
        FirebaseUtil.getOtherProfilePicStorageRef(thisStoreUserId).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            UriFromStorage=task.getResult();
                            Log.e(TAG, "image Uri: "+UriFromStorage );

                            AndroidUtil.setProfilePic(Vender_store_activity.this, UriFromStorage, profile_image);
                        }
                    }
                });


        final UserModel[] otherUser = {new UserModel()};

        //setInProgress(true);
        FirebaseUtil.otherUserDetails(thisStoreUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // setInProgress(false);
                otherUser[0] =task.getResult().toObject(UserModel.class);
                Log.i(TAG, "onComplete: "+otherUser[0].getName());
                user_name.setText(otherUser[0].getName());
                Username=otherUser[0].getName();
                Email_add=otherUser[0].getEmail();
                otherUser[0].getImage();
                user_email.setText(otherUser[0].getEmail());
                Timestamp timestamp = otherUser[0].getCreatedTimestamp();
                String
                BookingtimeStamp = AndroidUtil.formatTimestamp(timestamp);
                user_time_stamp.setText("Member Since "+BookingtimeStamp);



            }
        });
    }


    private void fetchDataFromFirestore() {
        FirebaseUtil.getCurrentCoverPicStorageRef().getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            UriFromStorage=task.getResult();
                            Log.e(TAG, "image Uri: "+UriFromStorage );
                            AndroidUtil.setCoverPic(Vender_store_activity.this, UriFromStorage, image_slider);
                        }else {
                            Log.e(TAG, "no image found: " );
                            Glide.with(Vender_store_activity.this). load(R.drawable.cover_img). centerCrop().into(image_slider);
                        }
                    }
                });


    }
}