package com.coolopool.coolopool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coolopool.coolopool.Adapter.PhotoListAdapter;
import com.coolopool.coolopool.Adapter.TripListAdapter;
import com.coolopool.coolopool.Application.MyApplication;
import com.coolopool.coolopool.Backend.Model.User;
import com.coolopool.coolopool.Class.Triplist;
import com.coolopool.coolopool.Fragments.ProfileFragment;
import com.coolopool.coolopool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    String blogId;
    String userId;

    TextView followButton;

    TextView trips, followers, pics;

    CircleImageView mProfileImage;

    RecyclerView tripList, photoList;

    public PhotoListAdapter photoAdapter;
    public TripListAdapter tripAdapter;

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
        mRef.collection("users").document(userId).update("followers", FieldValue.arrayRemove(mAuth.getUid())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRef.runTransaction(new Transaction.Function<Void>() {
                    @androidx.annotation.Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentReference docRef = mRef.collection("users").document(userId);
                        DocumentSnapshot data = transaction.get(docRef);
                        Integer updateNoOfFollowers = data.getLong("noOfFollowers").intValue() - 1;
                        transaction.update(docRef, "noOfFollowers", updateNoOfFollowers);
                        return null;
                    }
                });
            }
        });
    }

    private void follow(){
        if(userId.equals(mAuth.getUid())){
            return;
        }
        mRef.collection("users").document(userId).update("followers", FieldValue.arrayUnion(mAuth.getUid())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRef.runTransaction(new Transaction.Function<Void>() {
                    @androidx.annotation.Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentReference docRef = mRef.collection("users").document(userId);
                        DocumentSnapshot data = transaction.get(docRef);
                        Integer updateNoOfFollowers = data.getLong("noOfFollowers").intValue() + 1;
                        transaction.update(docRef, "noOfFollowers", updateNoOfFollowers);
                        return null;
                    }
                });
            }
        });
    }

    private void init(){
        getIntentData();
        mRef = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mProfileImage = findViewById(R.id.profile_profile_image);
        trips = findViewById(R.id.others_live_trips);
        followers = findViewById(R.id.others_live_followers);
        pics = findViewById(R.id.others_live_photos);
        followButton = findViewById(R.id.followProfileButton);
        tripList = findViewById(R.id.trip_RecyclerView);
        photoList = findViewById(R.id.photo_RecyclerView);
        setUpTrips();
        setUpPhotos();
        setUpBackend();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        blogId = intent.getStringExtra("BLOG_ID");
        userId = intent.getStringExtra("USER_ID");
    }

    private void setUpTrips(){
        tripAdapter = new TripListAdapter(new ArrayList<Triplist>() , this);
        tripList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tripList.setAdapter(tripAdapter);
    }

    private void setUpPhotos(){
        photoAdapter = new PhotoListAdapter(new ArrayList<String>(), this);
        photoList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        photoList.setHasFixedSize(false);
        photoList.setAdapter(photoAdapter);
    }

    private void setUpBackend(){
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        FirebaseFirestore mRef = FirebaseFirestore.getInstance();

        storageReference.getReference("Users/profileImages/"+userId)
                .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Picasso.get().load(task.getResult().toString()).fit().into(mProfileImage);
                }
            }
        });

        mRef.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(snapshot != null && snapshot.exists()){
                    User userData = snapshot.toObject(User.class);
                    Log.d("::::::::::: ", "onEvent: "+userData.getNoOfPhotos());
                    pics.setText(""+userData.getNoOfPhotos());
                    followers.setText(""+userData.getNoOfFollowers());
                    trips.setText(""+userData.getNoOfTrips());
                }
            }
        });

        FetchData task = new FetchData();
        task.execute();

    }

    public class FetchData extends AsyncTask<Void, Void, ArrayList<ArrayList<String>>> {

        FirebaseFirestore mRef;
        FirebaseStorage mStorage;

        ArrayList<ArrayList<String>> result = new ArrayList<>();

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(Void... voids) {

            Query query = mRef.collection("blogs").whereEqualTo("id", userId);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    Log.d("Documents ", "onPostExecute: "+documents.size());
                    for(DocumentSnapshot documentSnapshot: documents){

                        mRef.collection("blogs").document(documentSnapshot.getId())
                                .collection("days").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> days = queryDocumentSnapshots.getDocuments();
                                ArrayList<String> blogPics = new ArrayList<>();
                                for(DocumentSnapshot currentDay: days){
                                    blogPics.addAll((List<String>)currentDay.get("images"));
                                }
                                result.add(blogPics);
                                photoAdapter.addAllUrl(blogPics);
                                photoAdapter.notifyDataSetChanged();

                                if(blogPics.size() > 0){
                                    tripAdapter.addTrip(new Triplist(blogPics.get(0),"Place", "2"));
                                    tripAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            });


            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRef = FirebaseFirestore.getInstance();
            mStorage = FirebaseStorage.getInstance();
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists) {
            super.onPostExecute(arrayLists);

        }
    }
}
