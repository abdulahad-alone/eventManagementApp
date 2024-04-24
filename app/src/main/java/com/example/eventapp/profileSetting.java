package com.example.eventapp;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventapp.R;
import com.example.eventapp.utils.AndroidUtil;
import com.example.eventapp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class profileSetting extends AppCompatActivity {
    private CircleImageView userProfile;
    private EditText UserName;
    private TextView UserEmail;
    private AppCompatButton updateButton;
    String Name,email;
    String uri1;
    ProgressBar loadingProgressBar;
    Uri selectedImageUri,uri,UriFromStorage;
    ActivityResultLauncher<Intent> imagePickLauncher;
    private  int Pick_Image=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setting);

        UserName=findViewById(R.id.name_update);
        UserEmail=findViewById(R.id.email_update);
        updateButton=findViewById(R.id.update);
        userProfile=findViewById(R.id.profile_image);
        loadingProgressBar =findViewById(R.id.loadingProgressBar);
        Name = getIntent().getStringExtra("Username");
        email = getIntent().getStringExtra("Email");

        Log.e(TAG, "Username: "+Name );
        Log.e(TAG, "Email: " +email);

        UserName.setText(Name);
        UserEmail.setText(email);
        fetchDataFromFirestore();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                updatebtnClick();

            }
        });
        imagePickLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode()== RESULT_OK){
                        Intent data=result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri=data.getData();
                            AndroidUtil.setProfilePic(this, selectedImageUri, userProfile);
                        }
                    }
                }

        );

        userProfile.setOnClickListener(view1 -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchDataFromFirestore();
    }

    private void fetchDataFromFirestore() {
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            UriFromStorage=task.getResult();
                            Log.e(TAG, "image Uri: "+UriFromStorage );
                            AndroidUtil.setProfilePic(profileSetting.this, UriFromStorage, userProfile);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Pick_Image && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getData()!=null) {

                uri=data.getData();
                userProfile.setImageURI(uri);

            }
        }
    }
    void setInProgress(Boolean inProgress)
    {
        if(inProgress){
            //loadingProgressBar.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.GONE);
        }else{
            //loadingProgressBar.setVisibility(View.GONE);
            updateButton.setVisibility(View.VISIBLE);
        }

    }
    void updatebtnClick()
    {
        String newUserName=UserName.getText().toString();
        Log.e(TAG, "updatebtnClick: "+newUserName );
        if(newUserName.isEmpty()|| newUserName.length()<3){
            UserName.setError("Username lenght should be at least 3 chars");
            return;
        }else
            UserName.setError(null);
        if(selectedImageUri!=null){
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    loadingProgressBar.setVisibility(View.GONE);
                    AndroidUtil.showToast(profileSetting.this, "Image uploaded Successfully");
                }
            });
            updateToFireStore();
        }else
        {
            updateToFireStore();
        }


    }

    void updateToFireStore(){
        String name= UserName.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.e(TAG, "current user: "+ FirebaseUtil.currentuserId());
        DocumentReference userRef = db.collection("users").document(FirebaseUtil.currentuserId());
        Map<String, Object> updates = new HashMap<>();
        if(selectedImageUri!=null){
            updates.put("image", selectedImageUri);

        }
        updates.put("image", selectedImageUri);

        updates.put("name", name);
        userRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                loadingProgressBar.setVisibility(View.GONE);
                AndroidUtil.showToast(profileSetting.this, "Updated SuccessFully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}