package fragment;

import static auth.DB_Contract.ip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.List;

import Adapter.GetUser;
import Adapter.Materi;
import Adapter.MateriAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment{
//    ambil data RequestUserid pada class main
    private static String UserId;
    private RecyclerView recyclerView;
//    private MateriAdapter materiAdapter;
    private TextView auth_name;
    private View view;
//    private ArrayList<Materi> materiArrayList;
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

        recyclerView = view.findViewById(R.id.recycler_view);

        auth_name = view.findViewById(R.id.auth_name);
//        materiArrayList = new ArrayList<>();

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
        return view;
    }

//    materi
    private void fetchMateriData(){
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getmateriawal.php";
        String imageUrl = "http://" + ip +"/website%20mybimo/mybimo/src/getData/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String fotoIcon = imageUrl + jsonObject.getString("foto_icon");
                        String nama = jsonObject.getString("judul_materi");

//                        materiArrayList.add(new Materi(fotoIcon, nama));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setupRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"ERROR MATERI"+error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(getActivity()).add(jsonArrayRequest);
    }

    private void setupRecyclerView(){
//        materiAdapter = new MateriAdapter(getActivity(),materiArrayList);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
//        recyclerView.setAdapter(materiAdapter);
    }

    private void fetchUser (String UserId) {
        String URL = "http://" + ip + "/mybimo/getData/getUser.php?id="+ UserId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Pastikan ada data dalam response
                            if (response.length() > 0) {
                                // Ambil data pembayaran pertama dari response
                                JSONObject jsonObject = response.getJSONObject(0);
                                GetUser user = new GetUser (
                                        jsonObject.getString("id"),
                                        jsonObject.getString("username"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("phone"),
                                        jsonObject.getString("upload_image"),
                                        jsonObject.getString("password")
                                );

                                // Set data ke TextView yang sesuai
                                if (view != null) {
                                    String formattedHarga = "Hii, " + user.getUsername();
                                    auth_name.setText(formattedHarga);
                                } else {
                                    if (isAdded()) {
                                        Toast.makeText(getContext(), "View tidak valid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (isAdded()) {
                                    Toast.makeText(getContext(), "Tidak ada data yang tersedia", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
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
                        if (isAdded()) {
                            System.out.println(UserId);
                            Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Tambahkan request ke queue
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
    }

}