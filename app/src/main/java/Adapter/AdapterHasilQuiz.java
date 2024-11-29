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

public class AdapterHasilQuiz extends RecyclerView.Adapter<AdapterHasilQuiz.ViewHolder> {
    private Context context;
    private ArrayList<HasilQuiz> hasilQuizArrayList;

    public AdapterHasilQuiz(Context context, ArrayList<HasilQuiz> hasilQuizArrayList) {
        this.context = context;
        this.hasilQuizArrayList = hasilQuizArrayList;
    }

    @NonNull
    @Override
    public AdapterHasilQuiz.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hasil_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHasilQuiz.ViewHolder holder, int position) {
        HasilQuiz hasilQuiz = hasilQuizArrayList.get(position);
        holder.nilai_benar.setText(String.valueOf(hasilQuiz.getJumlah_benar()));
        holder.nilai_salah.setText(String.valueOf(hasilQuiz.getJumlah_salah()));
        holder.tanggal.setText(hasilQuiz.getTanggal());
    }

    @Override
    public int getItemCount() {
        return hasilQuizArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nilai_benar, nilai_salah, tanggal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nilai_benar = itemView.findViewById(R.id.nilai_benar);
            nilai_salah = itemView.findViewById(R.id.nilai_salah);
            tanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}
