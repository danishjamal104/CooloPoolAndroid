package com.coolopool.coolopool.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.coolopool.coolopool.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    TextView mProfileButton, mPrivacyButton, mAboutButton, mLogoutButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();

        mProfileButton = findViewById(R.id.profilebtn);
        mLogoutButton = findViewById(R.id.logoutBtn);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                finish();
            }
        });

        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(SettingActivity.this, ProfileSettingActivity.class);
                startActivity(intent);
            }
        });

        /*
        (findViewById(R.id.setting_activity_wallet_holder_linearLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, WalletActivity.class));
            }
        });
        */



    }

}
