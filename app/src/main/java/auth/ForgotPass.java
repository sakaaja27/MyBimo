package auth;

import static auth.DB_Contract.ip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Adapter.GetOtp;
import Adapter.GetUser;

public class ForgotPass extends AppCompatActivity {
    private TextInputLayout emailLayout;
    private Button send_email;
    private TextInputEditText emailText;
    private SharedPreferences sharedPreferences;
    private boolean isSubmitting = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_pass);
        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        emailLayout = findViewById(R.id.email);

        emailText = (TextInputEditText) emailLayout.getEditText();
        send_email = findViewById(R.id.send_email);
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });


    }

    private void submit() {
        if (isSubmitting) return;

        String email = emailText.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        isSubmitting = true;
        send_email.setEnabled(false);

        String URL = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getOtp.php";
        Log.d("ForgotPass", "Full URL: " + URL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ForgotPass", "Response from server: " + response);
                        isSubmitting = false;
                        send_email.setEnabled(true);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.putString("otp", jsonObject.getString("otp"));
                                editor.putLong("timeOTP", System.currentTimeMillis());
                                editor.apply();

                                Intent intent = new Intent(ForgotPass.this, OtpVerify.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed to parse response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isSubmitting = false;
                        send_email.setEnabled(true);
                        Toast.makeText(ForgotPass.this, "Network error, please check your connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000, // 30 seconds timeout
                0, // no retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(stringRequest);
    }

        }