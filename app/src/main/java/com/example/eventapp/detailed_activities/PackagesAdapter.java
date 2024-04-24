package com.example.eventapp.detailed_activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.eventapp.R;

import java.io.Serializable;
import java.util.ArrayList;

public class PackagesAdapter extends  RecyclerView.Adapter<PackagesAdapter.ViewHolder> {

    ArrayList<PackagesModel> packagesModels;
    private Context context;
    private FragmentManager fragmentManager;
    String Key;
    String imageUrI;
    String proType;

    public PackagesAdapter(Context context, ArrayList<PackagesModel> packagesModels, FragmentManager fragmentManager, String Key, String imageUrI, String proType) {
        this.packagesModels = packagesModels;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.Key = Key;
        this.imageUrI = imageUrI;
        this.proType = proType;
    }
    @NonNull
    @Override
    public PackagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_of_package, parent, false);
        return new PackagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackagesAdapter.ViewHolder holder, int position) {
                   holder.name.setText("Package: "+packagesModels.get(position).getNameOfPackage());
                   holder.des.setText(packagesModels.get(position).getDescription());
                   holder.price.setText("Pkr "+packagesModels.get(position).getPriceOfPackage());

        StringBuilder stringBuilder = new StringBuilder();
        for (String service : packagesModels.get(position).getServicesList()) {
            stringBuilder.append("* ").append(service).append("\n");
            Log.e(TAG, "onBindViewHolder: "+ packagesModels.get(position).getServicesList()+"skns"+stringBuilder.toString());
        }
        holder.packageServices.setText(stringBuilder.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_for_booking_package fragment_for_booking_package = new fragment_for_booking_package();
                Bundle args = new Bundle();
                args.putString("sourceActivity", "Package");
                args.putString("nameOfPackage",packagesModels.get(position).getNameOfPackage());
                args.putString("description",packagesModels.get(position).getDescription());
                args.putString("priceOfPackage",packagesModels.get(position).getPriceOfPackage());
                args.putString("PackageRef",packagesModels.get(position).getPackageRef());
                args.putString("id",Key);
                args.putString("imageUrI",imageUrI);
                args.putString("proType",proType);
                args.putSerializable("ServicesList",(Serializable)packagesModels.get(position).getServicesList());
                fragment_for_booking_package.setArguments(args);
                fragment_for_booking_package.show(fragmentManager, null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return packagesModels.size();
    }

    public  static class ViewHolder extends  RecyclerView.ViewHolder{
          TextView price,name,des,packageServices;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            price=itemView.findViewById(R.id.price);
            name=itemView.findViewById(R.id.name);
            des=itemView.findViewById(R.id.des);
            packageServices=itemView.findViewById(R.id.packageServices);
        }
    }
}
