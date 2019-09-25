package com.coolopool.coolopool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.coolopool.coolopool.Adapter.FollowersListAdapter;
import com.coolopool.coolopool.Backend.Model.User;
import com.coolopool.coolopool.Class.followList;
import com.coolopool.coolopool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FollowersActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView mFollowerList;
    EditText mfollowersSearchBtn;

    TextView noOfFollowers, username;

    FollowersListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        swipeRefreshLayout = findViewById(R.id.followers_refresh);

        noOfFollowers = findViewById(R.id.no_of_followers);
        username = findViewById(R.id.Username);

        mfollowersSearchBtn = findViewById(R.id.followersSearch);
        mfollowersSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfollowersSearchBtn.setFocusableInTouchMode(true);
            }
        });

        mfollowersSearchBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.filterFollowers(editable.toString());
                if(editable.toString().equals("")){
                    fetchUser();
                }
            }
        });


        adapter = new FollowersListAdapter(new ArrayList<followList>(),this);

        mFollowerList = findViewById(R.id.Followers_RecyclerView);
        mFollowerList.setLayoutManager(new LinearLayoutManager(this));
        mFollowerList.setAdapter(adapter);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchUser();
            }
        });

        getIntentData();
        fetchUser();

    }

    private void getIntentData(){
        Intent intent = getIntent();
        noOfFollowers.setText(intent.getStringExtra("FOLLOWERS"));
        (FirebaseFirestore.getInstance()).collection("users")
                .document(FirebaseAuth.getInstance().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                String[] a = user.getUsername().split("@");
                username.setText(a[0]);
            }
        });
    }

    private void fetchUser(){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore mRef = FirebaseFirestore.getInstance();
        adapter.resetFollowers();

        mRef.collection("users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> followersUid = (ArrayList<String>) documentSnapshot.get("followers");
                for(final String uid: followersUid){
                    mRef.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            final User currentUser = documentSnapshot.toObject(User.class);
                            FirebaseStorage storageReference = FirebaseStorage.getInstance();

                            storageReference.getReference("Users/profileImages/"+currentUser.getUid())
                                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    followList followList = new followList(uri.toString(), currentUser.getName(), currentUser.getUsername(), currentUser.getUid());
                                    adapter.addFollowers(followList);
                                }
                            });
                        }
                    });
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
