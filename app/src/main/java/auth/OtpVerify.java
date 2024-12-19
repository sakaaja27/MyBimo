package auth;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static auth.DB_Contract.ip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarException;

import Adapter.GetUser;

public class OtpVerify extends AppCompatActivity {
    private TextView sendAgain;
    private Button verify;
    private TextView timeText;
    private SharedPreferences sharedPreferences;
    private Handler handler;
    private long startTime;
    private CountDownTimer count;
    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private static final long OTP_EXPIRATION = TimeUnit.MINUTES.toMillis(5);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_verify);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        timeText = findViewById(R.id.timeText);

        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String email = getIntent().getStringExtra("email");


        sendAgain = findViewById(R.id.sendAgain);
        verify = findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(OtpVerify.this, NewPassword.class);
//                startActivity(intent);
                verifikasiOtp();

            }
        });
        sendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtp();

            }
        });

        otpBoxInput();

        countDown();

    }

    private void countDown() {
        count = new CountDownTimer(OTP_EXPIRATION, 1000) {
            @Override
            public void onTick(long l) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(l);
                long second = TimeUnit.MILLISECONDS.toSeconds(l);
                timeText.setText(String.format("Time Remaining %2d:%2d", minutes, second % 60));
            }

            @Override
            public void onFinish() {
                timeText.setText("Time Expired");
                verify.setEnabled(false);
                sendAgain.setEnabled(true);

            }
        }.start();
    }

    private void resendOtp() {
        String email = getIntent().getStringExtra("email");
        requestOtp(email);
    }

    private void requestOtp(String email) {
        String URL = "http://"+ip+"/WEBSITE%20MYBIMO/mybimo/src/getData/getOtp.php";
        Log.d("ForgotPass", "Full URL: " + URL);

        RequestQueue requestQueue = Volley.newRequestQueue(OtpVerify.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String email = jsonObject.getString("email");
                        editor.putString("email", email);
                        editor.putString("otp", jsonObject.getString("otp"));
                        editor.putLong("timeOTP", System.currentTimeMillis());
                        editor.apply();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OtpVerify.this, "Please check connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    private void verifikasiOtp() {
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String otp = sharedPreferences.getString("otp", "");
        String email = sharedPreferences.getString("email", "");
        String input =  otp1.getText().toString() +
                otp2.getText().toString() +
                otp3.getText().toString() +
                otp4.getText().toString() +
                otp5.getText().toString() +
                otp6.getText().toString();
        if (otp.equals(input)){
            Toast.makeText(OtpVerify.this, "successfully verify", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OtpVerify.this, NewPassword.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(OtpVerify.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }



    private void otpBoxInput() {
        otp1.addTextChangedListener(new OtpTextWatcher(null,otp1, otp2));
        otp2.addTextChangedListener(new OtpTextWatcher(otp1, otp2, otp3));
        otp3.addTextChangedListener(new OtpTextWatcher(otp2, otp3, otp4));
        otp4.addTextChangedListener(new OtpTextWatcher(otp3, otp4, otp5));
        otp5.addTextChangedListener(new OtpTextWatcher(otp4, otp5, otp6));
        otp6.addTextChangedListener(new OtpTextWatcher(otp5, otp6, null));
    }

    private class OtpTextWatcher implements TextWatcher {
        private final EditText beforeBox;
        private final EditText currentBox;
        private final EditText afterBox;

        public OtpTextWatcher(EditText beforeBox, EditText currentBox, EditText afterBox) {
            this.beforeBox = beforeBox;
            this.currentBox = currentBox;
            this.afterBox = afterBox;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() == 1 && afterBox != null) {
                afterBox.requestFocus();
            } else if (charSequence.length() == 0 && beforeBox != null) {
                beforeBox.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}

