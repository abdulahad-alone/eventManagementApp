package com.example.eventapp.recommendation;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.Models.productList;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class showRecomendations extends AppCompatActivity implements ItemListener {
    ImageView close;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private  List<ShowingVenueDetails> propertyList=new ArrayList<>();
    StringBuilder csvContent = new StringBuilder();
    StringBuilder csvContent2 = new StringBuilder();
    RecyclerView favorities_RV;
    private allviewRV adapter;
    LinearLayout noBookingFound;
    TextView totalAds;
    List<ShowingVenueDetails> propertyList2=new ArrayList<>();

    private static final double MIN_SIMILARITY_THRESHOLD = 0.1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_recomendations);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        showLoadingBar();

        favorities_RV=findViewById(R.id.favorities_RV);
        close=findViewById(R.id.close);
        totalAds=findViewById(R.id.totalAds);



        noBookingFound=findViewById(R.id.noBookingFound);
        noBookingFound.setVisibility(View.GONE);
        totalAds.setVisibility(View.GONE);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CollectionReference bookingRef = db.collection("Booking");
        HashMap<String, Set<String>> userItemMatrix = new HashMap<>();

        Query query;
        query = bookingRef.whereEqualTo("CurrentUserId", FirebaseUtil.currentuserId());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {

                    } else {
                        csvContent2 = new StringBuilder();
                        csvContent2.append("UserId,ItemId\n");
                        String itemId;
                        String userId;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            itemId  = document.getString("ProductId");
                            userId  = document.getString("CurrentUserId");

                            if (!userItemMatrix.containsKey(userId)) {
                                userItemMatrix.put(userId, new HashSet<>());
                            }
                            userItemMatrix.get(userId).add(itemId);
                            csvContent2.append(userId).append(",")
                                    .append(itemId).append("\n")
                            ;

                        }

                      /*  try {
                            // Write CSV content to a file
                            File file = new File(getExternalFilesDir(null), "booking_data.csv");
                            FileWriter fileWriter = new FileWriter(file);
                            fileWriter.write(csvContent.toString());
                            fileWriter.close();
                            Log.d(TAG, "CSV file exported successfully!");
                        } catch (IOException e) {
                            Log.e(TAG, "Error exporting CSV file: " + e.getMessage());
                        }
                        Log.e(TAG, "onComplete: "+userItemMatrix );
                        String targetUserId = FirebaseUtil.currentuserId();*/
                       /* HashMap<String, Double> userSimilarities = calculateUserSimilarities(targetUserId, userItemMatrix);
                        Set<String> recommendations = generateRecommendations(targetUserId, userItemMatrix, userSimilarities);*/
                        /* Log.d(TAG, "Recommendations for user " + targetUserId + ": " + recommendations);*/

                    }
                    csvContent.append("ID,Category,City,Price,Rating\n");
                    //getting data from products
                    Map<String, Map<String, Object>> productFeaturesMap = new HashMap<>();
                    FirebaseFirestore.getInstance().collection("Products")
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    Log.d(ContentValues.TAG, "onSuccess: we are getting the data");
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
                                                (String) data.get("rating")
                                        ));
                                        String productId = snapshot.getId();
                                        String category = (String) snapshot.get("category");
                                        String city = (String) snapshot.get("city");
                                        String price = (String) snapshot.get("price");
                                        String rating = (String) snapshot.get("rating");
                                        Map<String, Object> productFeatures = new HashMap<>();
                                        productFeatures.put("category", category);
                                        productFeatures.put("city", city);
                                        productFeatures.put("price", price);
                                        productFeatures.put("rating", rating);
                                        productFeaturesMap.put(productId, productFeatures);
                                        csvContent.append(productId).append(",")
                                                .append(category).append(",")
                                                .append(city).append(",")
                                                .append(price).append(",")
                                                .append(rating).append("\n");

                                    }
                                    Log.e(TAG, "csvContent: "+csvContent );
                                    processCSVContent(csvContent,csvContent2);
                                    try {
                                        // Write CSV content to a file
                                        File file = new File(getExternalFilesDir(null), "data.csv");
                                        FileWriter fileWriter = new FileWriter(file);
                                        fileWriter.write(csvContent.toString());
                                        fileWriter.close();
                                        Log.d(TAG, "CSV file exported successfully!");
                                    } catch (IOException e) {
                                        Log.e(TAG, "Error exporting CSV file: " + e.getMessage());
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(ContentValues.TAG, "onFailure: ",e );
                                }
                            });
                    //getting data from booking


                }else
                {
                    Log.d(ContentValues.TAG, "Error getting favorites: ", task.getException());
                }
            }
        });






        File csvFile = new File(getExternalFilesDir(null), "data.csv");
        String csvFilePath = csvFile.getAbsolutePath();

// Log the path to the CSV file
        Log.d(TAG, "CSV file path: " + csvFilePath);


        // "context" must be an Activity, Service or Application object from your app.

    }
    private void processCSVContent(StringBuilder csvContent,StringBuilder csvContent2) {

            String csvString = csvContent.toString();
            String csvString2 = csvContent2.toString();
        Log.e(TAG, "csvContent2: "+csvContent2 );

            if(!csvString2.isEmpty())
            {
            Python py = Python.getInstance();
            PyObject result=   py.getModule("script").callAttr("process_csv_content", csvString,csvString2);
            List<String> recommendedIdsList = new ArrayList<>();

            // Iterate over the PyObjects and convert them to Java Strings
            for (PyObject obj : result.asList()) {
                recommendedIdsList.add(obj.toString());
            }

            // Convert the list to an array of strings (recommended_ids)
            String[] recommendedIds = recommendedIdsList.toArray(new String[0]);
                String size= String.valueOf(recommendedIdsList.size());
                totalAds.setText(size+" ads");
                getDataFromFirestore(recommendedIds);

            Log.e(TAG, "recommendedIdsList: "+recommendedIdsList );
            Log.e(TAG, "recommendedIds: "+recommendedIds.toString() );
            }else
            {
                dismissLoadingBar();
                noBookingFound.setVisibility(View.VISIBLE);
                Log.e(TAG, "processCSVContent: Empty " );
            }


    }

    private void getDataFromFirestore(String[] recommendedIds) {
        totalAds.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (String id : recommendedIds) {
            // Query Firestore for document with the specified ID
            db.collection("Products")
                    .document(id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            dismissLoadingBar();
                            if (documentSnapshot.exists()) {
                                // Document exists, you can retrieve its data here
                                Map<String, Object> data = documentSnapshot.getData();

                                propertyList2.add(new ShowingVenueDetails(
                                        documentSnapshot.getId(),
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
                                        (ArrayList<String>) data.get("features"))
                                        );
                                // Process the data as needed
                                Log.d(TAG, "Data for ID " + id + ": " + data);
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "No such document for ID " + id);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error getting document for ID " + id, e);
                        }
                    });
        }

        adapter = new allviewRV(showRecomendations.this, propertyList2,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favorities_RV.setLayoutManager(linearLayoutManager);
        favorities_RV.setAdapter(adapter);
    }

    private void showLoadingBar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissLoadingBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void OnItemPosition(int position) {


        if(propertyList2.get(position).getCategory().equals("Photographer") || propertyList2.get(position).getCategory().equals("Transportation Service")) {
            Intent intent=new Intent(this, Detailed_Activity_for.class);
            intent.putExtra("price",propertyList2.get(position).getPrice());
            intent.putExtra("location",propertyList2.get(position).getCity());
            intent.putExtra("description",propertyList2.get(position).getDescription());
            intent.putExtra("VenueTitle",propertyList2.get(position).getVenueTitle());
            intent.putExtra("id",propertyList2.get(position).getId());
            intent.putExtra("langitute",propertyList2.get(position).getLongitude());
            intent.putExtra("latitude",propertyList2.get(position).getLatitude());
            intent.putExtra("userId",propertyList2.get(position).getUserId());
            intent.putExtra("contactNo",propertyList2.get(position).getContactNo());
            intent.putExtra("category",propertyList2.get(position).getCategory());
            String timeStamp=(FirebaseUtil.formatedTimeStamp(propertyList2.get(position).getTimestamp()));
            intent.putExtra("timeStamp",timeStamp);

            ArrayList<String> imagesArrayList = new ArrayList<>(propertyList2.get(position).getImages());
            ArrayList<String> features = new ArrayList<>(propertyList2.get(position).getFeatures());
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

            ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(position).getImages());
            ArrayList<String> features = new ArrayList<>(propertyList.get(position).getFeatures());
            intent.putStringArrayListExtra("serviceArrayList", features);
            intent.putStringArrayListExtra("images", imagesArrayList);

            //intent.putExtra("id",keyitems.get(position).getKey());
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, detailed.class);
            intent.putExtra("price", propertyList2.get(position).getPrice());
            intent.putExtra("location", propertyList2.get(position).getCity());
            intent.putExtra("description", propertyList2.get(position).getDescription());
            intent.putExtra("VenueTitle", propertyList2.get(position).getVenueTitle());
            intent.putExtra("id", propertyList2.get(position).getId());
            intent.putExtra("langitute", propertyList2.get(position).getLongitude());
            intent.putExtra("latitude", propertyList2.get(position).getLatitude());
            intent.putExtra("userId", propertyList2.get(position).getUserId());
            intent.putExtra("contactNo", propertyList2.get(position).getContactNo());
            intent.putExtra("category", propertyList2.get(position).getCategory());
            String timeStamp = (FirebaseUtil.formatedTimeStamp(propertyList2.get(position).getTimestamp()));
            intent.putExtra("timeStamp", timeStamp);

            ArrayList<String> imagesArrayList = new ArrayList<>(propertyList2.get(position).getImages());
            ArrayList<String> features = new ArrayList<>(propertyList2.get(position).getFeatures());
            intent.putStringArrayListExtra("serviceArrayList", features);
            intent.putStringArrayListExtra("images", imagesArrayList);

            //intent.putExtra("id",keyitems.get(position).getKey());
            startActivity(intent);
        }
    }

}
