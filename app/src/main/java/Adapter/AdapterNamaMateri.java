package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mybimo.R;

import java.util.List;

public class AdapterNamaMateri extends BaseAdapter {
    private Activity activity;
    private List<GetNamaMateri> getListMateri;
    private View itemView;

    public AdapterNamaMateri(Activity activity, List<GetNamaMateri> getListMateri) {
        this.activity = activity;
        this.getListMateri = getListMateri;
    }

    @Override
    public int getCount() {
        return getListMateri.size();
    }

    @Override
    public Object getItem(int position) {
        return getListMateri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView judulMateri = itemView.findViewById(R.id.sub_materi);
//        GetNamaMateri getNamaMateri = getListMateri.get(position);




        return itemView;
    }
}
