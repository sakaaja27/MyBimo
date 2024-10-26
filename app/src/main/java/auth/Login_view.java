package auth;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = emailEditText.getText().toString();
                String password = passEditText.getText().toString();

                if (!(mail.isEmpty() || password.isEmpty())) {
                    RequestQueue requestQueue = Volley.newRequestQueue(Login_view.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DB_Contract.urlLogin, new Response.Listener<String>() {
                        @Override

                        public void onResponse(String response) {
                            try {
                                JSONObject jsonRespon = new JSONObject(response);
                                if (jsonRespon.getString("status").equals("Login Berhasil")){
                                    String id = jsonRespon.getString("id");
                                    String username = jsonRespon.getString("username");
                                    String email = jsonRespon.getString("email");
                                    String phone = jsonRespon.getString("phone");
                                    String role = jsonRespon.getString("role");
                                    String uploadImage = jsonRespon.getString("upload_image");
                                    String password = jsonRespon.getString("password");


                                    Toast.makeText(getApplicationContext(),"Login Berhasil",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Main.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("username", username);
                                    intent.putExtra("email", email);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("role", role);
                                    intent.putExtra("upload_image", uploadImage);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(),"Login Gagal",Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", mail);
                            params.put("password", password);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "Field Kosong", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }
}

//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String y_email = email.getText().toString();
//                String password = pass.getText().toString();
//                // jika ada field yang kosong maka akan muncul toast
//                if (y_email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(Login_view.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                }
//                else {
////                    jika tidak, maka akan mengecek apakah email dan password sudah terdaftar atau belum
//                    SQLiteDatabase db = DBHelper.getReadableDatabase();
//                    Cursor cursor = db.query("users", new String[]{"id", "email", "password"}, "email=? AND password=?", new String[]{y_email, password}, null, null, null);
////                    jika email dan password sudah terdaftar maka akan muncul toast dan mengarah ke halaman dashboard
//                    if (cursor.getCount() > 0) {
//                        Toast.makeText(Login_view.this, "Login successful", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Login_view.this, Main.class);
//                        startActivity(intent);
//                        finish();
//                    }
////                    jika tidak, maka akan muncul toast
//                    else {
//                        Toast.makeText(Login_view.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });