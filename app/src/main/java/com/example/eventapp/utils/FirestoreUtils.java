package com.example.eventapp.utils;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreUtils {

    public static void fetchUserId(String documentId, OnUserIdFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Products").document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, extract the value of "userId"
                        String userId = documentSnapshot.getString("userId");
                        // Use the userId as needed
                        if (listener != null) {
                            listener.onUserIdFetched(userId);
                        }
                    } else {
                        if (listener != null) {
                            listener.onDocumentNotFound();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreUtils", "Error getting document: " + e.getMessage());
                    if (listener != null) {
                        listener.onError(e.getMessage());
                    }
                });
    }

    public interface OnUserIdFetchedListener {
        void onUserIdFetched(String userId);
        void onDocumentNotFound();
        void onError(String errorMessage);
    }
}
