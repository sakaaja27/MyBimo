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

//import carousel.MateriSub;

public class AdapterSubVocab extends RecyclerView.Adapter<AdapterSubVocab.ViewHolder>{
    private ArrayList<MateriSub> materiSubArrayList;
    private Context context;
    private AdapterSubVocab.OnClickListener onClickListener;

    public AdapterSubVocab(Context context, ArrayList<MateriSub> materiSubArrayList) {
        this.context = context;
        this.materiSubArrayList = materiSubArrayList;
    }
    public AdapterSubVocab(ArrayList<MateriSub> materiSubArrayList, Context context, AdapterSubVocab.OnClickListener onClickListener) {
        this.materiSubArrayList = materiSubArrayList;
        this.context = context;
        this.onClickListener = onClickListener;
    }
    @Override
    public AdapterSubVocab.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submateri_merge, parent, false);
        return new AdapterSubVocab.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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

        public void itemView(MateriSub materiSub, AdapterSubVocab.OnClickListener onClickListener) {
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
        void onClickListener(int subvocab);
    }
}
