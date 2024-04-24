package com.example.eventapp.fragments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.R;
import com.example.eventapp.detailed;
import com.example.eventapp.detailed_activities.Detailed_Activity_for;
import com.example.eventapp.serviceProvider.DetailedActivityForServices;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyBottomSheetMapFragment  extends BottomSheetDialogFragment {
    private String propertyId;
    CardView ShowAd;
    String firstImageUrl,pri,tit;
    TextView price,title;
    private List<ShowingVenueDetails> propertyList;
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.my_bottom_sheet_map_fragment, container, false);
        return view;

    }

    public static MyBottomSheetMapFragment newInstance(String propertyId) {
        MyBottomSheetMapFragment fragment = new MyBottomSheetMapFragment();
        Bundle args = new Bundle();
        args.putString("propertyId", propertyId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "setPropertyId: "+propertyId );
        propertyList=new ArrayList<>();
        ProgressBar loadingProgressBar =view.findViewById(R.id.loadingProgressBar);
        loadingProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Products");
        ImageView imageView=view.findViewById(R.id.house_img);
        price=view.findViewById(R.id.price_of_house);
        title=view.findViewById(R.id.title_of_house);
        productsRef.whereEqualTo("id", propertyId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        Map<String, Object> data = document.getData();
                        List<String> imageUrls = (List<String>) document.get("images");
                        propertyList.add(new ShowingVenueDetails(
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
                                (ArrayList<String>) data.get("features"),
                                (String) data.get("rating"),
                                (String) data.get("numberOfRatings")

                        ));
                        // Log.d(TAG, "onViewCreated: ok"+productList.get(0).getId());
                        //Log.d(TAG, "onViewCreated: ok"+productList.get(0).getPrice());
                        loadingProgressBar.setVisibility(View.GONE);
                        firstImageUrl =  imageUrls.get(0);
                        pri=propertyList.get(0).getPrice();
                        tit=propertyList.get(0).getCategory();
                    }
                    price.setText(pri);
                    String lol=propertyList.get(0).getCategory()+" in "+propertyList.get(0).getCity();
                    title.setText(lol);
                    Glide.with(getContext()).load(firstImageUrl).into(imageView);
                    Log.e(TAG, "imageUri: "+firstImageUrl );
                    Log.e(TAG, "price: "+pri );
                    Log.e(TAG, "title: "+tit );
                }else
                {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " );
            }
        });





        ShowAd=view.findViewById(R.id.show_ad);
        ShowAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(propertyList.get(0).getCategory().equals("Photographer")
                        || propertyList.get(0).getCategory().equals("Transportation Service")
                        || propertyList.get(0).getCategory().equals("DJ")
                        || propertyList.get(0).getCategory().equals("Makeup Artist")
                )
                {
                    Intent intent=new Intent(getContext(), Detailed_Activity_for.class);
                    intent.putExtra("price",propertyList.get(0).getPrice());
                    intent.putExtra("location",propertyList.get(0).getCity());
                    intent.putExtra("description",propertyList.get(0).getDescription());
                    intent.putExtra("VenueTitle",propertyList.get(0).getVenueTitle());
                    intent.putExtra("id",propertyList.get(0).getId());
                    intent.putExtra("langitute",propertyList.get(0).getLongitude());
                    intent.putExtra("latitude",propertyList.get(0).getLatitude());
                    intent.putExtra("userId",propertyList.get(0).getUserId());
                    intent.putExtra("contactNo",propertyList.get(0).getContactNo());
                    intent.putExtra("category",propertyList.get(0).getCategory());
                    String timeStamp=(FirebaseUtil.formatedTimeStamp(propertyList.get(0).getTimestamp()));
                    intent.putExtra("timeStamp",timeStamp);
                    intent.putExtra("rating",propertyList.get(0).getRating());
                    intent.putExtra("numberOfRatings",propertyList.get(0).getNumberOfRatings());
                    ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(0).getImages());
                    ArrayList<String> features = new ArrayList<>(propertyList.get(0).getFeatures());
                    intent.putStringArrayListExtra("serviceArrayList", features);
                    intent.putStringArrayListExtra("images", imagesArrayList);

                    //intent.putExtra("id",keyitems.get(0).getKey());
                    startActivity(intent);
                }else if(propertyList.get(0).getCategory().equals("Service Only"))
                {

                    Intent intent=new Intent(getContext(), DetailedActivityForServices.class);
                    intent.putExtra("price",propertyList.get(0).getPrice());
                    intent.putExtra("location",propertyList.get(0).getCity());
                    intent.putExtra("description",propertyList.get(0).getDescription());
                    intent.putExtra("VenueTitle",propertyList.get(0).getVenueTitle());
                    intent.putExtra("id",propertyList.get(0).getId());
                    intent.putExtra("langitute",propertyList.get(0).getLongitude());
                    intent.putExtra("latitude",propertyList.get(0).getLatitude());
                    intent.putExtra("userId",propertyList.get(0).getUserId());
                    intent.putExtra("contactNo",propertyList.get(0).getContactNo());
                    intent.putExtra("category",propertyList.get(0).getCategory());
                    String timeStamp=(FirebaseUtil.formatedTimeStamp(propertyList.get(0).getTimestamp()));
                    intent.putExtra("timeStamp",timeStamp);
                    intent.putExtra("rating",propertyList.get(0).getRating());
                    intent.putExtra("numberOfRatings",propertyList.get(0).getNumberOfRatings());
                    ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(0).getImages());
                    ArrayList<String> features = new ArrayList<>(propertyList.get(0).getFeatures());
                    intent.putStringArrayListExtra("serviceArrayList", features);
                    intent.putStringArrayListExtra("images", imagesArrayList);

                    //intent.putExtra("id",keyitems.get(0).getKey());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getContext(), detailed.class);
                    intent.putExtra("price", propertyList.get(0).getPrice());
                    intent.putExtra("location", propertyList.get(0).getCity());
                    intent.putExtra("description", propertyList.get(0).getDescription());
                    intent.putExtra("VenueTitle", propertyList.get(0).getVenueTitle());
                    intent.putExtra("id", propertyList.get(0).getId());
                    intent.putExtra("langitute", propertyList.get(0).getLongitude());
                    intent.putExtra("latitude", propertyList.get(0).getLatitude());
                    intent.putExtra("userId", propertyList.get(0).getUserId());
                    intent.putExtra("contactNo", propertyList.get(0).getContactNo());
                    intent.putExtra("category", propertyList.get(0).getCategory());
                    String timeStamp = (FirebaseUtil.formatedTimeStamp(propertyList.get(0).getTimestamp()));
                    intent.putExtra("timeStamp", timeStamp);
                    intent.putExtra("rating",propertyList.get(0).getRating());
                    intent.putExtra("numberOfRatings",propertyList.get(0).getNumberOfRatings());
                    ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(0).getImages());
                    ArrayList<String> features = new ArrayList<>(propertyList.get(0).getFeatures());
                    intent.putStringArrayListExtra("serviceArrayList", features);
                    intent.putStringArrayListExtra("images", imagesArrayList);

                    //intent.putExtra("id",keyitems.get(0).getKey());
                    startActivity(intent);
                }
            }
        });
    }
}