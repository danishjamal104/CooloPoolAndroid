package com.coolopool.coolopool.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.coolopool.coolopool.R;

import java.util.Timer;
import java.util.TimerTask;

public class WhatsHotActivity extends AppCompatActivity {

    AppCompatImageView imageView;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_hot);

        imageView = findViewById(R.id.whats_hot_imageView);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.what_hot_animation);
        imageView.setAnimation(animation);

        splash();


    }

    private void splash(){
        new Timer().schedule(new TimerTask() {
            public void run() {
                switch (flag){
                    case 0:
                        imageView.setImageResource(R.drawable.whats_hot_2);
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.whats_hot_3);
                        break;
                    case 3:
                        Intent intent = new Intent(WhatsHotActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;
                }
                flag++;
                splash();
            }
        }, 2000);
    }
}
