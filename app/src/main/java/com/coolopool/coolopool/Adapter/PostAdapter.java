package com.coolopool.coolopool.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coolopool.coolopool.Activity.PostActivity;
import com.coolopool.coolopool.Activity.ProfileActivity;
import com.coolopool.coolopool.Class.Post;
import com.coolopool.coolopool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    ArrayList<Post> posts;
    Context mContext;

    public String profileImageUrl;

    public PostAdapter(ArrayList<Post> postArrayList, Context context){
        posts = postArrayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.root_layout_post,viewGroup,false);
        PostViewHolder postViewHolder= new PostViewHolder(view);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder viewHolder, int i) {

        final Post current_post = posts.get(i);

        viewHolder.title.setText(current_post.getBlog().getTitle());
        FirebaseFirestore mRef = FirebaseFirestore.getInstance();
        mRef.collection("users/").document(current_post.getBlog().getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                viewHolder.userName.setText(documentSnapshot.getString("name"));
            }
        });
        viewHolder.setUpNestedStackView(mContext, current_post, profileImageUrl);

        viewHolder.profileSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.openCurrentProfile(mContext, current_post.getId(), current_post.getBlog().getId());
            }
        });

        viewHolder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.openCurrentPost(mContext, current_post, profileImageUrl);
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference("Users/profileImages/"+current_post.getBlog().getId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri uri = task.getResult();
                Picasso.get().load(uri).fit().into(viewHolder.profilePic);
                profileImageUrl = uri.toString();
            }
        });


    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addAllPosts(ArrayList<Post> p){
        posts.addAll(p);
        notifyDataSetChanged();
    }

    public void addPost(Post post){
        posts.add(post);
        notifyDataSetChanged();
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView userName;
        ImageView profilePic;
        CardStackView stackView;
        LinearLayout profileSection;
        View v;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            profileSection = itemView.findViewById(R.id.userInfo);
            title = itemView.findViewById(R.id.title);
            userName = itemView.findViewById(R.id.userName);
            stackView = itemView.findViewById(R.id.card_stack_view);
            profilePic = itemView.findViewById(R.id.profileImage);
        }

        public void openCurrentPost(Context mContext, Post post, String imageUrl){
            Intent postIntent = new Intent(mContext, PostActivity.class);
            postIntent.putExtra("BLOG_ID", post.getId());
            postIntent.putExtra("USER_ID", post.getBlog().getId());
            postIntent.putExtra("BLOG", post.getBlog());
            postIntent.putExtra("IMAGE", imageUrl);
            mContext.startActivity(postIntent);
        }

        public void openCurrentProfile(Context context, String postId, String userId){
            Intent profileIntent = new Intent(context, ProfileActivity.class);
            profileIntent.putExtra("BLOG_ID", postId);
            profileIntent.putExtra("USER_ID", userId);
            context.startActivity(profileIntent);
        }

        public void setUpNestedStackView(final Context context, final Post post, final String imageUrl){
            CardStackListener listener = new CardStackListener() {
                @Override
                public void onCardDragging(Direction direction, float ratio) {

                }

                @Override
                public void onCardSwiped(Direction direction) {

                }

                @Override
                public void onCardRewound() {

                }

                @Override
                public void onCardCanceled() {

                }

                @Override
                public void onCardAppeared(View view, int position) {

                }

                @Override
                public void onCardDisappeared(View view, int position) {
                    if(position+1 == stackView.getAdapter().getItemCount()){
                        post.setUpAdapter();
                       stackView.setAdapter(post.getAdapter());
                       stackView.getAdapter().notifyDataSetChanged();
                    }

                }
            };

            final CardStackLayoutManager manager = new CardStackLayoutManager(context, listener);

            stackView.setLayoutManager(manager);
            manager.setStackFrom(StackFrom.Right);
            manager.setVisibleCount(4);
            manager.setTranslationInterval(30f);
            manager.setScaleInterval(0.85f);

            stackView.setAdapter(post.getAdapter());
            stackView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openCurrentPost(context, post, imageUrl);
                }
            });


        }
    }
}
