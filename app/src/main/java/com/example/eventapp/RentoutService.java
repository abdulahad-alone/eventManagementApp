package com.example.eventapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventapp.adapter.AddMealAdapter;
import com.example.eventapp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RentoutService extends AppCompatActivity {
    ImageView coverAreaimg, imageView;
    EditText rentOut_name, des,price,minimum_order;
    AppCompatButton post_ad;
    Uri selectedImageUri;
    String Key_of_product;
    ActivityResultLauncher<Intent> imagePickLauncher;
    AddMealAdapter imageAdapter;
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rentout_service);
        coverAreaimg = findViewById(R.id.coverAreaimg);
        post_ad = findViewById(R.id.post_ad);
        price = findViewById(R.id.price);
        imageView = findViewById(R.id.imageView);
        minimum_order = findViewById(R.id.minimum_order);
        des = findViewById(R.id.des);
        Key_of_product=getIntent().getStringExtra("product_id");
        rentOut_name = findViewById(R.id.rentOut_name);
        coverAreaimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoPopup(coverAreaimg);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(RentoutService.this).cropSquare().compress(512).maxResultSize(512, 512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            Glide.with(RentoutService.this).load(selectedImageUri).centerCrop().into(imageView);

                        }
                    }
                }

        );

        post_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, description,pricest,minimum_orderSt;
                name = rentOut_name.getText().toString().toLowerCase();
                description = des.getText().toString().trim();
                pricest = price.getText().toString().trim();
                minimum_orderSt = minimum_order.getText().toString().trim();

                if (name.isEmpty()) {
                    rentOut_name.setError("Enter name");
                } else {
                    rentOut_name.setError(null);
                    if (description.isEmpty()) {
                        rentOut_name.setError("Enter description");
                    } else {
                        rentOut_name.setError(null);
                        if(selectedImageUri==null)
                        {
                            Toast.makeText(getApplicationContext(), "Select image first", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            if(pricest.isEmpty())
                            {
                                price.setError("Enter price");
                            }else{
                                price.setError(null);
                            if(minimum_orderSt.isEmpty())
                            {
                                minimum_order.setError("Enter ");
                            }else
                            {
                                minimum_order.setError(null);
                                upload(name,description,selectedImageUri,pricest,minimum_orderSt);
                            }

                            }

                        }
                    }
                }


            }
        });
    }

    private void upload(String name, String description, Uri selectedImageUri, String pricest, String minimum_orderSt) {
        progressDialog = new ProgressDialog(RentoutService.this);
        progressDialog.setMessage("Posting please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("rent_images/" + UUID.randomUUID().toString());
        storageRef.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                Map<String, Object> mealData = new HashMap<>();
                                mealData.put("ServiceName", name);
                                mealData.put("ServicePrice", pricest);
                                mealData.put("description", description);
                                mealData.put("imageUrl", downloadUri.toString());
                                mealData.put("minimum_orderSt", minimum_orderSt);
                                mealData.put("id", Key_of_product);
                                FirebaseUtil.getServiceReference(Key_of_product).add(mealData)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(RentoutService.this, "posted successfully", Toast.LENGTH_SHORT).show();
                                                String documentId = documentReference.getId();
                                                documentReference.update("ServiceRef", documentId);
                                                progressDialog.dismiss();
                                                finish();

                                            }
                                        });
                            }
                        });
                    }
                });


    }


    private void showInfoPopup(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_info, null);
        TextView infoText = popupView.findViewById(R.id.info_text);

        // Set your information text here
        infoText.setText("Enter keywords with small letters. It helps you to get more impression on your ads.\n" +
                "(eg. chair, table, tent, waiter service and etc)");
        PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);

        // Calculate the screen's width and height using the activity's context
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        // Measure the popupView to get its dimensions
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = popupView.getMeasuredWidth();
        int popupHeight = popupView.getMeasuredHeight();

        // Calculate the location to display the popup window (top of the button)
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int offsetX = (anchorView.getWidth() - popupWidth) / 2; // Center horizontally
        int offsetY = -popupHeight - anchorView.getHeight(); // At the top of the button

        // Ensure the popup stays within the screen bounds
        int x = Math.max(0, Math.min(screenWidth - popupWidth, location[0] + offsetX));
        int y = Math.max(0, Math.min(screenHeight - popupHeight, location[1] + offsetY));

        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

    }

}