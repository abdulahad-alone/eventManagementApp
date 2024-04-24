package com.example.eventapp.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.eventapp.Models.Meals;
import com.example.eventapp.R;
import com.example.eventapp.adapter.BookProductAdapter;
import com.example.eventapp.adapter.ShowMealAdapter;
import com.example.eventapp.meals;
import com.example.eventapp.utils.FirebaseUtil;
import com.example.eventapp.utils.OrderNumberGenerator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BookBottomFragment extends BottomSheetDialogFragment  implements ShowMealAdapter.OnItemClickListener {

    private String Price, VenueTitle,description, totalPrice, langitute, latitude, Key,profile,phone, userName,location_, current_userId, this_userId;
    String purpose, ProType, bedroomno,bathroomno,areaunitselector,area_size ,timeStamp;
    private ArrayList<String> images;
    RelativeLayout fav_relative_layout;
    ArrayList<String> newDataList = new ArrayList<>();
    ArrayList<String> newDataPriceList = new ArrayList<>();
    TextView fav_price,fav_short_description,fav_location;
    RecyclerView recycler_view_for_meals,newRecyclerView;
    AppCompatButton book_now,set_persons;
    CalendarView calendar;
    EditText getnumber,getphoneNo,getFullName,getaddress;
    TextView date_view,total_price_in_bottom_toolbar;
    List<String> selectedDates = new ArrayList<>();
    boolean checknotif=true;
    double totalpriceFromMeal=0.0,totalpriceFromcalender=0.0,totalpersons=0.0,universalPrice=0.0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;

    List<Meals> meals = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_bottom_fragment, container, false);
        return  view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fav_price=view.findViewById(R.id.fav_price);
        fav_relative_layout=view.findViewById(R.id.fav_relative_layout);
        fav_short_description=view.findViewById(R.id.fav_short_description);
        fav_location=view.findViewById(R.id.fav_location);
        recycler_view_for_meals=view.findViewById(R.id.recycler_view_for_meals);
        book_now=view.findViewById(R.id.book_now);
        newRecyclerView=view.findViewById(R.id.newRecyclerView);
        getnumber=view.findViewById(R.id.getnumber);
        getaddress=view.findViewById(R.id.getaddress);
        getFullName=view.findViewById(R.id.getFullName);
        getphoneNo=view.findViewById(R.id.getphoneNo);
        set_persons=view.findViewById(R.id.set_persons);
        total_price_in_bottom_toolbar=view.findViewById(R.id.total_price_in_bottom_toolbar);
        calendar = (CalendarView)
                view.findViewById(R.id.calendar);
        date_view = (TextView)
                view.findViewById(R.id.date_view);
        calendar.setVisibility(View.INVISIBLE);
        Bundle args = getArguments();
        if (args != null) {
            Price=args.getString("price");
            location_=args.getString("location");
            description=args.getString("description");
            VenueTitle=args.getString("VenueTitle");
            Key=args.getString("id");
            langitute=args.getString("langitute");
            latitude=args.getString("latitude");
            this_userId=args.getString("userId");
            phone=args.getString("contactNo");
            ProType=args.getString("category");
            timeStamp=args.getString("timeStamp");
            images = args.getStringArrayList("images");
            meals = (List<Meals>) args.getSerializable("meals");
            Log.e(TAG, "images Uri: "+images );
            Log.e(TAG, "meal Uri: "+meals.get(0).getMealName() );
            fav_price.setText("per head "+Price);
            fav_short_description.setText(VenueTitle);
            fav_location.setText(location_);
            total_price_in_bottom_toolbar.setText("0");
        }
        String firstImg= images.get(0);
        Glide.with(this)
                .load(firstImg)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        fav_relative_layout.setBackground( resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {


                    }
                });
        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.setVisibility(View.VISIBLE);
            }
        });
        showMeals(meals);


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
                                    minusTotalPriceFromCalender();
                                } else {
                                    // Otherwise, add it to the list
                                    updateTotalPriceFromCalender(universalPrice);
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
        set_persons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=getnumber.getText().toString(),parsePrice;
                Double total=0.0;
                if(number!=null && ! number.isEmpty())
                {
                    getnumber.setError(null);
                    total=totalpersons*Double.parseDouble(number);
                    parsePrice=formatPrice(String.valueOf(total));
                    universalPrice=total;
                    totalPrice= String.valueOf(total);
                    total_price_in_bottom_toolbar.setText(parsePrice);

                }else{
                    getnumber.setError("Please enter number first");
                }
            }
        });
        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Posting please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String Price, phoneNo, noOfPersons, totalPrice_,fullName,address;
                Timestamp timestamp;
                Price = total_price_in_bottom_toolbar.getText().toString();
                totalPrice_ = totalPrice;
                noOfPersons = getnumber.getText().toString();
                phoneNo = getphoneNo.getText().toString();
                fullName = getFullName.getText().toString();
                address = getaddress.getText().toString();

                Map<String, Object> booking = new HashMap<>();
                booking.put("price", Price);
                booking.put("totalPrice", totalPrice_);
                booking.put("noOfPersons", noOfPersons);
                booking.put("CurrentUserId", FirebaseUtil.currentuserId());
                booking.put("ProductId", Key);
                booking.put("ProductUserId", this_userId);
                booking.put("BookingDates", selectedDates);
                booking.put("phone", phoneNo);
                booking.put("FullName", fullName);
                booking.put("address", address);
                booking.put("timeStamp", Timestamp.now());
                booking.put("Type_of_ad", ProType);
                booking.put("Notification", checknotif);
                String orderNumber = OrderNumberGenerator.generateOrderNumber();
                booking.put("orderNumber", orderNumber);

                if (!newDataList.isEmpty()) {
                    List<Meals> selectedMeals = new ArrayList<>();
                    for (String serviceName : newDataList) {
                        for (Meals meal : meals) {
                            if (meal.getMealName().equals(serviceName)) {
                                selectedMeals.add(meal);
                                break; // Break the inner loop once the meal is found
                            }
                        }
                    }
                    booking.put("meals", selectedMeals);
                }
                DocumentReference productRef =db.collection("Booking").document(UUID.randomUUID().toString());
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
    private void updateTotalPriceFromCalender(double UniversalPrice) {
        double total;
        Log.e(TAG, "updateTotalPriceFromCalender: "+UniversalPrice );
        Log.e(TAG, "updateTotalender: "+universalPrice );
        if (newDataPriceList == null || newDataPriceList.isEmpty()) {
            total=Double.parseDouble(Price)+totalpriceFromcalender;
            totalpriceFromcalender=total;
            totalpersons=total;
            totalpriceFromMeal=total;
            total_price_in_bottom_toolbar.setText(String.valueOf(total));
        }else {

            total=totalpriceFromMeal+Double.parseDouble(total_price_in_bottom_toolbar.getText().toString());
            totalpersons=total;
            universalPrice=total;
            total_price_in_bottom_toolbar.setText(String.valueOf(total));
        }


    }
    private void minusTotalPriceFromCalender() {
        double total;
        if (totalpriceFromMeal == 0.0)
        {
            total=universalPrice-Double.parseDouble(Price);
            totalpriceFromcalender=total;
            totalpersons=total;
            total_price_in_bottom_toolbar.setText(String.valueOf(total));
        }else
        {
            total=universalPrice-totalpriceFromMeal;
            totalpersons=total;
            total_price_in_bottom_toolbar.setText(String.valueOf(total));
        }
    }

    private void showMeals(List<Meals> meals) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_for_meals.setLayoutManager(layoutManager);
        ShowMealAdapter adapter = new ShowMealAdapter(meals);
        adapter.setOnItemClickListener(this);
        recycler_view_for_meals.setAdapter(adapter);


    }

    @Override
    public void onItemClick(int position) {
        // Handle item click here
        String servicesname = meals.get(position).getMealName();
        String newPrice = meals.get(position).getMealPrice();
        if (servicesname != null && !servicesname.isEmpty()) {
            newDataList.add(servicesname);
            newDataPriceList.add(newPrice);
        }
        if (!newDataList.isEmpty() && !newDataPriceList.isEmpty()) {
            Log.e(TAG, "features list: " + newDataList);
            Log.e(TAG, "price list: "+newDataPriceList );
            BookProductAdapter newDataAdapter = new BookProductAdapter(newDataList, newDataPriceList, total_price_in_bottom_toolbar);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            newRecyclerView.setLayoutManager(layoutManager);
            newRecyclerView.setAdapter(newDataAdapter);
            Log.d("ItemClicked", "Clicked item position: " + meals.get(position).getMealName());
            updateTotalPrice();
        }
    }
    private void updateTotalPrice() {
        double total = 0.0;
        for (String price : newDataPriceList) {
            total += Double.parseDouble(price);
        }
        if (newDataPriceList.isEmpty() || newDataPriceList==null)
        {
            total_price_in_bottom_toolbar.setText("0.0");
        }

        if (total_price_in_bottom_toolbar.getText().toString() =="0.0")
        {
            totalpriceFromMeal=total;
            totalpersons=total;
            total_price_in_bottom_toolbar.setText(String.valueOf(total));
        }else {
            total+=totalpriceFromcalender;
            totalpersons=total;
            total_price_in_bottom_toolbar.setText(String.valueOf(total));
        }


    }
}