package com.coolopool.coolopool.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coolopool.coolopool.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    String blogId;
    String userId;

    TextView followButton;

    FirebaseAuth mAuth;
    FirebaseFirestore mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        setUpFollow();

    }

    private void setOnClickFollowButton(int type){
        // type 1: follow type 2: unfollow
        if(type == 1){
            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    follow();
                    setUpFollow();
                }
            });
            return;
        }
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unFollow();
                setUpFollow();
            }
        });


    }

    private void setUpFollow(){
       mRef.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               List<String> followers = (List<String>) documentSnapshot.get("followers");
               if(followers.contains(mAuth.getUid())){
                   followButton.setText("Unfollow");
                   setOnClickFollowButton(0);
               }else {
                   followButton.setText("Follow");
                   setOnClickFollowButton(1);
               }

           }
       });
    }

    private void unFollow(){
        mRef.collection("users").document(userId).update("followers", FieldValue.arrayRemove(mAuth.getUid()));
    }

    private void follow(){
        if(userId.equals(mAuth.getUid())){
            return;
        }
        mRef.collection("users").document(userId).update("followers", FieldValue.arrayUnion(mAuth.getUid()));
    }

    private void init(){
        getIntentData();
        mRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        followButton = findViewById(R.id.followProfileButton);
    }

    private void getIntentData(){
        Intent intent = getIntent();
        blogId = intent.getStringExtra("BLOG_ID");
        userId = intent.getStringExtra("USER_ID");
        Toast.makeText(ProfileActivity.this, blogId, Toast.LENGTH_SHORT).show();
    }
}
