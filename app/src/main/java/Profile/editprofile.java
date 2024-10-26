package Profile;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;
import static auth.DB_Contract.ip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null){
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    uploadImageToServer(bitmapdata);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
//            imageView.setImageURI(uri);
//            uploadImageToServer(uri);
        }
    }

    private void uploadImageToServer(byte[] imageData) {
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getupdateuserimage.php";
        String fileName = UserId+".jpg";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Log.d("Response", jsonObject.toString());
                            // Check if the response contains an error
                            if (!jsonObject.getBoolean("success")) {
                                Log.d("Upload Error", jsonObject.getString("message"));
                            } else {
                                // Handle success (e.g., update UI or show a message)
                                Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", UserId); // Send the user ID
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("upload_image", new DataPart(fileName, imageData, "image/jpeg"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(volleyMultipartRequest);
    }

    // Metode untuk mengonversi InputStream menjadi byte array
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void fetchUser (String UserId) {
        String URL = "http://" + ip + "/mybimo/getData/getUser.php?id=" + UserId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Pastikan ada data dalam response
                            if (response.length() > 0) {
                                // Ambil data pengguna pertama dari response
                                JSONObject jsonObject = response.getJSONObject(0);
                                username.setText(jsonObject.getString("username"));
                                email.setText(jsonObject.getString("email"));
                                phone.setText(jsonObject.getString("phone"));

                                String imageUrl = "http://" + ip +"/website%20mybimo/mybimo/src/getData/"+jsonObject.getString("upload_image");
                                Log.d("", imageUrl); // Log URL gambar
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    loadImage(imageUrl);
                                } else {
                                    Toast.makeText(editprofile.this, "URL gambar tidak valid", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(editprofile.this, "Tidak ada data yang tersedia", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(editprofile.this, "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(editprofile.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Tambahkan request ke queue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private void loadImage(String imageUrl) {
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // nampilkan gambar di ImageView
                        System.out.println("Stringresponse"+response);
                        imageView.setImageBitmap(response);

                    }
                },
                0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("Image Load Error", "Error Code: " + error.networkResponse.statusCode);
                        }
                        System.out.println("CUMIII"+error);
                        imageView.setImageResource(R.drawable.icon_profile);
                    }
                });

        // Tambahkan request ke queue
        Volley.newRequestQueue(this).add(imageRequest);
    }

    private void updateUser() {
        String updatedUsername = username.getText().toString().trim();
        String updatedEmail = email.getText().toString().trim();
        String updatedPhone = phone.getText().toString().trim();

        // Validate input
        if (updatedUsername.isEmpty() || updatedEmail.isEmpty() || updatedPhone.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getupdateuser.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Toast.makeText(editprofile.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            if (jsonResponse.getBoolean("success")) {
                                finish(); // Close activity on success
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(editprofile.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(editprofile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", UserId); // Send the user ID
                params.put("username", updatedUsername);
                params.put("email", updatedEmail);
                params.put("phone", updatedPhone);
                return params;
            }
        };

        // Add request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

}