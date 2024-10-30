package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybimo.R;

import java.util.ArrayList;

public class AdapterSubGrammer extends RecyclerView.Adapter<AdapterSubGrammer.ViewHolder> {

    private ArrayList<MateriSub> materiSubArrayList;
    private Context context;
    private OnClickListener onClickListener;

    //buat parameter dan isi value dari private inisiasi var tdi dengan value parameter
    public AdapterSubGrammer(Context context, ArrayList<MateriSub> materiSubArrayList) {
        this.context = context;
        this.materiSubArrayList = materiSubArrayList;
    }
    //buat parameter dan isi value dari private inisiasi var tdi dengan value parameter
    public AdapterSubGrammer(ArrayList<MateriSub> materiSubArrayList, Context context, OnClickListener onClickListener) {
        this.materiSubArrayList = materiSubArrayList;
        this.context = context;
        this.onClickListener = onClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        adapter hmpir sama yg wktu itu kukirim
//        kujelasin yg penting aja,OncreateViewHolder ini fungsinya buat ngebuat viewholder
//        yang dimana ngambil layout sub materi yang tdi udah dibuat di xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submaterigrammar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSubGrammer.ViewHolder holder, int position) {
//        Fungsinya onBindViewHolder ini untuk inisiasi data dari arraylist ke viewholder
//        yang nantinya agar bisa onclick di arraylist recyclermu
        MateriSub materiSub = materiSubArrayList.get(position);
        holder.textView.setText(materiSub.getNama());
        holder.itemView(materiSubArrayList.get(position),onClickListener);
        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null){
                onClickListener.onClickListener(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return materiSubArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //buat inisiasi var apa yg kmu perlukan buat ditampilin di recycle contoh
//        aku kan tdi di submateri xml itu mo nampilin image sama text nah jadi idnya itu dimasukkin disini
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_sub);
            textView = itemView.findViewById(R.id.text_subgrammar);
        }

        //buat method untuk onclick
        public void itemView(MateriSub materiSub, OnClickListener onClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClickListener(getAdapterPosition());
                }
            });
        }
    }
    //interface clicklistener
    public interface OnClickListener{
        void onClickListener(int subgrammar);
    }
}
