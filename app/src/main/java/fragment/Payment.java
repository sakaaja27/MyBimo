package fragment;

import static android.app.Activity.RESULT_OK;
import static auth.DB_Contract.ip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import Adapter.GetPayment;
import Payment.AfterPayment;

public class Payment extends Fragment {
    public static final String URL = "http://" + ip + "/mybimo/getData/payment.php";
    private ImageView splashImage;
    private Button btn_submit;
    private Button btn_upload;
    private TextView namaPembayaran;
    private TextView harga;
    private TextView nomorBank;
    private View view; // Deklarasi variabel view
    List<GetPayment> paymentList = new ArrayList<>();

    public Payment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment, container, false); // Simpan referensi ke view
        splashImage = view.findViewById(R.id.animationpayment);
        btn_submit = view.findViewById(R.id.submit);
        namaPembayaran = view.findViewById(R.id.nama_pembayaran);
        harga = view.findViewById(R.id.harga);
        btn_upload = view.findViewById(R.id.upload_file);
        nomorBank = view.findViewById(R.id.nomor_bank);

        // Animation
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.animasi);
        splashImage.startAnimation(animation);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Payment.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AfterPayment.class);
                startActivity(intent);
            }
        });

        // Fetch data
        fetchPayment();

        return view;
    }

   



    private void fetchPayment() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Pastikan ada data dalam response
                            if (response.length() > 0) {
                                // Ambil data pembayaran pertama dari response
                                JSONObject jsonObject = response.getJSONObject(0);
                                GetPayment payment = new GetPayment(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("nama_pembayaran"),
                                        jsonObject.getString("harga"),
                                        jsonObject.getString("nomor_bank")
                                );

                                // Set data ke TextView yang sesuai
                                if (view != null) {
                                    namaPembayaran.setText(payment.getNama_pembayaran());
                                    harga.setText(payment.getHarga());
                                    // Tambahkan "Rp" dan "/Month" pada harga
                                    String formattedHarga = "Rp. " + payment.getHarga() + " /Month";
                                    harga.setText(formattedHarga);
                                    // Format nomor bank dengan spasi setiap 3 karakter
                                    String formattedNomorBank = formatNomorBank(payment.getNomor_bank());
                                    nomorBank.setText(formattedNomorBank);
                                    // Tambahkan letter spacing 35%
                                    nomorBank.setLetterSpacing(0.35f);
                                } else {
                                    Toast.makeText(getContext(), "View tidak valid", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Tidak ada data yang tersedia", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Tambahkan request ke queue
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }

    private String formatNomorBank(String nomorBank) {
        // Menghapus karakter non-digit dari nomor bank, jika ada
        nomorBank = nomorBank.replaceAll("[^\\d]", "");

        // StringBuilder untuk menyimpan hasil format
        StringBuilder formatted = new StringBuilder();

        // Memformat nomor bank dengan menambahkan spasi setiap 3 karakter
        for (int i = 0; i < nomorBank.length(); i++) {
            if (i > 0 && i % 3 == 0) {
                formatted.append(" "); // Tambahkan spasi setiap 3 karakter
            }
            formatted.append(nomorBank.charAt(i));
        }

        return formatted.toString();
    }
}