package auth;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.Main;
import com.example.mybimo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fragment.Dashboard;

public class Login_view extends AppCompatActivity {

    public ImageView splashImageView;
    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout passLayout;
    private TextInputEditText passEditText;
    private Preference preference;
    private TextView forgot_pass;
    Button btn_login;
    TextView register;
//    private  DBHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_view);

        emailLayout = findViewById(R.id.email);
        emailEditText = (TextInputEditText) emailLayout.getEditText();
        passLayout = findViewById(R.id.pass);
        passEditText = (TextInputEditText) passLayout.getEditText();
        register = findViewById(R.id.register);
        btn_login = findViewById(R.id.btn_login);

        forgot_pass = findViewById(R.id.forgot_pass);
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_view.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

//        preference = new Preference(this);
//        preference.checkLogin();
//        if (preference.isUserLoggedIn()) {
//            Intent intent = new Intent(Login_view.this, Main.class);
//            startActivity(intent);
//            finish();
//        }
//        DBHelper = new DBHelper(this);
        splashImageView =  findViewById(R.id.animationmybimo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animasi);
        splashImageView.startAnimation(animation);


        //        pindah ke regis

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_view.this, Register_view.class);
                startActivity(intent);
                finish();
            }
        });
//        API
        // Menambahkan OnClickListener untuk tombol login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil input email dan password dari EditText
                String mail = emailEditText.getText().toString();
                String password = passEditText.getText().toString();

                // Memeriksa apakah email dan password tidak kosong
                if (!(mail.isEmpty() || password.isEmpty())) {
                    // Membuat antrian request Volley
                    RequestQueue requestQueue = Volley.newRequestQueue(Login_view.this);

                    // Membuat StringRequest untuk POST request
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            DB_Contract.urlLogin, // URL endpoint login
                            // Response Listener - menangani response sukses
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        // Parse response JSON
                                        JSONObject jsonRespon = new JSONObject(response);

                                        // Cek status login
                                        if (jsonRespon.getString("status").equals("Login Berhasil")){
                                            // Mengambil data user dari response
                                            String id = jsonRespon.getString("id");
                                            String username = jsonRespon.getString("username");
                                            String email = jsonRespon.getString("email");
                                            String phone = jsonRespon.getString("phone");
                                            String role = jsonRespon.getString("role");
                                            String uploadImage = jsonRespon.getString("upload_image");
                                            String password = jsonRespon.getString("password");

                                            // Menampilkan pesan sukses
                                            Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();

                                            // Membuat intent ke Main Activity
                                            Intent intent = new Intent(getApplicationContext(), Main.class);
                                            // Menambahkan data user ke intent
                                            intent.putExtra("id", id);
                                            intent.putExtra("username", username);
                                            intent.putExtra("email", email);
                                            intent.putExtra("phone", phone);
                                            intent.putExtra("role", role);
                                            intent.putExtra("upload_image", uploadImage);
                                            intent.putExtra("password", password);
                                            // Memulai Main Activity
                                            startActivity(intent);
                                        } else {
                                            // Menampilkan pesan gagal login
                                            Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        // Menangani error parsing JSON
                                        Log.e("Volley Error", e.toString());
                                        e.printStackTrace();
                                    }
                                }
                            },
                            // Error Listener - menangani error request
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println(error);
                                    Log.e("Volley Error", error.toString());
                                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ){
                        // Override getParams untuk menambahkan parameter POST
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", mail);
                            params.put("password", password);
                            return params;
                        }
                    };
                    // Menambahkan request ke antrian
                    requestQueue.add(stringRequest);
                } else {
                    // Menampilkan pesan jika field kosong
                    Toast.makeText(getApplicationContext(), "Field Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}