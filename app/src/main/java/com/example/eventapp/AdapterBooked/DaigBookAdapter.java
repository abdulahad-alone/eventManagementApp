package com.example.eventapp.AdapterBooked;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
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
import com.example.eventapp.activities.DaigModelForBooked;
import com.example.eventapp.activities.Daigbooked_details;
import com.example.eventapp.listeners.ItemListener;
import com.example.eventapp.showBookingAds;
import com.example.eventapp.utils.FirebaseUtil;

import java.util.List;

public class DaigBookAdapter extends RecyclerView.Adapter<DaigBookAdapter.ViewHolider>{
    private Context context;
    private List<DaigModelForBooked> productList;
    private ItemListener itemListener;
    private boolean notification;
    private String  productID;

    public DaigBookAdapter(Context context, List<DaigModelForBooked> productList, ItemListener itemListener,
                           boolean notification, String productID) {
        this.context = context;
        this.productList = productList;
        this.itemListener = itemListener;
        this.notification = notification;
        this.productID = productID;
    }
    @NonNull
    @Override
    public DaigBookAdapter.ViewHolider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.show_booking_items,parent,false);
        return new DaigBookAdapter.ViewHolider(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaigBookAdapter.ViewHolider holder, int position) {

        holder.location.setText(productList.get(position).getAddress());
        String lol=productList.get(position).getTitle();
        holder.shortDiscription.setText(lol);
        holder.perHead.setVisibility(View.GONE);
        holder.show_status.setVisibility(View.INVISIBLE);
        if(notification && productList.get(position).getProductId().equals(productID))
        {
            Log.e(TAG, "from adapter notification: "+notification );
            Log.e(TAG, "productID: "+productID );

            holder.show_status.setVisibility(View.VISIBLE);
        }
        String firstImage = null;
        String price=productList.get(position).getTotalprice();
        String formattedPrice;
        if (price.isEmpty()) {
            formattedPrice= "";
        }

        double priceValue = Double.parseDouble(price);

        if (priceValue >= 10000000) {
            formattedPrice = String.format("%.2f", priceValue / 10000000) + " Crore"; // Convert to Crore
        }
        else if (priceValue >= 100000) {
            formattedPrice = String.format("%.2f", priceValue / 100000) + " Lakh"; // Convert to Lakhs
        } else if (priceValue >= 1000) {
            formattedPrice = String.format("%.1f", priceValue / 1000) + " Thousand"; // Convert to Thousands
        }
        else {
            formattedPrice = price; // No conversion needed
        }

        holder.pricee.setText(formattedPrice);

            firstImage = productList.get(position).getImageUrl();
            // Load the image into the ImageView using an image loading library like Picasso or Glide
            // For example, using Picasso:

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Daigbooked_details.class);
                intent.putExtra("DaigName", productList.get(position).getTitle());
                intent.putExtra("id", productList.get(position).getId());
                intent.putExtra("price", productList.get(position).getTotalprice());
                intent.putExtra("CurrentUserId", productList.get(position).getCurrentUserId());
                intent.putExtra("BookingDate", productList.get(position).getBookingDate());
                intent.putExtra("ProductId", productList.get(position).getProductId());
                intent.putExtra("this_product_userId", productList.get(position).getProductUserId());
                intent.putExtra("BookingTime", productList.get(position).getBookingTime());
                intent.putExtra("description", productList.get(position).getDescription());
                intent.putExtra("DaigRef", productList.get(position).getDaigRef());
                intent.putExtra("phone", productList.get(position).getPhone());
                intent.putExtra("FullName", productList.get(position).getFullName());
                intent.putExtra("address", productList.get(position).getAddress());

                intent.putExtra("Type_of_ad", productList.get(position).getType_of_ad());
                intent.putExtra("imageUrl", productList.get(position).getImageUrl());
                intent.putExtra("orderNumber", productList.get(position).getOrderNumber());
                intent.putExtra("TotalKgs", productList.get(position).getTotalKgs());
                intent.putExtra("totalNoOfDaigs", productList.get(position).getTotalNoOfDaigs());

                String BookingtimeStamp = FirebaseUtil.formatTimestamp(productList.get(position).getTimeStamp());
                intent.putExtra("timeStamp",BookingtimeStamp);


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();

    }
    public class ViewHolider extends RecyclerView.ViewHolder {
        TextView pricee,bedno,bathno,areano;
        private TextView location,perHead;
        private TextView shortDiscription;
        private RelativeLayout relativeLayout;
        LinearLayout show_status;



        public ViewHolider(@NonNull View itemView) {
            super(itemView);
            pricee=itemView.findViewById(R.id.fav_price);
            location=itemView.findViewById(R.id.fav_location);
            relativeLayout=itemView.findViewById(R.id.fav_relative_layout);
            show_status=itemView.findViewById(R.id.show_status);
            shortDiscription=itemView.findViewById(R.id.fav_short_description);
            perHead=itemView.findViewById(R.id.per_head);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.OnItemPosition(getAdapterPosition());
                }
            });
        }
    }
}
