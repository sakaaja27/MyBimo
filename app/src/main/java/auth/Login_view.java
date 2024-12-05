package auth;

import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences sharedPreferences;
//    private  DBHelper DBHelper;

    public static final String SHARED_PREF_NAME = "mypref";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ROLE = "role";
    public static final String KEY_IMAGE = "upload_image";
    public static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_view);

        initializeView();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        checkifUserLoggedIn();



        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_view.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


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
        btn_login.setOnClickListener(view -> loginUser());
    }

    private void initializeView(){
        emailLayout = findViewById(R.id.email);
        emailEditText = (TextInputEditText) emailLayout.getEditText();
        passLayout = findViewById(R.id.pass);
        passEditText = (TextInputEditText) passLayout.getEditText();
        register = findViewById(R.id.register);
        btn_login = findViewById(R.id.btn_login);
        forgot_pass = findViewById(R.id.forgot_pass);
    }

    private void checkifUserLoggedIn(){
        String name = sharedPreferences.getString(KEY_NAME,null);
        if (name != null){
            Intent intent = new Intent(getApplicationContext(),Main.class);
            startActivity(intent);
            finish();
        }
    }

    private void loginUser(){
        String mail = emailEditText.getText().toString();
        String password = passEditText.getText().toString();

        if (!(mail.isEmpty() || password.isEmpty())){
            RequestQueue requestQueue = Volley.newRequestQueue(Login_view.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST,DB_Contract.urlLogin,response -> handleLoginResponse(response),error -> Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show()){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put("email",mail);
                    params.put("password",password);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(),"Field Kosong",Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLoginResponse(String response){
        Log.d("LoginResponse", response);
        try {
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.getString("status").equals("Login Berhasil")){
                saveUserData(jsonResponse);
                Toast.makeText(getApplicationContext(),"Login Berhasil",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Main.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveUserData(JSONObject jsonResponse) throws JSONException{
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID,jsonResponse.getString("id"));
        editor.putString(KEY_NAME,jsonResponse.getString("username"));
        editor.putString(KEY_EMAIL,jsonResponse.getString("email"));
        editor.putString(KEY_PHONE,jsonResponse.getString("phone"));
        editor.putString(KEY_ROLE,jsonResponse.getString("role"));
        editor.putString(KEY_IMAGE,jsonResponse.getString("upload_image"));
        editor.putString(KEY_PASSWORD,jsonResponse.getString("password"));
        editor.apply();

        // Cek apakah ID tersimpan dengan benar
        String savedUserId = sharedPreferences.getString(KEY_ID, null);
        Log.d("SavedUser ID", "ID yang disimpan: " + savedUserId); // Tambahkan log ini

    }
}