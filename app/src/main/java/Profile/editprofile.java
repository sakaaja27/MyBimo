package Profile;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;
import static auth.DB_Contract.ip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.Main;
import com.example.mybimo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.github.dhaval2404.imagepicker.ImagePicker;

import Adapter.DataPart;
import Adapter.GetUser;
import auth.Login_view;
import auth.Register_view;
import fragment.Profile;

public class editprofile extends AppCompatActivity {
    private ImageView arrow;
    private ShapeableImageView imageView;
    TextView button;
    private View view;
    private ImagePicker imagePicker;
    private static String UserId;
    private TextInputLayout usernamelay,emaillay,phonelay;
    private TextInputEditText username,email,phone;
    private ShapeableImageView image_profile;
    List<GetUser> getUserList = new ArrayList<>();
    List<DataPart> DataPart = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.lay_editprofile);

        // Inisialisasi komponen UI
        arrow = findViewById(R.id.arrow_back);
        usernamelay = findViewById(R.id.username);
        username = (TextInputEditText) usernamelay.getEditText();
        emaillay = findViewById(R.id.email);
        swipeRefreshLayout = findViewById(R.id.refershlayout);
        email = (TextInputEditText) emaillay.getEditText();
        phonelay = findViewById(R.id.phone);
        phone = (TextInputEditText) phonelay.getEditText();
        imageView = findViewById(R.id.image_profile);
        button = findViewById(R.id.update_image);



//        Image profile
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.black)));
        } else {
            // Tangani kasus di mana ActionBar tidak ada
            Log.e("MyApp", "ActionBar is null");
        }

        // Ambil id pengguna dari variable statis di Main
        UserId = Main.RequestUserId;


// Set listener untuk arrow
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke aktivitas sebelumnya
            }
        });
        // Fetch data
        if (UserId != null) {
            fetchUser (UserId); // Panggil fetchUser  dengan UserId
        } else {
            Toast.makeText(this, "User  ID tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        // Set up listener for save changes button
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser ();
            }
        });

//        upload Image
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.with(editprofile.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshScreen();
            }
        });
    }
    @SuppressLint("NewApi")
    private void refreshScreen(){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(editprofile.this,"Refresh",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Memeriksa apakah hasil aktivitas berhasil dan data tidak null
        if (resultCode == RESULT_OK && data != null) {
            // Mendapatkan URI dari data yang dikembalikan
            Uri uri = data.getData();

            if (uri != null){
                try {
                    // Mengubah URI menjadi Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                    // Mengubah Bitmap menjadi array byte
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    // Mengunggah data gambar ke server
                    uploadImageToServer(bitmapdata);

                    // Menampilkan gambar di ImageView
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            // Kode yang dikomentari:
            // imageView.setImageURI(uri);  // Menampilkan gambar langsung dari URI
            // uploadImageToServer(uri);    // Mengunggah URI ke server
        }
    }

    private void uploadImageToServer(byte[] imageData) {
        // URL endpoint untuk upload gambar
        String url = "http://" + ip + "/WEBSITE%20MYBIMO/mybimo/src/getData/getupdateuserimage.php";

        // Nama file yang akan disimpan di server (menggunakan ID pengguna)
        String fileName = UserId + ".jpg";

        // Membuat request multipart untuk mengirim file
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                Request.Method.POST, // Metode HTTP POST
                url,
                // Listener untuk menangani respons sukses
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            // Mengubah respons menjadi JSONObject
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Log.d("Response", jsonObject.toString());

                            // Memeriksa apakah respons mengandung error
                            if (!jsonObject.getBoolean("success")) {
                                Log.d("Upload Error", jsonObject.getString("message"));
                            } else {
                                // Jika sukses, tampilkan pesan berhasil
                                Toast.makeText(getApplicationContext(),
                                        "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                // Listener untuk menangani error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                }) {
            // Override method untuk menambahkan parameter tambahan
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", UserId); // Menambahkan ID pengguna ke parameter
                return params;
            }

            // Override method untuk menambahkan data file
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // Menambahkan file gambar dengan parameter 'upload_image'
                params.put("upload_image", new DataPart(fileName, imageData, "image/jpeg"));
                return params;
            }
        };

        // Membuat antrian request dan menambahkan request ke dalamnya
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(volleyMultipartRequest);
    }

    // Metode utilitas untuk mengonversi InputStream menjadi byte array
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        // Membaca data dari InputStream ke ByteArrayOutputStream
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void fetchUser(String UserId) {
        // Membuat URL untuk mengambil data user dari server
        String URL = "http://" + ip + "/WEBSITE%20MYBIMO/mybimo/src/getData/getUser.php?id=" + UserId;

        // Membuat request JSON Array ke server
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, // Menggunakan metode HTTP GET
                URL,               // URL endpoint
                null,             // tidak ada body request
                // Listener untuk menangani respons sukses
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Memeriksa apakah ada data dalam response
                            if (response.length() > 0) {
                                // Mengambil data user pertama dari array JSON
                                JSONObject jsonObject = response.getJSONObject(0);

                                // Mengisi TextView dengan data user
                                username.setText(jsonObject.getString("username"));
                                email.setText(jsonObject.getString("email"));
                                phone.setText(jsonObject.getString("phone"));

                                // Membuat URL lengkap untuk gambar
                                String imageUrl = "http://" + ip + "/WEBSITE%20MYBIMO/mybimo/src/getData/" + jsonObject.getString("upload_image");

                                // Log URL gambar untuk debugging
                                Log.d("", imageUrl);

                                // Memeriksa validitas URL gambar
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    // Memuat gambar jika URL valid
                                    loadImage(imageUrl);
                                } else {
                                    // Menampilkan pesan error jika URL tidak valid
                                    Toast.makeText(editprofile.this,
                                            "URL gambar tidak valid",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // Menampilkan pesan jika tidak ada data
                                Toast.makeText(editprofile.this,
                                        "Tidak ada data yang tersedia",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Menangani error parsing JSON
                            e.printStackTrace();
                            Toast.makeText(editprofile.this,
                                    "Kesalahan parsing data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                // Listener untuk menangani error request
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(editprofile.this,
                                "Gagal mengambil data",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Menambahkan request ke antrian Volley
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    // Fungsi untuk memuat gambar dari URL
    private void loadImage(String imageUrl) {
        // Membuat request gambar menggunakan Volley
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                // Listener untuk menangani respons sukses
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Menampilkan gambar di ImageView
                        System.out.println("Stringresponse"+response);
                        imageView.setImageBitmap(response);
                    }
                },
                0, 0, // Ukuran maksimal gambar (0 = ukuran asli)
                ImageView.ScaleType.CENTER_CROP, // Tipe scaling gambar
                Bitmap.Config.RGB_565, // Konfigurasi bitmap
                // Listener untuk menangani error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("Image Load Error",
                                    "Error Code: " + error.networkResponse.statusCode);
                        }
                        System.out.println("Error: "+error);
                        // Set gambar default jika terjadi error
                        imageView.setImageResource(R.drawable.icon_profile);
                    }
                });

        // Menambahkan request ke antrian Volley
        Volley.newRequestQueue(this).add(imageRequest);
    }

    // Fungsi untuk mengupdate data pengguna
    private void updateUser() {
        // Mengambil data dari input fields
        String updatedUsername = username.getText().toString().trim();
        String updatedEmail = email.getText().toString().trim();
        String updatedPhone = phone.getText().toString().trim();

        // Validasi input
        if (updatedUsername.isEmpty() || updatedEmail.isEmpty() ||
                updatedPhone.isEmpty()) {
            Toast.makeText(this, "All fields are required",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // URL endpoint untuk update user
        String URL = "http://" + ip + "/WEBSITE%20MYBIMO/mybimo/src/getData/getupdateuser.php";

        // Membuat StringRequest untuk POST request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                // Listener untuk menangani respons sukses
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // Menampilkan pesan dari server
                            Toast.makeText(editprofile.this,
                                    jsonResponse.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                            // Jika sukses, tutup activity
                            if (jsonResponse.getBoolean("success")) {
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(editprofile.this,
                                    "Error parsing response: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                // Listener untuk menangani error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(editprofile.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            // Override method untuk menambahkan parameter POST
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", UserId);
                params.put("username", updatedUsername);
                params.put("email", updatedEmail);
                params.put("phone", updatedPhone);
                return params;
            }
        };

        // Menambahkan request ke antrian Volley
        Volley.newRequestQueue(this).add(stringRequest);
    }

}