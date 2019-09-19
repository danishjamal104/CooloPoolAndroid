package com.coolopool.coolopool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.coolopool.coolopool.R;

public class ProfileActivity extends AppCompatActivity {

    String blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getIntentData();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        blogId = intent.getStringExtra("BLOG_ID");
        Toast.makeText(ProfileActivity.this, blogId, Toast.LENGTH_SHORT).show();
    }
}
