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

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    private Context context;
    private ArrayList<GetHistory> historyArrayList;

    public AdapterHistory(ArrayList<GetHistory> historyArrayList, Context context) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public AdapterHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistory.ViewHolder holder, int position) {
        GetHistory getHistory = historyArrayList.get(position);
        holder.materi.setText(getHistory.getJudul_materi());
        holder.nilai_benar.setText(String.valueOf(getHistory.getJumlah_benar()));
        holder.nilai_salah.setText(String.valueOf(getHistory.getJumlah_salah()));
        holder.tanggal.setText(getHistory.getTanggal());
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView materi, nilai_benar, nilai_salah, tanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            materi = itemView.findViewById(R.id.materi);
            nilai_benar = itemView.findViewById(R.id.nilai_benar);
            nilai_salah = itemView.findViewById(R.id.nilai_salah);
            tanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}
