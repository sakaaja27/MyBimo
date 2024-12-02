package fragment;

import static auth.DB_Contract.ip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.Main;
import com.example.mybimo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.AdapterZoom;
import Adapter.getZoom;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Zoom#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Zoom extends Fragment {

    private RecyclerView recyclerView;
    private AdapterZoom adapterZoom;
    private TextView nama_judul;
    private TextView tanggal;
    private TextView waktu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<getZoom> zoomArrayList;
    private View view;
    private static String UserId;
    private static final String TAG = "Zoom";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Zoom() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Zoom.
     */
    // TODO: Rename and change types and number of parameters
    public static Zoom newInstance(String param1, String param2) {
        Zoom fragment = new Zoom();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_zoom, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        zoomArrayList = new ArrayList<>();
        swipeRefreshLayout = view.findViewById(R.id.refreshlayoutzoom);
        adapterZoom = new AdapterZoom(getContext(),zoomArrayList,this);
        recyclerView.setAdapter(adapterZoom);

        UserId = Main.RequestUserId;

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        fetchZoom();
        return view;
    }

    @SuppressLint("MyApi")
    private void refresh(){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"Refresh",Toast.LENGTH_SHORT).show();
                fetchZoom();
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
    }

    private void fetchZoom(){
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getZoom.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    zoomArrayList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String nama_judul = jsonObject.getString("nama_judul");
                        String link_zoom = jsonObject.getString("link_zoom");
                        String status_zoom = jsonObject.getString("status_zoom");
                        String tanggal = jsonObject.getString("tanggal");
                        String waktu = jsonObject.getString("waktu");

                        zoomArrayList.add(new getZoom(id,nama_judul,link_zoom,status_zoom,tanggal,waktu));
                    }


                    recyclerView.setAdapter(adapterZoom);
                    adapterZoom.notifyDataSetChanged();
                    if (zoomArrayList.isEmpty()){
                        Toast.makeText(getContext(),"Tidak ada data untuk ditampilkan!",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(),"Kesalahan dalam memproses data!",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Gagal mendapatkan data: " + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }

    public void checkUserTransaction(String zoomId, String linkZoom) {
        String urlTransaksi = "http://" + ip + "/website%20mybimo/mybimo/src/getData/gettransaksi.php?id_user=" + UserId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlTransaksi, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        JSONObject transaksi = response.getJSONObject(0);
                        int status = transaksi.getInt("status");

                        if (status == 1) {
                            // Buka link Zoom
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkZoom));
                            getActivity().startActivity(browserIntent);
                        } else {
                            Toast.makeText(getActivity(), "Silahkan langganan dulu", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Silahkan langganan dulu", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }
}