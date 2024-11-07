package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybimo.R;

import java.util.ArrayList;


import carousel.MateriSub;

public class AdapterSubGrammer extends RecyclerView.Adapter<AdapterSubGrammer.ViewHolder> {

    private ArrayList<MateriSub> materiSubArrayList;
    private Context context;
    private AdapterSubGrammer.OnClickListener onClickListener;

    public AdapterSubGrammer(Context context, ArrayList<MateriSub> materiSubArrayList) {
        this.context = context;
        this.materiSubArrayList = materiSubArrayList;
    }
    public AdapterSubGrammer(ArrayList<MateriSub> materiSubArrayList, Context context, OnClickListener onClickListener) {
        this.materiSubArrayList = materiSubArrayList;
        this.context = context;
        this.onClickListener = onClickListener;
    }
    @Override
    public AdapterSubGrammer.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submateri_merge, parent, false);
        return new AdapterSubGrammer.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSubGrammer.ViewHolder holder, int position) {
        MateriSub materi = materiSubArrayList.get(position);
        holder.textView.setText(materi.getNama());
        holder.itemView(materiSubArrayList.get(position),onClickListener);

    }

    @Override
    public int getItemCount() {
        return materiSubArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);

        }
        // buat click listener materi di dashboard

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
        void onClickListener(int subgrammer);
    }
}
