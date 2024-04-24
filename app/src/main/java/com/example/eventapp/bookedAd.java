package com.example.eventapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.eventapp.utils.FirebaseUtil.getUserType;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eventapp.AdapterBooked.DaigBookAdapter;
import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.activities.DaigModelForBooked;
import com.example.eventapp.adapter.ShowBookedAdAdapter;
import com.example.eventapp.detailed_activities.BookedPackageAdapter;
import com.example.eventapp.detailed_activities.BookedPackageModel;
import com.example.eventapp.listeners.ItemListener;
import com.example.eventapp.service.ServiceBookAdapter;
import com.example.eventapp.service.serviceModel;
import com.example.eventapp.serviceProvider.ServiceProviderModel;
import com.example.eventapp.serviceProvider.serviceProviderAdapter;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class bookedAd extends AppCompatActivity implements ItemListener {
    RecyclerView bookedAd_RV, bookedAd_RV_daig,bookedAd_RV_service,bookedAd_RV_package,bookedAd_RV_serviceOnly;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId,productID;
    CardView noBookingFound;
    boolean notification=false;
    private ShowBookedAdAdapter adapter;
    private DaigBookAdapter daigBookAdapter;
    private ServiceBookAdapter serviceBookAdapter;
    private BookedPackageAdapter bookedPackageAdapter;
    private serviceProviderAdapter serviceProviderAdapter;
    ProgressBar loadingProgressBar;
    TextView services_only;
    private List<ShowingVenueDetails> propertyList;
    private List<DaigModelForBooked> daigLint;
    private List<serviceModel> serviceList;
    private List<BookedPackageModel> packageList;
    private List<ServiceProviderModel> ServiceForOnly;
    ArrayList<String> servicesLists = new ArrayList<>();
    ArrayList<String> servicesLists2 = new ArrayList<>();
    ArrayList<String> servicesDateLists = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked_ad);
        bookedAd_RV = findViewById(R.id.bookedAd_RV);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        bookedAd_RV_daig = findViewById(R.id.bookedAd_RV_daig);
        bookedAd_RV_serviceOnly = findViewById(R.id.bookedAd_RV_serviceOnly);
        bookedAd_RV_service = findViewById(R.id.bookedAd_RV_service);
        bookedAd_RV_package = findViewById(R.id.bookedAd_RV_package);
        noBookingFound = findViewById(R.id.noBookingFound);
        services_only = findViewById(R.id.services_only);
        noBookingFound.setVisibility(View.GONE);
        services_only.setVisibility(View.GONE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            Log.d(ContentValues.TAG, "user id: " + userId);
            // You can now use the userId variable to access the current user's ID
        } else {
            // No user is currently logged in
        }
        if (getIntent().hasExtra("notification")) {
            notification = getIntent().getBooleanExtra("notification", true);// Replace `false` with the default value you want to use if the key is not found
            productID=getIntent().getStringExtra("bookingId");
            Log.e(TAG, "onCreate: notification" );
        }
        loadingProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        propertyList = new ArrayList<>();
        daigLint = new ArrayList<>();
        serviceList = new ArrayList<>();
        packageList = new ArrayList<>();
        ServiceForOnly = new ArrayList<>();
// Reference to "favorites" collection
        CollectionReference favoritesRef = db.collection("Booking");
        getUserType().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String typeValue) {
                Query query;
                if (typeValue != null && typeValue.equals("Customer")) {
                    // User type is "vendor", make the button invisible
                    query = favoritesRef.whereEqualTo("CurrentUserId", userId);
                } else {
                    // Type value is not available
                    query = favoritesRef.whereEqualTo("ProductUserId", userId);

                }

                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {

                            loadingProgressBar.setVisibility(View.GONE);
                            noBookingFound.setVisibility(View.VISIBLE);
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String productId = document.getString("ProductId");
                                String Type_of_ad = document.getString("Type_of_ad");
                                Log.e(TAG, "onSuccess: " );

                                if (Type_of_ad.equals("Marrquee") || Type_of_ad.equals("Banquet Hall")) {
                                    Log.e(TAG, "onSuccess: ");
                                    getting_marrquee_banquatHall(productId);
                                }
                                if (Type_of_ad.equals("Daig")) {

                                    String Id = document.getString("documentId");
                                    String ProductId = document.getString("ProductId");
                                    getting_daig(Id, ProductId);
                                }
                                if (Type_of_ad.equals("Service")) {
                                    String Id = document.getString("documentId");
                                    String ProductId = document.getString("ProductId");
                                    getting_service(Id, ProductId);
                                }if(Type_of_ad.equals("Package"))
                                {
                                    String Id = document.getString("documentId");
                                    String ProductId = document.getString("ProductId");
                                    getting_package(Id, ProductId);
                                }if(Type_of_ad.equals("Service Only"))
                                {
                                    String Id = document.getString("documentId");
                                    String ProductId = document.getString("ProductId");
                                    getting_Service_only(Id, ProductId);
                                }

                                // Now, retrieve products based on productId and currentUserId

                            }
                        }
                    } else {
                        // Handle errors while retrieving favorites
                        Log.d(ContentValues.TAG, "Error getting favorites: ", task.getException());
                    }
                });
            }
        });
        Log.e(TAG, "notification: "+notification );
        Log.e(TAG, "productID: "+productID );

        adapter = new ShowBookedAdAdapter(this, propertyList, this,notification,productID);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookedAd_RV.setLayoutManager(linearLayoutManager);
        bookedAd_RV.setAdapter(adapter);


        daigBookAdapter = new DaigBookAdapter(this, daigLint, this,notification,productID);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        bookedAd_RV_daig.setLayoutManager(linearLayoutManager1);
        bookedAd_RV_daig.setAdapter(daigBookAdapter);


        serviceBookAdapter = new ServiceBookAdapter(this, serviceList, this,notification,productID);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        bookedAd_RV_service.setLayoutManager(linearLayoutManager2);
        bookedAd_RV_service.setAdapter(serviceBookAdapter);

        bookedPackageAdapter = new BookedPackageAdapter(this, packageList, this,notification,productID);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        bookedAd_RV_package.setLayoutManager(linearLayoutManager3);
        bookedAd_RV_package.setAdapter(bookedPackageAdapter);

        serviceProviderAdapter = new serviceProviderAdapter(this, ServiceForOnly, this,notification,productID);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this);
        linearLayoutManager4.setOrientation(LinearLayoutManager.VERTICAL);
        bookedAd_RV_serviceOnly.setLayoutManager(linearLayoutManager4);
        bookedAd_RV_serviceOnly.setAdapter(serviceProviderAdapter);




    }

    private void getting_Service_only(String id, String productId) {
        CollectionReference productsRef = db.collection("Booking");
        productsRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Id = document.getString("documentId");
                        String Totalprice = document.getString("Price");
                        String Budget = document.getString("Budget");
                        String CurrentUserId = document.getString("CurrentUserId");
                        String ProductId = document.getString("ProductId");
                        String this_product_userId = document.getString("ProductUserId");
                        servicesLists2=(ArrayList<String>) document.get("ServicesEditList");
                        servicesDateLists=(ArrayList<String>) document.get("selectedDates");
                        String phone = document.getString("phone");
                        String FullName = document.getString("FullName");
                        String address = document.getString("address");
                        Timestamp timeStamp = document.getTimestamp("timeStamp");
                        String Type_of_ad = document.getString("Type_of_ad");
                        String imageUrl = document.getString("imageUrl");
                        String orderNumber = document.getString("orderNumber");
                        boolean Notification = document.getBoolean("Notification");
                        String Daigs = document.getString("Daigs");
                        String Type = document.getString("Type");
                        String event = document.getString("event");
                        String noOfPersons = document.getString("noOfPersons");
                        String imageUri = document.getString("imageUri");

                        ServiceForOnly.add(new ServiceProviderModel(Id,Totalprice,Budget,CurrentUserId,
                                ProductId,this_product_userId,servicesLists2,servicesDateLists,
                                phone,FullName,address,timeStamp,Type_of_ad,imageUrl,
                                orderNumber,Notification,Daigs,Type,event,noOfPersons,imageUri));

                    }
                    loadingProgressBar.setVisibility(View.GONE);
                    services_only.setVisibility(View.VISIBLE);
                    serviceProviderAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getting_package(String id, String productId) {
        CollectionReference productsRef = db.collection("Booking");
        productsRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Id = document.getString("documentId");
                        String Totalprice = document.getString("priceOfPackage");
                        String CurrentUserId = document.getString("CurrentUserId");
                        String ProductId = document.getString("ProductId");
                        String this_product_userId = document.getString("ProductUserId");

                        servicesLists=(ArrayList<String>) document.get("serviceList");

                        String Title = document.getString("nameOfPackage");
                        String description = document.getString("description");
                        String PackageRef = document.getString("PackageRef");
                        String phone = document.getString("phone");
                        String FullName = document.getString("FullName");
                        String address = document.getString("address");
                        Timestamp timeStamp = document.getTimestamp("timeStamp");
                        String Type_of_ad = document.getString("Type_of_ad");
                        String imageUrl = document.getString("imageUrl");
                        String orderNumber = document.getString("orderNumber");
                        String BookingDate = document.getString("BookingDate");
                        String BookingTime = document.getString("BookingTime");
                        packageList.add(new BookedPackageModel(PackageRef,description,Title,Totalprice,Id,servicesLists,CurrentUserId
                        ,ProductId,this_product_userId,BookingDate,BookingTime,phone,FullName,address,timeStamp,Type_of_ad,
                                imageUrl,orderNumber));
                    }
                    loadingProgressBar.setVisibility(View.GONE);
                     bookedPackageAdapter.notifyDataSetChanged();
                }else
                {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });
    }

    private void getting_service(String id, String productId) {
        CollectionReference productsRef = db.collection("Booking");
        productsRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Id = document.getString("documentId");
                        String Totalprice = document.getString("Totalprice");
                        String CurrentUserId = document.getString("CurrentUserId");
                        String ProductId = document.getString("ProductId");
                        String this_product_userId = document.getString("ProductUserId");
                        ArrayList<String> datesarray = new ArrayList<>();
                        datesarray=(ArrayList<String>) document.get("BookingDates");
                        String Title = document.getString("Title");
                        String description = document.getString("description");
                        String ServiceRef = document.getString("ServiceRef");
                        String phone = document.getString("phone");
                        String FullName = document.getString("FullName");
                        String address = document.getString("address");
                        Timestamp timeStamp = document.getTimestamp("timeStamp");
                        String Type_of_ad = document.getString("Type_of_ad");
                        String imageUrl = document.getString("imageUrl");
                        String orderNumber = document.getString("orderNumber");
                        String manydays = document.getString("manydays");
                        String totalNoOf = document.getString("totalNoOf");
                        serviceList.add(new serviceModel(Id, Totalprice, CurrentUserId, ProductId, this_product_userId,
                                datesarray, Title, description, ServiceRef, phone, FullName, address, timeStamp, Type_of_ad,
                                imageUrl, orderNumber, manydays, totalNoOf));
                    }
                    loadingProgressBar.setVisibility(View.GONE);
                    serviceBookAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }

            }
        });
    }

    private void getting_daig(String id, String productId) {
        CollectionReference productsRef = db.collection("Booking");
        productsRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Id = document.getString("documentId");
                        String Totalprice = document.getString("Totalprice");
                        String CurrentUserId = document.getString("CurrentUserId");
                        String ProductId = document.getString("ProductId");
                        String this_product_userId = document.getString("ProductUserId");
                        String BookingDate = document.getString("BookingDate");
                        String BookingTime = document.getString("BookingTime");
                        String Title = document.getString("Title");
                        String description = document.getString("description");
                        String DaigRef = document.getString("DaigRef");
                        String phone = document.getString("phone");
                        String FullName = document.getString("FullName");
                        String address = document.getString("address");
                        Timestamp timeStamp = document.getTimestamp("timeStamp");
                        String Type_of_ad = document.getString("Type_of_ad");
                        String imageUrl = document.getString("imageUrl");
                        String orderNumber = document.getString("orderNumber");
                        String TotalKgs = document.getString("TotalKgs");
                        String totalNoOfDaigs = document.getString("totalNoOfDaigs");

                        daigLint.add(new DaigModelForBooked(Id, Totalprice, CurrentUserId, ProductId, this_product_userId
                                , BookingDate, BookingTime, Title, description, DaigRef, phone, FullName, address, timeStamp, Type_of_ad,
                                imageUrl, orderNumber, TotalKgs, totalNoOfDaigs));

                    }
                    loadingProgressBar.setVisibility(View.GONE);
                    daigBookAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });
    }

    private void getting_marrquee_banquatHall(String productId) {
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
                                    (ArrayList<String>) data.get("features")
                            ));
                        }
                        loadingProgressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {
                        // Handle errors while retrieving products
                        Log.d(ContentValues.TAG, "Error getting products: ", productTask.getException());
                    }
                });
    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, showBookingAds.class);
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

        //intent.putExtra("id",keyitems.get(position).getKey());
        startActivity(intent);

    }
}