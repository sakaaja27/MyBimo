package fragment;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.getSystemService;
import static Notifikasi.NotificationUtil.createNotificationChannel;
import static Notifikasi.NotificationUtil.startCheckingTransaksi;
import static auth.DB_Contract.ip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import Notifikasi.NotificationUtil;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.Main;
import com.example.mybimo.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.GetPayment;
import Adapter.VolleyMultipartRequest;
import Payment.AfterPayment;

public class Payment extends Fragment {
    public static final String URL = "http://" + ip + "/mybimo/getData/payment.php";
    private ImageView splashImage;
    private Button btn_upload;
    private String idPembayaran;
    private ImageView imgtransaksi;
    private TextView afterpay;
    private ImagePicker imagePicker;
    private static String UserId;
    private TextView namaPembayaran;
    private ImageView animationpayment;
    private TextView harga;
    private TextView nomorBank;
    private TextView statuspay;
    private String CHANNEL_ID = "notification";
    private ImageView pay_card;
    private View view; // Deklarasi variabel view
    List<GetPayment> paymentList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;


    public Payment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment, container, false); // Simpan referensi ke view
        imgtransaksi = view.findViewById(R.id.img_transaksi);
        statuspay = view.findViewById(R.id.status_pay);
        namaPembayaran = view.findViewById(R.id.nama_pembayaran);
        harga = view.findViewById(R.id.harga);
        btn_upload = view.findViewById(R.id.upload_file);
        nomorBank = view.findViewById(R.id.nomor_bank);
        animationpayment = view.findViewById(R.id.animationpayment);
        pay_card = view.findViewById(R.id.pay_card);
        afterpay = view.findViewById(R.id.afterpay);
        swipeRefreshLayout = view.findViewById(R.id.paymentrefresh);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


//        id user
        UserId = Main.RequestUserId;
        getTransaksiStatus(UserId);
        createNotificationChannel(getContext());
        startCheckingTransaksi(getContext(), UserId);


        fetchPayment();

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });



        return view;
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
                fetchPayment();
                getTransaksiStatus(UserId);
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);
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

                                idPembayaran  = payment.getId();

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
                            System.out.println("gagal"+e.getMessage());
                            Toast.makeText(getContext(), "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("gagal"+error);
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

//    transaksi image
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
        Uri uri = data.getData();

        if (uri != null) {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                byte[] imageData = getBytes(inputStream);
                uploadToServer(imageData);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e){
                System.out.println("gagal"+e.getMessage());
                e.printStackTrace();
            }
        }
    }
}




    private void uploadToServer(byte[] imageData) {
        // URL endpoint untuk upload gambar
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/transaksi.php";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    if (jsonObject.getBoolean("success")) {
                        Toast.makeText(getContext(),"Upload berhasil",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    System.out.println("gagal"+e.getMessage());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("gagal"+error);
                        Toast.makeText(getContext(), "Gagal mengupload", Toast.LENGTH_SHORT).show();
                    }
                }) {
            // Override method untuk menambahkan parameter tambahan
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", UserId); // Menambahkan ID pengguna ke parameter
                params.put("id_pembayaran", idPembayaran); // Menambahkan ID pembayaran ke parameter
                params.put("status", "0"); // Menambahkan status ke parameter
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String fileName = UserId + "-"+System.currentTimeMillis() + ".jpg";
                params.put("upload_bukti", new DataPart(fileName, imageData, "image/jpeg"));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(volleyMultipartRequest);
    }
    private byte[] getBytes(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1){
            byteBuffer.write(buffer,0,len);
        }
        return byteBuffer.toByteArray();
    }

    private void getTransaksiStatus(String UserId){
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getstatustransaksi.php?id_user=" + UserId;
        System.out.println("API"+url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray transaksiarray = response.getJSONArray("transaksi");
                    if (transaksiarray.length() == 0) {
                        statuspay.setText("Belum Bayar");

                    } else {
                        JSONObject transaksi = transaksiarray.getJSONObject(0);
                        int status = transaksi.getInt("status");
                        System.out.println(transaksi.getInt("status"));

                        if (status == 3) {
                            statuspay.setText("Tidak Aktif");
                        } else if (status == 0) {
                            statuspay.setText("Status Pending");

                        } else if (status == 1) {
                            animationpayment.setVisibility(View.GONE);
                            pay_card.setImageResource(R.drawable.rounded_borderdash);
                            namaPembayaran.setVisibility(View.INVISIBLE);
                            harga.setVisibility(View.INVISIBLE);
                            nomorBank.setVisibility(View.INVISIBLE);
                            afterpay.setVisibility(View.VISIBLE);
                            btn_upload.setVisibility(View.INVISIBLE);
                            statuspay.setText("Terkonfirmasi");


                        } else if (status == 2){
                            statuspay.setText("Ditolak");


                        }
                    }
                } catch (JSONException e) {
                    System.out.println("gagal"+e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("gagal"+error.getMessage());
                Toast.makeText(getContext(),"Gagal mengambil data",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);

    }




}