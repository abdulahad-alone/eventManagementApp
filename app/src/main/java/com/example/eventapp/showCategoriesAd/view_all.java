package com.example.eventapp.showCategoriesAd;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.R;
import com.example.eventapp.adapter.allviewRV;
import com.example.eventapp.detailed;
import com.example.eventapp.detailed_activities.Detailed_Activity_for;
import com.example.eventapp.listeners.ItemListener;
import com.example.eventapp.serviceProvider.DetailedActivityForServices;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class view_all extends AppCompatActivity implements ItemListener {
    private List<ShowingVenueDetails> propertyList=new ArrayList<>();
    private allviewRV adapter;
    public String id,userId;
    TextView totalAds,toptoolBarText;
    String size;
    CardView noBooking;
    String category;
    ImageView close;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_activity);


        RecyclerView topDealsRV_ = findViewById(R.id.favorities_RV);
        totalAds = findViewById(R.id.totalAds);
        noBooking = findViewById(R.id.noBookingFound);
        toptoolBarText = findViewById(R.id.toptoolBarText);
        close = findViewById(R.id.close);
        ProgressBar loadingProgressBar =findViewById(R.id.loadingProgressBar);
        totalAds.setVisibility(View.GONE);
        noBooking.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        propertyList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Products")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        loadingProgressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onSuccess: we are getting the data");
                        List<DocumentSnapshot> snapshotList=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot:snapshotList){
                            Map<String, Object> data = snapshot.getData();
                            propertyList.add(new ShowingVenueDetails(
                                    snapshot.getId(),
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


                            Log.d(TAG, "onSuccess: "+propertyList.get(0).getId());
                            loadingProgressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }

                        totalAds.setVisibility(View.VISIBLE);
                        size = String.valueOf(propertyList.size());
                        totalAds.setText(size + " ads");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e );
                    }
                });
        adapter = new allviewRV(view_all.this, propertyList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view_all.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        topDealsRV_.setLayoutManager(linearLayoutManager);
        topDealsRV_.setAdapter(adapter);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    public void OnItemPosition(int position) {
        if(propertyList.get(position).getCategory().equals("Photographer")
                || propertyList.get(position).getCategory().equals("Transportation Service")
                || propertyList.get(position).getCategory().equals("DJ")
                || propertyList.get(position).getCategory().equals("Makeup Artist")
        )
        {
            Intent intent=new Intent(this, Detailed_Activity_for.class);
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
        }else if(propertyList.get(position).getCategory().equals("Service Only"))
        {

            Intent intent=new Intent(this, DetailedActivityForServices.class);
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
            Intent intent = new Intent(this, detailed.class);
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
}