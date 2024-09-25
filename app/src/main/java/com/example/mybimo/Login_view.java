package com.example.mybimo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login_view extends AppCompatActivity {

    public ImageView splashImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_view);


        splashImageView =  findViewById(R.id.animationmybimo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animasi);
        splashImageView.startAnimation(animation);
        View btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_view.this, Dash_view.class);
                startActivity(intent);
                finish();
            }
        });

        //        pindah ke regis
        View register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_view.this, Register_view.class);
                startActivity(intent);
                finish();
            }
        });
    }
}