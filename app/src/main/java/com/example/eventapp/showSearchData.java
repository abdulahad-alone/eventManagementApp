package com.example.eventapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.adapter.allviewRV;
import com.example.eventapp.detailed_activities.Detailed_Activity_for;
import com.example.eventapp.listeners.ItemListener;
import com.example.eventapp.serviceProvider.DetailedActivityForServices;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class showSearchData extends AppCompatActivity {
    String PriceStart,PriceEnd,purpose,AreaSize,AreaUnit,Type,City;
    private List<ShowingVenueDetails> propertyList;
    private allviewRV adapter;
    public Query baseQuery;
    ProgressBar loadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_search_data);
        propertyList=new ArrayList<>();
        PriceStart=getIntent().getStringExtra("priceRangeStart");
        PriceEnd=getIntent().getStringExtra("priceRangeEnd");
        Type=getIntent().getStringExtra("type");
        City=getIntent().getStringExtra("city");

        loadingProgressBar =findViewById(R.id.loadingProgressBar);
        RecyclerView recyclerView=findViewById(R.id.showSearch_RV);

        loadingProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Products");
        baseQuery = productsRef;
        if (City != null && !City.isEmpty()) {
            baseQuery = baseQuery.whereEqualTo("city", City);
            Log.e(ContentValues.TAG, "onCreate: "+ City );
        }
        if (Type != null && !Type.isEmpty()) {
            baseQuery = baseQuery.whereEqualTo("category", Type);
            Log.e(ContentValues.TAG, "onCreate: "+ Type );
        }
        if (PriceStart != null && !PriceStart.isEmpty() && PriceEnd != null && !PriceEnd.isEmpty()) {
            baseQuery = baseQuery.whereGreaterThanOrEqualTo("price", PriceStart.trim())
                    .whereLessThanOrEqualTo("price", PriceEnd.trim());
            Log.e(TAG, "PriceStart: "+PriceStart );
            Log.e(TAG, "PriceEnd: "+PriceEnd );
        }
        baseQuery
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                Log.d(ContentValues.TAG, "onViewCreated: ok"+productDoc.getString("type"));
                                Log.d(ContentValues.TAG, "onViewCreated: ok"+productDoc.getString("city"));
                                Log.d(ContentValues.TAG, "onViewCreated: ok"+productDoc.getString("title"));
                                Log.d(ContentValues.TAG, "onViewCreated: ok"+productDoc.getString("price"));

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
                if(propertyList.get(position).getCategory().equals("Photographer")
                        || propertyList.get(position).getCategory().equals("Transportation Service")
                        || propertyList.get(position).getCategory().equals("Makeup Artist")
                        || propertyList.get(position).getCategory().equals("DJ")
                )
                {
                    Intent intent=new Intent(showSearchData.this, Detailed_Activity_for.class);
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
                }else if(propertyList.get(position).getCategory().equals("Service Only"))
                {
                    Intent intent=new Intent(showSearchData.this, DetailedActivityForServices.class);
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
                    intent.putExtra("rating",propertyList.get(position).getRating());
                    intent.putExtra("numberOfRatings",propertyList.get(position).getNumberOfRatings());
                    ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(position).getImages());
                    ArrayList<String> features = new ArrayList<>(propertyList.get(position).getFeatures());
                    intent.putStringArrayListExtra("serviceArrayList", features);
                    intent.putStringArrayListExtra("images", imagesArrayList);

                    //intent.putExtra("id",keyitems.get(position).getKey());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(showSearchData.this, detailed.class);
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
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}