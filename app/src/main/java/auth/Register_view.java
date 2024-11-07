package auth;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.example.mybimo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

public class Register_view extends AppCompatActivity {
    private ImageView splashImage;
    private TextInputLayout userLayout;
    private TextInputEditText userlEditText;
    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout phoneLayout;
    private TextInputEditText phoneEditText;
    private TextInputLayout passLayout;
    private TextInputEditText passEditText;
    TextView login;
    Button signup;

//    private DBHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_view);

        View login = findViewById(R.id.login);
        splashImage = findViewById(R.id.mybimo_font);
        userLayout = findViewById(R.id.username);
        userlEditText = (TextInputEditText) userLayout.getEditText();
        emailLayout = findViewById(R.id.email);
        emailEditText = (TextInputEditText) emailLayout.getEditText();
        phoneLayout = findViewById(R.id.phone);
        phoneEditText = (TextInputEditText) phoneLayout.getEditText();
        passLayout = findViewById(R.id.pass);
        passEditText = (TextInputEditText) passLayout.getEditText();
        signup = findViewById(R.id.sign_up);
//        DBHelper = new DBHelper(this);

//        animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animasi);
        splashImage.startAnimation(animation);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_view.this, Login_view.class);
                startActivity(intent);
                finish();
            }
        });

//        API
        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String user = userlEditText.getText().toString();
                String mail = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passEditText.getText().toString();

                if (user.isEmpty() || mail.isEmpty() || phone.isEmpty() || password.isEmpty()){
                    Toast.makeText(Register_view.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else if (phone.length() < 12) {
                    Toast.makeText(Register_view.this, "Phone number must be at least 12 number", Toast.LENGTH_SHORT).show();
                }
                else if (password.length() < 8) {
                    Toast.makeText(Register_view.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                }  else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DB_Contract.urlRegister, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login_view.class));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.toString());
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected HashMap<String, String>getParams() throws AuthFailureError{
                            HashMap<String, String> params = new HashMap<>();
                            params.put("username", user);
                            params.put("email", mail);
                            params.put("phone", phone);
                            params.put("role", "0");
                            params.put("password", password);
                            // Add this line to set default role to 0
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });

//        save signup
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = t_username.getText().toString();
//                String email = t_email.getText().toString();
//                String phone = t_phone_number.getText().toString();
//                String password = t_pass.getText().toString();
////                jika ada field yang kosong maka akan muncul toast
//                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()){
//                    Toast.makeText(Register_view.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    //jika tidak, maka akan menyisipkan data ke database
//                    SQLiteDatabase db = DBHelper.getWritableDatabase();
////                    check apakah email sudah terdaftar atau belum
//                    Cursor cursor = db.query("users", new String[]{"id"}, "email=?", new String[]{email}, null, null, null);
////                   jika email sudah terdaftar maka akan muncul toast
//                    if (cursor.getCount() > 0) {
//                        Toast.makeText(Register_view.this, "User  already exists", Toast.LENGTH_SHORT).show();
//                    }
////                    jika tidak,maka akan memasukkan field ke database untuk registrasi
//                    else {
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("name", name);
//                        contentValues.put("email", email);
//                        contentValues.put("phone", phone);
//                        contentValues.put("password", password);
////                        insert field ke database
//                        db.insert("users", null, contentValues);
////                        muncul toast jika berhasil
//                        Toast.makeText(Register_view.this, "Registered successfully", Toast.LENGTH_SHORT).show();
////                        dan mengarahkan ke halaman splash
//                        Intent intent = new Intent(Register_view.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            }
//        });

    }
}