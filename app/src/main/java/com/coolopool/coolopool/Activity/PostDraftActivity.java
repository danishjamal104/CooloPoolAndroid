package com.coolopool.coolopool.Activity;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.coolopool.coolopool.Backend.Model.Blog;
import com.coolopool.coolopool.Backend.Model.Day;
import com.coolopool.coolopool.Interface.BlogUploadingListener;
import com.coolopool.coolopool.Interface.TaskCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolopool.coolopool.Adapter.NewDayAdapter;
import com.coolopool.coolopool.Class.NewDay;
import com.coolopool.coolopool.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.ArrayList;
import java.util.Collections;

public class PostDraftActivity extends AppCompatActivity {

    public static final int SELECT_PICTURE = 100;

    private String tripTitle, tripDescription, tripDate;

    CatLoadingView loadingView;

    DrawerLayout drawer;

    ArrayList<NewDay> days;
    NewDayAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    int noOfBlogs;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_draft);
        fetchNoOfBlogs();
        init();
        loadIntentData();
        setUpBackButton();
        setUpNavigationDrawer();
        setUpToolbar();
        setUpFabButton();

    }


    private void init() {
        loadingView = new CatLoadingView();
        mAuth = FirebaseAuth.getInstance();
        drawer = findViewById(R.id.drawer_layout);
        days = new ArrayList<>();
        adapter = new NewDayAdapter(days, this);
        recyclerView =  findViewById(R.id.post_draft_activity_content_holder_linearLayout);
        linearLayoutManager = new LinearLayoutManager(PostDraftActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
    }

    private void fetchNoOfBlogs(){
        SharedPreferences preferences = getSharedPreferences("user", 0);
        noOfBlogs =  preferences.getInt("noOfBlogs", -1);
        Toast.makeText(PostDraftActivity.this, "Pref value: "+noOfBlogs, Toast.LENGTH_SHORT).show();
    }

    public void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == SELECT_PICTURE) {
                if(data.getClipData() != null){
                    getMultipleSelectedPics(data);
                }
                adapter.addPhoto(Collections.singletonList(data.getData()));
            }
        }
    }

    private void getMultipleSelectedPics(Intent data){
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        ArrayList<String> imagesEncodedList = new ArrayList<String>();
        String imageEncoded;
        ClipData mClipData = data.getClipData();
        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
        for (int i = 0; i < mClipData.getItemCount(); i++) {

            ClipData.Item item = mClipData.getItemAt(i);
            Uri uri = item.getUri();
            mArrayUri.add(uri);
            // Get the cursor
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imageEncoded  = cursor.getString(columnIndex);
            imagesEncodedList.add(imageEncoded);
            cursor.close();
        }
        ArrayList<Uri> selectedPhotos = new ArrayList<>();
        for(int i=0; i<mArrayUri.size(); i++){
            if(mArrayUri.get(i) != null){
                selectedPhotos.add(mArrayUri.get(i));
            }
        }
        adapter.addPhoto(selectedPhotos);
    }

    private void setUpNavigationDrawer() {
        ImageView menuToggle = (ImageView) findViewById(R.id.post_draft_activity_toolbar_menu_opener_imageView);

        menuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        ((ImageView) findViewById(R.id.menu_item_icon_new_day_imageView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostDraftActivity.this, "New day", Toast.LENGTH_SHORT).show();
                adapter.addDays(new NewDay("", PostDraftActivity.this));
                adapter.notifyDataSetChanged();
            }
        });

        ((ImageView) findViewById(R.id.menu_item_photos_icon_imageView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostDraftActivity.this, "Photos", Toast.LENGTH_SHORT).show();
                pickImage();
            }
        });

    }

    private void setUpFabButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadBlog();
            }
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void loadIntentData() {
        Intent intent = getIntent();
        tripTitle = intent.getStringExtra("Title");
        tripDescription = intent.getStringExtra("Description");
        tripDate = intent.getStringExtra("Date");
        ((TextView) findViewById(R.id.post_draft_activity_toolbar_title_textView)).setText(tripTitle);
        ((TextView) findViewById(R.id.post_draft_activity_toolbar_date_textView)).setText(tripDate);

    }

    private void setUpBackButton() {
        ((ImageView) findViewById(R.id.post_draft_activity_toolbar_back_button_imageView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }


    private void uploadBlog(){
        loadingView.setText("Pics...");
        loadingView.show(getSupportFragmentManager(), "");
        loadingView.setCanceledOnTouchOutside(false);
        BlogUploadingTask blogUploadingTask = new BlogUploadingTask(new BlogUploadingListener() {
            @Override
            public void onSuccess(String blogId) {
                DaysPicUploadingTask daysPicUploadingTask = new DaysPicUploadingTask(new TaskCompleteListener<String>() {
                    @Override
                    public void onSuccess() {
                        TripUpdatingTask tripUpdatingTask = new TripUpdatingTask(new TaskCompleteListener<String>() {
                            @Override
                            public void onSuccess() {
                                loadingView.dismiss();
                                SharedPreferences preferences = getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("noOfBlogs", noOfBlogs+1);
                                editor.commit();
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                        tripUpdatingTask.execute();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                daysPicUploadingTask.execute(blogId);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        blogUploadingTask.execute(new Blog(mAuth.getUid(), tripTitle, tripDescription, tripDate, new ArrayList<String>(), new ArrayList<String>()));
    }


    public class DaysPicUploadingTask extends AsyncTask<String, Void, Void> {

        FirebaseAuth mAuth;

        TaskCompleteListener<String> mCallback;
        Exception mException;

        String blogId;

        ArrayList<ArrayList<String>> resultUrl;

        public DaysPicUploadingTask(TaskCompleteListener<String> mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        protected Void doInBackground(String... strings) {
            blogId = strings[0];
            final ArrayList<NewDay> newDays = adapter.getNewDays();
            ArrayList<String> description = getDescriptionOfEachDay();
            Log.d(">>>>>>>>>>>>>>>>> ", "doInBackground: size: " + newDays.get(0).getmImageUri().size());
            for(int i=0; i<newDays.size(); i++){
                if(newDays.get(i).getmImageUri().size() > 0){
                    FirebaseFirestore mRef = FirebaseFirestore.getInstance();
                    Day d = new Day(""+i, "TITLE", "<DEC>", new ArrayList<String>());
                    mRef.collection("blogs").document(blogId)
                            .collection("days").document("day"+i).set(d);
                    Log.d("---------------- ", "doInBackground: storing pic of day "+i);
                    storePicsOfSingleDay(newDays.get(i).getmImageUri(), i);
                    Log.d("---------------- ", "doInBackground: storing pic of day "+i+" completed");

                }

            }
            return null;
        }


        private ArrayList<String> getDescriptionOfEachDay(){
            ArrayList<String> result = new ArrayList<>();
            final int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            final int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()+1;
            for (int i = firstVisibleItemPosition; i < lastVisibleItemPosition; i++) {
                RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                String description = ((EditText)holder.itemView.findViewById(R.id.create_new_day_list_item_desc_editText)).getText().toString().trim();
                result.add(description);
            }
            return result;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingView.setText("Uploading...");
            loadingView.setCanceledOnTouchOutside(false);
            mAuth = FirebaseAuth.getInstance();
            resultUrl = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(">>>>>>>>>>>>>>>>>> ", "onPostExecute: result: " + resultUrl.size());
            if (mCallback != null) {
                if (mException == null) {
                    mCallback.onSuccess();
                } else {
                    mCallback.onFailure(mException);
                }
            }

        }

        private void storePicsOfSingleDay(ArrayList<Uri> uris, final int dayCounter){
            //Todo: create a day array and upload pics of all day in a nested loop
            StorageReference mRef = FirebaseStorage.getInstance().getReference()
                    .child("blogs/"+mAuth.getUid())
                    .child("blog"+noOfBlogs).child(""+dayCounter);
            for(int k=0; k<uris.size(); k++){
                if(uris.get(k) != null){
                    final StorageReference currentRef = mRef.child(""+k);
                    Task t = currentRef.putFile(uris.get(k));
                    t.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                currentRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        FirebaseFirestore mRef = FirebaseFirestore.getInstance();
                                        mRef.collection("blogs").document(blogId)
                                                .collection("days").document("day"+dayCounter)
                                                .update("images", FieldValue.arrayUnion(uri.toString()));
                                    }
                                });

                            }
                        }
                    });

                }

            }
        }

    }

    public class BlogUploadingTask extends AsyncTask<Blog, Void, String>{

        FirebaseFirestore mRef;

        BlogUploadingListener<String> mCallback;
        Exception mException;

        String id;

        public BlogUploadingTask(BlogUploadingListener callback) {
            mCallback = callback;
        }

        @Override
        protected String doInBackground(Blog... blogs) {
            mRef.collection("blogs").add(blogs[0]).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    id = documentReference.getId();
                    documentReference.update("timestamp", FieldValue.serverTimestamp());
                    mCallback.onSuccess(id);
                }
            });
            return id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingView.setText("Up Blog...");
            mRef = FirebaseFirestore.getInstance();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

    public class TripUpdatingTask extends AsyncTask<Void, Void, Void>{

        FirebaseFirestore db;

        TaskCompleteListener<String> mCallback;
        Exception mException;

        public TripUpdatingTask(TaskCompleteListener<String> mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db = FirebaseFirestore.getInstance();
            db.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference docRef = db.collection("users").document(mAuth.getUid());
                    DocumentSnapshot data = transaction.get(docRef);
                    Integer updateNoOfTripOrBlogs = data.getLong("noOfTrips").intValue() + 1;
                    transaction.update(docRef, "noOfTrips", updateNoOfTripOrBlogs);
                    return null;
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = FirebaseFirestore.getInstance();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mCallback != null && mException == null){
                mCallback.onSuccess();
            }
            mCallback.onFailure(mException);

        }
    }

}
