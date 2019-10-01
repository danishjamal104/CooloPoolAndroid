package com.coolopool.coolopool.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coolopool.coolopool.Activity.PostActivity;
import com.coolopool.coolopool.Activity.ProfileActivity;
import com.coolopool.coolopool.Backend.Model.Blog;
import com.coolopool.coolopool.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class StackCardAdapter extends RecyclerView.Adapter<StackCardAdapter.StackCardViewHolder> implements Serializable {

    ArrayList<String> imageUrl;
    ArrayList<String> description;
    String profileImageUrl;
    String postId;
    Blog blog;
    Context mContext;



    public StackCardAdapter(ArrayList<String> imageUrl, ArrayList<String> description, Context mContext, String postId, Blog blog, String profileImageUrl) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.mContext = mContext;
        this.postId = postId;
        this.blog = blog;
        this.profileImageUrl = profileImageUrl;
    }

    @NonNull
    @Override
    public StackCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post, parent, false);
        return new StackCardViewHolder((v));
    }

    @Override
    public void onBindViewHolder(@NonNull StackCardViewHolder holder, int position) {
        if(imageUrl.get(position) != null) {
            Picasso.get().load(imageUrl.get(position)).into(holder.imageView);
        }
        holder.description.setText(description.get(position));

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postIntent = new Intent(mContext, PostActivity.class);
                postIntent.putExtra("BLOG_ID", postId);
                postIntent.putExtra("USER_ID", blog.getId());
                postIntent.putExtra("BLOG", blog);
                postIntent.putExtra("IMAGE", profileImageUrl);
                mContext.startActivity(postIntent);
            }
        });

    }

    public String getDes(int position){
        return description.get(position);
    }

    public void addImage(String image){
        imageUrl.add(image);
        notifyDataSetChanged();
    }

    public void addDesc(String desc){
        description.add(desc);
    }

    @Override
    public int getItemCount() {
        return imageUrl.size();
    }

    public class StackCardViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView description;
        View v;

        public StackCardViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            imageView = itemView.findViewById(R.id.post_image);
            description = itemView.findViewById(R.id.desc);

        }
    }
}
