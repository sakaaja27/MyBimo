package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybimo.R;

import java.util.ArrayList;

import fragment.Zoom;

public class AdapterZoom extends RecyclerView.Adapter<AdapterZoom.ViewHolder> {
    private Context context;
    private ArrayList<getZoom> zoomArrayList;
    private Zoom zoomFragment;


    public AdapterZoom(Context context,ArrayList<getZoom> zoomArrayList,Zoom zoomFragment) {
        this.zoomArrayList = zoomArrayList;
        this.context = context;
        this.zoomFragment = zoomFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zoom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        getZoom getzoom = zoomArrayList.get(position);
        holder.nama_judul.setText(getzoom.getNama_judul());
        holder.tanggal.setText(getzoom.getTanggal());
        holder.waktu.setText(getzoom.getWaktu());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomFragment.checkUserTransaction(getzoom.getId(),getzoom.getLink_zoom());
            }
        });

    }

    @Override
    public int getItemCount() {
        return zoomArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_judul, tanggal, waktu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_judul = itemView.findViewById(R.id.judul_zoom);
            tanggal = itemView.findViewById(R.id.tanggal);
            waktu = itemView.findViewById(R.id.waktu);
        }
    }
}
