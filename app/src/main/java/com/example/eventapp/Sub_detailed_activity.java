package com.example.eventapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.eventapp.Models.DaigModel;
import com.example.eventapp.adapter.ShowSomeThing;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Sub_detailed_activity extends AppCompatActivity implements ShowSomeThing.TotalPriceListener,
        View.OnClickListener, ShowSomeThing.UpdateQuanlity {
    TextView minimum_order, propduct_name, total_price_in_bottom_toolbar;
    TextView t1, t2, t3, total_kgs;
    String totalNoOfDaigs;
    AppCompatButton book_now;
    String despOFQuanliySt;
    CardView add_daigs_quanlity;
    ShowSomeThing adapter;
    boolean checknotif=true;
    int pricesSize;
    EditText getphoneNo, getFullName, getaddress;
    String total_price, DaigRef;
    ProgressDialog progressDialog;
    HashMap<String, Object> hashMapData;
    String name, description, DaigPrice, id, minimumOrder;
    ArrayList<String> imageUrls = new ArrayList<>();
    ArrayList<String> despOFQuanliyList = new ArrayList<>();
    RecyclerView Daig_ingrediet_rv;
    Double lol_price;
    String this_product_userId, selectDate, selectTime;
    ArrayList<DaigModel> daigModelsList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    String url, Type_of_ad;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private List<String> dataList = new ArrayList<>();
    private List<String> clickedDataList = new ArrayList<>();
    List<DaigModel> daigModels = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_detailed_activity);
        minimum_order = findViewById(R.id.minimum_order);
        propduct_name = findViewById(R.id.propduct_name);
        book_now = findViewById(R.id.book_now);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        add_daigs_quanlity = findViewById(R.id.add_daigs_quanlity);
        t3 = findViewById(R.id.t3);
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        total_kgs = (TextView) findViewById(R.id.total_kgs);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        Daig_ingrediet_rv = findViewById(R.id.Daig_ingrediet_rv);
        total_price_in_bottom_toolbar = findViewById(R.id.total_price_in_bottom_toolbar);


        getaddress = findViewById(R.id.getaddress);
        getFullName = findViewById(R.id.getFullName);
        getphoneNo = findViewById(R.id.getphoneNo);


        ImageSlider imageSlider;
        imageSlider = findViewById(R.id.image_slider);

        name = getIntent().getStringExtra("DaigName");
        description = getIntent().getStringExtra("description");

        url = getIntent().getStringExtra("imageUrl");
        imageUrls.add(url);
        DaigPrice = getIntent().getStringExtra("DaigPrice");
        id = getIntent().getStringExtra("id");
        DaigRef = getIntent().getStringExtra("DaigRef");
        Type_of_ad = getIntent().getStringExtra("Type");
        minimumOrder = getIntent().getStringExtra("minimumOrder");
        daigModelsList = getIntent().getParcelableArrayListExtra("daigModels");
        propduct_name.setText(name);
        minimum_order.setText("Minimum order is " + minimumOrder + "kg");
        getingUserId();
        Log.e(TAG, "Type_of_ad: " + Type_of_ad);
        Log.e(TAG, "imageUrl: " + daigModelsList.get(0));
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
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        imageSlider.setImageList(imageList, ScaleTypes.FIT);
        imageSlider.stopSliding();
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                String imageUrl = imageUrls.get(i);
                new StfalconImageViewer.Builder<>(Sub_detailed_activity.this, Collections.singletonList(imageUrl), (imageView, url) -> {
                    // Load the image from the URL into the provided ImageView using your image loading library
                    Picasso.get().load(url).into(imageView);
                }).show();
            }

            @Override
            public void doubleClick(int i) {

            }
        });

        gettingDaigIngredient();

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

                Double quantity = Double.parseDouble(t2.getText().toString());
                totalNoOfDaigs = t2.getText().toString();
                Log.e(TAG, "total: " + lol_price);
                Log.e(TAG, "despOFQuanliyList: " + despOFQuanliyList);
                Double total;
                total = lol_price * quantity;
                total_price_in_bottom_toolbar.setText(String.valueOf(total));


// Assuming dataList is your list containing elements


            }
        });
        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int listSize = despOFQuanliyList.size();
                if (listSize >= pricesSize) {
                    List<String> lastNElements = despOFQuanliyList.subList(listSize - pricesSize, listSize);
                    Log.e(TAG, "lastNElements: " + lastNElements);
                    despOFQuanliyList = new ArrayList<>();
                    // Now lastNElements contains the last 'size' elements from dataList
                    // You can use lastNElements as needed
                } else {
                    // Handle case where dataList has fewer than 'size' elements
                }

                String Price, phoneNo, noOfPersons, totalInkgs_, fullName, address;
                Timestamp timestamp;
                Price = total_price_in_bottom_toolbar.getText().toString();
                phoneNo = getphoneNo.getText().toString();
                fullName = getFullName.getText().toString();
                address = getaddress.getText().toString();
                totalInkgs_ = total_kgs.getText().toString();

                if (Price.isEmpty() && phoneNo.isEmpty() && fullName.isEmpty() && address.isEmpty() && totalInkgs_.isEmpty()) {
                    Toast.makeText(Sub_detailed_activity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog = new ProgressDialog(Sub_detailed_activity.this);
                    progressDialog.setMessage("Posting please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Map<String, Object> booking = new HashMap<>();
                    booking.put("Totalprice", Price);
                    booking.put("CurrentUserId", FirebaseUtil.currentuserId());
                    booking.put("ProductId", id);
                    booking.put("ProductUserId", this_product_userId);
                    booking.put("BookingDate", selectDate);
                    booking.put("BookingTime", selectTime);
                    booking.put("Title", name);
                    booking.put("description", description);
                    booking.put("DaigRef", DaigRef);
                    booking.put("phone", phoneNo);
                    booking.put("FullName", fullName);
                    booking.put("address", address);
                    booking.put("timeStamp", Timestamp.now());
                    booking.put("imageUrl", url);
                    booking.put("Type_of_ad", Type_of_ad);
                    booking.put("TotalKgs", totalInkgs_);
                    booking.put("totalNoOfDaigs", totalNoOfDaigs);
                    booking.put("Notification", checknotif);
                    String orderNumber = OrderNumberGenerator.generateOrderNumber();
                    booking.put("orderNumber", orderNumber);

                    DocumentReference productRef = db.collection("Booking").document(UUID.randomUUID().toString());
                    booking.put("documentId", productRef.getId());
                    productRef.set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(Sub_detailed_activity.this, "Booking successful", Toast.LENGTH_SHORT).show();

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
            }
        });


    }

    private void gettingDaigIngredient() {
        // HashMap<String, Object> hashMapData = (HashMap<String, Object>) getIntent().getSerializableExtra("daigModels");
        Log.e(TAG, "gettingDaigIngredient: " + daigModelsList.get(0));
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();

        for (HashMap<String, Object> hashMap : daigModelsList) {
            // Extract values from each HashMap
            String price = (String) hashMap.get("price");
            String ingredient = (String) hashMap.get("ingredient");

            // Add values to respective ArrayLists
            prices.add(price);
            ingredients.add(ingredient);
        }
        pricesSize = prices.size();
        LinearLayoutManager layoutManager = new LinearLayoutManager(Sub_detailed_activity.this, LinearLayoutManager.VERTICAL, false);
        Daig_ingrediet_rv.setLayoutManager(layoutManager);
        adapter = new ShowSomeThing(prices, ingredients, total_price_in_bottom_toolbar, this, this, total_kgs);
        //adapter.setOnItemClickListener(this);
        Daig_ingrediet_rv.setAdapter(adapter);
        String total_price = total_price_in_bottom_toolbar.getText().toString();
        Log.e(TAG, "gettingDaigIngredient: " + total_price);
// Check if the total_price is a valid numeric value before using it
        try {
            double totalPriceValue = Double.parseDouble(total_price);
            // Now you can use totalPriceValue as the total price
            Log.e(TAG, "gettingDaigIngredient: " + total_price);
        } catch (NumberFormatException e) {
            // Handle the case where total_price is not a valid numeric value
            // For example, display an error message or set a default value
            e.printStackTrace();
        }
    }

    private void inc(Boolean x) {
        int y = Integer.parseInt(t2.getText().toString());
        if (x) {
            y++;
            t2.setText(String.valueOf(y));
        } else {
            y--;
            if (y == 0) {

            } else {
                t2.setText(String.valueOf(y));
            }
        }


    }


    @Override
    public void onTotalPriceChanged(double totalPrice) {
        // Update the total price in your activity
        lol_price = totalPrice;

    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
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

    @Override
    public void onQuantityChanged(String des) {
        despOFQuanliySt = des;
        despOFQuanliyList.add(des);

        Log.e(TAG, "onQuantityChanged: " + des);
    }
}
