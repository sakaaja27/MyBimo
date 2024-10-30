package materi;

import static auth.DB_Contract.ip;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybimo.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class idiom_materigrammar extends AppCompatActivity {

    PDFView pdfView;
    CircularProgressIndicator progressBar;
    ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idiomgrammar);

        pdfView = findViewById(R.id.pdfView);
        arrow = findViewById(R.id.arrow_back);
        progressBar = findViewById(R.id.progress_circular);

        //  progress bar terlihat di awal
        progressBar.setVisibility(View.VISIBLE);

        // Set listener untuk arrow
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke aktivitas sebelumnya
            }
        });

        String url = getIntent().getStringExtra("upload_file");
        String uploadFile = "http://" + ip + "/website%20mybimo/mybimo/src/getData/" + url;
        System.out.println("TET" + uploadFile);
        if (uploadFile != null && !uploadFile.isEmpty()) {
            new RetrievePDFFromUrl().execute(uploadFile);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "File tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    class RetrievePDFFromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Pastikan dijalankan di UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            if (inputStream != null) {
                pdfView.fromStream(inputStream)
                        .onLoad(new OnLoadCompleteListener() {
                            @Override
                            public void loadComplete(int nbPages) {
                                // PDF berhasil dimuat
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        })
                        .onError(new OnErrorListener() {
                            @Override
                            public void onError(Throwable t) {
                                System.out.println("SAKA" + t.getMessage());
                                Toast.makeText(idiom_materigrammar.this, "Error loading PDF: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        })
                        .load();
            } else {
                Toast.makeText(idiom_materigrammar.this, "GAGAL LOAD PDF", Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
}