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

public class AdapterUser extends BaseAdapter {
    private Activity activity;
    private List<GetUser> getUserList;
    private LayoutInflater inflater;

    public AdapterUser(Activity activity, List<GetUser> getUserList){
        this.activity = activity;
        this.getUserList = getUserList;
        this.inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return getUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return getUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) convertView = inflater.inflate(R.layout.fragment_dashboard, null);

        TextView username = convertView.findViewById(R.id.auth_name);
//        TextView email = convertView.findViewById(R.id.harga);
//        TextView phone = convertView.findViewById(R.id.nomor_bank);
//        TextView password = convertView.findViewById(R.id.password);
        GetUser user = getUserList.get(position);
        username.setText(user.getUsername());
//        harga.setText(user.getHarga());
//        nomor_bank.setText(user.getNomor_bank());

        return convertView;
    }
}
