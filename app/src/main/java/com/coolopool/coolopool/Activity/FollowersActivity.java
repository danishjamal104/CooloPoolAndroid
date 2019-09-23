package com.coolopool.coolopool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

    RecyclerView mFollowerList;
    EditText mfollowersSearchBtn;

    FollowersListAdapter adapter;


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





        adapter = new FollowersListAdapter(new ArrayList<followList>(),this);

        mFollowerList = findViewById(R.id.Followers_RecyclerView);
        mFollowerList.setLayoutManager(new LinearLayoutManager(this));
        mFollowerList.setAdapter(adapter);

        fetchUser();
    }

    private void fetchUser(){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore mRef = FirebaseFirestore.getInstance();
        mRef.collection("users").document(mAuth.getUid()).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> followersUid = (ArrayList<String>) documentSnapshot.get("followers");
                for(final String uid: followersUid){
                    adapter.resetFollowers();
                    mRef.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            final User currentUser = documentSnapshot.toObject(User.class);

                            FirebaseStorage storageReference = FirebaseStorage.getInstance();

                            storageReference.getReference("Users/profileImages/"+currentUser.getUid())
                                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    followList followList = new followList(uri.toString(), currentUser.getName(), currentUser.getUsername());
                                    adapter.addFollowers(followList);
                                }
                            });
                        }
                    });
                }
            }
        });


    }
}
