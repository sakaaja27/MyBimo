package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mybimo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class AdapterUpdateProfile extends BaseAdapter {
    private Activity activity;
    private List<GetUser> getUserList;
    private LayoutInflater inflater;
    private TextInputEditText username;
    private TextInputLayout usernamelay;
    private TextInputEditText email;
    private TextInputLayout emaillay;
    private TextInputEditText phone;
    private TextInputLayout phonelay;

    public AdapterUpdateProfile(Activity activity, List<GetUser> getUserList){
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
        if (convertView == null) convertView = inflater.inflate(R.layout.lay_editprofile, null);

        usernamelay = convertView.findViewById(R.id.username);
        username = (TextInputEditText) usernamelay.getEditText();

        emaillay = convertView.findViewById(R.id.email);
        email = (TextInputEditText) emaillay.getEditText();

        phonelay = convertView.findViewById(R.id.phone);
        phone = (TextInputEditText) phonelay.getEditText();

        GetUser user = getUserList.get(position);
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());

        return convertView;
    }
}
