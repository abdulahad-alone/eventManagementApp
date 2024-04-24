package com.example.eventapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eventapp.Models.UserModel;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AndroidUtil {
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public  static  void passUserModelAsintent(Intent intent, UserModel userModel)

    {
        intent.putExtra("Username", userModel.getName());
        intent.putExtra("email", userModel.getEmail());
        intent.putExtra("userId", userModel.getUserId());


    }
    public static UserModel getUserModelFromIntent(Intent intent)
    {
        UserModel userModel=new UserModel();
        userModel.setName(intent.getStringExtra("Username"));
        userModel.setEmail(intent.getStringExtra("email"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return  userModel;
    }
    public static  void setProfilePic(Context context, Uri uri, CircleImageView imageView)
    {
        Glide.with(context).load(uri).into(imageView);
    }
    public static  void setCoverPic(Context context, Uri uri, ImageView imageView)
    {
        Glide.with(context).load(uri).centerCrop().into(imageView);
    }

    public static  void set_ProfilePic(Context context, Uri uri, ImageView imageView)
    {
        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy 'at' h:mm:ss a", Locale.ENGLISH);
        return sdf.format(timestamp.toDate());
    }

    public static String formatedDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);

        try {
            Date date = inputFormat.parse(inputDate);
            String formattedDate = outputFormat.format(date);
            return formattedDate;  // Output: Mar 30, 2024
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception, for example, return a default value or an error message
            return "Invalid Date"; // Or return null or throw a custom exception
        }
    }

    public static String formatTime(String inputTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        try {
            Date time = inputFormat.parse(inputTime);
            return outputFormat.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid Time"; // Or return null or throw a custom exception
        }
    }
}
