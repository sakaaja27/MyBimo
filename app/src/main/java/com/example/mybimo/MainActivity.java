package com.example.mybimo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;
    private ImageView splashImageView;
    private TextView splashImageView1,splashImageView2,splashImageView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_view);

        splashImageView = findViewById(R.id.splash_animation);
        splashImageView1 = findViewById(R.id.mybimo_ani1);
        splashImageView2 = findViewById(R.id.mybimo_ani2);
        splashImageView3 = findViewById(R.id.mybimo_ani3);

//        animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animasi);
        splashImageView.startAnimation(animation);
        Animation animation_text = AnimationUtils.loadAnimation(this, R.anim.animasi);
        splashImageView1.startAnimation(animation_text);
        Animation animation_text1 = AnimationUtils.loadAnimation(this, R.anim.animasi);
        splashImageView2.startAnimation(animation_text1);
        Animation animation_text2 = AnimationUtils.loadAnimation(this, R.anim.animasi);
        splashImageView3.startAnimation(animation_text2);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this, Login_view.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}