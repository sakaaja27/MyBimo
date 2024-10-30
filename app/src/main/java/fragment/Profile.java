package fragment;

import static android.content.Intent.getIntent;
import static auth.DB_Contract.ip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Adapter.GetUser;
import  Profile.editprofile;
import auth.Login_view;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybimo.Main;
import com.example.mybimo.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    private AppCompatButton btn_edit;
    private AppCompatButton btn_reset;
    private AppCompatButton btn_signout;
    private View view;
    private static String UserId;
    private TextView auth_name;
    private ShapeableImageView image_profil;
    private TextView email;
    List<GetUser> getUserList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_profile, container, false);

        btn_edit = view.findViewById(R.id.btn_editprofile);
        btn_reset = view.findViewById(R.id.btn_reset);
        btn_signout = view.findViewById(R.id.sign_out);
        auth_name = view.findViewById(R.id.auth_name);
        image_profil = view.findViewById(R.id.image_profile);
        email = view.findViewById(R.id.email);



        // Ambil id pengguna dari variable statis di Main
        UserId = Main.RequestUserId;

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), editprofile.class);
                startActivity(intent);

            }
        });


        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //buat agar sign out kembali ke halaman login
                Intent intent = new Intent(getActivity(), Login_view.class);
                startActivity(intent);
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });
        // Fetch data
        if (UserId != null) {
            fetchUser (UserId); // Panggil fetchUser  dengan UserId
        } else {
            Toast.makeText(getContext(), "User  ID tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
        return view;

    }

    private void fetchUser (String UserId) {
        String URL = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getUser.php?id=" + UserId;
        Log.d("API URL", URL); // Log URL untuk debugging

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                JSONObject jsonObject = response.getJSONObject(0);
                                GetUser  user = new GetUser (
                                        jsonObject.getString("id"),
                                        jsonObject.getString("username"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("phone"),
                                        jsonObject.getString("upload_image"),
                                        jsonObject.getString("password")
                                );

                                if (view != null) {
                                    auth_name.setText(user.getUsername());
                                    email.setText(user.getEmail());

                                    String imageUrl = "http://" + ip +"/website%20mybimo/mybimo/src/getData/"+user.getUploadImage();
                                    System.out.println("IMAGE"+imageUrl);
                                    Log.d("", imageUrl); // Log URL gambar
                                    if (imageUrl != null && !imageUrl.isEmpty()) {
                                        loadImage(imageUrl);
                                    } else {
                                        Toast.makeText(getContext(), "URL gambar tidak valid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (isAdded()) {
                                    Toast.makeText(getContext(), "Tidak ada data yang tersedia", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (isAdded()) {
                                Toast.makeText(getContext(), "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (isAdded()) {
                            Log.e("Volley Error", error.toString()); // Log kesalahan
                            Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Tambahkan request ke queue
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
    }

    private void loadImage(String imageUrl) {
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // nampilkan gambar di ImageView
                        System.out.println("Stringresponse"+response);
                        image_profil.setImageBitmap(response);


                    }
                },
                0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("Image Load Error", "Error Code: " + error.networkResponse.statusCode);
                        }
//                        tampilkan gambar drawable default
                        image_profil.setImageResource(R.drawable.icon_profile);
                    }
                });

        // Tambahkan request ke queue
        Volley.newRequestQueue(requireContext()).add(imageRequest);
    }

    private void showChangePasswordDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.lay_modalresetpass, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate the custom title layout
        View titleView = inflater.inflate(R.layout.dialog_title, null);
        builder.setCustomTitle(titleView) // Set the custom title
                .setView(dialogView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextInputEditText oldPassword = dialogView.findViewById(R.id.old_password);
                        TextInputEditText newPassword = dialogView.findViewById(R.id.new_password);
                        TextInputEditText confirmPassword = dialogView.findViewById(R.id.cofirm_password); // Fixed typo in ID
                        String oldPass = oldPassword.getText().toString().trim();
                        String newPass = newPassword.getText().toString().trim();
                        String confirmPass = confirmPassword.getText().toString().trim();

                        // Input validation
                        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                            massage("Fields are empty");
                            return; // Early exit
                        }
                        if (!newPass.equals(confirmPass)) {
                            massage("New password and confirm password do not match");
                            return; // Early exit
                        }

                        String URL = "http://" + ip + "/website%20mybimo/mybimo/src/getData/getPassword.php?id=" + UserId; // Use HTTPS

                        // Create request for password reset
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("Response", response);
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            String message = jsonResponse.getString("message");
                                            massage(message); // Show message from server
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            massage("Error parsing response: " + e.getMessage());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        massage("Error: " + error.getMessage());
                                        System.out.println(error.getMessage());
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("id", UserId);
                                params.put("old_password", oldPass);
                                params.put("new_password", newPass);
                                params.put("confirm_password", confirmPass);
                                return params;
                            }
                        };

                        // Add request to the queue
                        Volley.newRequestQueue(requireContext()).add(stringRequest);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Close dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Set dialog size
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // Full width
        params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5); // Half screen height
        dialog.getWindow().setAttributes(params);
    }

    public void massage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}


