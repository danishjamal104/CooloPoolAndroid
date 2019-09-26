package com.coolopool.coolopool.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolopool.coolopool.Activity.FollowersActivity;
import com.coolopool.coolopool.Activity.NewPicPostActivity;
import com.coolopool.coolopool.Activity.NewPostActivity;
import com.coolopool.coolopool.Activity.ProfileActivity;
import com.coolopool.coolopool.Activity.ProfileSettingActivity;
import com.coolopool.coolopool.Activity.SettingActivity;
import com.coolopool.coolopool.Adapter.PhotoListAdapter;
import com.coolopool.coolopool.Adapter.TripListAdapter;
import com.coolopool.coolopool.Application.MyApplication;
import com.coolopool.coolopool.Backend.Model.User;
import com.coolopool.coolopool.Class.Photolist;
import com.coolopool.coolopool.Class.Triplist;
import com.coolopool.coolopool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    View v;
    RecyclerView mTripList,mPhotoList;
    TextView  mAddPost, mAddPhoto, mSettingButton, mEditProfileButton;
    LinearLayout  mFollowButton;

    public PhotoListAdapter photoAdapter;
    public TripListAdapter tripAdapter;

    TextView noOfTrips, noOfFollowers, noOfPhotos;

    CircleImageView mProfileImage;

    FirebaseAuth mAuth;
    FirebaseFirestore mRef;
    FirebaseStorage mStorage;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();

        v =  inflater.inflate(R.layout.fragment_profile, container, false);

        mProfileImage = v.findViewById(R.id.profile_profile_image);

        noOfTrips = v.findViewById(R.id.live_trips);
        noOfFollowers = v.findViewById(R.id.live_followers);
        noOfPhotos = v.findViewById(R.id.live_photos);

        //for editing profile
        mEditProfileButton = v.findViewById(R.id.editProfileButton);
        mEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileSettingActivity.class));
            }
        });

        //for Setting
        mSettingButton = v.findViewById(R.id.settingbtn);
        mSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        // this is for Trip List View


        mTripList = v.findViewById(R.id.Trip_RecyclerView);
        setUpTrips();

        // this is for Photo List View

        mPhotoList = v.findViewById(R.id.Photo_RecyclerView);
        setUpPhotos();


        mFollowButton = v.findViewById(R.id.followbtn);
        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log_test", "followers activity is started");
                Intent intent = new Intent(getActivity(), FollowersActivity.class);
                intent.putExtra("FOLLOWERS", noOfFollowers.getText().toString());
                startActivity(intent);
            }
        });


        mAddPhoto = v.findViewById(R.id.addPhoto);
        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log_test","Add New photo is open");
                Intent intent = new Intent(getActivity(), NewPicPostActivity.class);
                startActivity(intent);
            }
        });

        mAddPost = v.findViewById(R.id.addTrip);
        mAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log_test","Add New photo is open");
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(intent);
            }
        });

        setUpBackEnd();

        return v;
    }

    private void setUpTrips(){
        tripAdapter = new TripListAdapter(new ArrayList<Triplist>() , getActivity());
        mTripList.setLayoutManager(new LinearLayoutManager(MyApplication.getAppContext(), LinearLayoutManager.HORIZONTAL, false));
        mTripList.setAdapter(tripAdapter);
    }

    private void setUpPhotos(){
        photoAdapter = new PhotoListAdapter(new ArrayList<String>(), getActivity());
        mPhotoList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mPhotoList.setHasFixedSize(false);
        mPhotoList.setAdapter(photoAdapter);
    }

    private void setUpBackEnd(){

        mStorage.getReference("Users/profileImages/"+mAuth.getUid())
                .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Picasso.get().load(task.getResult().toString()).fit().into(mProfileImage);
                }
            }
        });

        mRef.collection("users").document(mAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if(snapshot != null && snapshot.exists()){
                            User userData = snapshot.toObject(User.class);
                            noOfPhotos.setText(""+userData.getNoOfPhotos());
                            noOfFollowers.setText(""+userData.getFollowers().size());
                            noOfTrips.setText(""+userData.getNoOfTrips());
                        }
                    }
                });
        FetchData fetchData = new FetchData();
        fetchData.execute();

    }


    public View getView() {
        return v;
    }

    public class FetchData extends AsyncTask<Void, Void, ArrayList<ArrayList<String>>>{


        ArrayList<ArrayList<String>> result = new ArrayList<>();

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(Void... voids) {

            Query query = mRef.collection("blogs").whereEqualTo("id", mAuth.getUid());

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
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists) {
            super.onPostExecute(arrayLists);

        }
    }

}
