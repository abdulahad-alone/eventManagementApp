package com.example.eventapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Models.CateringServiceModel;
import com.example.eventapp.Models.DaigModel;
import com.example.eventapp.Models.Meals;
import com.example.eventapp.R;
import com.example.eventapp.Sub_detailed_activity;
import com.example.eventapp.service.service_by_vendor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CateringServiceAdapter extends  RecyclerView.Adapter<CateringServiceAdapter.ViewHolder> {

private List<CateringServiceModel> newDataList;
    private int selectedItemPosition = RecyclerView.NO_POSITION;
    private Context context;
    private ShowMealAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
public CateringServiceAdapter(Context context,List<CateringServiceModel> newDataList) {
        this.newDataList = newDataList;
    this.context = context;
        }
@NonNull
@Override
public CateringServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new CateringServiceAdapter.ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull CateringServiceAdapter.ViewHolder holder, int position) {
    CateringServiceModel cateringServiceModel = newDataList.get(position);

    holder.bind(cateringServiceModel);
    if (position == selectedItemPosition) {

    } else {
        holder.itemView.setBackgroundResource(R.drawable.item_selector_tranparent);
    }
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, service_by_vendor.class);
            intent.putExtra("ServiceName", cateringServiceModel.getServiceName());
            intent.putExtra("ServicePrice", cateringServiceModel.getServicePrice());
            intent.putExtra("description", cateringServiceModel.getDescription());
            intent.putExtra("imageUrl", cateringServiceModel.getImageUrl());
            intent.putExtra("id", cateringServiceModel.getId());
            intent.putExtra("minimum_orderSt", cateringServiceModel.getMinimum_orderSt());
            intent.putExtra("ServiceRef", cateringServiceModel.getServiceRef());
            context.startActivity(intent);

            // Notify the adapter that the data set has changed to apply the highlighting effect
            //notifyDataSetChanged();
        }
    });

      /*  holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        // Remove item from the list
        newDataList.remove(position);
        // Notify adapter about item removal
        notifyItemRemoved(position);
        // Notify any item change after the removed position
        notifyItemRangeChanged(position, newDataList.size());
        }
        });*/

        }
    public void setOnItemClickListener(ShowMealAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
@Override
public int getItemCount() {
        return newDataList.size();
        }
public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView mealNameTextView;
    private TextView mealPriceTextView;
    private TextView descriptionTextView;
    private ImageView imageView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mealNameTextView = itemView.findViewById(R.id.meal_name);
        mealPriceTextView = itemView.findViewById(R.id.meal_price);
        descriptionTextView = itemView.findViewById(R.id.meal_detail);
        imageView = itemView.findViewById(R.id.home_image);
    }
    public void bind(CateringServiceModel cateringServiceModel) {
        // Bind meal data to UI elements
        mealNameTextView.setText(cateringServiceModel.getServiceName());
        mealPriceTextView.setText("Rs "+cateringServiceModel.getServicePrice()+" per day");
        descriptionTextView.setText(cateringServiceModel.getDescription());
        Picasso.get().load(cateringServiceModel.getImageUrl()).into(imageView);
    }
}
}
