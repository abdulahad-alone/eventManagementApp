package com.example.eventapp.detailed_activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventapp.Models.Meals;
import com.example.eventapp.R;
import com.example.eventapp.Sub_detailed_activity;
import com.example.eventapp.utils.AndroidUtil;
import com.example.eventapp.utils.FirebaseUtil;
import com.example.eventapp.utils.FirestoreUtils;
import com.example.eventapp.utils.OrderNumberGenerator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class fragment_for_booking_package extends BottomSheetDialogFragment implements View.OnClickListener{
    String PriceOfPackage,NameOfPackage,description,PackageRef,selectDate, selectTime;
    ArrayList<String> serviceList=new ArrayList<>();
    TextView price,name,des,packageServices,total_price_in_bottom_toolbar;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    boolean checknotif=true;
    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText getphoneNo, getFullName, getaddress;
    AppCompatButton book_now;
    LinearLayout package_item,transportation_item;
    String id;
    public  RelativeLayout item_of_package;
    ProgressDialog progressDialog;
    String this_product_userId,imageUrI,Pro_type="0",TermsCondition,getCapacity;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_for_booking_package, container, false);

        price=view.findViewById(R.id.price);
        name=view.findViewById(R.id.name);
        des=view.findViewById(R.id.des);
        packageServices=view.findViewById(R.id.packageServices);
        btnDatePicker = (Button) view.findViewById(R.id.btn_date);
        btnTimePicker = (Button) view.findViewById(R.id.btn_time);
        txtDate = (EditText) view.findViewById(R.id.in_date);

        txtTime = (EditText) view.findViewById(R.id.in_time);

        book_now = view.findViewById(R.id.book_now);

        getaddress = view.findViewById(R.id.getaddress);
        getFullName = view.findViewById(R.id.getFullName);
        getphoneNo = view.findViewById(R.id.getphoneNo);
        total_price_in_bottom_toolbar = view.findViewById(R.id.total_price_in_bottom_toolbar);
        return  view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Bundle args = getArguments();




        if (args != null) {

                NameOfPackage=args.getString("nameOfPackage");
                PriceOfPackage=args.getString("priceOfPackage");
                description=args.getString("description");
                PackageRef=args.getString("PackageRef");
                id=args.getString("id");
                imageUrI=args.getString("imageUrI");
            serviceList = (ArrayList<String>) args.getSerializable("ServicesList");
            Log.e(TAG, "onViewCreated: "+imageUrI );

            price.setText("Pkr "+PriceOfPackage);
            name.setText(NameOfPackage);
            des.setText(description);

            total_price_in_bottom_toolbar.setText(formatPrice(PriceOfPackage));
            StringBuilder stringBuilder = new StringBuilder();
            for (String service : serviceList) {
                stringBuilder.append("* ").append(service).append("\n");
            }
            packageServices.setText(stringBuilder.toString());
        }
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        book_now.setOnClickListener(this);
        getingUserId();

    }
    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            selectDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            txtDate.setText(selectDate);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            selectTime = hourOfDay + ":" + minute;
                            txtTime.setText(selectTime);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if(v==book_now)
        {
            String Price, phoneNo, noOfPersons, totalInkgs_, fullName, address;
            Timestamp timestamp;

            phoneNo = getphoneNo.getText().toString();
            fullName = getFullName.getText().toString();
            address = getaddress.getText().toString();
            if (phoneNo.isEmpty() && fullName.isEmpty() && address.isEmpty() && txtDate.getText().toString().isEmpty() && txtTime.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();

            }else
            {
                upload(phoneNo,fullName,address);
            }
        }
    }

    private void upload(String phoneNo, String fullName, String address) {
       progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Posting please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.e(TAG, "upload: "+this_product_userId );
        Map<String, Object> booking = new HashMap<>();
        booking.put("priceOfPackage", PriceOfPackage);
        booking.put("nameOfPackage", NameOfPackage);
        booking.put("CurrentUserId", FirebaseUtil.currentuserId());
        booking.put("ProductId", id);
        booking.put("ProductUserId", this_product_userId);
        booking.put("BookingDate", selectDate);
        booking.put("BookingTime", selectTime);
        booking.put("description", description);
        booking.put("PackageRef", PackageRef);
        booking.put("serviceList", serviceList);
        booking.put("phone", phoneNo);
        booking.put("FullName", fullName);
        booking.put("address", address);
        booking.put("timeStamp", Timestamp.now());
        booking.put("imageUrl", imageUrI);
       booking.put("Type_of_ad", "Package");
        booking.put("Notification", checknotif);
        String orderNumber = OrderNumberGenerator.generateOrderNumber();
        booking.put("orderNumber", orderNumber);

        DocumentReference productRef = db.collection("Booking").document(UUID.randomUUID().toString());
        booking.put("documentId", productRef.getId());
        productRef.set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Booking successful", Toast.LENGTH_SHORT).show();

                // Use the document ID if needed
                // Add document ID to the booking data
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
    public void getingUserId() {
        Log.e(TAG, "getingUserId: " + id);
        FirestoreUtils.fetchUserId(id, new FirestoreUtils.OnUserIdFetchedListener() {
            @Override
            public void onUserIdFetched(String userId) {
                Log.d("TAG", "User ID: " + userId);
                this_product_userId = userId;
                // Use the userId as needed
            }

            @Override
            public void onDocumentNotFound() {
                Log.d("TAG", "No such document");
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("TAG", "Error: " + errorMessage);
            }
        });

    }
    private String formatPrice(String price) {
        if (price.isEmpty()) {
            return ""; // Return an empty string if the price is empty
        }

        double priceValue = Double.parseDouble(price);
        String formattedPrice;
        if (priceValue >= 10000000) {
            formattedPrice = String.format("%.2f", priceValue / 10000000) + " Crore"; // Convert to Crore
        }
        else if (priceValue >= 100000) {
            formattedPrice = String.format("%.2f", priceValue / 100000) + " Lakh"; // Convert to Lakhs
        } else if (priceValue >= 1000) {
            formattedPrice = String.format("%.1f", priceValue / 1000) + " Thousand"; // Convert to Thousands
        }
        else {
            formattedPrice = price; // No conversion needed
        }

        return formattedPrice;
    }
}