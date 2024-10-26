package auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mybimo.Main;

import java.util.HashMap;

public class Preference {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "User Login";
    private static final String LOGIN = "isUser LoggedIn";
    private static final String NAME = "username";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";

    public Preference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void createUserSession(String username, String email, String phone) {
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, username);
        editor.putString(EMAIL, email);
        editor.putString(PHONE, phone);
        editor.apply();
    }

    public void checkLogin() {
        if (!this.isUserLoggedIn()) {
            Intent intent = new Intent(context, Login_view.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            if (context instanceof Main) {
                ((Main) context).finish();
            }
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PHONE, sharedPreferences.getString(PHONE, null));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, Login_view.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Main) {
            ((Main) context).finish();
        }
    }
}
