package com.coolopool.coolopool.Activity;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coolopool.coolopool.Adapter.NewDayAdapter;
import com.coolopool.coolopool.Backend.Model.Blog;
import com.coolopool.coolopool.Backend.Model.Day;
import com.coolopool.coolopool.Class.NewDay;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    String blogId;
    String userId;
    String imageUrl;
    Blog blog;
    ArrayList<Day> days;

    FirebaseFirestore mRef;
    FirebaseAuth mAuth;

    ImageView shareButton, profileImage;
    TextView userName, description, views, likes, experienced;
    LinearLayout likeLayout;

    RecyclerView daysRecyclerView;
    NewDayAdapter newDayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        NewDay.COUNT = 1;

        init();

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareActionProvider shareActionProvider = new ShareActionProvider(PostActivity.this);
                startActivity(createShareIntent());
            }
        });

        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like();
            }
        });



        final ImageView back = findViewById(R.id.post_activity_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }

    private void setUpRecyclerView(){

        ArrayList<Uri> images = new ArrayList<>();
        images.add(Uri.parse("http://i.dailymail.co.uk/i/pix/2015/09/01/18/2BE1E88B00000578-3218613-image-m-5_1441127035222.jpg"));
        images.add(Uri.parse("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRW4Nk37FHhnKN751J2Q_NkQyZbmyxcXrLc07zuNkMTc2dEW1tL"));
        images.add(Uri.parse("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR0okvrDXft-KUed0v_z14BvNzuKqgpCNIrdHh4m-xOQ9u0fQLa"));
        images.add(Uri.parse("https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832_960_720.jpg"));
        images.add(Uri.parse("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcy6Jw27D75372TBFKumrXhCIV6TA3ckCMJocWwng-V-eNdKkjwg"));


        newDayAdapter = new NewDayAdapter(new ArrayList<NewDay>(), this, 1);

        daysRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        daysRecyclerView.setHasFixedSize(false);

        daysRecyclerView.setAdapter(newDayAdapter);
    }


    private void init(){
        shareButton = findViewById(R.id.shareButton);
        profileImage = findViewById(R.id.post_activity_profileImage);
        getIntentData();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseFirestore.getInstance();
        description = findViewById(R.id.post_activity_description_textView);
        userName = findViewById(R.id.post_activity_userName_textView);
        views = findViewById(R.id.post_activity_views_count_textView);
        likes = findViewById(R.id.post_activity_starts_count_textView);
        experienced = findViewById(R.id.post_activity_followers_count_textView);
        daysRecyclerView = findViewById(R.id.post_activity_days_recyclerView);
        likeLayout = findViewById(R.id.like_linearLayout);
        updateViews();
        displayBlog();
        setUpRecyclerView();
    }

    private void displayBlog(){
        mRef.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userName.setText(documentSnapshot.getString("name"));
                experienced.setText(""+((ArrayList<String>) documentSnapshot.get("followers")).size());
            }
        });
        description.setText(blog.getDescription());
        views.setText(""+blog.getViews().size());
        likes.setText(""+blog.getLikes().size());
        setLiveData();
        getBlog();
    }

    private void updateViews(){
        mRef.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference docRef = mRef.collection("blogs").document(blogId);
                transaction.update(docRef, "views", FieldValue.arrayUnion(mAuth.getUid()));
                return null;
            }
        });
    }

    private void like(){
        mRef.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference docRef = mRef.collection("blogs").document(blogId);
                transaction.update(docRef, "likes", FieldValue.arrayUnion(mAuth.getUid()));
                return null;
            }
        });
    }

    private void setLiveData(){
        getLiveLikes();
        getLiveViews();
        getLiveTrips();
    }

    private void getLiveViews(){
        mRef.collection("blogs").document(blogId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                ArrayList<String> viewers = (ArrayList<String>) documentSnapshot.get("views");
                views.setText(""+viewers.size());
            }
        });
    }

    private void getLiveLikes(){
        mRef.collection("blogs").document(blogId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                ArrayList<String> likers = (ArrayList<String>) documentSnapshot.get("likes");
                likes.setText(""+likers.size());
            }
        });
    }

    private void getLiveTrips(){
        mRef.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                int tripsCount = documentSnapshot.getDouble("noOfTrips").intValue();
                experienced.setText(""+tripsCount);
            }
        });
    }

    private void getIntentData(){
        Intent intent = getIntent();
        blogId = intent.getStringExtra("BLOG_ID");
        userId = intent.getStringExtra("USER_ID");
        blog = (Blog) intent.getSerializableExtra("BLOG");
        imageUrl = intent.getStringExtra("IMAGE");
        if(imageUrl.equals("") || imageUrl==null){
            FirebaseStorage storage = FirebaseStorage.getInstance();

            storage.getReference("Users/profileImages/"+userId).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri uri = task.getResult();
                    imageUrl = uri.toString();
                    Picasso.get().load(imageUrl).fit().into(profileImage);
                }
            });
        }else{
            Picasso.get().load(imageUrl).fit().into(profileImage);
        }
        Log.d(":::::::::::::::", "getIntentData: "+userId);
    }

    private void getBlog(){
        mRef.collection("blogs").document(blogId).collection("days").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot document: documents){
                    Day currentDay = document.toObject(Day.class);
                    ArrayList<Uri> images = new ArrayList<>();
                    for(String url: currentDay.getImages()){
                        images.add(Uri.parse(url));
                    }
                    NewDay currentNewDay = new NewDay(currentDay.getdescription(), PostActivity.this, images);
                    newDayAdapter.addDays(currentNewDay);
                    newDayAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private Intent createShareIntent(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ruby.bastardsbook.com/chapters/html-parsing/"));
        return intent;
    }
}
