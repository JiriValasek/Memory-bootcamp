package com.example.memorybootcamp;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent openMainActivity =  new Intent(SplashActivity.this, MainActivity.class);
            startActivity(openMainActivity);
            finish();
        }, 5000);

        ImageView splashImage = findViewById(R.id.imageViewSplash);
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.splash_fade_in);
        set.setTarget(splashImage);
        set.start();
    }
}