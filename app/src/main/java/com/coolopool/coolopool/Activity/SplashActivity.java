package com.coolopool.coolopool.Activity;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coolopool.coolopool.R;
import com.coolopool.coolopool.Storage.SharedPrefManager;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ImageView mLogo;
    private Context context = SplashActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mLogo = findViewById(R.id.Logo);

        // for Image in Logo

        Glide
                .with(context)
                .load(R.drawable.logo)
                .into(mLogo);

        new Timer().schedule(new TimerTask() {
            public void run() {
                Log.d("Log_test", "Login screen open");
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }


    }