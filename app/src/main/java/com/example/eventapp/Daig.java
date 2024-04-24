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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eventapp.Models.DaigModel;
import com.example.eventapp.Models.GetDaigs;
import com.example.eventapp.Models.Meals;
import com.example.eventapp.adapter.AddMealAdapter;
import com.example.eventapp.adapter.DaigAdapter;
import com.example.eventapp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Daig extends AppCompatActivity {
ImageView add_Daig_to_rv;
EditText IngredientEdit,meal_price;
RecyclerView Daig_ingrediet_rv,imageRecyclerView;
    EditText mealName,StartingPrice,description,minimum_order;
AppCompatButton Add_meals,uploadImg,postMealAd;
    private ProgressDialog progressDialog;
    private List<Daig> selectedMeals = new ArrayList<>();
    List<GetDaigs> getDaigs = new ArrayList<>();
    private DaigAdapter adapter;
    DaigModel dataList;
    Uri selectedImageUri;
    List<String> descriptionsList = new ArrayList<>();
    List<String> mealNamesList = new ArrayList<>();
    List<String> mealPricesList = new ArrayList<>();
    List<String> minimum_orderList = new ArrayList<>();
    String mealNameSt,mealPriceSt,descriptionSt,Key_of_product,minimum_orderSt;
    List<DaigModel> daigList;
    private final List<Uri> selectedImages = new ArrayList<>();
    ActivityResultLauncher<Intent> imagePickLauncher;
    AddMealAdapter imageAdapter;
    // List<DaigModel> selectedMeals = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daig_activity);
        add_Daig_to_rv=findViewById(R.id.add_Daig_to_rv);
        IngredientEdit=findViewById(R.id.IngredientEdit);
        mealName=findViewById(R.id.mealname);
        StartingPrice=findViewById(R.id.StartingPrice);
        minimum_order=findViewById(R.id.minimum_order);
        imageRecyclerView=findViewById(R.id.imageRecyclerView);
        description=findViewById(R.id.des);
        Add_meals=findViewById(R.id.Add_meals);
        postMealAd=findViewById(R.id.post_meal_ad);
        uploadImg=findViewById(R.id.upload_img);
        meal_price=findViewById(R.id.meal_price);
        Daig_ingrediet_rv=findViewById(R.id.Daig_ingrediet_rv);
        Key_of_product=getIntent().getStringExtra("product_id");
        List<String> DaigPricesList = new ArrayList<>();
        List<String> DaigNamesList = new ArrayList<>();

        daigList = new ArrayList<>();

        add_Daig_to_rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (IngredientEdit.getText().toString().trim().isEmpty() && meal_price.getText().toString().trim().isEmpty())
                {
                    IngredientEdit.setError("Please enter Ingredient");
                    meal_price.setError("Please enter price");
                }else
                {
                    IngredientEdit.setError(null);
                    meal_price.setError(null);
                    String ingredient = IngredientEdit.getText().toString().trim();
                    String price = meal_price.getText().toString().trim();
                    List<String> DaigPricesList = new ArrayList<>();
                    List<String> DaigNamesList = new ArrayList<>();

                     dataList=new DaigModel(ingredient,price);

                    daigList.add(dataList);


                    adapter = new DaigAdapter(daigList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Daig.this, LinearLayoutManager.HORIZONTAL, false);
                    Daig_ingrediet_rv.setLayoutManager(layoutManager);
                    Daig_ingrediet_rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    // Clear EditText fields after adding to RecyclerView
                    IngredientEdit.setText("");
                    meal_price.setText("");


                }
            }
        });
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getimg();
            }
        });
        Add_meals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mealNameSt=mealName.getText().toString();
                descriptionSt=description.getText().toString();
                mealPriceSt=StartingPrice.getText().toString();
                minimum_orderSt=minimum_order.getText().toString();
                if (mealNameSt.isEmpty() || mealPriceSt.isEmpty() || descriptionSt.isEmpty()) {
                    // Show a message or toast indicating that all fields are required
                    Toast.makeText(Daig.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method without adding to the RecyclerView
                }
                mealNamesList.add(mealNameSt);
                mealPricesList.add(mealPriceSt);
                descriptionsList.add(descriptionSt);
                minimum_orderList.add(minimum_orderSt);
                LinearLayoutManager layoutManager = new LinearLayoutManager(Daig.this, LinearLayoutManager.HORIZONTAL, false);
                imageRecyclerView.setLayoutManager(layoutManager);
                imageAdapter = new AddMealAdapter(selectedImages, mealNamesList, mealPricesList, descriptionsList,minimum_orderList,
                        daigList);
                imageRecyclerView.setAdapter(imageAdapter);
                mealName.setText("");
                StartingPrice.setText("");
                description.setText("");
                minimum_order.setText("");
                imageAdapter.notifyDataSetChanged();
            }
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
        postMealAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(Daig.this);
                progressDialog.setMessage("Posting please wait...");
                progressDialog.setCancelable(false);
                for (int i = 0; i < imageAdapter.getItemCount(); i++) {
                    // Get the meal information for the current item
                    String mealNameSt = imageAdapter.getMealName(i);
                    String mealPriceSt = imageAdapter.getMealPrice(i);
                    String descriptionSt = imageAdapter.getDescription(i);
                    String minum_order = imageAdapter.getMinimumorderList(i);
                    Uri selectedImageUri = imageAdapter.getSelectedImageUri(i);
                    DaigModel daigModel=imageAdapter.getdaigList(i);
                    Log.e(TAG, "sdsd: "+ daigModel.getIngredient() );

                    // Ensure meal information is not empty and image URI is not null
                    if (!mealNameSt.isEmpty() && !mealPriceSt.isEmpty() && !descriptionSt.isEmpty() && selectedImageUri != null) {
                       uploadImage(mealNameSt, mealPriceSt, descriptionSt, selectedImageUri,minum_order,daigModel);
                    }
                }
            }
        });


    }

    private void uploadImage(String mealNameSt, String mealPriceSt, String descriptionSt, Uri selectedImageUri,String minum_order ,DaigModel daigModel) {

        progressDialog.show();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Daig_images/" + UUID.randomUUID().toString());
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
                                mealData.put("DaigName", mealNameSt);
                                mealData.put("DaigPrice", mealPriceSt);
                                mealData.put("description", descriptionSt);
                                mealData.put("imageUrl", downloadUri.toString());
                                mealData.put("id", Key_of_product);
                                mealData.put("minimumOrder", minum_order);
                                List<Map<String, Object>> daigModelsList = new ArrayList<>();
                                for (DaigModel daigModel : daigList) {
                                    Map<String, Object> daigModelMap = new HashMap<>();
                                    daigModelMap.put("ingredient", daigModel.getIngredient());
                                    daigModelMap.put("price", daigModel.getIngredientPrice());

                                    // Add the DaigModel map to the list
                                    daigModelsList.add(daigModelMap);
                                }
                                mealData.put("daigModels", daigModelsList);
                                //mealData.put("IngredietDetail", daigList);


                                FirebaseUtil.getDaigReference(Key_of_product)
                                        .add(mealData)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                // Meal details uploaded successfully
                                                Toast.makeText(Daig.this, "Meal posted successfully", Toast.LENGTH_SHORT).show();
                                                // Clear EditText fields
                                                String documentId = documentReference.getId();
                                                documentReference.update("DaigRef", documentId)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(Daig.this, "Meal posted successfully", Toast.LENGTH_SHORT).show();
                                                            // Clear EditText fields
                                                            mealName.setText("");
                                                            StartingPrice.setText("");
                                                            description.setText("");
                                                            // Clear selected image URI
                                                            // Clear selectedImages list
                                                            selectedImages.clear();
                                                            progressDialog.dismiss();
                                                            finish();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Handle failure to update document
                                                            Toast.makeText(Daig.this, "Failed to update document with ID", Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure
                                                Toast.makeText(Daig.this, "Failed to post meal", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Daig.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getimg() {
        ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                .createIntent(new Function1<Intent, Unit>() {
                    @Override
                    public Unit invoke(Intent intent) {
                        imagePickLauncher.launch(intent);
                        return null;
                    }
                });
    }

}