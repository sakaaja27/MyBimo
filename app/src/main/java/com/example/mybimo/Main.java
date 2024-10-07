package com.example.mybimo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import fragment.Course;
import fragment.Dashboard;
import fragment.Payment;
import fragment.Profile;

public class Main extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activiy_layout);

        loadFragment(new Dashboard());
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(this);

        navigationView.setSelectedItemId(R.id.fr_home);

//
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