package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybimo.Main;
import com.example.mybimo.R;

import java.util.ArrayList;

public class AdapterHistory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HISTORY = 0;
    private static final int TYPE_QUIZ = 1;
    private Context context;
    private ArrayList<Object> historyArrayList;

    public AdapterHistory(ArrayList<Object> historyArrayList, Context context) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        if (historyArrayList.get(position) instanceof GetHistory) {
            return TYPE_HISTORY;
        } else if (historyArrayList.get(position) instanceof HasilQuiz) {
            return TYPE_QUIZ;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HISTORY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
            return new HistoryViewHolder(view);
        } else if (viewType == TYPE_QUIZ) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hasil_quiz, parent, false);
            return new HasilQuizViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (getItemViewType(position) == TYPE_HISTORY) {
            HistoryViewHolder historyViewHolder = (HistoryViewHolder) holder;
            GetHistory getHistory = (GetHistory) historyArrayList.get(position);

            historyViewHolder.materi.setText(getHistory.getJudul_materi());
            historyViewHolder.nilai_benar.setText(String.valueOf(getHistory.getJumlah_benar()));
            historyViewHolder.nilai_salah.setText(String.valueOf(getHistory.getJumlah_salah()));
            historyViewHolder.tanggal.setText(getHistory.getTanggal());
        } else if (getItemViewType(position) == TYPE_QUIZ) {
            HasilQuizViewHolder quizViewHolder = (HasilQuizViewHolder) holder;
            HasilQuiz hasilQuiz = (HasilQuiz) historyArrayList.get(position);

            quizViewHolder.nilai_benar.setText(String.valueOf(hasilQuiz.getJumlah_benar()));
            quizViewHolder.nilai_salah.setText(String.valueOf(hasilQuiz.getJumlah_salah()));
            quizViewHolder.tanggal.setText(hasilQuiz.getTanggal());
        }
    }


    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView materi, nilai_benar, nilai_salah, tanggal;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            materi = itemView.findViewById(R.id.materi);
            nilai_benar = itemView.findViewById(R.id.nilai_benar);
            nilai_salah = itemView.findViewById(R.id.nilai_salah);
            tanggal = itemView.findViewById(R.id.tanggal);
        }
    }

    public static class HasilQuizViewHolder extends RecyclerView.ViewHolder {
        public TextView nilai_benar, nilai_salah, tanggal;

        public HasilQuizViewHolder(@NonNull View itemView) {
            super(itemView);
            nilai_benar = itemView.findViewById(R.id.nilai_benar);
            nilai_salah = itemView.findViewById(R.id.nilai_salah);
            tanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}
