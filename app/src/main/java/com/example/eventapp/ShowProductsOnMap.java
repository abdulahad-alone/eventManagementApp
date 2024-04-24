package com.example.eventapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.eventapp.Models.CustomMarker;
import com.example.eventapp.fragments.HomeFragment;
import com.example.eventapp.fragments.MyBottomSheetMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowProductsOnMap extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener  {
    private GoogleMap mMap;
    EditText rangeEditText;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker userLocationMarker;
    private LatLng defaultLocation;
    private List<Marker> allMarkers = new ArrayList<>();
    private HashMap<Marker, Float> markerDistances = new HashMap<>();
    LinearLayout range;
    private double userLatitude;
    private double userLongitude;
    List<LatLng> latLngList;
    private static final int REQUEST_CODE_LOCATION = 1001;
    List<String>imageURI;
    HashMap<String, CustomMarker> markerImageUrls = new HashMap<>();
    private Circle circle;
    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products_on_map);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, enable location-related features
            enableMyLocation();
        } else {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // GPS is disabled, prompt the user to enable it
                Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                // Optionally, open the location settings for the user to enable GPS
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                Intent intent=new Intent(ShowProductsOnMap.this, HomeFragment.class);
                startActivity(intent);
            } else{




            }
        }else {
            Toast.makeText(getApplicationContext(), "Location services not available", Toast.LENGTH_SHORT).show();
        }
        range=findViewById(R.id.rangeLinear);
        rangeEditText = findViewById(R.id.range);
        Button doneButton = findViewById(R.id.RangeDone);
        imageURI=new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String latitudeStr = document.getString("latitude");
                                String longitudeStr = document.getString("longitude");
                                List<String> imageUrls = (List<String>) document.get("images");
                                String ProId=document.getString("id");
                                if (latitudeStr != null && longitudeStr != null) {
                                    String firstImageUrl = imageUrls.get(0);
                                    try {
                                        Log.e(TAG, "Invidivldm: " );
                                        //range.setVisibility(View.INVISIBLE);
                                        double latitude = Double.parseDouble(latitudeStr);
                                        double longitude = Double.parseDouble(longitudeStr);
                                        LatLng location = new LatLng(latitude, longitude);


                                        // Use latitude and longitude as double values

                                        latLngList.add(new LatLng(latitude, longitude));
                                        // Add markers, etc., on the map with this location
                                        // mMap.addMarker(new MarkerOptions().position(location).title("Marker"));
                                        MarkerOptions markerOptions = new MarkerOptions().position(location).title("Marker");
                                        Marker marker= mMap.addMarker(markerOptions);
                                        Marker marker1 = mMap.addMarker(new MarkerOptions().position(location));
                                        allMarkers.add(marker1);
                                        String markerId = marker.getId();
                                        markerImageUrls.put(markerId, new CustomMarker(firstImageUrl, ProId));
                                        marker.showInfoWindow();

                                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                            @Nullable
                                            @Override
                                            public View getInfoContents(@NonNull Marker marker) {
                                                CustomMarker markerData = getImageUrlForMarker(marker);
                                                String imageUrl="lol";
                                                if(markerData != null)
                                                {
                                                    imageUrl = markerData.getImageUrl();
                                                }


                                                if(imageUrl != null){
                                                    View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                                                    ImageView imageView = view.findViewById(R.id.imageView);

                                                    Picasso.get().load(imageUrl).placeholder(R.drawable.house).into(imageView);
                                                    return view;}
                                                return null;
                                            }

                                            @Nullable
                                            @Override
                                            public View getInfoWindow(@NonNull Marker marker) {
                                                return null;
                                            }
                                        });

                                        builder.include(location);
                                    } catch (NumberFormatException e) {
                                        // Handle parsing exceptions here
                                        e.printStackTrace();
                                    }
                                }
                                if (!latLngList.isEmpty()) {
                                    Log.e(TAG, "onComplete: losjocj " );
                                    filterMarkersWithinRange();
                                }
                                addMarkersToMap(latLngList);
                                LatLngBounds bounds = builder.build();
                                int padding = 50; // Padding in pixels
                                //  CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                // mMap.moveCamera(cu);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        // GPS is disabled, prompt the user to enable it
                        Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                        // Optionally, open the location settings for the user to enable GPS
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }else{
                        String rangeStr = rangeEditText.getText().toString().trim();
                        if (!rangeStr.isEmpty()) {
                            try {
                                int rangeInMeters = Integer.parseInt(rangeStr);
                                rangeInMeters = rangeInMeters * 1000;
                                rangeEditText.setText("");
                                // Update circle and markers with new range
                                updateMarkersVisibility(defaultLocation, rangeInMeters);
                                updateCircleAndMarkers(rangeInMeters);
                            }
                            catch (NumberFormatException e){
                                Toast.makeText(getApplicationContext(), "Please enter a valid integer", Toast.LENGTH_SHORT).show();
                            }
                            //filterMarkersWithinRange(rangeInMeters);
                        } else {
                            // Handle case when EditText is empty
                            Toast.makeText(getApplicationContext(), "Please enter a valid range", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    // Location manager is not available, handle this case accordingly
                    Toast.makeText(getApplicationContext(), "Location services not available", Toast.LENGTH_SHORT).show();
                    // Disable location-dependent functionality or show a message indicating unavailability
                }


            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    private CustomMarker getImageUrlForMarker(Marker marker) {
        String markerId = marker.getId();
        if (markerImageUrls.containsKey(markerId)) {
            return markerImageUrls.get(markerId); // Retrieve the associated image URL
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable location-related features
                enableMyLocation();
            } else {
                // Permission denied, handle accordingly (inform user, disable location-related features, etc.)
            }
        }
    }

    private void enableMyLocation() {
        if (mMap != null) {
            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                // Handle the exception
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            return;
        }
        mMap.setOnInfoWindowClickListener(this);
        // Initialize map settings
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                userLatitude = location.getLatitude();
                                userLongitude = location.getLongitude();
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                defaultLocation = new LatLng(latitude, longitude);

                                // Move the map to the user's location
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
                                drawCircle(defaultLocation);
                                // Add a marker for the user's location
                                if (userLocationMarker != null) {
                                    userLocationMarker.remove();
                                }
                    /*userLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(defaultLocation)
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));*/
                            }
                        }
                    });
                }
            }
        });

        fetchAndAddMarkersFromFirestore();
        // Other map configurations or operations can be performed here


    }
    private void filterMarkersWithinRange() {
        float[] results = new float[1];
        List<LatLng> filteredLatLngList = new ArrayList<>();
        int markersWithinRangeCount = 0;
        for (LatLng latLng : latLngList) {
            Location.distanceBetween(
                    userLatitude, userLongitude,
                    latLng.latitude, latLng.longitude,
                    results);
            Log.d(TAG, "Distance to marker: " + results[0]);
            // Check if the distance is within 1 km
            if (results[0] <= 1000) {
                filteredLatLngList.add(latLng);
                markersWithinRangeCount++;
            }
        }
        Log.d(TAG, "Markers within 1 km: " + markersWithinRangeCount);
        // Clear existing markers
        //mMap.clear();

        // Add filtered markers to the map
        addMarkersToMap(filteredLatLngList);
    }
    private void fetchAndAddMarkersFromFirestore() {
        // Your Firestore data retrieval logic here
        // Use the logic from the previous example to retrieve data and add markers
        // ...

        // For example:
        latLngList = new ArrayList<>();
        latLngList.add(new LatLng(40.7128, -74.0060)); // Example LatLng, replace with your fetched LatLng objects

        // Add markers to the map
        addMarkersToMap(latLngList);

    }
    private void addMarkersToMap(List<LatLng> latLngList) {
        if (mMap != null) {
            Bitmap iconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_house_24);

            // Create a BitmapDescriptor from the obtained bitmap
            // BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(iconBitmap);
            for (LatLng latLng : latLngList) {
                mMap.addMarker(new MarkerOptions().position(latLng));
                // mMap.addMarker(new MarkerOptions().position(latLng).icon(setIcon(this,R.drawable.ic_baseline_house_24)));
            }
        }
    }
    public BitmapDescriptor setIcon(Activity context, int drawableID){
        Drawable drawable=ActivityCompat.getDrawable(context, drawableID);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return  BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        String markerId = marker.getId();
        if (markerImageUrls.containsKey(markerId)) {
            CustomMarker markerData = markerImageUrls.get(markerId);
            if (markerData != null) {
                String propertyId = markerData.getPropertyId();
                // Show or use the property ID as needed
                Toast.makeText(getApplicationContext(), "Property ID: " + propertyId, Toast.LENGTH_SHORT).show();
                MyBottomSheetMapFragment bottomSheetFragment = new MyBottomSheetMapFragment();
                bottomSheetFragment.setPropertyId(propertyId);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        }
    }
    private void drawCircle(LatLng latLng) {
        if (mMap != null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(1000); // 1 km in meters
            circleOptions.strokeWidth(5);
            circleOptions.strokeColor(Color.BLUE);
            circleOptions.fillColor(Color.parseColor("#500084d3")); // Adjust transparency as needed
            mMap.addCircle(circleOptions);
        }
    }
    private void updateCircleAndMarkers(int rangeInMeters) {
        if (mMap != null && defaultLocation != null) {
            // Update circle radius
            if (circle != null) {
                circle.remove();
            }
            circle = mMap.addCircle(new CircleOptions()
                    .center(defaultLocation)
                    .radius(rangeInMeters)
                    .strokeWidth(5)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.parseColor("#500084d3")));  // Adjust transparency as needed
            filterMarkersWithinRange(defaultLocation,rangeInMeters);
        }
    }
    private void filterMarkersWithinRange(LatLng center, int range) {
        float[] results = new float[1];
        List<LatLng> filteredLatLngList = new ArrayList<>();
        int markersWithinRangeCount = 0;
        for (LatLng latLng : latLngList) {
            Location.distanceBetween(
                    userLatitude, userLongitude,
                    latLng.latitude, latLng.longitude,
                    results);
            Log.d(TAG, "Distance to marker: " + results[0]);
            // Check if the distance is within 1 km
            if (results[0] <= range) {
                filteredLatLngList.add(latLng);
                markersWithinRangeCount++;
            }
        }

        Log.d(TAG, "Markers within 1 km: " + markersWithinRangeCount);
        // Clear existing markers
        //mMap.clear();
        String lol=markersWithinRangeCount+" ads";
        rangeEditText.setText(lol);
        // Add filtered markers to the map
        addMarkersToMap(filteredLatLngList);

    }
    private void calculateDistances(LatLng userLocation) {
        for (Marker marker : allMarkers) {
            float[] results = new float[1];
            LatLng markerLocation = marker.getPosition();
            Location.distanceBetween(
                    userLocation.latitude, userLocation.longitude,
                    markerLocation.latitude, markerLocation.longitude,
                    results);
            markerDistances.put(marker, results[0]);
        }
    }
    private void updateMarkersVisibility(LatLng center, int range) {
        if (allMarkers != null) {
            for (Marker marker : allMarkers) {
                LatLng markerPosition = marker.getPosition();
                float[] results = new float[1];
                Location.distanceBetween(center.latitude, center.longitude, markerPosition.latitude, markerPosition.longitude, results);

                if (results[0] <= range) {
                    // Show markers within range
                    marker.setVisible(true);
                } else {
                    // Hide markers outside range
                    marker.setVisible(false);
                }
            }
        }
    }
   /* private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.color.reddish);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }*/
}