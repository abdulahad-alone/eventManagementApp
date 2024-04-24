package com.example.eventapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventapp.adapter.AddMealAdapter;
import com.example.eventapp.adapter.ImageAdapter;
import com.example.eventapp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class meals extends AppCompatActivity {
EditText mealName,mealPrice,description;
AppCompatButton uploadImg,AddMeals,postMealAd;
RecyclerView mealsRV;
    private ProgressDialog progressDialog;
String mealNameSt,mealPriceSt,descriptionSt,Key_of_product;
    Uri selectedImageUri;
    public Query baseQuery;
    AddMealAdapter imageAdapter;
    private final List<Uri> selectedImages = new ArrayList<>();
    ActivityResultLauncher<Intent> imagePickLauncher;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_activity);
        mealName=findViewById(R.id.mealname);
        mealPrice=findViewById(R.id.meal_price);
        description=findViewById(R.id.des);
        uploadImg=findViewById(R.id.upload_img);
        AddMeals=findViewById(R.id.Add_meals);
        postMealAd=findViewById(R.id.post_meal_ad);
        mealsRV=findViewById(R.id.imageRecyclerView);
        List<String> descriptionsList = new ArrayList<>();
        List<String> mealPricesList = new ArrayList<>();
        List<String> mealNamesList = new ArrayList<>();
        Key_of_product=getIntent().getStringExtra("product_id");


        uploadImg.setOnClickListener(view -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });


        });
        imagePickLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode()== RESULT_OK){
                        Intent data=result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri=data.getData();
                            selectedImages.add(selectedImageUri);
                            Log.e(TAG, "image uri: "+selectedImages );
                        }
                    }
                }

        );



        AddMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mealNameSt=mealName.getText().toString();
                mealPriceSt=mealPrice.getText().toString();
                descriptionSt=description.getText().toString();
                if (mealNameSt.isEmpty() || mealPriceSt.isEmpty() || descriptionSt.isEmpty()) {
                    // Show a message or toast indicating that all fields are required
                    Toast.makeText(meals.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method without adding to the RecyclerView
                }
                mealNamesList.add(mealNameSt);


                mealPricesList.add(mealPriceSt);


                descriptionsList.add(descriptionSt);

                Log.e(TAG, "details of current meal: "+descriptionsList );

                LinearLayoutManager layoutManager = new LinearLayoutManager(meals.this, LinearLayoutManager.HORIZONTAL, false);
                mealsRV.setLayoutManager(layoutManager);
                 imageAdapter = new AddMealAdapter(selectedImages, mealNamesList, mealPricesList, descriptionsList);
                mealsRV.setAdapter(imageAdapter);
                mealName.setText("");
                mealPrice.setText("");
                description.setText("");
                imageAdapter.notifyDataSetChanged();
            }
        });

        postMealAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(meals.this);
                progressDialog.setMessage("Posting please wait...");
                progressDialog.setCancelable(false);
                for (int i = 0; i < imageAdapter.getItemCount(); i++) {
                    // Get the meal information for the current item
                    String mealNameSt = imageAdapter.getMealName(i);
                    String mealPriceSt = imageAdapter.getMealPrice(i);
                    String descriptionSt = imageAdapter.getDescription(i);
                    Uri selectedImageUri = imageAdapter.getSelectedImageUri(i);

                    // Ensure meal information is not empty and image URI is not null
                    if (!mealNameSt.isEmpty() && !mealPriceSt.isEmpty() && !descriptionSt.isEmpty() && selectedImageUri != null) {
                        uploadImage(mealNameSt, mealPriceSt, descriptionSt, selectedImageUri);
                    }
                }
            }
        });

    }
    private void uploadImage(String mealNameSt, String mealPriceSt, String descriptionSt, Uri selectedImageUri) {
        progressDialog.show();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("meal_images/" + UUID.randomUUID().toString());
        storageRef.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image upload successful, get the download URL
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Upload meal details along with the image URL to Firestore
                                Map<String, Object> mealData = new HashMap<>();
                                mealData.put("mealName", mealNameSt);
                                mealData.put("mealPrice", mealPriceSt);
                                mealData.put("description", descriptionSt);
                                mealData.put("imageUrl", downloadUri.toString());
                                mealData.put("id", Key_of_product);
                                FirebaseUtil.getMealReference(Key_of_product)
                                        .add(mealData)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                // Meal details uploaded successfully
                                                Toast.makeText(meals.this, "Meal posted successfully", Toast.LENGTH_SHORT).show();
                                                // Clear EditText fields
                                                mealName.setText("");
                                                mealPrice.setText("");
                                                description.setText("");
                                                // Clear selected image URI
                                                // Clear selectedImages list
                                                selectedImages.clear();
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure
                                                Toast.makeText(meals.this, "Failed to post meal", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle image upload failure
                        Toast.makeText(meals.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}