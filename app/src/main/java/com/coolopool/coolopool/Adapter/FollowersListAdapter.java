package com.coolopool.coolopool.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coolopool.coolopool.Activity.ProfileActivity;
import com.coolopool.coolopool.Application.MyApplication;
import com.coolopool.coolopool.Class.followList;
import com.coolopool.coolopool.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersListAdapter extends RecyclerView.Adapter<FollowersListAdapter.FollowersViewHolder> {

    private ArrayList<followList> FollowList;
    Context context;

    public FollowersListAdapter(ArrayList<followList> FollowList, Context context) {
        this.FollowList = FollowList;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.followerslist, viewGroup, false);
        return new FollowersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersViewHolder followersViewHolder, int i) {
        final followList FollowersList = FollowList.get(i);

        Picasso.get().load(FollowersList.getmUserProfilePic()).fit().into(followersViewHolder.mUserProfilePic);
        followersViewHolder.mUserName.setText(FollowersList.getmUserName());
        followersViewHolder.mUserFullName.setText(FollowersList.getmFullName());

        followersViewHolder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(context, ProfileActivity.class);
                profileIntent.putExtra("USER_ID", FollowersList.getUserId());
                context.startActivity(profileIntent);
            }
        });


    }

    public void filterFollowers(String text){
        ArrayList<followList> copy = FollowList;
        FollowList = new ArrayList<>();
        for(followList follower: copy){
            if(follower.getmUserName().toLowerCase().contains(text.toLowerCase())){
                FollowList.add(follower);
                notifyDataSetChanged();
            }
        }

    }

    @Override
    public int getItemCount() {
        return FollowList.size();
    }

    public void addFollowers(followList follower){
        FollowList.add(follower);
        notifyDataSetChanged();
    }

    public ArrayList<followList> getFollowList() {
        return FollowList;
    }

    public void resetFollowers(){
        FollowList = new ArrayList<>();
    }

    public class FollowersViewHolder extends RecyclerView.ViewHolder{

        CircleImageView mUserProfilePic;
        TextView mUserName, mUserFullName;
        View v;

        public FollowersViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            mUserProfilePic = itemView.findViewById(R.id.userProfilePic);
            mUserName = itemView.findViewById(R.id.Username);
            mUserFullName = itemView.findViewById(R.id.Name);
        }
    }
}
