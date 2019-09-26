package com.coolopool.coolopool.Activity;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolopool.coolopool.Backend.Model.User;
import com.coolopool.coolopool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 100;
    private Context context;

    CatLoadingView loadingView;

    CircleImageView profileImage;
    EditText name, phoneNumber, email, password;
    ImageButton confrim;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseFirestore mRef;
    FirebaseStorage mStorage;

    User user;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        init();
        getUser();


    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        confrim = findViewById(R.id.confirmBtn);
        context = ProfileSettingActivity.this;
        profileImage = findViewById(R.id.setting_profilepic);
        name = findViewById(R.id.setting_name);
        phoneNumber = findViewById(R.id.setting_phoneNo);
        email = findViewById(R.id.setting_email);
        password = findViewById(R.id.setting_password);
        loadingView = new CatLoadingView();
        progressBar = findViewById(R.id.progress);
        setUpViews();
    }

    private void setUpViews(){
        Glide.with(context).load(R.drawable.userfacepic).into(profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent = new Intent();
                galleryintent.setType("image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryintent, RESULT_LOAD_IMAGE);
            }
        });


        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isDataChanged()){

                    progressBar.setVisibility(View.VISIBLE);
                    loadingView.setText("Uploading");
                    loadingView.show(getSupportFragmentManager(), "Uploading");
                    final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getUsername(), user.getPassword());
                    firebaseUser.reauthenticate(credential)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileSettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setProgress(300);
                                    finish();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    user.setName(name.getText().toString());
                                    user.setPhoneNumber(phoneNumber.getText().toString());
                                    user.setEmail(email.getText().toString());
                                    user.setPassword(password.getText().toString());

                                    firebaseUser.updatePassword(user.getPassword()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressBar.setProgress(100);
                                            updateData();
                                        }
                                    });
                                }
                            });

                }else{
                    Toast.makeText(ProfileSettingActivity.this, "Nothing changed", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }

    private void getUser(){
        mRef.collection("users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                name.setText(user.getName());
                phoneNumber.setText(user.getPhoneNumber());
                email.setText(user.getEmail());
                password.setText(user.getPassword());
            }
        });

        mStorage.getReference("Users/profileImages/"+mAuth.getUid())
                .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Picasso.get().load(task.getResult().toString()).fit().into(profileImage);
                }
            }
        });

    }

    private void updateProfileImage(){
        mStorage.getReference().child("Users/profileImages/"+mAuth.getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setProgress(300);
                Toast.makeText(ProfileSettingActivity.this, "Restart required", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
                finish();
            }
        });
    }

    private void updateData(){
        mRef.collection("users").document(mAuth.getUid()).set(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileSettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(uri!=null && !uri.toString().equals("")){
                    progressBar.setProgress(200);
                    updateProfileImage();
                }else{
                    progressBar.setProgress(300);
                    Toast.makeText(ProfileSettingActivity.this, "Restart required", Toast.LENGTH_SHORT).show();
                    loadingView.dismiss();
                    finish();
                }

            }
        });
    }

    private boolean isDataChanged(){
        if( user.getName().equals(name.getText().toString()) &&
        user.getPhoneNumber().equals(phoneNumber.getText().toString()) &&
        user.getEmail().equals(email.getText().toString()) &&
        user.getPassword().equals(password.getText().toString()) &&
        uri==null ){
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE &&resultCode == RESULT_OK && data != null ){
            uri = data.getData();
            profileImage.setScaleType(ImageButton.ScaleType.CENTER_CROP);
            profileImage.setImageURI(uri);
        }
    }
}
