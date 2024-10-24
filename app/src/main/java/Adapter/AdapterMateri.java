package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mybimo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import materi.Idiom;

public class AdapterMateri extends BaseAdapter {
    private Activity activity;
    private Context context;
    private List<GetMateri> getMateriList;
    private View itemView;

    public AdapterMateri(Context context, List<GetMateri> getMateriList) {
        this.context = context;
        this.getMateriList = getMateriList;
    }

    @Override
    public int getCount() {
        return getMateriList.size();
    }

    @Override
    public Object getItem(int position) {
        return getMateriList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(getMateriList.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView sub_materi = itemView.findViewById(R.id.sub_materi);
        GetMateri getMateri = getMateriList.get(position);


        sub_materi.setText("PDF " + getMateri.getId());


        return itemView;
    }
}
