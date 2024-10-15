package carousel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mybimo.R;

import java.util.ArrayList;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.ViewHolder> {

    private ArrayList<Materi> materiArrayList;
    private Context context;

    public MateriAdapter(Context context, ArrayList<Materi> materiArrayList) {
        this.context = context;
        this.materiArrayList = materiArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_materi_merge, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Materi materi = materiArrayList.get(position);
        holder.imageView.setImageResource(materi.getIcon());
        holder.textView.setText(materi.getNama());
    }

    @Override
    public int getItemCount() {
        return materiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon_image);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
