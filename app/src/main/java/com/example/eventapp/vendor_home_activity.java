package com.example.eventapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.eventapp.utils.FirebaseUtil.getUserType;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.eventapp.fragments.AccountFragment;
import com.example.eventapp.fragments.ChatFragment;
import com.example.eventapp.fragments.FavouriteFragment;
import com.example.eventapp.fragments.HomeFragment;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

public class vendor_home_activity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView navView;
    public DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    FloatingActionButton actionButton;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);

        actionButton =findViewById(R.id.fab);
        getUserType().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String typeValue) {
                if (typeValue != null && typeValue.equals("Customer")) {
                    // User type is "vendor", make the button invisible
                    actionButton.setVisibility(View.INVISIBLE);
                } else {
                    // Type value is not available
                    actionButton.setVisibility(View.VISIBLE);
                }
            }
        });


     actionButton.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view) {
        Intent intent = new Intent(vendor_home_activity.this, adposting.class);
        startActivity(intent);
    }
    });
    navView =

    findViewById(R.id.nav_view_buttom);
        navView.setOnItemSelectedListener(this);

    load(new HomeFragment());
}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {

            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_fav:
                fragment = new FavouriteFragment();
                break;
            case R.id.menu_chat:
                fragment = new ChatFragment();
                break;
            case R.id.menu_account:
                fragment = new AccountFragment();
                break;
        }
        return load(fragment);
    }

    boolean load(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);


    }
}