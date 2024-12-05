package com.example.mybimo;

import static android.content.ContentValues.TAG;

import static auth.Login_view.KEY_EMAIL;
import static auth.Login_view.KEY_ID;
import static auth.Login_view.KEY_IMAGE;
import static auth.Login_view.KEY_NAME;
import static auth.Login_view.KEY_PASSWORD;
import static auth.Login_view.KEY_PHONE;
import static auth.Login_view.KEY_ROLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import Notifikasi.NotificationUtil;
import auth.Login_view;
import auth.Preference;
import fragment.MyCourse;
import fragment.Dashboard;
import fragment.Payment;
import fragment.Profile;
import fragment.Zoom;

    public class Main extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener,Profile.LogoutListener {

        // Variabel static untuk menyimpan data user
        public static String RequestUserId;
        public static String RequestUsername;
        public static String RequestEmail;
        public static String RequestPhone;
        public static String RequestRole;
        public static String RequestPassword;
        public static String uploadImage;
        private int currentSelectedItem = -1;
        private SharedPreferences sharedPreferences;
        private BottomNavigationView navigationView;

        @Override
        public void onLogout() {
            // Hentikan pemeriksaan transaksi
            NotificationUtil.stopCheckingTransaksi();

            sharedPreferences = getSharedPreferences(Login_view.SHARED_PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Menghapus semua data
            editor.apply();


            Intent intent = new Intent(this, Login_view.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Tutup activity saat ini
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Mengaktifkan fitur EdgeToEdge untuk UI yang lebih modern
            EdgeToEdge.enable(this);
            // Mengatur layout untuk activity
            setContentView(R.layout.activiy_layout); // Perbaiki nama layout jika perlu

            // Mengambil SharedPreferences
            sharedPreferences = getSharedPreferences(Login_view.SHARED_PREF_NAME, MODE_PRIVATE);
            String userId = sharedPreferences.getString(KEY_ID, null); // Pastikan KEY_ID didefinisikan

            // Memeriksa apakah userId ditemukan
            if (userId != null) {
                Log.d("User  ID", "User  ID ditemukan: " + userId);
                // Lakukan sesuatu dengan userId jika diperlukan
            } else {
                Log.d("User  ID", "User  ID tidak ditemukan");
                // Tindakan jika userId tidak ditemukan, misalnya mengarahkan ke halaman login
            }

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
            RequestUserId = sharedPreferences.getString(KEY_ID,null);
            RequestUsername = sharedPreferences.getString(KEY_NAME,null);
            RequestEmail = sharedPreferences.getString(KEY_EMAIL,null);
            RequestPhone = sharedPreferences.getString(KEY_PHONE,null);
            RequestRole = sharedPreferences.getString(KEY_ROLE,null);
            uploadImage = sharedPreferences.getString(KEY_IMAGE,null);
            RequestPassword = sharedPreferences.getString(KEY_PASSWORD,null);
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