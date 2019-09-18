package com.coolopool.coolopool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.coolopool.coolopool.Adapter.FollowersListAdapter;
import com.coolopool.coolopool.Class.followList;
import com.coolopool.coolopool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FollowersActivity extends AppCompatActivity {

    RecyclerView mFollowerList;
    EditText mfollowersSearchBtn;

    ArrayList<followList> followersLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        mfollowersSearchBtn = findViewById(R.id.followersSearch);
        mfollowersSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfollowersSearchBtn.setFocusableInTouchMode(true);
            }
        });

        followersLists = new ArrayList<>();

        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));
        followersLists.add(new followList(R.drawable.userfacepic,"Leo","Rahul Singh"));

        mFollowerList = findViewById(R.id.Followers_RecyclerView);
        mFollowerList.setLayoutManager(new LinearLayoutManager(this));
        mFollowerList.setAdapter(new FollowersListAdapter(followersLists,this));
    }

    private void fetchUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mRef = FirebaseFirestore.getInstance();
        mRef.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });

    }
}
