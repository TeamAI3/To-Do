package com.example.to_do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private static final int SPLASH = 5000;
    Animation topAnim, bottomAnim;
    ImageView imageView;
     TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
     bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        imageView = findViewById(R.id.imageView);
        appName = findViewById(R.id.appName);

        imageView.setAnimation(topAnim);
        appName.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH);
    }
}