package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mybimo.R;

import java.util.ArrayList;

// Adapter untuk RecyclerView yang menampilkan daftar materi
public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.ViewHolder> {
    private ArrayList<Materi> materiArrayList; // List untuk menyimpan data materi
    private Context context; // Context aplikasi
    private OnClickListener onClickListener; // Listener untuk menangani klik item

    // Constructor dengan OnClickListener
    public MateriAdapter(ArrayList<Materi> materiArrayList, Context context, OnClickListener onClickListener) {
        this.materiArrayList = materiArrayList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    // Membuat dan menginisialisasi ViewHolder baru
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_materi_merge, parent, false);
        return new ViewHolder(view);
    }

    // Mengisi data ke dalam ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Materi materi = materiArrayList.get(position);
        // Memuat gambar menggunakan Glide
        Glide.with(context).load(materi.getIcon()).into(holder.imageView);
        holder.textView.setText(materi.getNama());

        // Menambahkan listener klik pada item
        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClickListener(position);
            }
        });
    }

    // Mengembalikan jumlah item dalam list
    @Override
    public int getItemCount() {
        return materiArrayList.size();
    }

    // ViewHolder untuk menyimpan referensi ke views dalam setiap item
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon_image);
            textView = itemView.findViewById(R.id.text_view);
        }
    }

    // Interface untuk menangani klik pada item
    public interface OnClickListener {
        void onClickListener(int position);
    }
}