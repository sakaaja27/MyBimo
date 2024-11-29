package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybimo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterSoal extends RecyclerView.Adapter<AdapterSoal.ViewHolder> {
    private Context context;
    private ArrayList<MateriSoal> listSoal;
    private HashMap<Integer, String> jawabanUser = new HashMap<>();

    public AdapterSoal(Context context, ArrayList<MateriSoal> listSoal) {
        this.context = context;
        this.listSoal = listSoal;
    }

    @NonNull
    @Override
    public AdapterSoal.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_soal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MateriSoal materiSoal = listSoal.get(position);
        holder.textView.setText(materiSoal.getNama_soal());
        holder.radio_a.setText(materiSoal.getJawaban_a());
        holder.radio_b.setText(materiSoal.getJawaban_b());
        holder.radio_c.setText(materiSoal.getJawaban_c());

        // menyimpan jawaban yang dipilih ke shared preferences
        String pilihJawaban = jawabanUser.get(position);
       // untuk mengatur radio button yang dipilih berdasarkan jawaban
        if (pilihJawaban != null) {

            if (pilihJawaban.equals(materiSoal.getJawaban_a())) {
                holder.radioGroup.check(holder.radio_a.getId());
            } else if (pilihJawaban.equals(materiSoal.getJawaban_b())) {
                holder.radioGroup.check(holder.radio_b.getId());
            } else if (pilihJawaban.equals(materiSoal.getJawaban_c())) {
                holder.radioGroup.check(holder.radio_c.getId());
            }
            } else {
            holder.radioGroup.clearCheck();
        }
        //listener untuk radio group dalam menyimpan jawaban
        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) ->{
            String jawaban_user = "";
            if (checkedId == holder.radio_a.getId()){
                jawaban_user = materiSoal.getJawaban_a();
            } else if (checkedId == holder.radio_b.getId()) {
                jawaban_user  = materiSoal.getJawaban_b();
            } else if (checkedId == holder.radio_c.getId()) {
                jawaban_user  = materiSoal.getJawaban_c();
            }
            jawabanUser.put(materiSoal.getId(), jawaban_user);

        });

    }

    @Override
    public int getItemCount() {
        return listSoal.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RadioGroup radioGroup;
        public RadioButton radio_a;
        public RadioButton radio_b;
        public RadioButton radio_c;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.soal);
            radioGroup = itemView.findViewById(R.id.radio_group);
            radio_a = itemView.findViewById(R.id.radio_a);
            radio_b = itemView.findViewById(R.id.radio_b);
            radio_c = itemView.findViewById(R.id.radio_c);
        }

    }

    //untuk mendapat jawaban yang dipilih
    public String getJawabanUser(int position) {
        return jawabanUser.get(position);
    }

    //untuk mendapat semua jawaban yang dipilih
    public HashMap<Integer, String> getJawabanUser() {
        return jawabanUser;
    }
}

