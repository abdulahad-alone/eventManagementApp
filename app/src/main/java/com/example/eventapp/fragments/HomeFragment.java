package com.example.eventapp.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static com.example.eventapp.utils.FirebaseUtil.getUserType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eventapp.MainActivity;
import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.Models.UserModel;
import com.example.eventapp.Models.VenueDetails;
import com.example.eventapp.Models.product;
import com.example.eventapp.R;
import com.example.eventapp.ShowProductsOnMap;
import com.example.eventapp.adapter.HomeAdapter;
import com.example.eventapp.adposting;
import com.example.eventapp.bookedAd;
import com.example.eventapp.detailed;
import com.example.eventapp.detailed_activities.Detailed_Activity_for;
import com.example.eventapp.listeners.ItemListener;
import com.example.eventapp.recommendation.showRecomendations;
import com.example.eventapp.serviceProvider.DetailedActivityForServices;
import com.example.eventapp.showCategoriesAd.showCategoriesAd;
import com.example.eventapp.showCategoriesAd.view_all;
import com.example.eventapp.signup;
import com.example.eventapp.utils.AndroidUtil;
import com.example.eventapp.utils.FirebaseUtil;
import com.example.eventapp.vendor_home_activity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements ItemListener, View.OnClickListener {
    private  List<ShowingVenueDetails> propertyList;
    private HomeAdapter adapter;
    private RecyclerView topDealsRV;
    ProgressBar loadingProgressBar;
    TextView userName, View_all;
    TextView search;
    private CircleImageView profileImg;
   CardView mapper,show_recomendation,show_booking,post_ad;
    LinearLayout notification;
   RelativeLayout show_marrquee,showCatering,Banquetshow,dj,makeup,photographerShow,transport,services_only;


    private static final int MAP_REQUEST_CODE = 123;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName=view.findViewById(R.id.textView);
        mapper=view.findViewById(R.id.mapper);
        profileImg=view.findViewById(R.id.profile_image);
        propertyList=new ArrayList<>();
        topDealsRV=view.findViewById(R.id.top_deals_RV);
        show_recomendation=view.findViewById(R.id.show_recomendation);
        loadingProgressBar =view.findViewById(R.id.loadingProgressBar);
        show_booking =view.findViewById(R.id.show_booking);
        View_all =view.findViewById(R.id.view_all);
        services_only =view.findViewById(R.id.services_only);
        show_marrquee =view.findViewById(R.id.show_marrquee);
        showCatering =view.findViewById(R.id.showCatering);
        notification = view.findViewById(R.id.notification);
        Banquetshow =view.findViewById(R.id.Banquetshow);
        post_ad =view.findViewById(R.id.post_ad);
        dj =view.findViewById(R.id.dj);
        makeup =view.findViewById(R.id.makeup);
        photographerShow =view.findViewById(R.id.photographerShow);
        transport =view.findViewById(R.id.transport);
        notification.setVisibility(View.GONE);
        show_marrquee.setOnClickListener(this);
        showCatering.setOnClickListener(this);
        Banquetshow.setOnClickListener(this);
        dj.setOnClickListener(this);
        makeup.setOnClickListener(this);
        photographerShow.setOnClickListener(this);
        transport.setOnClickListener(this);
        View_all.setOnClickListener(this);
        services_only.setOnClickListener(this);


        adapter = new HomeAdapter(getContext(), propertyList, HomeFragment.this);
        topDealsRV.setAdapter(adapter);
        loadingProgressBar.setVisibility(View.VISIBLE);
        search=view.findViewById(R.id.search_edit);
      fetchDataFromStorage();
        checkingUserType();


        post_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), adposting.class);
                startActivity(intent);
            }
        });
        mapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapActivity();
            }
        });
        show_recomendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(getContext(),
                        showRecomendations.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore dbo = FirebaseFirestore.getInstance();
                CollectionReference usersRef = dbo.collection("Booking");

                Query query = usersRef.whereEqualTo("Notification", true).whereEqualTo("ProductUserId", FirebaseUtil.currentuserId());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String bookingId = document.getString("ProductId");
                                // Update the "Notification" field to false
                                document.getReference().update("Notification", false)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "Notification flag set to false for document: " + document.getId());
                                                notification.setVisibility(View.GONE);
                                                Intent intent
                                                        = new Intent(getContext(),
                                                        bookedAd.class);
                                                intent.putExtra("notification", true);
                                                intent.putExtra("bookingId", bookingId);
                                                startActivity(intent);
                                            }
                                        }) .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "Error updating document: " + document.getId(), e);
                                            }
                                        });

                            }
                        }else{
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

            }
        });
        show_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent
                        = new Intent(getContext(),
                        bookedAd.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBottomSheetSearchFragment bottomSheetFragment = new MyBottomSheetSearchFragment();
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
            }
        });





    }
    @Override
    public void onResume() {
        super.onResume();
        fetchDataFromFirestore(); // Replace this with your data retrieval logic
        fetchDataFromStorage();
        checkingUserType();
        fetchNotification();
        Log.e(TAG, "onResume: " );
    }

    private void checkingUserType() {
        getUserType().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String typeValue) {
                if (typeValue != null && typeValue.equals("Customer")) {
                    // User type is "vendor", make the button invisible
                    post_ad.setVisibility(View.GONE);
                } else {
                    // Type value is not available
                    post_ad.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void fetchNotification() {
        FirebaseFirestore dbo = FirebaseFirestore.getInstance();
        CollectionReference usersRef = dbo.collection("Booking");

        Query query = usersRef.whereEqualTo("Notification", true).whereEqualTo("ProductUserId", FirebaseUtil.currentuserId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        boolean isNotificationEnabled = document.getBoolean("Notification");
                        if (isNotificationEnabled) {
                            // Highlight the image view
                            notification.setVisibility(View.VISIBLE);
                            // Assuming you want to highlight only one image view, you can break the loop
                            break;
                        }
                    }
                }
            }
        });
    }

    private void fetchDataFromStorage() {
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Uri uri=task.getResult();

                            AndroidUtil.setProfilePic(getContext(), uri, profileImg);
                        }
                    }
                });
    }
    private void fetchDataFromFirestore() {
        propertyList.clear();

        final UserModel[] otherUser = {new UserModel()};
        FirebaseUtil.currentUserDetials().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                otherUser[0] =task.getResult().toObject(UserModel.class);
                Log.i(TAG, "onComplete: "+otherUser[0].getName());
                userName.setText(otherUser[0].getName());
            }
        });



        FirebaseFirestore.getInstance().collection("Products")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e );
                    }
                });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topDealsRV.setLayoutManager(linearLayoutManager);

    }
    @Override
    public void OnItemPosition(int position) {
        if(propertyList.get(position).getCategory().equals("Photographer")
                || propertyList.get(position).getCategory().equals("Transportation Service")
                || propertyList.get(position).getCategory().equals("DJ")
                || propertyList.get(position).getCategory().equals("Makeup Artist")
        )
        {
            Intent intent=new Intent(getContext(), Detailed_Activity_for.class);
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
            intent.putExtra("rating",propertyList.get(position).getRating());
            intent.putExtra("numberOfRatings",propertyList.get(position).getNumberOfRatings());
            String timeStamp=(FirebaseUtil.formatedTimeStamp(propertyList.get(position).getTimestamp()));
            intent.putExtra("timeStamp",timeStamp);

            ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(position).getImages());
            ArrayList<String> features = new ArrayList<>(propertyList.get(position).getFeatures());
            intent.putStringArrayListExtra("serviceArrayList", features);
            intent.putStringArrayListExtra("images", imagesArrayList);

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
            intent.putExtra("rating",propertyList.get(position).getRating());
            intent.putExtra("numberOfRatings",propertyList.get(position).getNumberOfRatings());
            String timeStamp = (FirebaseUtil.formatedTimeStamp(propertyList.get(position).getTimestamp()));
            intent.putExtra("timeStamp", timeStamp);

            ArrayList<String> imagesArrayList = new ArrayList<>(propertyList.get(position).getImages());
            ArrayList<String> features = new ArrayList<>(propertyList.get(position).getFeatures());
            intent.putStringArrayListExtra("serviceArrayList", features);
            intent.putStringArrayListExtra("images", imagesArrayList);

            //intent.putExtra("id",keyitems.get(position).getKey());
            startActivity(intent);
        }
    }

    private void openMapActivity() {
        Intent intent = new Intent(getContext(), ShowProductsOnMap.class);
        startActivityForResult(intent, MAP_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            double latitude = data.getDoubleExtra("latitude", 0.0);
            double longitude = data.getDoubleExtra("longitude", 0.0);
            String location = "Latitude: " + latitude + ", Longitude: " + longitude;
            // Use the latitude and longitude in your AdPostingActivity
        }
    }

    @Override
    public void onClick(View view) {
        if(view==show_marrquee)
        {
            Intent intent = new Intent(getContext(), showCategoriesAd.class);
            intent.putExtra("category", "Marrquee");
            startActivity(intent);
        }if(view==showCatering)
        {
            Intent intent = new Intent(getContext(), showCategoriesAd.class);
            intent.putExtra("category", "Catering");
            startActivity(intent);
        }if(view==Banquetshow)
        {
            Intent intent = new Intent(getContext(), showCategoriesAd.class);
            intent.putExtra("category", "Banquet Hall");
            startActivity(intent);
        }if(view==dj)
        {
            Intent intent = new Intent(getContext(), showCategoriesAd.class);
            intent.putExtra("category", "DJ");
            startActivity(intent);
        }if(view==makeup)
        {
            Intent intent = new Intent(getContext(), showCategoriesAd.class);
            intent.putExtra("category", "Makeup Artist");
            startActivity(intent);
        }if(view==photographerShow)
        {
            Intent intent = new Intent(getContext(), showCategoriesAd.class);
            intent.putExtra("category", "Photographer");
            startActivity(intent);
        }if(view==transport)
        {
            Intent intent = new Intent(getContext(), showCategoriesAd.class);
            intent.putExtra("category", "Transportation Service");
            startActivity(intent);
        }if(view==services_only)
        {
            Intent intent = new Intent(getContext(), showCategoriesAd.class);
            intent.putExtra("category", "Service Only");
            startActivity(intent);
        }if(view==View_all)
        {
            Intent intent = new Intent(getContext(), view_all.class);
            startActivity(intent);
        }
    }
}