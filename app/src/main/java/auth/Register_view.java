package auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybimo.R;

public class Register_view extends AppCompatActivity {
    private ImageView splashImage;
    TextView login;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_view);

        View login = findViewById(R.id.login);
        splashImage = findViewById(R.id.mybimo_font);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animasi);
        splashImage.startAnimation(animation);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_view.this, Login_view.class);
                startActivity(intent);
                finish();
            }
        });

    }
}