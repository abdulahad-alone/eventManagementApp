package com.example.eventapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private Marker selectedMarker;
    private LatLng selectedLatLng;
    double latitude=0;
    double longitude=0;
    private LatLng defaultLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker userLocationMarker;
    private LatLng userLocation;
    String firstImageUrl;
    private static final int REQUEST_CODE_LOCATION = 1001;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, enable location-related features
            enableMyLocation();
        } else {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        firstImageUrl = getIntent().getStringExtra("image");
        Log.e(TAG, "ImageUri: "+firstImageUrl );

        Log.e(TAG, "lantidutde: "+latitude );
        Log.e(TAG, "longitude: "+longitude );


        Button btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if a marker is selected
                onDoneButtonClick();
            }
        });
        mapFragment.getMapAsync(this);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            return;
        }   mMap.setMyLocationEnabled(true);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Get the current location coordinates
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    defaultLocation = new LatLng(latitude, longitude);

                    // Move the map to the user's location
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
                    if (userLocationMarker != null) {
                        userLocationMarker.remove();
                    }
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    builder.include(defaultLocation);

                    LatLngBounds bounds = builder.build();
                    /*userLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(defaultLocation)
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));*/
                }
            }
        });

        if(latitude==0.0&&longitude==0.0)
        {
            defaultLocation = new LatLng(33.64305840744145, 73.03287617862226);

        }else{
            defaultLocation = new LatLng(latitude, longitude); // Change this to your desired default location
            String markerLabel = "Selected Location";
            selectedMarker = mMap.addMarker(new MarkerOptions()
                    .position(defaultLocation)
                    .title(markerLabel) // Set marker title (label)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            googleMap.setOnInfoWindowClickListener(this);

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Nullable
                @Override
                public View getInfoContents(@NonNull Marker marker) {
                    return null;
                }

                @Nullable
                @Override
                public View getInfoWindow(@NonNull Marker marker) {
                    View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                    ImageView imageView = view.findViewById(R.id.imageView);
                    Glide.with(getApplicationContext()).load(firstImageUrl).placeholder(R.drawable.house).into(imageView);
                    /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.house2);
                    imageView.setImageBitmap(bitmap);*/
                    return view;
                }
            });
        }
        mMap.setOnMarkerClickListener(this);
        // Initialize the map with default location and zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
        // Set up a click listener to add a marker when the user taps the map
        Log.e(TAG, "onMapReady: "+defaultLocation );

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (selectedMarker != null) {
                    selectedMarker.remove();
                }

                selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng));

                // You can also set a custom marker color
                // You can also set a custom marker color


                // Store the selected LatLng in the variable
                selectedLatLng = latLng;
                Log.d(TAG, "onMapClick: "+selectedLatLng );

            }
        });
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        // Check if the clicked marker is the user's location marker
        if (marker.equals(userLocationMarker)) {
            // Get the destination LatLng from the clicked marker
            LatLng destinationLatLng = marker.getPosition();

            // Check if userLocation (user's current location) is available
            if (userLocation != null) {
                // Fetch route and draw on the map
                //drawRoute(userLocation, destinationLatLng);
            }
        }
        return false;
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
    private void onDoneButtonClick() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("latitude", selectedLatLng.latitude);
        resultIntent.putExtra("longitude", selectedLatLng.longitude);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }

}