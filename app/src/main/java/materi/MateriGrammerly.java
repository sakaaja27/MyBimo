package materi;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;
import static auth.DB_Contract.ip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mybimo.Main;
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
    private TextView materi;
    private ImageView icon_submateri;
    private ArrayList<MateriSub> materiSubArrayList;
    private ImageView rounded;
    private ImageView arrow;
    private static String UserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materi_grammerly);
        recyclerView = findViewById(R.id.recycler_view);
        materi = findViewById(R.id.materi);
        icon_submateri = findViewById(R.id.icon_grammar);
        materiSubArrayList = new ArrayList<>();
        arrow = findViewById(R.id.arrow_back);
        rounded = findViewById(R.id.rounded_login);

        btn_soal = findViewById(R.id.btn_soal);
        btn_soal.setVisibility(View.GONE);
        UserId = Main.RequestUserId;

        //btn untuk ke halaman soal
        btn_soal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MateriGrammerly.this, BankSoal.class);
                String materiId = getIntent().getStringExtra("id");
                intent.putExtra("userId", UserId);
                intent.putExtra("id", materiId);
                Log.d("User ID", UserId);
                Log.d("Materi ID", materiId);
                startActivity(intent);
            }
        });


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
        String url = "http://" + ip + "/WEBSITE%20MYBIMO/mybimo/src/getData/submateri.php";

        // Membuat request POST menggunakan StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            // Mengubah respons string menjadi JSONArray
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() == 0) {
                                // Menampilkan pesan jika tidak ada data
                                icon_submateri.setImageResource(R.drawable.comingsoon);
                                rounded.setVisibility(View.INVISIBLE);

                                // Mengatur scaleType agar gambar ditampilkan dengan proporsional
                                icon_submateri.setScaleType(ImageView.ScaleType.FIT_CENTER); // Atau bisa menggunakan CENTER_INSIDE

                                // Mengatur posisi icon_submateri ke tengah
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                                // Mengatur icon_submateri agar berada di tengah RelativeLayout
                                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                                icon_submateri.setLayoutParams(params); // Menerapkan LayoutParams ke icon_submateri
                                Toast.makeText(MateriGrammerly.this, "COMING SOON", Toast.LENGTH_SHORT).show();
                               // jika materi belum ada maka button tidak ditampilkan
                                btn_soal.setVisibility(View.GONE);
                                return;
                            }
                            // Iterasi melalui setiap objek dalam JSONArray
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Mengekstrak data dari setiap objek JSON
                                String id = jsonObject.getString("id");
                                String judul_materi = jsonObject.getString("judul_materi");
                                String nama = jsonObject.getString("nama_sub");
                                String upload_file = jsonObject.getString("upload_file");
                                String fotoIcon = jsonObject.getString("foto_icon");

                                // Menambahkan data ke ArrayList
                                materiSubArrayList.add(new MateriSub(id,judul_materi, nama,fotoIcon, upload_file));

                                materi.setText(judul_materi);
                                String imageUrl = "http://" + ip +"/WEBSITE%20MYBIMO/mybimo/src/getData/"+fotoIcon;
                                System.out.println("IMAGEICON"+imageUrl);
                                Log.d("", imageUrl); // Log URL gambar
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    loadImage(imageUrl);
                                } else {
                                    Toast.makeText(MateriGrammerly.this, "URL gambar tidak valid", Toast.LENGTH_SHORT).show();
                                }
                            }

                            // Memanggil method untuk setup RecyclerView setelah data diambil
                            setupRecyclerView();
                            checkSoal(id); // buat check apapakh dalam materi ini terdapat soal
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
    // buat check apapakah dalam materi ini terdapat soal, jika ada maka button soal akan ditampilkan
    private void checkSoal(String id) {
        String url = "http://" + ip + "/WEBSITE%20MYBIMO/mybimo/src/getData/getSoal.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                // Jika soal ada, menampilkan tombol soal
                                btn_soal.setVisibility(View.VISIBLE);
                            } else {
                                // Jika tidak ada soal, sembunyikan tombol soal
                                btn_soal.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MateriGrammerly.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        Volley.newRequestQueue(MateriGrammerly.this).add(stringRequest);

    }

    private void loadImage(String imageUrl) {
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // nampilkan gambar di ImageView
                        System.out.println("Stringresponse"+response);
                        icon_submateri.setImageBitmap(response);


                    }
                },
                0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("Image Load Error", "Error Code: " + error.networkResponse.statusCode);
                        }
//                        tampilkan gambar drawable default
                        icon_submateri.setImageResource(R.drawable.icon_listening);
                    }
                });

        // Tambahkan request ke queue
        Volley.newRequestQueue(MateriGrammerly.this).add(imageRequest);
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
                intent.putExtra("nama_sub",selectedMateri.getNama());
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