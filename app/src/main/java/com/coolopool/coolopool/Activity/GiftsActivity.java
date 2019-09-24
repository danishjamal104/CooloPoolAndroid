package com.coolopool.coolopool.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.coolopool.coolopool.Adapter.OffersAdapter;
import com.coolopool.coolopool.Backend.Model.Coupon;
import com.coolopool.coolopool.Class.Offer;
import com.coolopool.coolopool.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GiftsActivity extends AppCompatActivity {

    OffersAdapter mAdapter;
    RecyclerView mRecyclerView;

    FirebaseFirestore mRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashback_and_coupons_layout);

        init();
        setUpBackButton();
        setUpOffers();
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseFirestore.getInstance();
        mRecyclerView = (RecyclerView)findViewById(R.id.offers_recyclerView);
        mAdapter = new OffersAdapter(new ArrayList<Offer>(), GiftsActivity.this);
    }

    private void setUpBackButton(){
        (findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
    }

    private void setUpOffers(){

        mAdapter.addOffer(new Offer("Cashback won", 20, 16));
        mAdapter.addOffer(new Offer("Cashback won", 20, 04));
        mAdapter.addOffer(new Offer("Cashback won", 16, 13));

        mAdapter.notifyDataSetChanged();

        mRecyclerView.setLayoutManager(new GridLayoutManager(GiftsActivity.this, 2));
        mRecyclerView.setHasFixedSize(false);

        mRecyclerView.setAdapter(mAdapter);
        fetchCoupons();

    }

    private void fetchCoupons(){
        mRef.collection("users").document(mAuth.getUid()).collection("coupons").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot document: documents){
                            Coupon currentCoupan = document.toObject(Coupon.class);
                            mAdapter.addOffer(new Offer("Cashback won", currentCoupan.getAmount(), currentCoupan.getExpiry(), currentCoupan.getCode()));
                        }
                    }
                });
    }
}
