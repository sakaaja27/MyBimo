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

import auth.Preference;
import fragment.MyCourse;
import fragment.Dashboard;
import fragment.Payment;
import fragment.Profile;
import fragment.Zoom;

public class Main extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    // Variabel static untuk menyimpan data user
    public static String RequestUserId;
    public static String RequestUsername;
    public static String RequestEmail;
    public static String RequestPhone;
    public static String RequestRole;
    public static String RequestPassword;
    public static String uploadImage;
    private int currentSelectedItem = -1;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mengaktifkan fitur EdgeToEdge untuk UI yang lebih modern
        EdgeToEdge.enable(this);
        // Mengatur layout untuk activity
        setContentView(R.layout.activiy_layout);

        // Memuat fragment Dashboard sebagai tampilan awal
        loadFragment(new Dashboard());

        // Inisialisasi dan setup Bottom Navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(this);
        // Memuat fragment default saat activity dibuat
        if (savedInstanceState == null) {
            currentSelectedItem = R.id.fr_home; // Misalnya, fragment default adalah Dashboard
            loadFragment(new Dashboard());
        }
        // Mengatur item home sebagai item yang terpilih
        navigationView.setSelectedItemId(R.id.fr_home);

        // Mengambil data user dari intent
        RequestUserId = getIntent().getStringExtra("id");
        RequestUsername = getIntent().getStringExtra("username");
        RequestEmail = getIntent().getStringExtra("email");
        RequestPhone = getIntent().getStringExtra("phone");
        RequestRole = getIntent().getStringExtra("role");
        uploadImage = getIntent().getStringExtra("upload_image");
        RequestPassword = getIntent().getStringExtra("password");
    }

    // Metode untuk memuat fragment
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            // Mengganti fragment yang ditampilkan
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    // Implementasi dari interface NavigationBarView.OnItemSelectedListener
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Mendapatkan id item yang dipilih
        int selected = item.getItemId();

        // Cek apakah item yang dipilih sama dengan item yang saat ini dipilih
        if (selected == currentSelectedItem) {
            return false; // Tidak melakukan apa-apa jika item yang sama dipilih
        }
        Fragment fragment = null;

        // Memilih fragment berdasarkan item yang dipilih
        if (selected == R.id.fr_payment) {
            fragment = new Payment();
        } else if (selected == R.id.fr_home) {
            fragment = new Dashboard();
        } else if (selected == R.id.fr_course) {
            fragment = new MyCourse();
        } else if (selected == R.id.fr_zoom){
            fragment = new Zoom();
        }
        else {
            fragment = new Profile();
        }

        // Memuat fragment yang dipilih
        if (loadFragment(fragment)) {
            currentSelectedItem = selected; // Update item yang saat ini dipilih
        }

        // Memuat fragment yang dipilih
        return loadFragment(fragment);
    }
}