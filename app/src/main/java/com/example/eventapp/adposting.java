package com.example.eventapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.Models.VenueDetails;
import com.example.eventapp.adapter.AddFeaturesAdapter;
import com.example.eventapp.adapter.ImageAdapter;
import com.example.eventapp.fragments.MyBottomSheetCitiesFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class adposting extends AppCompatActivity implements MyBottomSheetCitiesFragment.CitySelectionListener {
String[] item={"Banquet Hall","Marrquee","Catering","Photographer","Makeup Artist","DJ","Transportation Service","Service Only"};
    String[] itemForCatring={"DaigModel ","Events"};
ArrayAdapter<String>  adapteritem ;
AutoCompleteTextView autoCompleteTextView,  serviceType;
    private EditText Price,Location,Description,Short_Discription,number,pricePerHead,venueTitleEdittext;
    LinearLayout serviceTypeLayout,services;
    private ProgressDialog progressDialog;
    RecyclerView imageRecyclerView;
    TextView citySelectionTextView,company_title,Price_per_head;
    private AppCompatButton post_ad;
    private AppCompatButton property_img;
    ArrayList<String> newDataList = new ArrayList<>();
    private final List<Uri> selectedImages = new ArrayList<>();
    private TextView lol,Geolocation,AreaUnitSelector, parsedPrice,AreaSize,Features_;
    private FirebaseFirestore firestore;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private final StorageReference storage = FirebaseStorage.getInstance().getReference();
    String formattedPrice,purpose, Type, bedroomno,bathroomno,city_,langi,lati,areaunitselector,area_size,userId ;
    private static final int MAP_REQUEST_CODE = 123;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adposting_activity);

        autoCompleteTextView=findViewById(R.id.auto_complete_Text);
        serviceTypeLayout=findViewById(R.id.serviceTypeLayout);
        services=findViewById(R.id.services);
        company_title=findViewById(R.id.company_title);
        serviceType=findViewById(R.id.serviceType);
        Geolocation=findViewById(R.id.geolocation);
        citySelectionTextView = findViewById(R.id.city);
        venueTitleEdittext = findViewById(R.id.venueTitle);
        property_img=findViewById(R.id.upload_img);
        adapteritem=new ArrayAdapter<String>(this,R.layout.list_item,item );
        autoCompleteTextView.setAdapter(adapteritem);
        imageRecyclerView = findViewById(R.id.imageRecyclerView);
        pricePerHead = findViewById(R.id.priceOfPro);
        Price_per_head = findViewById(R.id.Price_per_head);
        post_ad=findViewById(R.id.post_ad);
        firestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        number=findViewById(R.id.contactno);
        Description=findViewById(R.id.des);
        lol=findViewById(R.id.image_view);
        Features_=findViewById(R.id.Features);
        serviceTypeLayout.setVisibility(View.GONE);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Type=item;
                if(Type.equals("Catering"))
                {
                    serviceTypeLayout.setVisibility(View.VISIBLE);
                    services.setVisibility(View.GONE);
                    company_title.setText("Catering Service");
                    venueTitleEdittext.setHint("Enter your Service/Company name");
                    Price_per_head.setText("Starting from");

                }else if(Type.equals("Photographer"))
                {
                    serviceTypeLayout.setVisibility(View.GONE);
                    Features_.setHint("Add additional Services e.g. Album, Cinematography, etc");
                    company_title.setText("Title");
                    venueTitleEdittext.setHint("Enter your Artist name");
                    Price_per_head.setText("Price per day");
                }else if(Type.equals("Makeup Artist"))
                {
                    serviceTypeLayout.setVisibility(View.GONE);
                    Features_.setHint("Add additional Services e.g. Hair Styling, Manicure, etc");
                    company_title.setText("Title");
                    venueTitleEdittext.setHint("Enter your Salon/Artist name");
                    Price_per_head.setText("Starting from");
                }else if(Type.equals("DJ"))
                {
                    serviceTypeLayout.setVisibility(View.GONE);
                    Features_.setHint("Add additional Services e.g. Disco lightning, etc");
                    company_title.setText("Title");
                    venueTitleEdittext.setHint("Enter your Dj Artist name");
                    Price_per_head.setText("Price per day");
                }else if(Type.equals("Transportation Service"))
                {
                    serviceTypeLayout.setVisibility(View.GONE);
                    Features_.setHint("Add additional Services e.g. Comfortable rides, etc");
                    company_title.setText("Title");
                    venueTitleEdittext.setHint("Enter your company name");
                    Price_per_head.setText("Starting From");
                }else if(Type.equals("Service Only"))
                {
                    company_title.setText("Title");
                    venueTitleEdittext.setHint("Enter your Name");
                    Price_per_head.setText("Price per Service");
                    Features_.setHint("Add additional Services like which events you manage");
                }
                else {
                    serviceTypeLayout.setVisibility(View.GONE);
                    services.setVisibility(View.VISIBLE);
                    company_title.setText("Venue");
                    Price_per_head.setHint("Price per head");
                    Features_.setHint("Add additional Services e.g. parking, waiters, etc");
                    venueTitleEdittext.setHint("Enter Venue title");
                    Features_.setText("");
                }
                Log.e(TAG, "category: "+Type );
            }
        });
//city selection
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            userId= currentUser.getUid();
            Log.d(TAG, "user id: "+ userId);
            // You can now use the userId variable to access the current user's ID
        } else {
            // No user is currently logged in
        }
        citySelectionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBottomSheetCitiesFragment fragment = new MyBottomSheetCitiesFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cityName", city_);
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), fragment.getTag());
                Log.i(TAG, "onCreateView key: "+city_);
            }
        });

   //ad posting

        post_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lolwe();



               /* Log.e(TAG, "onClick adposting btn: "+venueTitleEdittext.getText().toString() );
               progressDialog = new ProgressDialog(adposting.this);
                progressDialog.setMessage("Posting please wait...");
                progressDialog.setCancelable(false); // Prevent dismissing by clicking outside

                uploadImage();*/

            }
        });

        TextView Geolocation = findViewById(R.id.geolocation);
        Geolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapActivity();
            }
        });

        property_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                selectImagesActivityResult.launch(intent);

                // startActivityForResult(Intent.createChooser(intent,"Selected Picture "),Pick_Image);
            }
        });
        TextView addFeatures=findViewById(R.id.addfeatures);
        addFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSecondActivity();
            }
        });

    }

    private void lolwe() {

        if(autoCompleteTextView.getText().toString().trim().isEmpty())
        {
            autoCompleteTextView.setError("Please Enter Types");
        }else {
            autoCompleteTextView.setError(null);
            if (venueTitleEdittext.getText().toString().trim().isEmpty())
            {
                venueTitleEdittext.setError("Please Enter Title");
            }else {
                venueTitleEdittext.setError(null);
                if(pricePerHead.getText().toString().trim().isEmpty())
                {
                    pricePerHead.setError("Please Enter Price");
                }else
                {
                    pricePerHead.setError(null);
                    if(Description.getText().toString().trim().isEmpty()){
                        Description.setError("Please Enter Description");
                    }else {
                        Description.setError(null);
                        if(citySelectionTextView.getText().toString().trim().isEmpty()){
                            citySelectionTextView.setError("Please Select City");
                        }else{
                            citySelectionTextView.setError(null);
                            if(Geolocation.getText().toString().trim().isEmpty()){
                                Geolocation.setError("Please Select Geolocation");
                            }else{
                                Geolocation.setError(null);
                                if(number.getText().toString().trim().isEmpty())
                                {
                                    number.setError("Please Enter Number");
                                }else
                                {
                                    number.setError(null);
                                    if(selectedImages.isEmpty())
                                    {
                                        lol.setError("Please select images");
                                    }else
                                    {
                                        lol.setError(null);
                                        Log.e(TAG, "lolwe: all ok " );
                                        progressDialog = new ProgressDialog(adposting.this);
                                        progressDialog.setMessage("Posting please wait...");
                                        progressDialog.setCancelable(false); // Prevent dismissing by clicking outside
                                        uploadImage();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
               /* Type,userId
                        ,number.getText().toString(),city_,lati , langi ,pricePerHead.getText().toString()
                        ,venueTitleEdittext.getText().toString()
                        ,  Description.getText().toString(),images, Timestamp.now()*/


    }

    static final int SECOND_ACTIVITY_REQUEST_CODE = 1;
    private void startSecondActivity() {
        Intent intent = new Intent(this, addServices.class);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);

    }






    private void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult(intent, MAP_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            double latitude = data.getDoubleExtra("latitude", 0.0);
            double longitude = data.getDoubleExtra("longitude", 0.0);
            String location = "Latitude: " + latitude + ", Longitude: " + longitude;
            Geolocation.setText(location);
            lati= String.valueOf(latitude);
            langi= String.valueOf(longitude);
            // Use the latitude and longitude in your AdPostingActivity
        }
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Retrieve data from the second activity
                String servicesname = data.getStringExtra("services");
                Log.e(TAG, "servicesname :"+servicesname );

                // Update your views or data with the received information
                if (servicesname != null && !servicesname.isEmpty()) {
                    newDataList.add(servicesname);
                }
                if (!newDataList.isEmpty()) {
                    Features_.setVisibility(View.INVISIBLE);
                    RecyclerView newRecyclerView = findViewById(R.id.newRecyclerView);

// Set up the new RecyclerView with the updated newDataList using a new adapter
                    AddFeaturesAdapter newDataAdapter = new AddFeaturesAdapter(newDataList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    newRecyclerView.setLayoutManager(layoutManager);
                    newRecyclerView.setAdapter(newDataAdapter);
                    Log.e(TAG, "features list: "+newDataList );
                }

            }
        }
    }
    final ActivityResultLauncher<Intent> selectImagesActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            if (intent.getClipData() != null) {
                                int count = intent.getClipData().getItemCount();
                                for (int i = 0; i < count; i++) {
                                    Uri imageUri = intent.getClipData().getItemAt(i).getUri();
                                    selectedImages.add(imageUri);
                                }
                            } else {
                                Uri imageUri = intent.getData();
                                selectedImages.add(imageUri);
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(adposting.this, LinearLayoutManager.HORIZONTAL, false);
                            imageRecyclerView.setLayoutManager(layoutManager);
                            Log.e(TAG, "selected images: "+selectedImages );
                            ImageAdapter imageAdapter = new ImageAdapter(selectedImages,lol);
                            imageRecyclerView.setAdapter(imageAdapter);
                            updateImages();

                        }
                    }
                }
            });
    private void updateImages() {
        lol.setText(String.valueOf(selectedImages.size()));
    }

    @Override
    public void onCitySelected(String cityName) {
        if (citySelectionTextView != null) {
            citySelectionTextView.setText(cityName);
            city_=cityName;
            Log.e(TAG, "onCitySelected: "+cityName );
        } else {
            Log.e(TAG, "City TextView is null!"+cityName);
        }
    }

    private List<byte[]> getImagesByteArrays() {
        List<byte[]> imagesByteArray = new ArrayList<>();
        for (Uri imageUri : selectedImages) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap imageBmp;
            try {
                imageBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                if (imageBmp.compress(Bitmap.CompressFormat.JPEG, 85, stream)) {
                    byte[] imageAsByteArray = stream.toByteArray();
                    imagesByteArray.add(imageAsByteArray);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imagesByteArray;
    }
    private void uploadImage() {
        progressDialog.show();
        List<byte[]> imagesByteArrays = getImagesByteArrays();
        List<String> images = new ArrayList<>();
        // Counter to keep track of uploaded images
        AtomicInteger uploadedImageCount = new AtomicInteger(0);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    ArrayList<String> imageUrls = new ArrayList<>();
                    for (byte[] byteArray : imagesByteArrays) {
                        String id = UUID.randomUUID().toString();
                        StorageReference imagesStorage = storage.child("products/images/" + id);
                        UploadTask uploadTask = imagesStorage.putBytes(byteArray);
                        Tasks.await(uploadTask);

                        // Get the download URL of the uploaded image
                        Uri downloadUrl = Tasks.await(imagesStorage.getDownloadUrl());
                        String downloadUrlString = downloadUrl.toString();
                        imageUrls.add(downloadUrlString);
                        if (uploadedImageCount.incrementAndGet() == imagesByteArrays.size()) {
                            // All images have been successfully uploaded
                            images.addAll(imageUrls);
                            progressDialog.dismiss();

                            // Now that images are uploaded, store the product in Firestore
                            storeProductToFirestore(images);
                        }
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.e("FirebaseUploadError", "Image upload failed: " + e.getMessage(), e);
                    Toast.makeText(adposting.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    // state.invoke(false);
                }
            }
        }).start();

       /* StorageReference refe= FirebaseStorage.getInstance().getReference()
                .child("images/"+ UUID.randomUUID().toString());
        refe.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                refe.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("URL",""+uri);

                        //update data in firebase
                        Map<String,String> map =new HashMap<>();
                        map.put("price",Price.getText().toString());
                        Log.e(TAG, "onSuccess: "+Price.getText().toString());
                        map.put("shortDescription",Short_Discription.getText().toString());
                        map.put("description",Description.getText().toString());

                        if(uri!=null){
                            map.put("image",uri.toString());
                        }
                        map.put("location",Location.getText().toString());

                        reference=FirebaseDatabase.getInstance().getReference().child("images").push();
                        reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //update user authentication email
                                Toast.makeText(adposting.this,"Success",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                })  ;
            }
        });*/

    }
    private void storeProductToFirestore(List<String> images) {

        DocumentReference productRef = firestore.collection("Products").document(UUID.randomUUID().toString());
        VenueDetails venueDetails = new VenueDetails(productRef.getId(),Type,userId
                ,number.getText().toString(),city_,lati , langi ,pricePerHead.getText().toString().trim()
                ,venueTitleEdittext.getText().toString()
                ,  Description.getText().toString(),images, Timestamp.now() );
        if(newDataList!=null)
        {
            venueDetails.setFeatures(newDataList);
            Log.e(TAG, "storeProductToFirestore: "+ venueDetails.getFeatures() );
        }
        productRef.set(venueDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(adposting.this, "Product uploaded successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.e("FirebaseUploadError", "Product upload failed: " + e.getMessage(), e);
                Toast.makeText(adposting.this, "Product upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }




}