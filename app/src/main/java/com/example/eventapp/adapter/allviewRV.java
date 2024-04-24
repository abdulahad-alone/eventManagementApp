package com.example.eventapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.R;
import com.example.eventapp.fragments.FavouriteFragment;
import com.example.eventapp.listeners.ItemListener;

import java.util.List;

public class allviewRV extends RecyclerView.Adapter<allviewRV.ViewHolider> {
    private Context context;
    private List<ShowingVenueDetails> productList;
    private ItemListener itemListener;

    public allviewRV(Context context, List<ShowingVenueDetails> productList, ItemListener itemListener) {
        this.context = context;
        this.productList = productList;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.favorities, parent, false);
        return new ViewHolider(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolider holder, int position) {

        holder.location.setText(productList.get(position).getCity());
        String lol = productList.get(position).getVenueTitle();
        String getNumberOfRatings = productList.get(position).getNumberOfRatings();
        String Rating = productList.get(position).getRating();
        holder.shortDiscription.setText(lol);
        if (Rating != null && getNumberOfRatings != null) {
            holder.showRating.setText(Rating+"/5"+" ("+getNumberOfRatings+")");
        } else {
            holder.showRating.setText("0/0 (0)");
        }
        if (productList.get(position).getCategory().equals("Photographer")
                || productList.get(position).getCategory().equals("Transportation Service")
                || productList.get(position).getCategory().equals("Service Only")
                || productList.get(position).getCategory().equals("DJ")
                || productList.get(position).getCategory().equals("Makeup Artist")
        ) {
            holder.pricee.setText("From Pkr " + productList.get(position).getPrice());
            holder.per_head.setVisibility(View.INVISIBLE);
        } else if (productList.get(position).getCategory().equals("Catering")) {
            holder.pricee.setText("Starting Pkr " + productList.get(position).getPrice());
            holder.per_head.setVisibility(View.INVISIBLE);
        } else {
            holder.pricee.setText(productList.get(position).getPrice() + " Per head");


        }
        String firstImage = null;
        if (productList.get(position).getImages() != null && !productList.get(position).getImages().isEmpty()) {
            // Display the first image (you can change this logic as needed)
            firstImage = productList.get(position).getImages().get(0);
            // Load the image into the ImageView using an image loading library like Picasso or Glide
            // For example, using Picasso:

        }
        // Bind other data as needed
        Glide.with(context)
                .load(firstImage)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.relativeLayout.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {


                    }
                });
    }

    @Override
    public int getItemCount() {
        return productList.size();

    }

    public class ViewHolider extends RecyclerView.ViewHolder {
        TextView pricee, per_head, showRating, areano;
        private TextView location;
        private TextView shortDiscription;
        private RelativeLayout relativeLayout;
        LinearLayout bednolin, bathnolin;


        public ViewHolider(@NonNull View itemView) {
            super(itemView);
            pricee = itemView.findViewById(R.id.fav_price);
            location = itemView.findViewById(R.id.fav_location);
            relativeLayout = itemView.findViewById(R.id.fav_relative_layout);
            showRating = itemView.findViewById(R.id.showRating);
            shortDiscription = itemView.findViewById(R.id.fav_short_description);
            per_head = itemView.findViewById(R.id.per_head);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.OnItemPosition(getAdapterPosition());
                }
            });
        }
    }
}
