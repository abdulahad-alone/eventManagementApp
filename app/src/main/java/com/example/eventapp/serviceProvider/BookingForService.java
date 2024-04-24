package com.example.eventapp.serviceProvider;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.R;
import com.example.eventapp.RentoutService;
import com.example.eventapp.adapter.AddFeaturesAdapter;
import com.example.eventapp.detailed_activities.add_packages;
import com.example.eventapp.utils.FirebaseUtil;
import com.example.eventapp.utils.OrderNumberGenerator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BookingForService extends AppCompatActivity {
AutoCompleteTextView autoCompleteTextView;
    String[] item={"Indoor","Outdoor"};
    TextView FoodTx,parsedPriceStart,date_view,citytxt;
    EditText ServicesEdit,enter_price,event,DaigType,total_number_of_persons;
    private ProgressDialog progressDialog;
    ImageView add_Daig_to_rv,coverAreaimg,close;
    ArrayList<String> ServicesEditList = new ArrayList<>();
    RecyclerView Daig_ingrediet_rv;
    AddFeaturesAdapter adapter;
    String formattedPrice;
    String PriceEnd,UpperPrice, totalNoOfDaigs;
    CalendarView calendar;
    List<String> selectedDates = new ArrayList<>();
    String Type,ProType,Key,this_userId;
    AppCompatButton book_now;
    TextView t1, t2, t3,total_price_in_bottom_toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CardView add_daigs_quanlity;
String City,Price,imageUri;
    EditText getnumber,getphoneNo,getFullName,getaddress;
    boolean checknotif=true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_for_service);
        autoCompleteTextView=findViewById(R.id.auto_complete_Text);
        FoodTx=findViewById(R.id.FoodTx);
        ServicesEdit=findViewById(R.id.ServicesEdit);
        add_Daig_to_rv=findViewById(R.id.add_Daig_to_rv);
        citytxt=findViewById(R.id.citytxt);
        parsedPriceStart=findViewById(R.id.parsedPriceStart);
        Daig_ingrediet_rv=findViewById(R.id.Daig_ingrediet_rv);
        total_number_of_persons=findViewById(R.id.total_number_of_persons);
        book_now=findViewById(R.id.book_now);
        event=findViewById(R.id.event);
        enter_price=findViewById(R.id.enter_price);
        DaigType=findViewById(R.id.DaigType);
        date_view=findViewById(R.id.date_view);
        close=findViewById(R.id.close);
        coverAreaimg=findViewById(R.id.coverAreaimg);
        getnumber=findViewById(R.id.getnumber);
        getaddress=findViewById(R.id.getaddress);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        add_daigs_quanlity = findViewById(R.id.add_daigs_quanlity);
        total_price_in_bottom_toolbar = findViewById(R.id.total_price_in_bottom_toolbar);


        getFullName=findViewById(R.id.getFullName);
        getphoneNo=findViewById(R.id.getphoneNo);
        calendar=findViewById(R.id.calendar);
        City = getIntent().getStringExtra("City");
        ProType = getIntent().getStringExtra("Category");
        Key = getIntent().getStringExtra("id");
        this_userId = getIntent().getStringExtra("this_userId");
        imageUri = getIntent().getStringExtra("imageUri");
        Price = getIntent().getStringExtra("Price");
        citytxt.setText("We Operate in "+City);



        ArrayAdapter<String>  adapteritem ;
        adapteritem=new ArrayAdapter<String>(this,R.layout.list_item,item );
        autoCompleteTextView.setAdapter(adapteritem);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Type=item;

            }
        });
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
                    LinearLayoutManager layoutManager = new LinearLayoutManager(BookingForService.this, LinearLayoutManager.HORIZONTAL, false);
                    Daig_ingrediet_rv.setLayoutManager(layoutManager);
                    Daig_ingrediet_rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    // Clear EditText fields after adding to RecyclerView
                    ServicesEdit.setText("");



                }
            }
        });

        coverAreaimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoPopup(coverAreaimg);
            }
        });
        enter_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String price_ = charSequence.toString();
                formattedPrice = formatPrice(price_); // Implement the formatting logic
                parsedPriceStart.setText(formattedPrice);
                PriceEnd=enter_price.getText().toString();
                UpperPrice=charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.setVisibility(View.VISIBLE);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
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
                total=Double.parseDouble(Price)*quantity;
                total_price_in_bottom_toolbar.setText(String.valueOf(total));
            }
        });
    }

    private void upload() {
        String eventSt=event.getText().toString();
        String DaigTypeSt=DaigType.getText().toString();
        String total_number_of_personsSt=total_number_of_persons.getText().toString();
        String enter_priceSt=enter_price.getText().toString();
        String ActualPrice=total_price_in_bottom_toolbar.getText().toString();
        String  phoneNo,fullName,address;

        phoneNo = getphoneNo.getText().toString();
        fullName = getFullName.getText().toString();
        address = getaddress.getText().toString();

        if(eventSt.isEmpty() && Type.isEmpty() && DaigTypeSt.isEmpty()
                && ServicesEditList.isEmpty() && selectedDates.isEmpty()
                && total_number_of_personsSt.isEmpty() && enter_priceSt.isEmpty()
                && phoneNo.isEmpty() && fullName.isEmpty() && address.isEmpty()
                &&ActualPrice.isEmpty()
        )
        {
            Toast.makeText(BookingForService.this, "Please Fill all Field", Toast.LENGTH_SHORT).show();

        }else
        {
            progressDialog = new ProgressDialog(BookingForService.this);
            progressDialog.setMessage("Posting please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Map<String, Object> booking = new HashMap<>();
            booking.put("Budget", enter_priceSt);
            booking.put("Price", ActualPrice);
            booking.put("event", eventSt);
            booking.put("Type", Type);
            booking.put("Daigs", DaigTypeSt);
            booking.put("ServicesEditList", ServicesEditList);
            booking.put("selectedDates", selectedDates);
            booking.put("noOfPersons", total_number_of_personsSt);
            booking.put("phone", phoneNo);
            booking.put("FullName", fullName);
            booking.put("address", address);
            booking.put("timeStamp", Timestamp.now());
            booking.put("imageUri", imageUri);
            booking.put("CurrentUserId", FirebaseUtil.currentuserId());
            booking.put("ProductId", Key);
            booking.put("ProductUserId", this_userId);
            booking.put("BookingDates", selectedDates);
            booking.put("Type_of_ad", "Service Only");
            String orderNumber = OrderNumberGenerator.generateOrderNumber();
            booking.put("orderNumber", orderNumber);
            booking.put("Notification", checknotif);
            DocumentReference productRef =db.collection("Booking").document(UUID.randomUUID().toString());
            booking.put("documentId", productRef.getId());
            productRef.set(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(BookingForService.this, "Booking successful", Toast.LENGTH_SHORT).show();

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

    private void showInfoPopup(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_info, null);
        TextView infoText = popupView.findViewById(R.id.info_text);

        // Set your information text here
        infoText.setText("What you need in event, such as Stage, Chair, Table and etc. If it is listed of will get it for you");
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
}