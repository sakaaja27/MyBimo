package materi;

import static auth.DB_Contract.ip;

import android.app.Dialog;
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
import java.util.Map;

import Adapter.AdapterSoal;
import Adapter.MateriSoal;

public class GeneralQuiz extends AppCompatActivity {
    private AdapterSoal materisoal;
    private RecyclerView recyclerView;
    private ArrayList<MateriSoal> listSoal;
    private ImageView arrow;
    private Button btn_submit;
    private static String UserId;
    private Dialog dialog;
    private ImageView close;
    private TextView nilai_benar;
    private TextView nilai_salah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_general_quiz);
        recyclerView = findViewById(R.id.recycler_view);

        UserId = Main.RequestUserId;
        if (UserId == null || UserId.isEmpty()) {
            Toast.makeText(this, "User ID tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        dialog = new Dialog(GeneralQuiz.this);
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
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Integer, String> jawabanUser = materisoal.getJawabanUser();
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
        fetchQuiz();
    }

    private void fetchQuiz() {
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getGeneralQuiz.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("GeneralQuiz", "Response: " + response);

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
                                    Log.d("GeneralQuiz", "Adding Soal: " + soal);
                                }

                                materisoal = new AdapterSoal(GeneralQuiz.this, listSoal);
                                recyclerView.setLayoutManager(new LinearLayoutManager(GeneralQuiz.this));
                                recyclerView.setAdapter(materisoal);
                                materisoal.notifyDataSetChanged();

                            } else {
                                Toast.makeText(GeneralQuiz.this, "Soal belum tersedia", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GeneralQuiz.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GeneralQuiz.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("GeneralQuiz", "Volley error: ", error);
            }
        }){
            
        };
        // Menambahkan permintaan Volley ke antrian
        Volley.newRequestQueue(GeneralQuiz.this).add(stringRequest);
    }

    private void tampilNilai(String userId, HashMap<Integer, String> jawabanUser) {
            String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getJawaban.php";

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", userId); // Mengirimkan user_id
                jsonObject.put("id", getIntent().getStringExtra("id")); // ID materi dari Intent

                // Membuat objek JSON untuk jawaban user
                JSONObject jsonJawaban = new JSONObject();
                for (Map.Entry<Integer, String> entry : jawabanUser.entrySet()) {
                    jsonJawaban.put(String.valueOf(entry.getKey()), entry.getValue());
                }
                jsonObject.put("jawaban_user", jsonJawaban);

                Log.d("GeneralQuiz", "JSON Sent to Server: " + jsonObject.toString());
            } catch (JSONException e) {
                Log.e("GeneralQuiz", "Error constructing JSON: ", e);
                Toast.makeText(this, "Terjadi kesalahan dalam menyiapkan data", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    response -> {
                        Log.d("GeneralQuiz", "Response: " + response.toString());

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
                            Log.e("GeneralQuiz", "Error parsing response: ", e);
                            Toast.makeText(this, "Kesalahan mengirim data dari server", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Log.e("GeneralQuiz", "Volley Error: ", error);
                        Toast.makeText(this, "Terjadi kesalahan koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            requestQueue.add(jsonObjectRequest);

    }

    private void modalHasil(GeneralQuiz generalQuiz, int jumlahBenar, int jumlahSalah) {
        dialog.show();
        //untuk menampilkan hasil bank soal
        nilai_benar.setText(String.valueOf(jumlahBenar));
        nilai_salah.setText(String.valueOf(jumlahSalah));
    }
}