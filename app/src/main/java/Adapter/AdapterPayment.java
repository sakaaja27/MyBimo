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

public class AdapterPayment extends BaseAdapter {
    private Activity activity;
    private List<GetPayment> getPaymentList;
    private LayoutInflater inflater;

    public AdapterPayment(Activity activity, List<GetPayment> getPaymentList){
        this.activity = activity;
        this.getPaymentList = getPaymentList;
    }
    @Override
    public int getCount() {
        return getPaymentList.size();
    }

    @Override
    public Object getItem(int position) {
        return getPaymentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) convertView = inflater.inflate(R.layout.fragment_payment, null);

        TextView nama_pembayaran = convertView.findViewById(R.id.nama_pembayaran);
        TextView harga = convertView.findViewById(R.id.harga);
        TextView nomor_bank = convertView.findViewById(R.id.nomor_bank);
        GetPayment payment = getPaymentList.get(position);
        nama_pembayaran.setText(payment.getNama_pembayaran());
        harga.setText(payment.getHarga());
        nomor_bank.setText(payment.getNomor_bank());

        return convertView;
    }
}
