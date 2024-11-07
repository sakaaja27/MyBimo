package com.example.mybimo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import auth.Preference;
import fragment.Course;
import fragment.Dashboard;
import fragment.Payment;
import fragment.Profile;

public class Main extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    public static String RequestUserId;
    public static String RequestUsername;
    public static String RequestEmail;
    public static String RequestPhone;
    public static String RequestRole;
    public static String RequestPassword;
    public static String uploadImage;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this)     ;
        setContentView(R.layout.activiy_layout);
        loadFragment(new Dashboard());
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.fr_home);

        // Ambil data dari intent
        RequestUserId = getIntent().getStringExtra("id");
        RequestUsername = getIntent().getStringExtra("username");
        RequestEmail = getIntent().getStringExtra("email");
        RequestPhone = getIntent().getStringExtra("phone");
        RequestRole = getIntent().getStringExtra("role");
        uploadImage = getIntent().getStringExtra("upload_image");
        RequestPassword = getIntent().getStringExtra("password");


    }
    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.fragment_container,fragment).commit();
            return  true;
        }
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int selected = item.getItemId();
        Fragment fragment = null;
        if (selected==R.id.fr_payment){
            fragment = new Payment();
        }else if (selected==R.id.fr_home){
            fragment = new Dashboard();
        } else if (selected==R.id.fr_course) {
            fragment = new Course();
        }else {
            fragment = new Profile();
        }
        return loadFragment(fragment);
    }

}