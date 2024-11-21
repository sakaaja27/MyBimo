package Notifikasi;

import static auth.DB_Contract.ip;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationUtil {
    private static final String CHANNEL_ID = "notification";
    private static final String PREFS_NAME = "MyBimoPrefs";
    private static final String LAST_STATUS_KEY = "last_status";
    private static final String NOTIFICATION_SHOW_KEY = "notification_show";
    private static final int CHECK_INTERVAL = 1000;

    private static Handler handler = new Handler();
    private static Runnable checkTransaksiRunnable;

    public static void startCheckingTransaksi(Context context,String userId){
        checkTransaksiRunnable = new Runnable() {
            @Override
            public void run() {
                getTransaksiStatus(context,userId);
                handler.postDelayed(this,CHECK_INTERVAL);
            }
        };
        handler.post(checkTransaksiRunnable);
    }

    public static void stopCheckingTransaksi(){
        if (checkTransaksiRunnable != null){
            handler.removeCallbacks(checkTransaksiRunnable);
        }
    }

    private static void getTransaksiStatus(Context context,String userId){
        String url = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getstatustransaksi.php?id_user=" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray transaksiArray = response.getJSONArray("transaksi");
                    if (transaksiArray.length() == 0) {
                        return;
                    } else {
                        JSONObject transaksi = transaksiArray.getJSONObject(0);
                        int status = transaksi.getInt("status");
                        System.out.println("Status" + status);

                        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
                        int LastStatus = pref.getInt(LAST_STATUS_KEY,-1);
                        if (status != LastStatus){
                            switch (status) {
                                case 3:
                                    sendNotification(context, "MyBimo", "Status tidak aktif silahkan berlangganan lagi");
                                    break;
                                case 2:
                                    sendNotification(context, "MyBimo", "Status transaksi Ditolak");
                                    break;
                                case 1:
                                    sendNotification(context, "MyBimo", "Status transaksi telah disetujui");
                                    break;
                                case 0:
                                    sendNotification(context, "MyBimo", "Status transaki menunggu persetujuan ");
                                    break;
                            }
//                            simpan status baru buat status trakhir
                            pref.edit().putInt(LAST_STATUS_KEY,status).apply();
                        }

                    }
                } catch (JSONException e) {
                    System.out.println("GAGAL" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error"+ error);
                Toast.makeText(context,"GAGAL MENGAMBIL DATA",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public static void sendNotification(Context context,String title,String message){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        boolean notificationShow = prefs.getBoolean(NOTIFICATION_SHOW_KEY,false);

//        if (notificationShow){
//            return;
//        }

        // Periksa apakah notifikasi diizinkan
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null && !notificationManager.areNotificationsEnabled()) {
            // Notifikasi dinonaktifkan, beri tahu pengguna
            showNotificationSettingsDialog(context);
            return;
        }

//        kirim notif
        try {
            // Kirim notifikasi
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.mybimo_font)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());

            // Simpan status notifikasi telah ditampilkan
            prefs.edit().putBoolean(NOTIFICATION_SHOW_KEY, true).apply();
        } catch (SecurityException e) {
            // Tangani SecurityException jika terjadi
            Toast.makeText(context, "Gagal mengirim notifikasi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static void showNotificationSettingsDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Notifikasi Dinonaktifkan")
                .setMessage("Notifikasi untuk aplikasi ini dinonaktifkan. Silakan aktifkan di pengaturan.")
                .setPositiveButton("Pengaturan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }


    public static void createNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "MyBimo Channel";
            String description = "Channel untuk notifikasi MyBimo";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }



}
