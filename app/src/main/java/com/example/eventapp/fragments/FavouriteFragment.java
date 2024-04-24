package com.example.eventapp.fragments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.Models.favitems;
import com.example.eventapp.Models.productList;
import com.example.eventapp.R;
import com.example.eventapp.adapter.allviewRV;
import com.example.eventapp.detailed;

import com.example.eventapp.detailed_activities.Detailed_Activity_for;
import com.example.eventapp.listeners.ItemListener;
import com.example.eventapp.serviceProvider.DetailedActivityForServices;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FavouriteFragment extends Fragment implements ItemListener {
    private List<ShowingVenueDetails> propertyList=new ArrayList<>();
    private List<favitems> itemsList;
    private allviewRV adapter;
    public String id,userId;
    TextView totalAds;
    List<productList> productList=new ArrayList<>();
    String size;
    LinearLayout noBooking,notification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.favourite_fragment, container, false);

        noBooking = view.findViewById(R.id.noBookingFound);
        return  view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView topDealsRV_ = view.findViewById(R.id.favorities_RV);
        totalAds = view.findViewById(R.id.totalAds);

        ProgressBar loadingProgressBar =view.findViewById(R.id.loadingProgressBar);
        totalAds.setVisibility(View.GONE);
        noBooking.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        productList=new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            userId= currentUser.getUid();
            Log.d(TAG, "user id: "+ userId);
            // You can now use the userId variable to access the current user's ID
        } else {
            // No user is currently logged in
        }
        propertyList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to "favorites" collection
        CollectionReference favoritesRef = db.collection("favourites");

        // Query to find favorites of the current user
        Query query = favoritesRef.whereEqualTo("UserId", userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {

                    loadingProgressBar.setVisibility(View.GONE);
                    noBooking.setVisibility(View.VISIBLE);
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String productId = document.getString("PropertyId");

                        // Now, retrieve products based on productId and currentUserId
                        CollectionReference productsRef = db.collection("Products");

                        productsRef
                                .whereEqualTo("id", productId)
                                .get()
                                .addOnCompleteListener(productTask -> {
                                    if (productTask.isSuccessful()) {
                                        List<DocumentSnapshot> productlist = productTask.getResult().getDocuments();
                                        for (DocumentSnapshot productDoc : productlist) {
                                            Map<String, Object> data = productDoc.getData();
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
                                            Log.d(TAG, "onSuccess: " + productList);
                                            adapter.notifyDataSetChanged();
                                            // Retrieve product details matching the criteria
                                            // Access the fields like productDoc.getString("field_name")
                                            // Process the retrieved products here
                                            Log.d(TAG, "onViewCreated: ok" + productDoc.getString("city"));
                                        }
                                        totalAds.setVisibility(View.VISIBLE);
                                        size = String.valueOf(propertyList.size());
                                        totalAds.setText(size + " ads");
                                        loadingProgressBar.setVisibility(View.GONE);

                                    } else {
                                        noBooking.setVisibility(View.VISIBLE);
                                        // Handle errors while retrieving products
                                        Log.d(TAG, "Error getting products: ", productTask.getException());
                                    }
                                });
                    }
                }
            }else{
                    noBooking.setVisibility(View.VISIBLE);
                    // Handle errors while retrieving favorites
                    Log.d(TAG, "Error getting favorites: ", task.getException());
                }

        });

        adapter = new allviewRV(getContext(), propertyList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        topDealsRV_.setLayoutManager(linearLayoutManager);
        topDealsRV_.setAdapter(adapter);


    }

    @Override
    public void OnItemPosition(int position) {
        if (propertyList.get(position).getCategory().equals("Photographer")
                || propertyList.get(position).getCategory().equals("Transportation Service")
                || propertyList.get(position).getCategory().equals("DJ")
                || propertyList.get(position).getCategory().equals("Makeup Artist")
        ) {
            Intent intent = new Intent(getContext(), Detailed_Activity_for.class);
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
            Intent intent=new Intent(getContext(), DetailedActivityForServices.class);
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
            Intent intent = new Intent(getContext(), detailed.class);
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