package fragment;

import static auth.DB_Contract.ip;

import android.content.Intent;
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
import carousel.Materi;
import carousel.MateriAdapter;
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
    private MateriAdapter materiAdapter;
    private TextView auth_name;
    private View view;
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

        ArrayList<Materi> materiArrayList = new ArrayList<>();
        materiArrayList.add(new Materi(R.drawable.icon_vocab, "Vocabulary"));
        materiArrayList.add(new Materi(R.drawable.icon_grammer, "Grammerly"));
        materiArrayList.add(new Materi(R.drawable.icon_listening, "Listening"));
        materiArrayList.add(new Materi(R.drawable.icon_pronount, "Pronount"));
        materiArrayList.add(new Materi(R.drawable.icon_listening, "Listening"));

        materiAdapter = new MateriAdapter(getActivity(), materiArrayList);
        materiAdapter = new MateriAdapter(materiArrayList, getActivity(), new MateriAdapter.OnClickListener() {
                    @Override
                    public void onClickListener(int materi) {
                        // untuk klik setiap item materi
                        switch (materi) {
                            case 0:
                                // buka ke sub materi vocab
                                startActivity(new Intent(getActivity(), MateriVocab.class));
                                break;
        //                    case 1:
        //                        // buka ke sub materi grammerly
        //                        startActivity(new Intent(getActivity(), GrammerlyActivity.class));
        //                        break;
        //                    case 2:
        //                        // buka ke sub materi listening
        //                        startActivity(new Intent(getActivity(), ListeningActivity.class));
        //                        break;
        //                    case 3:
        //                        // buka ke sub materi pronoun
        //                        startActivity(new Intent(getActivity(), PronountActivity.class));
        //                        break;
                            default:
                                Toast.makeText(getActivity(), "Belum ada materi", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(materiAdapter);





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