package materi;

import static auth.DB_Contract.ip;

import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterMateri;
import Adapter.GetMateri;

public class Idiom extends AppCompatActivity {
    public static final String URL = "http://" + ip + "/mybimo/getData/submateri.php";

    private TextView nama_sub;
    private View view; // Deklarasi variabel view
    private String pdfData;
    private AdapterMateri adapterMateri;
    private WebView webView;


    List<GetMateri> materiArrayList = new ArrayList<>();

    public Idiom() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_idiom);

        nama_sub= findViewById(R.id.nama_sub);
        webView = findViewById(R.id.pdfView);
        // Fetch data
        fetchIdiom();
    }
    private void fetchIdiom() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                JSONObject jsonObject = response.getJSONObject(0);


                                GetMateri materi = new GetMateri(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("id_materi"),
                                        jsonObject.getString("nama_sub"),
                                        jsonObject.getString("upload_file")
                                );
                                if (nama_sub != null ) {
                                    nama_sub.setText(materi.getNamaSub());
                                    String pdfData = materi.getPdfData();
                                    webView = findViewById(R.id.pdfView);
                                    // Mengatur WebView
                                    WebSettings webSettings = webView.getSettings();
                                    webSettings.setJavaScriptEnabled(true);
                                    String Url ="https://docs.google.com/gview?embedded=true&url=" + "file://" + pdfData;
                                    webView.loadUrl(Url);


                                } else {
                                    Toast.makeText(Idiom.this, "View pdf tidak ada", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Idiom.this, "Tidak ada data pdf yang tersedia", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Idiom.this, "Pdf tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Idiom.this, "Gagal memuat materi", Toast.LENGTH_SHORT).show();
                    }
                }

        );


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

    }

}