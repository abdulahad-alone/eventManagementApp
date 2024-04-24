package com.example.eventapp.detailed_activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class TransportationAdapter extends  RecyclerView.Adapter<TransportationAdapter.ViewHolder> {

    ArrayList<TransportationModel> transportationModels;
    private Context context;
    private FragmentManager fragmentManager;
    String Key;
    String Pro_type;

    public TransportationAdapter(Context context, ArrayList<TransportationModel> transportationModels, FragmentManager fragmentManager, String Key, String Pro_type) {
        this.transportationModels = transportationModels;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.Key = Key;
        this.Pro_type = Pro_type;
    }

    @NonNull
    @Override
    public TransportationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_of_tranport, parent, false);
        return new TransportationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransportationAdapter.ViewHolder holder, int position) {
        holder.name.setText(transportationModels.get(position).getRideName());
        holder.des.setText(transportationModels.get(position).getDescription());
        holder.price.setText("Rs " + transportationModels.get(position).getServicePrice() + "/Day(s)");
        holder.capacitytext.setText(transportationModels.get(position).getCapacity());

        Picasso.get().load(transportationModels.get(position).getImageUrl()).into(holder.Insert_img);
        StringBuilder stringBuilder = new StringBuilder();
        for (String service : transportationModels.get(position).getServicesList()) {
            stringBuilder.append("* ").append(service).append("\n");
            Log.e(TAG, "onBindViewHolder: " + transportationModels.get(position).getServicesList() + "skns" + stringBuilder.toString());
        }
        holder.packageServices.setText(stringBuilder.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transportation_bottom_fragment fragment_for_booking_package = new Transportation_bottom_fragment();
                Bundle args = new Bundle();
                args.putString("sourceActivity", "Transport");
                args.putString("nameOfPackage",transportationModels.get(position).getRideName());
                args.putString("description",transportationModels.get(position).getDescription());
                args.putString("priceOfPackage",transportationModels.get(position).getServicePrice());
                args.putString("PackageRef",transportationModels.get(position).getPackageRef());
                args.putString("id",Key);
                args.putString("imageUrI",transportationModels.get(position).getImageUrl());
                args.putString("TermsCondition",transportationModels.get(position).getTermsCondition());
                args.putString("getCapacity",transportationModels.get(position).getCapacity());
                args.putString("ProType",Pro_type);
                args.putSerializable("ServicesList",(Serializable)transportationModels.get(position).getServicesList());
                fragment_for_booking_package.setArguments(args);
                fragment_for_booking_package.show(fragmentManager, null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return transportationModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView price, name, des, packageServices, capacitytext;
        ImageView Insert_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            name = itemView.findViewById(R.id.name);
            des = itemView.findViewById(R.id.des);
            Insert_img = itemView.findViewById(R.id.Insert_img);
            capacitytext = itemView.findViewById(R.id.capacitytext);
            packageServices = itemView.findViewById(R.id.packageServices);
        }
    }
}
