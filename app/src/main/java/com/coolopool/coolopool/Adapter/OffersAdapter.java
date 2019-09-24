package com.coolopool.coolopool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coolopool.coolopool.Class.Offer;
import com.coolopool.coolopool.R;

import java.util.ArrayList;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OfferViewHolder> {

    ArrayList<Offer> offers;
    Context context;

    public OffersAdapter(ArrayList<Offer> offers, Context context) {
        this.offers = offers;
        this.context = context;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupons_list_item, parent, false);
        return new OfferViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        final Offer currentOffer = offers.get(position);

        holder.mType.setText(currentOffer.getmType());
        holder.mDescription.setText("Flat" + currentOffer.getmAmount() + "% off*");
        holder.mExpiry.setText("Expires in " + currentOffer.getmExpiry() + " days");

        holder.mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Code: "+currentOffer.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public void addOffer(Offer offer){
        offers.add(offer);
        notifyDataSetChanged();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mType;
        TextView mDescription;
        TextView mExpiry;
        Button mGetCode;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.picture);
            mType = itemView.findViewById(R.id.type);
            mDescription = itemView.findViewById(R.id.description);
            mExpiry = itemView.findViewById(R.id.expiry);
            mGetCode = itemView.findViewById(R.id.getCode);

        }
    }
}
