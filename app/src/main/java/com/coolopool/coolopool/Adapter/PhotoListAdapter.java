package com.coolopool.coolopool.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.coolopool.coolopool.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends  RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>{

    private ArrayList<String> mUrl;
    Context context;

    public PhotoListAdapter(ArrayList<String> url, Context context){
        this.mUrl = url;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_list_layout, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder photoViewHolder, int i) {

        Picasso.get().load(mUrl.get(i)).fit().into(photoViewHolder.mPhoto);


        photoViewHolder.mPhotoName.setText("Name");
        photoViewHolder.mPhotoPlace.setText("Place");
    }

    @Override
    public int getItemCount() {
        return mUrl.size();
    }

    public void addUrl(String url){
        mUrl.add(url);
    }

    public void addAllUrl(List<String> urls){
        mUrl.addAll(urls);
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

       ImageButton mPhoto;
       TextView mPhotoName, mPhotoPlace;

       public PhotoViewHolder(@NonNull View itemView) {
           super(itemView);
           mPhoto = itemView.findViewById(R.id.photo);
           mPhotoName = itemView.findViewById(R.id.photo_name);
           mPhotoPlace = itemView.findViewById(R.id.photo_place);
       }
   }
}
