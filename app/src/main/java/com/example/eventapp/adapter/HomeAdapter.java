package com.example.eventapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.eventapp.Models.ShowingVenueDetails;
import com.example.eventapp.Models.VenueDetails;
import com.example.eventapp.R;
import com.example.eventapp.listeners.ItemListener;
import com.example.eventapp.utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolider> {

    private Context context;
    private List<ShowingVenueDetails> ShowProductList;
    private ItemListener itemListener;
    ArrayList<String> imageKeys = new ArrayList<>();
    DatabaseReference likeref;
    boolean status=false;
    private int index=0;
    boolean ProKey;
    public HomeAdapter(Context context, List<ShowingVenueDetails> ShowProductList, ItemListener itemListener) {
        this.context = context;
        this.ShowProductList = ShowProductList;
        this.itemListener=itemListener;
    }




    @NonNull
    @Override
    public ViewHolider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context)
               .inflate(R.layout.top_deals,parent,false);
        return new ViewHolider(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolider holder, int position) {

        if(ShowProductList.get(position).getCategory().equals("Photographer")
                ||ShowProductList.get(position).getCategory().equals("Transportation Service")
                ||ShowProductList.get(position).getCategory().equals("DJ")
                ||ShowProductList.get(position).getCategory().equals("Makeup Artist")
                ||ShowProductList.get(position).getCategory().equals("Service Only") )
        {
            holder.pricee.setText("From Pkr "+ShowProductList.get(position).getPrice());
        }else if(ShowProductList.get(position).getCategory().equals("Catering"))
        {
            holder.pricee.setText("Starting Pkr "+ShowProductList.get(position).getPrice());
        }else
        {
            holder.pricee.setText(ShowProductList.get(position).getPrice()+" Per head");
        }

//fetching user id;

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userid= FirebaseUtil.currentuserId();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("images").child("1");
likeref=FirebaseDatabase.getInstance().getReference("likes");

        String postkey = ShowProductList.get(position).getId();

String lol=ShowProductList.get(position).getCategory()+" in "+ShowProductList.get(position).getCity();
        holder.location.setText(lol);
holder.shortDiscription.setText(ShowProductList.get(position).getCity());
        String firstImage = null;
        if (ShowProductList.get(position).getImages() != null && !ShowProductList.get(position).getImages().isEmpty()) {
            // Display the first image (you can change this logic as needed)
             firstImage = ShowProductList.get(position).getImages().get(0);
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
        return ShowProductList.size();
    }

    public class ViewHolider extends RecyclerView.ViewHolder {
        private TextView pricee;
        private TextView location;
        private TextView shortDiscription;
        private RelativeLayout relativeLayout;
        DatabaseReference reference;

        public ViewHolider(@NonNull View itemView) {
            super(itemView);
            pricee=itemView.findViewById(R.id.price);
            location=itemView.findViewById(R.id.locations);
            relativeLayout=itemView.findViewById(R.id.relative_layout);
            shortDiscription=itemView.findViewById(R.id.short_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.OnItemPosition(getAdapterPosition());
                }
            });
        }


    }
}
