package fragment;

import static Notifikasi.NotificationUtil.createNotificationChannel;
import static Notifikasi.NotificationUtil.startCheckingTransaksi;
import static auth.DB_Contract.ip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mybimo.Main;
import com.example.mybimo.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.HeroCarouselStrategy;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import Notifikasi.NotificationUtil;
import Adapter.GetUser;
import Adapter.ImageAdapter;
import Adapter.Materi;
import Adapter.MateriAdapter;
import materi.GeneralQuiz;
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
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView auth_name;
    private View view;
    private ShapeableImageView image_profil;
    private RecyclerView carouselRecyclerView;
    private ArrayList<Materi> materiArrayList;
    private ImageView generalQuiz;
//    private boolean allQuiz = true;
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
        swipeRefreshLayout = view.findViewById(R.id.refreshlayoutdashboard);
        auth_name = view.findViewById(R.id.auth_name);
        materiArrayList = new ArrayList<>();
        image_profil = view.findViewById(R.id.image_profile);

        generalQuiz = view.findViewById(R.id.generalQuiz);
        generalQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekSoalQuiz();
            }
        });



        fetchMateriData();




//        dinamis materi


        // Ambil id pengguna dari variable statis di Main
        UserId = Main.RequestUserId;

//        notif
        createNotificationChannel(getContext());
        startCheckingTransaksi(getContext(), UserId);

        // Fetch data
        if (UserId != null) {
            fetchUser (UserId); // Panggil fetchUser  dengan UserId
        } else {
            Toast.makeText(getContext(), "User  ID tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
        setupCarousel();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        // Menambahkan listener untuk ikon "View All"
        TextView viewAllTextView = view.findViewById(R.id.view_all);
        viewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllMateriDialog();
            }
        });
        return view;


    }
// buat cek ada soal apa ngga
    private void cekSoalQuiz() {
        generalQuiz.setEnabled(false);

        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getGeneralQuiz.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Jika ada soal, maka ketika general quiz di klik akan berpindah ke halaman quiz
                            if (response.length() > 0) {
                                Intent intent = new Intent(getContext(), GeneralQuiz.class);
                                startActivity(intent);
                            } else {
                                // Jika tidak ada soal maka akan menampilakn quiz bekum tersedia
                                Toast.makeText(getContext(), "Quiz belum tersedia", Toast.LENGTH_SHORT).show();
                            }
                        }
                        finally {
                            generalQuiz.setEnabled(true); // Aktifkan kembali tombol
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Gagal memuat soal: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        generalQuiz.setEnabled(true); // Aktifkan kembali tombol
                    }
                });
        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Hentikan pengecekan status transaksi saat Activity dihancurkan
        NotificationUtil.stopCheckingTransaksi();
    }

    @SuppressLint("MyApi")
    private void refresh(){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"Refresh",Toast.LENGTH_SHORT).show();
                fetchUser(UserId);
                loadImage(UserId);
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
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
                                String id = jsonObject.getString("id");
                                String fotoIcon = imageUrl + jsonObject.getString("foto_icon");
                                String nama = jsonObject.getString("judul_materi");

                                // Menambahkan data ke ArrayList
                                materiArrayList.add(new Materi(id, fotoIcon, nama));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // Hanya ambil 5 materi untuk ditampilkan di dashboard
                        if (materiArrayList.size() > 3) {
                            List<Materi> displayedMateri = materiArrayList.subList(0,3);
                            setupRecyclerView(displayedMateri);
                        } else {
                            setupRecyclerView(materiArrayList);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "ERROR MATERI: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Menambahkan request ke antrian Volley
        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }

    // Metode untuk mengatur RecyclerView
    private void setupRecyclerView(List<Materi> displayedMateri) {
        // Mengatur GridLayoutManager dengan 3 kolom
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        materiAdapter = new MateriAdapter(new ArrayList<>(displayedMateri), getActivity(), new MateriAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                Materi selectedMateri = displayedMateri.get(position);
                checkUserTransaction(selectedMateri.getId()); // Memanggil metode yang benar
            }
        });

        recyclerView.setAdapter(materiAdapter);
    }

    private void showAllMateriDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.bottomdialog_materi, null);
        RecyclerView allMateriRecyclerView = dialogView.findViewById(R.id.all_materi_recycler_view);

        // Mengatur GridLayoutManager dengan 3 kolom
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        allMateriRecyclerView.setLayoutManager(gridLayoutManager);

        // Menambahkan ItemDecoration untuk jarak antar item
        allMateriRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int column = position % 3; // item column

                // Atur jarak di kiri dan kanan
                outRect.left = (column * 8); // Margin kiri
                outRect.right = (8 - column) * 8; // Margin kanan

                // Atur jarak di atas dan bawah
                if (position < 3) { // jika item di baris pertama
                    outRect.top = 8; // Margin atas
                }
                outRect.bottom = 8; // Margin bawah
            }
        });

        // Buat adapter dengan OnClickListener
        MateriAdapter allMateriAdapter = new MateriAdapter(materiArrayList, getActivity(), new MateriAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                // Tangani klik item di sini
                Materi selectedMateri = materiArrayList.get(position);
                checkUserTransaction(selectedMateri.getId()); // Memanggil metode yang benar
            }
        });

        allMateriRecyclerView.setAdapter(allMateriAdapter);

        dialog.setContentView(dialogView);
        dialog.show();
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

        // Menambahkan parameter unik untuk menghindari caching
        String uniqueImageUrl = imageUrl + "?t=" + System.currentTimeMillis();

        try {
            Glide.with(requireContext())
                    .load(uniqueImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Nonaktifkan caching disk
                    .skipMemoryCache(true) // Nonaktifkan caching memori
                    .placeholder(R.drawable.icon_profile)
                    .error(R.drawable.icon_profile)
                    .into(image_profil);
        } catch (Exception e) {
            Log.e("Dashboard", "Error loading image: " + e.getMessage());
            image_profil.setImageResource(R.drawable.icon_profile); // Set gambar default jika error
        }
    }

}