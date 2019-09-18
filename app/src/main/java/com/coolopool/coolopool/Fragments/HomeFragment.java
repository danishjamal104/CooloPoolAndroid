package com.coolopool.coolopool.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import com.coolopool.coolopool.Backend.Model.Blog;
import com.coolopool.coolopool.Backend.Model.Day;
import com.coolopool.coolopool.Class.Post;
import com.coolopool.coolopool.Interface.TaskCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.coolopool.coolopool.Adapter.PostAdapter;
import com.coolopool.coolopool.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;

    PostAdapter postAdapter;
    View view;

    FirebaseAuth mAuth;

    int flag;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        postAdapter = new PostAdapter(new ArrayList<Post>(), getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home, container, false);

        init();
        getBlogs();

        return view;
    }

    private void init(){
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(postAdapter);

    }

    private void getBlogs() {
        FetchBlogTask task = new FetchBlogTask(new TaskCompleteListener<String>() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "Post loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        task.execute();
    }

    public class FetchBlogTask extends AsyncTask<Void, Void, ArrayList<Post>>{

        FirebaseFirestore mRef = FirebaseFirestore.getInstance();

        TaskCompleteListener<String> mCallback;
        Exception mException;

        public FetchBlogTask(TaskCompleteListener<String> mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        protected ArrayList<Post> doInBackground(Void... voids) {
            final ArrayList<Post> posts = new ArrayList<>();


            mRef.collection("blogs").document(mAuth.getUid())
                    .collection("blogs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        for(final QueryDocumentSnapshot document: task.getResult()){

                            document.getReference().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    final Blog currentBlog = documentSnapshot.toObject(Blog.class);
                                    final Post currentPost = new Post(new ArrayList<Day>(), currentBlog, getActivity());
                                    currentPost.setId(document.getId());
                                    //posts.add(currentPost);
                                    postAdapter.addPost(currentPost);
                                    postAdapter.notifyDataSetChanged();


                                    document.getReference().collection("days").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            ArrayList<Day> days = new ArrayList<>();
                                            for(final QueryDocumentSnapshot daysDoc: task.getResult()){
                                                Day currentDay = daysDoc.toObject(Day.class);
                                                days.add(currentDay);
                                            }
                                            currentPost.addAllDays(days);
                                        }
                                    });

                                }
                            });

                        }
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRef = FirebaseFirestore.getInstance();
        }

        @Override
        protected void onPostExecute(ArrayList<Post> postArrayList) {
            super.onPostExecute(postArrayList);
            if(mCallback != null){
                if(mException == null){
                    mCallback.onSuccess();
                } else {
                    mCallback.onFailure(mException);
                }
            }
        }
    }


}
