package com.example.eventapp.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.example.eventapp.listeners.ItemListener;

import java.util.List;

public class ShowBookedAdAdapter extends RecyclerView.Adapter<ShowBookedAdAdapter.ViewHolider>{
    private Context context;
    private List<ShowingVenueDetails> productList;
    private ItemListener itemListener;
    private boolean notification;
    private String  productID;

    public ShowBookedAdAdapter(Context context, List<ShowingVenueDetails> productList,
                               ItemListener itemListener, boolean notification, String productID) {
        this.context = context;
        this.productList = productList;
        this.itemListener = itemListener;
        this.notification = notification;
        this.productID = productID;
    }
    @NonNull
    @Override
    public ShowBookedAdAdapter.ViewHolider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.show_booking_items,parent,false);
        return new ShowBookedAdAdapter.ViewHolider(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowBookedAdAdapter.ViewHolider holder, int position) {
        holder.pricee.setText("Starting from "+productList.get(position).getPrice());
        holder.show_status.setVisibility(View.INVISIBLE);
        holder.location.setText(productList.get(position).getCity());
        String lol=productList.get(position).getVenueTitle();
        holder.shortDiscription.setText(lol);
        holder.show_status.setVisibility(View.INVISIBLE);
        if(notification && productList.get(position).getId().equals(productID))
        {
            Log.e(TAG, "from adapter notification: "+notification );
            Log.e(TAG, "productID: "+productID );

            holder.show_status.setVisibility(View.VISIBLE);
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
                        holder.relativeLayout.setBackground( resource);
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
        TextView pricee,bedno,bathno,areano;
        private TextView location;
        private TextView shortDiscription;
        private RelativeLayout relativeLayout;
        LinearLayout show_status,bathnolin;



        public ViewHolider(@NonNull View itemView) {
            super(itemView);
            pricee=itemView.findViewById(R.id.fav_price);
            location=itemView.findViewById(R.id.fav_location);
            show_status=itemView.findViewById(R.id.show_status);
            relativeLayout=itemView.findViewById(R.id.fav_relative_layout);
            shortDiscription=itemView.findViewById(R.id.fav_short_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.OnItemPosition(getAdapterPosition());
                }
            });
        }
    }
}

