package materi;

import static android.app.PendingIntent.getActivity;

import static auth.DB_Contract.ip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.AdapterSubGrammer;
import Adapter.MateriSub;

public class MateriGrammerly extends AppCompatActivity {

    private AdapterSubGrammer materisub;
    private AppCompatButton btn_soal;
    private RecyclerView recyclerView;
    private ArrayList<MateriSub> materiSubArrayList;
    private ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materi_grammerly);
        recyclerView = findViewById(R.id.recycler_view);
        materiSubArrayList = new ArrayList<>();
        arrow = findViewById(R.id.arrow_back);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke aktivitas sebelumnya
            }
        });

//        ini itu buat method buat nampung recycle viewnya yang datanya dari db
        fetchSubGrammar();

    }

    //buat method buat nampung recycle viewnya yang datanya dari db
    public void fetchSubGrammar() {
        // Mengambil ID materi yang dikirim dari aktivitas sebelumnya
        String id = getIntent().getStringExtra("id");

        // URL endpoint untuk mengambil data sub materi
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/submateri.php";

        // Membuat request POST menggunakan StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Mengubah respons string menjadi JSONArray
                            JSONArray jsonArray = new JSONArray(response);

                            // Iterasi melalui setiap objek dalam JSONArray
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Mengekstrak data dari setiap objek JSON
                                String id = jsonObject.getString("id");
                                String nama = jsonObject.getString("nama_sub");
                                String upload_file = jsonObject.getString("upload_file");

                                // Menambahkan data ke ArrayList
                                materiSubArrayList.add(new MateriSub(id, nama, upload_file));
                            }

                            // Memanggil method untuk setup RecyclerView setelah data diambil
                            setupRecyclerView();
                        } catch (JSONException e) {
                            // Menangani error parsing JSON
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menampilkan pesan error jika terjadi kesalahan jaringan
                        Toast.makeText(MateriGrammerly.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Menyiapkan parameter untuk request POST
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        // Menambahkan request ke antrian Volley untuk dieksekusi
        Volley.newRequestQueue(MateriGrammerly.this).add(stringRequest);
    }

    private void setupRecyclerView() {
        // Inisialisasi adapter dengan context dan data
        materisub = new AdapterSubGrammer(MateriGrammerly.this, materiSubArrayList);

        // Inisialisasi ulang adapter dengan menambahkan listener untuk klik item
        materisub = new AdapterSubGrammer(materiSubArrayList, MateriGrammerly.this, new AdapterSubGrammer.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                // Mendapatkan item yang diklik berdasarkan posisi
                MateriSub selectedMateri = materiSubArrayList.get(position);

                // Membuat intent untuk berpindah ke aktivitas idiom_materigrammar
                Intent intent = new Intent(MateriGrammerly.this, idiom_materigrammar.class);

                // Menambahkan data tambahan (upload_file) ke intent
                intent.putExtra("upload_file", selectedMateri.getUpload_file());

                // Memulai aktivitas baru
                startActivity(intent);
            }
        });
        // Mengatur layout manager untuk RecyclerView (LinearLayoutManager)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Menetapkan adapter ke RecyclerView
        recyclerView.setAdapter(materisub);
    }
}