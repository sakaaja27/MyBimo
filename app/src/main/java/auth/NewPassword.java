package auth;

import static auth.DB_Contract.ip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPassword extends AppCompatActivity {
    private TextInputLayout passLayout;
    private TextInputEditText passEditText;
    private TextInputLayout confirmLayout;
    private TextInputEditText confrimEditText;
    private Button done;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_password);

        passLayout = findViewById(R.id.newPass);
        passEditText = (TextInputEditText) passLayout.getEditText();
        confirmLayout = findViewById(R.id.confirm_pass);
        confrimEditText = (TextInputEditText) confirmLayout.getEditText();
        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        done = findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });


    }

    private void resetPassword() {
        String newPass = passEditText.getText().toString();
        String confirmPass = confrimEditText.getText().toString();
        String email  = sharedPreferences.getString("email", "");


        if(newPass.isEmpty()||confirmPass.isEmpty()){
            Toast.makeText(NewPassword.this, "Please fill the field", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(NewPassword.this, "Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://"+ip+"/website%20mybimo/mybimo/src/getData/getNewPassword.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");

                    if (status) {
                        Toast.makeText(NewPassword.this, "Password successfully update", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewPassword.this, Login_view.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                        Log.d("NewPassword", "Response: " + response);
                        Log.d("NewPassword", "Email: " + email);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPassword.this, "Please check connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("new_password", newPass);
                return params;
            }
        };
        queue.add(stringRequest);

    }



}
