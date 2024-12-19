package fragment;

import static auth.DB_Contract.ip;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.Main;
import com.example.mybimo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.AdapterHistory;
import Adapter.GetHistory;

public class MyCourse extends Fragment {
    private RecyclerView recyclerView;
    private AdapterHistory adapterHistory;
    private ArrayList<GetHistory> historyArrayList;
    private static String UserId;
    private View view;
    private static final String TAG = "MyCourse";
    private SwipeRefreshLayout swipeRefreshLayout;

    public MyCourse() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout = view.findViewById(R.id.refreshlayoutmycourse);
        historyArrayList = new ArrayList<>();
        adapterHistory = new AdapterHistory(historyArrayList, getContext());
        recyclerView.setAdapter(adapterHistory);


        UserId = Main.RequestUserId;

        if (UserId != null ){
            fetchHistory(UserId);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return view;
    }
    @SuppressLint("MyApi")
    private void refresh(){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"Refresh",Toast.LENGTH_SHORT).show();
                fetchHistory(UserId);
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }

    private void fetchHistory(String UserId) {
        String url = "http://" + ip + "/WEBSITE%20MYBIMO/mybimo/src/getData/getHistory.php?user_id=" + UserId;
        Log.d(TAG, "Request URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            historyArrayList.clear(); // Kosongkan data lama
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String userId = jsonObject.getString("user_id");
                                String idMateri = jsonObject.getString("id_materi");
                                String judulMateri = jsonObject.getString("judul_materi");
                                int jumlahBenar = jsonObject.getInt("jumlah_benar");
                                int jumlahSalah = jsonObject.getInt("jumlah_salah");
                                String tanggal = jsonObject.getString("tanggal");

                                historyArrayList.add(new GetHistory(id, userId, idMateri, judulMateri, jumlahBenar, jumlahSalah, tanggal));
                            }
                            // untuk memperbarui data recyclerview
                            adapterHistory.notifyDataSetChanged();

                            if (historyArrayList.isEmpty()) {
                                Toast.makeText(getContext(), "Tidak ada data untuk ditampilkan!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON Array: " + e.getMessage());
                            Toast.makeText(getContext(), "Kesalahan dalam memproses data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Belum ada bank soal yang dikerjakan " , Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Volley Error: " + error.getMessage());
                    }
                });

        // Tambahkan request ke antrian
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }
}
