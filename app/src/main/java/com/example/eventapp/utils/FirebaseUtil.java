package com.example.eventapp.utils;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FirebaseUtil {


    public static String currentuserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserDetials() {
        return FirebaseFirestore.getInstance().collection("users").document(currentuserId());

    }
    public static DocumentReference otherUserDetails(String id) {
        return FirebaseFirestore.getInstance().collection("users").document(id);

    }
    public static  CollectionReference allUserCollectionReference()
    {
        return FirebaseFirestore.getInstance().collection("users");

    }

    public static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());

    }

    public static String formatedTimeStamp(Timestamp timestamp) {
        Date timestampDate = timestamp.toDate();
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifference = currentTimeMillis - timestampDate.getTime();
        long secondsDifference = timeDifference / 1000;
        String relativeTime = DateUtils.getRelativeTimeSpanString(timestampDate.getTime(), currentTimeMillis, DateUtils.MINUTE_IN_MILLIS).toString();
        return relativeTime;
    }

    public static StorageReference getCurrentProfilePicStorageRef() {
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUtil.currentuserId());
    }

    public static StorageReference getCurrentCoverPicStorageRef() {
        return FirebaseStorage.getInstance().getReference().child("cover_pic")
                .child(FirebaseUtil.currentuserId());
    }
    public static StorageReference getOtherCoverPicStorageRef(String otherUserId) {
        return FirebaseStorage.getInstance().getReference().child("cover_pic")
                .child(otherUserId);
    }
    public static StorageReference getOtherProfilePicStorageRef(String otherUserId) {
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(otherUserId);
    }

    public static DocumentReference getProductReference(String ProductId) {
        return FirebaseFirestore.getInstance().collection("Products").document(ProductId);

    }

    public static CollectionReference getMealReference(String mealId) {
        return getProductReference(mealId).collection("Meals");
    }
    public static CollectionReference getDaigReference(String mealId) {
        return getProductReference(mealId).collection("Daigs");
    }
    public static CollectionReference getServiceReference(String mealId) {
        return getProductReference(mealId).collection("Service");
    }

    public static CollectionReference getPackageReference(String mealId) {
        return getProductReference(mealId).collection("Packages");
    }
    public static CollectionReference getRatingReference(String mealId) {
        return getProductReference(mealId).collection("Ratings");
    }

    public static DocumentReference getRatingReference(String mealId,String userId) {
        return getProductReference(mealId).collection("Ratings").document(userId);
    }




    public static Task<String> getUserType() {
        return FirebaseFirestore.getInstance().collection("users").document(currentuserId())
                .get()
                .continueWith(new Continuation<DocumentSnapshot, String>() {
                    @Override
                    public String then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        String typeValue = null;
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists() && documentSnapshot.contains("type")) {
                                typeValue = documentSnapshot.getString("type");
                            }
                        }
                        return typeValue;
                    }
                });
    }

    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatroom").document(chatroomId);

    }
    public static String getChatroomId(String userId1,String userId2){
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        }else{
            return userId2+"_"+userId1;
        }

    }
    public static CollectionReference getChatroomMessageReference(String chatroomId)
    {
        return getChatroomReference(chatroomId). collection("chats");
    }
    public static DocumentReference getChatroomPropertyReference(String chatroomId)
    {
        return getChatroomReference(chatroomId). collection("CurrentProperty").document(chatroomId);
    }
    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatroom");
    }

    public static DocumentReference getOtheerUserFromChatroomModel(List<String> userIds){
        if(userIds.get(0).equals(FirebaseUtil.currentuserId()))
        {
            return allUserCollectionReference().document(userIds.get(1));
        }else{
            return allUserCollectionReference().document(userIds.get(0));
        }
    }
    public static DocumentReference otherUserDetials(String id){
        return FirebaseFirestore.getInstance().collection("users").document(id);

    }
    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy 'at' h:mm:ss a 'UTC'Z", Locale.ENGLISH);
        return sdf.format(timestamp.toDate());
    }

}


