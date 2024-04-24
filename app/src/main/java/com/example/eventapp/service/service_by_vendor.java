package com.example.eventapp.service;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.eventapp.R;
import com.example.eventapp.Sub_detailed_activity;
import com.example.eventapp.utils.FirebaseUtil;
import com.example.eventapp.utils.FirestoreUtils;
import com.example.eventapp.utils.OrderNumberGenerator;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class service_by_vendor extends AppCompatActivity {
String name,imageUrl,description,minimum_orderSt,id,ServicePrice,this_product_userId,ServiceRef,
        totalNoOfDaigs,lol_price,manydays;
    TextView minimum_order,propduct_name,total_price_in_bottom_toolbar,des;
    TextView t1, t2, t3,total_kgs,quantity,date_view;
EditText getnumber;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
AppCompatButton set_quantity,book_now;
    List<String> selectedDates = new ArrayList<>();
    CalendarView calendar;
CardView add_daigs_quanlity;
ProgressDialog progressDialog;
    EditText getphoneNo,getFullName,getaddress;
    boolean checknotif=true;
    ArrayList<String> imageUrls=new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_by_vendor);

        name=getIntent().getStringExtra("ServiceName");
        imageUrl=getIntent().getStringExtra("imageUrl");
        description=getIntent().getStringExtra("description");
        minimum_orderSt=getIntent().getStringExtra("minimum_orderSt");
        id=getIntent().getStringExtra("id");
        ServiceRef=getIntent().getStringExtra("ServiceRef");
        ServicePrice=getIntent().getStringExtra("ServicePrice");
        Log.e(TAG, "onCreate: "+minimum_orderSt );

        ImageSlider imageSlider;
        imageSlider =findViewById(R.id.image_slider);
        imageUrls.add(imageUrl);
        ArrayList<PhotoView> photoViews = new ArrayList<>();
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for (String urls : imageUrls) {
            imageList.add(new SlideModel(urls, null));
            PhotoView photoView = new PhotoView(this);
            Glide.with(this).load(urls).into(photoView);
            photoView.setMaximumScale(10); // Set max zoom level

            // Add the PhotoView to the list
            photoViews.add(photoView);



        }
        imageSlider.setImageList(imageList, ScaleTypes.FIT);
        imageSlider.stopSliding() ;
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                String imageUrl = imageUrls.get(i);
                new StfalconImageViewer.Builder<>(service_by_vendor.this, Collections.singletonList(imageUrl), (imageView, url) -> {
                    // Load the image from the URL into the provided ImageView using your image loading library
                    Picasso.get().load(url).into(imageView);
                }).show();
            }

            @Override
            public void doubleClick(int i) {

            }
        });

        minimum_order=findViewById(R.id.minimum_order);
        propduct_name=findViewById(R.id.propduct_name);
        des=findViewById(R.id.des);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        book_now = findViewById(R.id.book_now);
        getnumber = findViewById(R.id.getnumber);
        set_quantity = findViewById(R.id.set_quantity);
        quantity = findViewById(R.id.quantity);
        add_daigs_quanlity = findViewById(R.id.add_daigs_quanlity);
        getaddress=findViewById(R.id.getaddress);
        getFullName=findViewById(R.id.getFullName);
        getphoneNo=findViewById(R.id.getphoneNo);
        total_price_in_bottom_toolbar = findViewById(R.id.total_price_in_bottom_toolbar);
        calendar = (CalendarView)
                findViewById(R.id.calendar);
        date_view = (TextView)
                findViewById(R.id.date_view);
        calendar.setVisibility(View.INVISIBLE);

        quantity.setText("Total "+name);
        getingUserId();
        propduct_name.setText(name);
        des.setText(description);
        minimum_order.setText("Minimum order is "+minimum_orderSt);
        set_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double totalNumber= Double.parseDouble(getnumber.getText().toString());
                totalNumber=totalNumber*Double.parseDouble(ServicePrice);
                lol_price=String.valueOf(totalNumber);
                total_price_in_bottom_toolbar.setText(lol_price);
                manydays=t2.getText().toString();

            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inc(false);
            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inc(true);
            }
        });

        add_daigs_quanlity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double quantity=Double.parseDouble(t2.getText().toString());
                totalNoOfDaigs=t2.getText().toString();

                Double total;
                total=Double.parseDouble(lol_price)*quantity;
                total_price_in_bottom_toolbar.setText(String.valueOf(total));
            }
        });
        calendar
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override

                            // In this Listener have one method
                            // and in this method we will
                            // get the value of DAYS, MONTH, YEARS
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {

                                // Store the value of date with
                                // format in String type Variable
                                // Add 1 in month because month
                                // index is start with 0
                                String date
                                        = dayOfMonth + "-"
                                        + (month + 1) + "-" + year;
                                if (selectedDates.contains(date)) {
                                    selectedDates.remove(date);

                                } else {
                                    // Otherwise, add it to the list

                                    selectedDates.add(date);


                                }

                                // Update the UI to show the selected dates (optional)
                                StringBuilder selectedDatesText = new StringBuilder();
                                for (String selectedDate : selectedDates) {
                                    selectedDatesText.append(selectedDate).append("\n");
                                    Log.e(TAG, "selected date in list: "+selectedDates );
                                }
                                date_view.setText(selectedDatesText.toString());
                            }

                        });
        calendar.setUnfocusedMonthDateColor(Color.BLACK);
        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.setVisibility(View.VISIBLE);
            }
        });
        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Price, phoneNo,  fullName, address,total;
                Timestamp timestamp;
                Price = total_price_in_bottom_toolbar.getText().toString();
                phoneNo = getphoneNo.getText().toString();
                fullName = getFullName.getText().toString();
                address = getaddress.getText().toString();
                total=getnumber.getText().toString();


                if (Price.isEmpty() && phoneNo.isEmpty() && fullName.isEmpty() && address.isEmpty()&& total.isEmpty()&& manydays.isEmpty()) {
                    Toast.makeText(service_by_vendor.this, "Please fill all fields", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog = new ProgressDialog(service_by_vendor.this);
                    progressDialog.setMessage("Posting please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    uploading(Price,phoneNo,  fullName, address,total);
                }
            }
        });

    }

    private void uploading(String price, String phoneNo, String fullName, String address, String total) {
        Map<String, Object> booking = new HashMap<>();
        booking.put("Totalprice", price);
        booking.put("CurrentUserId", FirebaseUtil.currentuserId());
        booking.put("ProductId", id);
        booking.put("ProductUserId", this_product_userId);
        booking.put("BookingDates", selectedDates);
        booking.put("Title", name);
        booking.put("description", description);
        booking.put("ServiceRef", ServiceRef);
        booking.put("phone", phoneNo);
        booking.put("FullName", fullName);
        booking.put("address", address);
        booking.put("timeStamp", Timestamp.now());
        booking.put("imageUrl", imageUrl);
        booking.put("Type_of_ad", "Service");
        booking.put("manydays", t2.getText().toString());
        booking.put("totalNoOf", total);
        booking.put("Notification", checknotif);
        String orderNumber = OrderNumberGenerator.generateOrderNumber();
        booking.put("orderNumber", orderNumber);
        DocumentReference productRef = db.collection("Booking").document(UUID.randomUUID().toString());
        booking.put("documentId", productRef.getId());
        productRef.set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(service_by_vendor.this, "Booking successful", Toast.LENGTH_SHORT).show();

                // Use the document ID if needed
                // Add document ID to the booking data
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void inc(Boolean x){
        int y = Integer.parseInt(t2.getText().toString());
        if(x){
            y++;
            t2.setText(String.valueOf(y));
        }else {
            y--;
            if(y == 0){

            }else {
                t2.setText(String.valueOf(y));
            }
        }



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

}