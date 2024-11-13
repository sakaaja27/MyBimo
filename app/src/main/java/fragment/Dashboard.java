package fragment;

import static auth.DB_Contract.ip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mybimo.Main;
import com.example.mybimo.R;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.HeroCarouselStrategy;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.GetUser;
import Adapter.ImageAdapter;
import Adapter.Materi;
import Adapter.MateriAdapter;
import materi.MateriGrammerly;
import materi.MateriVocab;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment{
//    ambil data RequestUserid pada class main
    private static String UserId;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private MateriAdapter materiAdapter;

    private TextView auth_name;
    private View view;
    private ShapeableImageView image_profil;
    private RecyclerView carouselRecyclerView;
    private ArrayList<Materi> materiArrayList;
    List<GetUser> getUserList = new ArrayList<>();


    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        carouselRecyclerView = view.findViewById(R.id.carousel_recycler_view); // Tambahkan ini
        recyclerView = view.findViewById(R.id.recycler_view);

        auth_name = view.findViewById(R.id.auth_name);
        materiArrayList = new ArrayList<>();
        image_profil = view.findViewById(R.id.image_profile);

        fetchMateriData();


//        dinamis materi


        // Ambil id pengguna dari variable statis di Main
        UserId = Main.RequestUserId;

        // Fetch data
        if (UserId != null) {
            fetchUser (UserId); // Panggil fetchUser  dengan UserId
        } else {
            Toast.makeText(getContext(), "User  ID tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
        setupCarousel();
        return view;


    }

    private void setupCarousel() {
        ArrayList<Integer> imageResources = new ArrayList<>();
        imageResources.add(R.drawable.bg_corousel1);
        imageResources.add(R.drawable.bg_corousel2);
        imageResources.add(R.drawable.bg_corousel3);

        ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageResources);

        // Menggunakan CarouselLayoutManager
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(new HeroCarouselStrategy());
        carouselRecyclerView.setLayoutManager(layoutManager);
        carouselRecyclerView.setAdapter(imageAdapter);

        // Menggunakan CarouselSnapHelper
        CarouselSnapHelper snapHelper = new CarouselSnapHelper();
        snapHelper.attachToRecyclerView(carouselRecyclerView);

        // Mulai otomatis scroll
        startAutoScroll(imageResources.size());
    }

    private void startAutoScroll(final int itemCount) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int currentItem = 0;

            @Override
            public void run() {
                if (currentItem < itemCount) {
                    carouselRecyclerView.smoothScrollToPosition(currentItem);
                    currentItem++;
                } else {
                    currentItem = 0; // Kembali ke item pertama
                }
                handler.postDelayed(this, 1500); // buat ganti image ygy slma 2 dtik
            }
        };
        handler.postDelayed(runnable, 1500); // mulai image 3 detik
    }

    //materi
    private void fetchMateriData() {
        // URL untuk mengambil data materi
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getmateriawal.php";
        String urlTransaksi = "http://" + ip + "/website%20mybimo/mybimo/src/getData/transaksi.php";
        // URL dasar untuk gambar
        String imageUrl = "http://" + ip +"/website%20mybimo/mybimo/src/getData/";

        // Membuat request JSON Array untuk materi
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Loop melalui setiap item dalam response
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                // Mengambil data dari JSON
                                String id = jsonObject.getString("id");
                                String fotoIcon = imageUrl + jsonObject.getString("foto_icon");
                                String nama = jsonObject.getString("judul_materi");

                                // Menambahkan data ke ArrayList
                                materiArrayList.add(new Materi(id, fotoIcon, nama));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // Setelah data diambil, setup RecyclerView
                        setupRecyclerView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menampilkan pesan error jika terjadi kesalahan
                        Toast.makeText(getActivity(), "ERROR MATERI: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Menambahkan request ke antrian Volley
        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }

    // Metode untuk mengatur RecyclerView
    private void setupRecyclerView() {
        // Inisialisasi adapter
        materiAdapter = new MateriAdapter(getActivity(), materiArrayList);
        materiAdapter = new MateriAdapter(materiArrayList, getActivity(), new MateriAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                Materi selectedMateri = materiArrayList.get(position);
                checkUserTransaction(selectedMateri.getId());
            }
        });

        // Mengatur layout manager (GridLayoutManager dengan 3 kolom)
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        // Menetapkan adapter ke RecyclerView
        recyclerView.setAdapter(materiAdapter);
    }

    // Metode untuk memeriksa status transaksi pengguna
    private void checkUserTransaction(String materiId) {
        String urlTransaksi = "http://" + ip + "/website%20mybimo/mybimo/src/getData/gettransaksi.php?id_user=" + UserId; // Ganti userId dengan ID pengguna yang sesuai

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlTransaksi, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                JSONObject transaksi = response.getJSONObject(0);
                                int status = transaksi.getInt("status"); // Mengambil status transaksi

                                if (status == 1) {
                                    // Arahkan ke aktivitas jika status = 1
                                    Intent intent = new Intent(getActivity(), MateriGrammerly.class);
                                    intent.putExtra("id", materiId);
                                    startActivity(intent);
                                } else {
                                    // Tampilkan toast jika status = 0
                                    Toast.makeText(getActivity(), "Silahkan langganan dulu", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Tampilkan toast jika tidak ada data transaksi
                                Toast.makeText(getActivity(), "Silahkan langganan dulu", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "ERROR TRANSAKSI: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Menambahkan request ke antrian Volley
        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }

//User
// Metode untuk mengambil data user dari server berdasarkan UserId
private void fetchUser (String UserId) {
    // Membuat URL untuk request dengan menambahkan UserId sebagai parameter
    String URL = "http://" + ip + "/mybimo/getData/getUser.php?id=" + UserId;

    // Membuat request JSON Array menggunakan Volley
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET, // Metode HTTP GET
            URL,               // URL endpoint
            null,             // tidak ada body request
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        // Memeriksa apakah response memiliki data
                        if (response.length() > 0) {
                            // Mengambil objek pertama dari array JSON
                            JSONObject jsonObject = response.getJSONObject(0);

                            // Membuat objek GetUser  dari data JSON
                            GetUser  user = new GetUser (
                                    jsonObject.getString("id"),
                                    jsonObject.getString("username"),
                                    jsonObject.getString("email"),
                                    jsonObject.getString("phone"),
                                    jsonObject.getString("upload_image"),
                                    jsonObject.getString("password")
                            );

                            // Memeriksa apakah view sudah diinisialisasi
                            if (view != null) {
                                String imageUrl = "http://" + ip + "/website%20mybimo/mybimo/src/getData/" + user.getUploadImage();
                                System.out.println("IMAGE: " + imageUrl);
                                Log.d("Image URL", imageUrl); // Log URL gambar
                                loadImage(imageUrl); // Panggil metode loadImage untuk menampilkan gambar
                            }
                        } else {
                            // Menampilkan pesan jika tidak ada data
                            if (isAdded()) {
                                Toast.makeText(getContext(), "Tidak ada data yang tersedia", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        // Menangani error parsing JSON
                        e.printStackTrace();
                        if (isAdded()) {
                            Toast.makeText(getContext(), "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Menangani error network request
                    if (isAdded()) {
                        System.out.println(UserId);
                        Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    // Menambahkan request ke Volley Request Queue
    Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
}

    private void loadImage(String imageUrl) {
        if (getContext() == null) {
            Log.e("Dashboard", "Context is null");
            return;
        }

        try {
            Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.icon_profile) // Gambar placeholder saat loading
                    .error(R.drawable.icon_profile) // Gambar yang ditampilkan jika terjadi error
                    .into(image_profil);
        } catch (Exception e) {
            Log.e("Dashboard", "Error loading image: " + e.getMessage());
            image_profil.setImageResource(R.drawable.icon_profile); // Set gambar default jika error
        }
    }

}