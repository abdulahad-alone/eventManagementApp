package com.example.eventapp.detailed_activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventapp.R;
import com.example.eventapp.adapter.AddFeaturesAdapter;
import com.example.eventapp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class add_packages extends AppCompatActivity {
String category,key;
EditText name,StartingPrice,ServicesEdit,des,terms_condition_editText,capacityEdit;
TextView publish;
    ImageView add_Daig_to_rv;
    AddFeaturesAdapter adapter;
    LinearLayout imageLayout,termConditionLayout,capacitylayout;
    ActivityResultLauncher<Intent> imagePickLauncher;
    private  int Pick_Image=1;
    Uri selectedImageUri;
    ImageView Insert_img;
    RecyclerView Daig_ingrediet_rv;
    ProgressDialog progressDialog;
    ArrayList<String> ServicesEditList = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_packages);
        name=findViewById(R.id.name);
        StartingPrice=findViewById(R.id.StartingPrice);
        Insert_img=findViewById(R.id.Insert_img);
        add_Daig_to_rv=findViewById(R.id.add_Daig_to_rv);
        capacitylayout=findViewById(R.id.capacitylayout);
        ServicesEdit=findViewById(R.id.ServicesEdit);
        termConditionLayout=findViewById(R.id.termConditionLayout);
        des=findViewById(R.id.des);
        imageLayout=findViewById(R.id.imageLayout);
        terms_condition_editText=findViewById(R.id.terms_condition_editText);
        publish=findViewById(R.id.publish);
        Daig_ingrediet_rv=findViewById(R.id.Daig_ingrediet_rv);
        capacityEdit=findViewById(R.id.capacityEdit);
        imageLayout.setVisibility(View.GONE);
        termConditionLayout.setVisibility(View.GONE);
        capacitylayout.setVisibility(View.GONE);
        category = getIntent().getStringExtra("category");
        key = getIntent().getStringExtra("id");


        if(category.equals("Photographer"))
        {
            name.setHint("Enter Title e.g: Dreamy");
            StartingPrice.setHint("eg: Enter per day price");

        }
       if(category.equals("Transportation Service"))
       {
           imageLayout.setVisibility(View.VISIBLE);
           capacitylayout.setVisibility(View.VISIBLE);
           termConditionLayout.setVisibility(View.VISIBLE);
           name.setHint("Enter Ride name e.g:Honda Civic");
           StartingPrice.setHint("eg: Enter per day price");
       }

        add_Daig_to_rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ServicesEdit.getText().toString().trim().isEmpty())
                {
                    ServicesEdit.setError("Please enter service");

                }else
                {
                    ServicesEdit.setError(null);
                    String ServicesEditSt = ServicesEdit.getText().toString().trim();


                    ServicesEditList.add(ServicesEditSt);


                    adapter = new AddFeaturesAdapter(ServicesEditList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(add_packages.this, LinearLayoutManager.HORIZONTAL, false);
                    Daig_ingrediet_rv.setLayoutManager(layoutManager);
                    Daig_ingrediet_rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    // Clear EditText fields after adding to RecyclerView
                    ServicesEdit.setText("");



                }
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(category.equals("Transportation Service"))
                {
                    String price=StartingPrice.getText().toString();
                    String nameOfPackage=name.getText().toString();
                    String description=des.getText().toString();
                    String termsCondition=terms_condition_editText.getText().toString();
                    String capacitySt=capacityEdit.getText().toString();
                    if(price.isEmpty()&& nameOfPackage.isEmpty()&& description.isEmpty() && ServicesEditList.isEmpty()
                    && termsCondition.isEmpty() && capacitySt.isEmpty())
                    {
                        Toast.makeText(add_packages.this, "please fill all fields", Toast.LENGTH_SHORT).show();

                    }else {
                        if(selectedImageUri==null)
                        {
                            Toast.makeText(add_packages.this, "select image first", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            uploadingTranport(price,nameOfPackage,description,ServicesEditList,termsCondition,selectedImageUri,capacitySt);
                        }

                    }
                }else
                {
                    String price=StartingPrice.getText().toString();
                    String nameOfPackage=name.getText().toString();
                    String description=des.getText().toString();
                    if(price.isEmpty()&& nameOfPackage.isEmpty()&& description.isEmpty() && ServicesEditList.isEmpty() )
                    {
                        Toast.makeText(add_packages.this, "please fill all fields", Toast.LENGTH_SHORT).show();

                    }else {
                        uploading(price,nameOfPackage,description,ServicesEditList);
                    }
                }

            }
        });

        Insert_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(add_packages.this).crop(16f, 9f).compress(512).maxResultSize(600, 512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });

        imagePickLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode()== RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            Glide.with(add_packages.this).load(selectedImageUri).centerCrop().into(Insert_img);
                        }
                    }
                } );

    }

    private void uploadingTranport(String price, String nameOfPackage, String description, ArrayList<String> servicesEditList, String termsCondition, Uri selectedImageUri, String capacitySt) {
        progressDialog = new ProgressDialog(add_packages.this);
        progressDialog.setMessage("Posting please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("rent_images/" + UUID.randomUUID().toString());
        storageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> Data = new HashMap<>();
                        Data.put("RideName", nameOfPackage);
                        Data.put("ServicePrice", price);
                        Data.put("description", description);
                        Data.put("imageUrl", uri.toString());
                        Data.put("servicesList", servicesEditList);
                        Data.put("termsCondition", termsCondition);
                        Data.put("capacity", capacitySt);
                        Data.put("id", key);
                        FirebaseUtil.getPackageReference(key).add(Data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(add_packages.this, "posted successfully", Toast.LENGTH_SHORT).show();
                                String documentId = documentReference.getId();
                                documentReference.update("PackageRef", documentId);
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                })    ;
            }
        });

    }

    private void uploading(String price, String nameOfPackage, String description, ArrayList<String> servicesEditList) {
        progressDialog = new ProgressDialog(add_packages.this);
        progressDialog.setMessage("Posting please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, Object> Data = new HashMap<>();
        Data.put("nameOfPackage", nameOfPackage);
        Data.put("priceOfPackage", price);
        Data.put("description", description);
        Data.put("servicesList", servicesEditList);
        FirebaseUtil.getPackageReference(key).add(Data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(add_packages.this, "posted successfully", Toast.LENGTH_SHORT).show();
                        String documentId = documentReference.getId();
                        documentReference.update("PackageRef", documentId);
                        progressDialog.dismiss();
                        finish();

                    }
                });


    }
}