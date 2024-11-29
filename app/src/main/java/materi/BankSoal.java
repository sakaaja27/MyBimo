package materi;

import static auth.DB_Contract.ip;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.Main;
import com.example.mybimo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.AdapterSoal;
import Adapter.MateriSoal;
import auth.NewPassword;

public class BankSoal extends AppCompatActivity {
    private AdapterSoal materisoal;
    private RecyclerView recyclerView;
    private ArrayList<MateriSoal> listSoal;
    private ImageView arrow;
    private Button btn_submit;
    private Dialog dialog;
    private ImageView close;
    private TextView nilai_benar;
    private TextView nilai_salah;
    private static String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bank_soal);
        recyclerView = findViewById(R.id.recycler_view);
        //untuk mendapatkan id user
        UserId = Main.RequestUserId;
        if (UserId == null || UserId.isEmpty()) {
            Toast.makeText(this, "User ID tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d("BankSoal", "User ID: " + UserId);



        //mengirim jawaban user
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Integer, String> jawabanUser = materisoal.getJawabanUser();
                Log.d("BankSoal", "Sending User ID: " + UserId);
                Log.d("User IDD", UserId);
                //untuk nampilin jumlah bener dan salah jawaban user
                tampilNilai(UserId, jawabanUser);
                dialog.show();



            }
        });

        listSoal = new ArrayList<>();
        arrow = findViewById(R.id.arrow_back);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //untuk menampilkan soal
        fetchSoal();


        dialog = new Dialog(BankSoal.this);
        dialog.setContentView(R.layout.lay_modalnilai);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_modalnilai);
        dialog.setCancelable(false);
        close = dialog.findViewById(R.id.close);
        nilai_benar = dialog.findViewById(R.id.nilai_benar);
        nilai_salah = dialog.findViewById(R.id.nilai_salah);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void tampilNilai(String userId, HashMap<Integer, String> jawabanUser ) {
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getJawaban.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);
            jsonObject.put("id", getIntent().getStringExtra("id"));

            // Membuat objek JSON untuk jawaban user
            JSONObject jsonJawaban = new JSONObject();
            for (Map.Entry<Integer, String> entry : jawabanUser .entrySet()) {
                jsonJawaban.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            jsonObject.put("jawaban_user", jsonJawaban);

            Log.d("BankSoal", "JSON Sent to Server: " + jsonObject.toString());
        } catch (JSONException e) {
            Log.e("BankSoal", "Error constructing JSON: ", e);
            Toast.makeText(this, "Terjadi kesalahan dalam menyiapkan data", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    Log.d("BankSoal", "Response: " + response.toString());

                    try {
                        String status = response.getString("status");
                        if ("selesai".equals(status)) {
                            int jumlahBenar = response.getInt("jumlah_benar");
                            int jumlahSalah = response.getInt("jumlah_salah");
                            modalHasil(this, jumlahBenar, jumlahSalah);
                        } else {
                            String message = response.optString("message", "Unknown error");
                            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("BankSoal", "Error parsing response: ", e);
                        Toast.makeText(this, "Kesalahan mengirim data dari server", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("BankSoal", "Volley Error: ", error);
                    Toast.makeText(this, "Terjadi kesalahan koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void modalHasil(BankSoal bankSoal, int jumlahBenar, int jumlahSalah) {
        dialog.show();
        //untuk menampilkan hasil bank soal
        nilai_benar.setText(String.valueOf(jumlahBenar));
        nilai_salah.setText(String.valueOf(jumlahSalah));
    }


    private void fetchSoal() {
        String id = getIntent().getStringExtra("id");
        Log.d("BankSoal", "ID: " + id);
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getSoal.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("BankSoal", "Response: " + response);

                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                listSoal.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String soal = jsonObject.getString("nama_soal");
                                    String jawaban_a = jsonObject.getString("pilihan_a");
                                    String jawaban_b = jsonObject.getString("pilihan_b");
                                    String jawaban_c = jsonObject.getString("pilihan_c");


                                    listSoal.add(new MateriSoal(id, soal, jawaban_a, jawaban_b, jawaban_c));
                                    Log.d("BankSoal", "Adding Soal: " + soal);
                                }

                                    materisoal = new AdapterSoal(BankSoal.this, listSoal);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(BankSoal.this));
                                    recyclerView.setAdapter(materisoal);
                                    materisoal.notifyDataSetChanged();

                            } else {
                                Toast.makeText(BankSoal.this, "Soal belum tersedia", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BankSoal.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BankSoal.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BankSoal", "Volley error: ", error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        // Menambahkan permintaan Volley ke antrian
        Volley.newRequestQueue(BankSoal.this).add(stringRequest);
    }



}