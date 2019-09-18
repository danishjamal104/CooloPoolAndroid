package com.coolopool.coolopool.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolopool.coolopool.Activity.FollowersActivity;
import com.coolopool.coolopool.Activity.NewPicPostActivity;
import com.coolopool.coolopool.Activity.NewPostActivity;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    View v;
    RecyclerView mTripList,mPhotoList;
    TextView  mAddPost, mAddPhoto, mSettingButton, mEditProfileButton;
    LinearLayout  mFollowButton;

    TextView noOfTrips, noOfFollowers, noOfPhotos;

    CircleImageView mProfileImage;

    FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

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

        ArrayList<Triplist> tripList = new ArrayList<>();

        tripList.add(new Triplist(R.drawable.trip1,"Goa","2"));
        tripList.add(new Triplist(R.drawable.trip2,"Sikkim","2"));
        tripList.add(new Triplist(R.drawable.trip1,"Goa","2"));
        tripList.add(new Triplist(R.drawable.trip2,"Sikkim","2"));
        tripList.add(new Triplist(R.drawable.trip1,"Goa","2"));
        tripList.add(new Triplist(R.drawable.trip2,"Sikkim","2"));

        mTripList = v.findViewById(R.id.Trip_RecyclerView);
        mTripList.setLayoutManager(new LinearLayoutManager(MyApplication.getAppContext(), LinearLayoutManager.HORIZONTAL, false));
        mTripList.setAdapter(new TripListAdapter(tripList,MyApplication.getAppContext()));

        // this is for Photo List View

        ArrayList<Photolist> photoList = new ArrayList<>();

        photoList.add(new Photolist(R.drawable.photo4,"Room","USA"));
        photoList.add(new Photolist(R.drawable.photo1,"Food","kathi junction"));
        photoList.add(new Photolist(R.drawable.photo5,"Night Room","London"));
        photoList.add(new Photolist(R.drawable.photo2,"Burger","burger king"));
        photoList.add(new Photolist(R.drawable.photo4,"Room","USA"));
        photoList.add(new Photolist(R.drawable.photo3,"BedRoom","OYO"));

        mPhotoList = v.findViewById(R.id.Photo_RecyclerView);
        mPhotoList.setLayoutManager(new LinearLayoutManager(MyApplication.getAppContext(), LinearLayoutManager.HORIZONTAL,false));
        mPhotoList.setAdapter(new PhotoListAdapter(photoList,MyApplication.getAppContext()));

        mFollowButton = v.findViewById(R.id.followbtn);
        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Log_test", "followers activity is started");
                Intent intent = new Intent(getActivity(), FollowersActivity.class);
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

    private void setUpBackEnd(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        FirebaseFirestore mRef = FirebaseFirestore.getInstance();

        storageReference.getReference("Users/profileImages/"+mAuth.getUid())
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
                            Log.d("::::::::::: ", "onEvent: "+userData.getNoOfPhotos());
                            noOfPhotos.setText(""+userData.getNoOfPhotos());
                            noOfFollowers.setText(""+userData.getNoOfFollowers());
                            noOfTrips.setText(""+userData.getNoOfTrips());
                        }
                    }
                });

    }

    public View getView() {
        return v;
    }

}
